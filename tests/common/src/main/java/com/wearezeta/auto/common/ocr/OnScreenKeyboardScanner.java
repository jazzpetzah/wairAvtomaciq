package com.wearezeta.auto.common.ocr;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

/**
 * http://stackoverflow.com/questions/23289387/extract-words-in-rectangles-from-
 */
public class OnScreenKeyboardScanner {
	private static final Logger log = ZetaLogger
			.getLog(OnScreenKeyboardScanner.class.getSimpleName());

	private static boolean isOpenCVLibLoaded = false;

	private static synchronized void loadOpenCVLibrary() throws Exception {
		if (isOpenCVLibLoaded) {
			return;
		}
		try {
			System.load(CommonUtils
					.getOpenCVLibPathFromConfig(OnScreenKeyboardScanner.class));
		} catch (UnsatisfiedLinkError e) {
			log.warn("Some OpenCV features will not be available. Please make sure you have all OpenCV libraries installed on your system and the path in config is correct!\n"
					+ "You can run 'brew install homebrew/science/opencv --with-java' from the command line to install all the necessary components");
			throw e;
		}
		isOpenCVLibLoaded = true;
	}

	public OnScreenKeyboardScanner() throws Exception {
		loadOpenCVLibrary();
	}

	public List<List<Rect>> getButtonCoordinates(String screenshotPath) {
		final Mat image = Highgui.imread(screenshotPath);
		List<MatOfPoint> squaresFound = new ArrayList<>();
		final List<Function<Mat, Mat>> thresholdProducers = new ArrayList<>();
		thresholdProducers.add(OnScreenKeyboardScanner::tresholdImage2);
		thresholdProducers.add(OnScreenKeyboardScanner::tresholdImage1);
		List<List<Rect>> rows = new ArrayList<>();
		for (Function<Mat, Mat> thresholdProducer : thresholdProducers) {
			squaresFound = findScquaresInMat(image, thresholdProducer);
			if (!squaresFound.isEmpty()) {
				rows = detectRows(squaresFound);
				if (!rows.isEmpty()) {
					break;
				}
			}
		}
		if (rows.isEmpty()) {
			throw new RuntimeException(
					"The current OCR algorithm cannot find any buttons.\n"
							+ "Please try to set the on-screen keyboard look and feel to Google Keyboard->'Holo White' "
							+ "and make sure the keyboard is visible on the screenshot.");
		}
		return rows;
	}

	private static List<MatOfPoint> findScquaresInMat(Mat image,
			Function<Mat, Mat> thresholdProducer) {
		Mat grayscale = convertToGrayscale(image);
		Mat treshold = thresholdProducer.apply(grayscale);
		List<MatOfPoint> contours = findContours(treshold);
		Mat contoursImage = fillCountours(contours, grayscale);
		Mat grayscaleWithContours = convertToGrayscale(contoursImage);
		return findSquares(grayscaleWithContours);
	}

	private static void insertNewRect(Map<Integer, List<Rect>> whereTo,
			Rect rect) {
		final int maxHeight = 200;
		if (rect.height > maxHeight) {
			return;
		}

		final int maxDeltaY = 15;
		final int minDeltaX = 25;
		List<Rect> dstRow = null;
		for (int rowY : whereTo.keySet()) {
			if (Math.abs(rowY - rect.y) <= maxDeltaY) {
				dstRow = whereTo.get(rowY);
				break;
			}
		}
		if (dstRow == null) {
			dstRow = new LinkedList<>();
			dstRow.add(rect);
			whereTo.put(rect.y, dstRow);
		} else {
			final List<Integer> xDistances = dstRow.stream()
					.map(x -> x.x - rect.x).collect(Collectors.toList());
			final List<Integer> absDistances = xDistances.stream()
					.map(x -> Math.abs(x)).collect(Collectors.toList());
			final int minDistance = Collections.min(absDistances);
			if (minDistance < minDeltaX) {
				return;
			} else {
				final int idx = absDistances.indexOf(minDistance);
				if (xDistances.get(idx) > 0) {
					dstRow.add(idx, rect);
				} else {
					if (idx + 1 >= absDistances.size()) {
						dstRow.add(rect);
					} else {
						dstRow.add(idx + 1, rect);
					}
				}
			}
		}
	}

	private static List<List<Rect>> detectRows(List<MatOfPoint> squares) {
		Map<Integer, List<Rect>> rowsAsMap = new HashMap<>();
		for (MatOfPoint square : squares) {
			final Rect rect = Imgproc.boundingRect(square);
			insertNewRect(rowsAsMap, rect);
		}
		final int minButtonsInRow = 4;
		List<List<Rect>> rows = new ArrayList<>();
		for (int y : new TreeSet<Integer>(rowsAsMap.keySet())) {
			if (rowsAsMap.get(y).size() >= minButtonsInRow) {
				rows.add(rowsAsMap.get(y));
			}
		}
		return rows;
	}

