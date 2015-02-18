package com.wearezeta.auto.common;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageUtil {
	public static final int RESIZE_NORESIZE = 0;
	public static final int RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION = 1;
	public static final int RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION = 2;
	public static final int RESIZE_FROM1920x1080OPTIMIZED = 3;
	public static final int RESIZE_FROM2560x1600OPTIMIZED = 4;
	public static final int RESIZE_TEMPLATE_TO_RESOLUTION = 5;
	public static final int RESIZE_FROM_OPTIMIZED = 6;
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	private static Mat convertImageToOpenCVMat(BufferedImage image) {
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		Mat imageMat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
		imageMat.put(0, 0, pixels);	
		return imageMat;
	}
	
	private static BufferedImage convertToBufferedImageOfType(BufferedImage original, int type) {
	    if (original == null) {
	        throw new IllegalArgumentException("original == null");
	    }

	    // Don't convert if it already has correct type
	    if (original.getType() == type) {
	        return original;
	    }

	    // Create a buffered image
	    BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), type);

	    // Draw the image onto the new buffer
	    Graphics2D g = image.createGraphics();
	    try {
	        g.setComposite(AlphaComposite.Src);
	        g.drawImage(original, 0, 0, null);
	    }
	    finally {
	        g.dispose();
	    }

	    return image;
	}
	
	public static Mat resizeTemplateMatrixFromOptimizedForResolution(Mat tpl, Mat ref, int etWidth, int etHeight) {
		Mat result;
		if (ref.width() != etWidth && ref.height() != etHeight) {
			result = new Mat();
			Size sz = new Size((tpl.width()*ref.width())/etWidth, (tpl.height()*ref.height())/etHeight);
			Imgproc.resize(tpl, result, sz);
		} else {
			result = tpl;
		}
		return result;
	}
	
	public static Mat resizeFirstMatrixToSecondMatrixResolution(Mat first, Mat second) {
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
	
	public static Mat resizeMatrixToResolution(Mat matrix, int exWidth, int exHeight) {
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
		return getOverlapScore(refImage, tplImage, RESIZE_FROM1920x1080OPTIMIZED);
	}
	
	public static double getOverlapScore(BufferedImage refImage, BufferedImage tplImage, int resizeMode) {
		return getOverlapScore(refImage, tplImage, resizeMode, 1, 1);
	}
	
	public static double getOverlapScore(BufferedImage refImage, BufferedImage tplImage, int resizeMode, int exWidth, int exHeight) {
		//convert images to matrixes
		refImage = convertToBufferedImageOfType(refImage, BufferedImage.TYPE_3BYTE_BGR);
		tplImage = convertToBufferedImageOfType(tplImage, BufferedImage.TYPE_3BYTE_BGR);
	    Mat ref = convertImageToOpenCVMat(refImage);
	    Mat tpl = convertImageToOpenCVMat(tplImage);
	    if (ref.empty() || tpl.empty()) {
	    	if (ref.empty()) { System.out.println("ERROR: No reference image found."); }
	    	if (tpl.empty()) { System.out.println("ERROR: No template image found."); }
	        return Double.NaN;
	    }

	    if (resizeMode == RESIZE_FROM1920x1080OPTIMIZED) {
	    	tpl = resizeTemplateMatrixFromOptimizedForResolution(tpl, ref, 1080, 1920);
	    } else if (resizeMode == RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION) {
	    	tpl = resizeFirstMatrixToSecondMatrixResolution(tpl, ref);
	    } else if (resizeMode == RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION) {
	    	ref = resizeFirstMatrixToSecondMatrixResolution(ref, tpl);
	    } else if (resizeMode == RESIZE_FROM2560x1600OPTIMIZED) {
	    	tpl = resizeTemplateMatrixFromOptimizedForResolution(tpl, ref, 1600, 2560);
	    } else if (resizeMode == RESIZE_TEMPLATE_TO_RESOLUTION) {
	    	tpl = resizeMatrixToResolution(tpl, exWidth, exHeight);
	    } else if (resizeMode == RESIZE_FROM_OPTIMIZED) {
	    	tpl = resizeTemplateMatrixFromOptimizedForResolution(tpl, ref, exWidth, exHeight);
	    }
	    
	    //get grayscale images for matching template
	    Mat gref = new Mat(), gtpl = new Mat();
	    Imgproc.cvtColor(ref, gref, Imgproc.COLOR_BGR2GRAY);
	    Imgproc.cvtColor(tpl, gtpl, Imgproc.COLOR_BGR2GRAY);

	    //matching template
	    Mat res = new Mat(ref.rows()-tpl.rows()+1, ref.cols()-tpl.cols()+1, CvType.CV_32FC1);
	    Imgproc.matchTemplate(gref, gtpl, res, Imgproc.TM_CCOEFF_NORMED);

        MinMaxLocResult minMaxLocResult = Core.minMaxLoc(res);
        return minMaxLocResult.maxVal;
	}
	
	public static BufferedImage readImageFromFile(String filePath) throws IOException {
		return ImageIO.read(new File(filePath));
	}

	public static void storeImageToFile(BufferedImage im, String filePath) throws IOException {
		ImageIO.write(im, "PNG", new File(filePath));
	}
}
