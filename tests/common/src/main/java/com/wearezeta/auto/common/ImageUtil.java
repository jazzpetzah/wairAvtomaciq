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
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class ImageUtil {

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
	
	public static double getOverlapScore(BufferedImage refImage, BufferedImage tplImage) {
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
}
