package com.wearezeta.auto.common;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageUtil {

    public static final int RESIZE_NORESIZE = 0;
    public static final int RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION = 1;
    public static final int RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION = 2;
    public static final int RESIZE_TEMPLATE_TO_RESOLUTION = 5;
    public static final int RESIZE_TO_MAX_SCORE = 7;

    static {
        String arch = System.getProperty("sun.arch.data.model");
        String libPath = Core.NATIVE_LIBRARY_NAME;
        String os = System.getProperty("os.name");
        // Check if OS is windows
        if (os.toLowerCase().contains("win")) {
            if ("64".equals(arch)) {
                libPath = libPath.replaceAll("opencv_java249",
                        "opencv_java249_x64");
            } else {
                libPath = libPath.replaceAll("opencv_java249",
                        "opencv_java249_x86");
            }
        }
        System.out.println("Loading OpenCV Lib from " + libPath);
        System.loadLibrary(libPath);
    }

    private static Mat convertImageToOpenCVMat(BufferedImage image) {
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer())
                .getData();
        Mat imageMat = new Mat(image.getHeight(), image.getWidth(),
                CvType.CV_8UC3);
        imageMat.put(0, 0, pixels);
        return imageMat;
    }

    private static BufferedImage convertToBufferedImageOfType(
            BufferedImage original, int type) {
        if (original == null) {
            throw new IllegalArgumentException("original == null");
        }

        // Create a buffered image
        BufferedImage image = new BufferedImage(original.getWidth(),
                original.getHeight(), type);

        // Draw the image onto the new buffer
        Graphics2D g = image.createGraphics();
        try {
            g.setComposite(AlphaComposite.Src);
            g.drawImage(original, 0, 0, null);
        } finally {
            g.dispose();
        }

        return image;
    }

    public static Mat resizeFirstMatrixToSecondMatrixResolution(Mat first,
                                                                Mat second) {
        Mat result;
        if (first.width() != second.width() || first.height() != second.height()) {
            result = new Mat();
            Size sz = new Size(second.width(), second.height());
            Imgproc.resize(first, result, sz);
        } else {
            result = first;
        }
        return result;
    }

    public static Mat resizeMatrixToResolution(Mat matrix, int exWidth,
                                               int exHeight) {
        Mat result;
        if (matrix.width() != exWidth || matrix.height() != exHeight) {
            result = new Mat();
            Size sz = new Size(exWidth, exHeight);
            Imgproc.resize(matrix, result, sz);
        } else {
            result = matrix;
        }
        return result;
    }

    public static double getOverlapScore(BufferedImage refImage, BufferedImage tplImage) {
        return getOverlapScore(refImage, tplImage, RESIZE_TO_MAX_SCORE);
    }

    public static double getOverlapScore(BufferedImage refImage, BufferedImage tplImage, int resizeMode) {
        if (resizeMode == RESIZE_TO_MAX_SCORE) {
            if (getOverlapScore(refImage, tplImage,
                    RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION, 1, 1) > getOverlapScore(
                    refImage, tplImage,
                    RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION, 1, 1)) {
                return getOverlapScore(refImage, tplImage,
                        RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION, 1, 1);
            } else {
                return getOverlapScore(refImage, tplImage,
                        RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION, 2, 1);
            }
        } else {
            return getOverlapScore(refImage, tplImage, resizeMode, 1, 1);
        }
    }

    public static double getOverlapScore(BufferedImage refImage,
                                         BufferedImage tplImage, int resizeMode, int exWidth, int exHeight) {
        refImage = convertToBufferedImageOfType(refImage, BufferedImage.TYPE_3BYTE_BGR);
        tplImage = convertToBufferedImageOfType(tplImage, BufferedImage.TYPE_3BYTE_BGR);
        Mat ref = convertImageToOpenCVMat(refImage);
        Mat tpl = convertImageToOpenCVMat(tplImage);
        if (ref.empty() || tpl.empty()) {
            if (ref.empty()) {
                System.out.println("ERROR: No reference image found.");
            }
            if (tpl.empty()) {
                System.out.println("ERROR: No template image found.");
            }
            return Double.NaN;
        }

        switch (resizeMode) {
            case RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION:
                tpl = resizeFirstMatrixToSecondMatrixResolution(tpl, ref);
                break;
            case RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION:
                ref = resizeFirstMatrixToSecondMatrixResolution(ref, tpl);
                break;
            case RESIZE_TEMPLATE_TO_RESOLUTION:
                tpl = resizeMatrixToResolution(tpl, exWidth, exHeight);
                break;
        }

        Mat res = new Mat(ref.rows() - tpl.rows() + 1, ref.cols() - tpl.cols() + 1, CvType.CV_32FC1);
        Imgproc.matchTemplate(ref, tpl, res, Imgproc.TM_CCOEFF_NORMED);

        MinMaxLocResult minMaxLocResult = Core.minMaxLoc(res);
        return minMaxLocResult.maxVal;
    }

    public static BufferedImage readImageFromFile(String filePath) throws IOException {
        return ImageIO.read(new File(filePath));
    }

    public static BufferedImage readImageFromBytes(byte[] srcImage) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(srcImage));
    }

    public static byte[] readBytesFromImage(BufferedImage srcImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(srcImage, "png", baos);
        byte[] imageInByte = baos.toByteArray();
        return imageInByte;
    }

    /**
     * Resizes image to the given ratio (use >1 to upscale, or <1 to downscale)
     */
    public static BufferedImage resizeImage(BufferedImage image, float resizeRatio) throws IOException {
        assert resizeRatio > 0 : "Resize ratio should be positive";
        int w = image.getWidth(), h = image.getHeight();
        int scaledW = Math.round(w * resizeRatio);
        int scaledH = Math.round(h * resizeRatio);
        BufferedImage result = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = result.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, scaledW, scaledH, null);
        return result;
    }

    public static BufferedImage tilt(BufferedImage image, double angle) {
        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = image.getWidth(), h = image.getHeight();
        int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h * cos + w * sin);
        BufferedImage result = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(image, null);
        return result;
    }

    /**
     * Calculates average similarity value between 'maxFrames' image frames
     * taken with help of elementStateScreenshoter method
     *
     * @param elementStateScreenshoter the function, which implements screenshoting
     * @param maxFrames                count of frames to compare. Is recommended to set this to 3 or
     *                                 greater
     * @param millisecondsDelay        minimum delay value between each screenshot. This delay can be
     *                                 greater on real device, because it depends on the actual CPU
     *                                 performance
     * @return overlap value: 0 <= value <= 1
     * @throws Exception
     */
    public static double getAnimationThreshold(
            FunctionalInterfaces.ISupplierWithException<Optional<BufferedImage>> elementStateScreenshoter,
            final int maxFrames, final long millisecondsDelay) throws Exception {
        assert maxFrames >= 3 : "Please set maxFrames value to 3 or greater";
        final List<BufferedImage> timelineScreenshots = new ArrayList<>();
        do {
            timelineScreenshots.add(elementStateScreenshoter.call()
                    .orElseThrow(IllegalStateException::new));
            Thread.sleep(millisecondsDelay);
        } while (timelineScreenshots.size() < maxFrames);
        int idx = 0;
        final List<Double> thresholds = new ArrayList<>();
        while (idx < timelineScreenshots.size() - 1) {
            thresholds.add(getOverlapScore(timelineScreenshots.get(idx + 1),
                    timelineScreenshots.get(idx),
                    ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION));
            idx++;
        }
        return thresholds.stream().min(Double::compare).orElse(100.0);
    }

    public static boolean isLandscape(BufferedImage bi) {
        return (bi.getWidth() > bi.getHeight());
    }

    public static BufferedImage scaleTo(BufferedImage originalImage, final int maxWidth, final int maxHeight) {
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();
        float resizeRatio = 1;
        if (width > maxWidth || height > maxHeight) {
            float resizeRatioW1 = (float) maxWidth / width;
            float resizeRatioW2 = (float) maxWidth / height;
            float resizeRatioH1 = (float) maxHeight / width;
            float resizeRatioH2 = (float) maxHeight / height;
            float resizeRatioH = (resizeRatioH1 < resizeRatioH2) ? resizeRatioH1 : resizeRatioH2;
            float resizeRatioW = (resizeRatioW1 < resizeRatioW2) ? resizeRatioW1 : resizeRatioW2;
            resizeRatio = (resizeRatioH > resizeRatioW) ? resizeRatioH : resizeRatioW;
        }
        try {
            return ImageUtil.resizeImage(originalImage, resizeRatio);
        } catch (IOException e) {
            e.printStackTrace();
            return originalImage;
        }
    }

    public static void storeImage(final BufferedImage screenshot, final File outputFile) {
        try {
            if (!outputFile.getParentFile().exists()) {
                // noinspection ResultOfMethodCallIgnored
                outputFile.getParentFile().mkdirs();
            }
            ImageIO.write(screenshot, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