	private static Mat convertToGrayscale(Mat input) {
		Mat grayscale = new Mat();
		Imgproc.cvtColor(input, grayscale, Imgproc.COLOR_BGR2GRAY);
		return grayscale;
	}

	private static Mat fillCountours(List<MatOfPoint> contours, Mat image) {
		Mat result = image.clone();
		Imgproc.cvtColor(result, result, Imgproc.COLOR_GRAY2RGB);
		for (int i = 0; i < contours.size(); i++) {
			Imgproc.drawContours(result, contours, i, new Scalar(255, 0, 0),
					-1, 8, new Mat(), 0, new Point());
		}
		return result;
	}

	private static List<MatOfPoint> findContours(Mat image) {
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_TREE,
				Imgproc.CHAIN_APPROX_NONE);
		return contours;
	}

	@SuppressWarnings("unused")
	private static BufferedImage convertMatToBufferedImage(Mat mat)
			throws IOException {
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".png", mat, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		InputStream in = new ByteArrayInputStream(byteArray);
		return ImageIO.read(in);
	}

	private static Mat tresholdImage1(Mat img) {
		Mat treshold = new Mat();
		Imgproc.threshold(img, treshold, 150, 255, Imgproc.THRESH_BINARY);
		return treshold;
	}

	@SuppressWarnings("unused")
	private static void saveMatAsImage(Mat mat, String path) {
		Highgui.imwrite(path, mat);
	}

	private static Mat tresholdImage2(Mat img) {
		Mat treshold = new Mat();
		Imgproc.threshold(img, treshold, -1, 255, Imgproc.THRESH_BINARY_INV
				+ Imgproc.THRESH_OTSU);
		return treshold;
	}

	private static List<MatOfPoint> findSquares(Mat input) {
		Mat pyr = new Mat();
		Mat timg = new Mat();

		// Down-scale and up-scale the image to filter out small noises
		Imgproc.pyrDown(input, pyr,
				new Size(input.cols() / 2, input.rows() / 2));
		Imgproc.pyrUp(pyr, timg, input.size());
		// Apply Canny with a threshold of 50
		Imgproc.Canny(timg, timg, 0, 50, 5, true);

		// Dilate canny output to remove potential holes between edge segments
		Imgproc.dilate(timg, timg, new Mat(), new Point(-1, -1), 1);

		// find contours and store them all as a list
		Mat hierarchy = new Mat();
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(timg, contours, hierarchy, Imgproc.RETR_LIST,
				Imgproc.CHAIN_APPROX_SIMPLE);
		List<MatOfPoint> squaresResult = new ArrayList<MatOfPoint>();
		for (int i = 0; i < contours.size(); i++) {

			// Approximate contour with accuracy proportional to the contour
			// perimeter
			MatOfPoint2f contour = new MatOfPoint2f(contours.get(i).toArray());
			MatOfPoint2f approx = new MatOfPoint2f();
			double epsilon = Imgproc.arcLength(contour, true) * 0.02;
			boolean closed = true;
			Imgproc.approxPolyDP(contour, approx, epsilon, closed);
			List<Point> approxCurveList = approx.toList();

			// Square contours should have 4 vertices after approximation
			// relatively large area (to filter out noisy contours)
			// and be convex.
			// Note: absolute value of an area is used because
			// area may be positive or negative - in accordance with the
			// contour orientation
			boolean aproxSize = approx.rows() == 4;
			boolean largeArea = Math.abs(Imgproc.contourArea(approx)) > 200;
			boolean isConvex = Imgproc.isContourConvex(new MatOfPoint(approx
					.toArray()));
			if (aproxSize && largeArea && isConvex) {
				double maxCosine = 0;
				for (int j = 2; j < 5; j++) {
					// Find the maximum cosine of the angle between joint edges
					double cosine = Math.abs(getAngle(
							approxCurveList.get(j % 4),
							approxCurveList.get(j - 2),
							approxCurveList.get(j - 1)));
					maxCosine = Math.max(maxCosine, cosine);
				}
				// If cosines of all angles are small
				// (all angles are ~90 degree) then write quandrange
				// vertices to resultant sequence
				if (maxCosine < 0.3) {
					Point[] points = approx.toArray();
					squaresResult.add(new MatOfPoint(points));
				}
			}
		}
		return squaresResult;
	}

	// angle: helper function.
	// Finds a cosine of angle between vectors from pt0->pt1 and from pt0->pt2.
	private static double getAngle(Point point1, Point point2, Point point0) {
		double dx1 = point1.x - point0.x;
		double dy1 = point1.y - point0.y;
		double dx2 = point2.x - point0.x;
		double dy2 = point2.y - point0.y;
		return (dx1 * dx2 + dy1 * dy2)
				/ Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2)
						+ 1e-10);
	}
}
