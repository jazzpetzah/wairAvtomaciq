package com.wearezeta.auto.osx.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.log.ZetaLogger;

public class AccentColorUtil {

	private static final Logger log = ZetaLogger.getLog(AccentColorUtil.class
			.getSimpleName());

	// color constraints
	public static final int COLOR_WIDTH_MIN_PX = 40;
	public static final int COLOR_HEIGHT_MIN_PX = 0;
	public static final int COLOR_WIDTH_MAX_PX = 300;
	public static final int COLOR_HEIGHT_MAX_PX = 20;
	public static final int MIN_COLOR_VALUE = 150;

	public static final AccentColor[] accentColorsList = new AccentColor[] {
			AccentColor.StrongBlue, AccentColor.StrongLimeGreen,
			AccentColor.BrightYellow, AccentColor.VividRed,
			AccentColor.BrightOrange, AccentColor.SoftPink, AccentColor.Violet };

	public static LinkedHashMap<Color, AccentColor> accentColorsRGBMap = new LinkedHashMap<Color, AccentColor>();

	/*
	 * Counts number of pixels of each color on specified image
	 */
	public static LinkedHashMap<Color, Integer> buildImageColorsMap(
			BufferedImage image) {
		return buildImageColorsMap(image, Integer.MAX_VALUE);
	}

	/*
	 * Counts number of pixels of each color on specified image and filters
	 * colors which count > filter
	 */
	public static LinkedHashMap<Color, Integer> buildImageColorsMap(
			BufferedImage screen, int filter) {
		LinkedHashMap<Color, Integer> colorsMap = new LinkedHashMap<Color, Integer>();
		for (int i = 0; i < screen.getWidth(); i++) {
			for (int j = 0; j < screen.getHeight(); j++) {
				Color c = new Color(screen.getRGB(i, j));
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				if (r < MIN_COLOR_VALUE && g < MIN_COLOR_VALUE
						&& b < MIN_COLOR_VALUE)
					continue;
				if (colorsMap.get(c) != null) {
					colorsMap.put(c, colorsMap.get(c) + 1);
				} else {
					colorsMap.put(c, 1);
				}
			}
		}
		LinkedHashMap<Color, Integer> filteredColorsMap = new LinkedHashMap<Color, Integer>();
		if (filter < Integer.MAX_VALUE) {
			for (Map.Entry<Color, Integer> entry : colorsMap.entrySet()) {
				Integer value = entry.getValue();
				if (value > filter) {
					filteredColorsMap.put(entry.getKey(), value);
				}
			}
		} else {
			filteredColorsMap = colorsMap;
		}

		return filteredColorsMap;
	}

	public static Color findMainColor(LinkedHashMap<Color, Integer> colorsMap) {
		Color maxColor = null;
		int maxColorCount = 0;
		for (Map.Entry<Color, Integer> color : colorsMap.entrySet()) {
			if (color.getValue() > maxColorCount) {
				maxColor = color.getKey();
				maxColorCount = color.getValue();
			}
		}
		return maxColor;
	}

	/*
	 * Retrieves accent color name for specified color (if such exists).
	 * 
	 * This should be called only when accent colors map is initialized.
	 */
	public static AccentColor getAccentColorByColor(Color color) {
		if (color != null) {
			return accentColorsRGBMap.get(color);
		} else {
			return AccentColor.Undefined;
		}
	}

	/*
	 * Calculates full color accent (contact list entries / text selection /
	 * avatars / ping animation / etc.)
	 */
	public static AccentColor calculateAccentColorForForeground(
			BufferedImage screen) {
		LinkedHashMap<Color, Integer> colors = buildImageColorsMap(screen);
		log.debug(colors);

		Color mainColor = findMainColor(colors);

		return getAccentColorByColor(mainColor);
	}

	/*
	 * Calculates accent color mask used by background in Wire.
	 */
	public static AccentColor calculateAccentColorForBackground(
			BufferedImage screen) {
		LinkedHashMap<Color, Integer> colorsMap = AccentColorUtil
				.buildImageColorsMap(screen);

		// calculate which channel has largest value for all pixels
		AccentColor result = null;
		int pixelsCount[] = new int[8];

		// count number of pixels of each color
		for (Map.Entry<Color, Integer> entry : colorsMap.entrySet()) {
			AccentColor tone = getPixelTone(entry.getKey());
			if (tone == null)
				continue;
			pixelsCount[tone.getId()] += entry.getValue();
		}

		// calculate accent color
		result = probableAccentColor(pixelsCount);

		log.debug(String.format("(%s: %d,%d,%d,%d,%d,%d,%d,%d) = " + colorsMap,
				result, pixelsCount[0], pixelsCount[1], pixelsCount[2],
				pixelsCount[3], pixelsCount[4], pixelsCount[5], pixelsCount[6],
				pixelsCount[7]));

		return result;
	}

	/*
	 * By color of current pixel (passed as parameter) determines its tone as
	 * one of accent colors used by Wire.
	 */
	private static AccentColor getPixelTone(Color color) {
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();

		final int VIVIDRED_GREEN_AND_BLUE_MAXVALUE = 100;
		final int YELLOW_ORANGE_GREEN_DELIMITER = 170;

		// gray (not focused window, Undefined used for gray): all channels have
		// the same value
		// blue: blue > green > red
		// green: green > blue and green > red
		// yellow: red > green > blue (green < YELLOW_ORANGE_RED_DELIMITER)
		// red: red > blue and green (green and blue <
		// VIVIDRED_GREEN_AND_BLUE_MAXVALUE)
		// orange: red > green > blue (green > YELLOW_ORANGE_RED_DELIMITER)
		// pink: red > blue > green
		// violet: blue > red > green
		if (red == green && red == blue) {
			return AccentColor.Undefined;
		} else {
			if (green >= red && green >= blue) {
				return AccentColor.StrongLimeGreen;
			} else if (blue >= red && blue >= green && green >= red) {
				return AccentColor.StrongBlue;
			} else if (blue >= red && blue >= green && red >= green) {
				return AccentColor.Violet;
			} else if (red >= blue && red >= green
					&& blue <= VIVIDRED_GREEN_AND_BLUE_MAXVALUE
					&& green <= VIVIDRED_GREEN_AND_BLUE_MAXVALUE) {
				return AccentColor.VividRed;
			} else if (red >= blue && red >= green && blue >= green) {
				return AccentColor.SoftPink;
			} else if (red >= blue && red >= green && green >= blue
					&& green <= YELLOW_ORANGE_GREEN_DELIMITER) {
				return AccentColor.BrightOrange;
			} else if (red >= blue && red >= green && green >= blue
					&& green > YELLOW_ORANGE_GREEN_DELIMITER) {
				return AccentColor.BrightYellow;
			}
		}
		return null;
	}

	/*
	 * Goes through pixels count and find which color of pixels are maximum for
	 * current image.
	 * 
	 * Each pixel color is determined as one of accent colors using
	 * getPixelTone() method.
	 * 
	 * Window determined as not in focus if gray pixels count larger than sum of
	 * colored pixels.
	 */
	private static AccentColor probableAccentColor(int[] tonePixelsCount) {
		int maxIndex = 1;
		int maxValue = 0;

		int coloredPixels = 0;
		for (int i = 1; i < tonePixelsCount.length; i++) {
			coloredPixels += tonePixelsCount[i];
		}
		if (tonePixelsCount[0] > coloredPixels) {
			return AccentColor.Undefined;
		}
		for (int i = 1; i < tonePixelsCount.length; i++) {
			if (tonePixelsCount[i] > maxValue) {
				maxIndex = i;
				maxValue = tonePixelsCount[i];
			}
		}

		AccentColor[] colors = AccentColor.values();
		return colors[maxIndex];
	}
}
