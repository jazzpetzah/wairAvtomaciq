package com.wearezeta.auto.android.util;

import io.appium.java_client.AppiumDriver;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.ImageUtil;
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

	private static void fillColorsRGBValuesMap(ArrayList<Color> accentColors) {
		AccentColorUtil.accentColorsRGBMap = new LinkedHashMap<Color, AccentColor>();
		for (int i = 0; i < AccentColorUtil.accentColorsList.length; i++) {
			if (AccentColorUtil.accentColorsList[i] == AccentColor.Undefined) {
				i--;
				continue;
			}
			log.debug("Filling colors: " + accentColors.get(i) + "="
					+ AccentColorUtil.accentColorsList[i]);
			AccentColorUtil.accentColorsRGBMap.put(accentColors.get(i),
					AccentColorUtil.accentColorsList[i]);
		}
	}

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
		Color minColor = null;
		int minColorCount = Integer.MAX_VALUE;
		for (Map.Entry<Color, Integer> color : colorsMap.entrySet()) {
			if (color.getValue() < minColorCount) {
				minColor = color.getKey();
				minColorCount = color.getValue();
			}
		}
		return minColor;
	}

	/*
	 * Retrieves accent color name for specified color (if such exists).
	 * 
	 * This should be called only when accent colors map is initialized.
	 */
	public static AccentColor getAccentColorByColor(Color color) {
		if (color != null) {
			log.debug(color);
			log.debug(accentColorsRGBMap);
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

	public static AccentColor findSelectedAccentColor(AppiumDriver driver,
			WebElement colorPicker) throws Exception {
		BufferedImage windowScreen = AndroidCommonUtils.getElementScreenshot(
				colorPicker, driver, "Android Device").get();
		return findSelectedAccentColor(windowScreen);
	}

	private static AccentColor findSelectedAccentColor(BufferedImage screen)
			throws IOException {
		ImageUtil.storeImageToFile(screen, "/Users/kaleksandrov/screen.png");
		LinkedHashMap<Color, Integer> colors = new LinkedHashMap<Color, Integer>();

		for (int i = 0; i < screen.getWidth(); i++) {
			Color c = new Color(screen.getRGB(i, screen.getHeight() / 2));
			int r = c.getRed();
			int g = c.getGreen();
			int b = c.getBlue();
			if (r < AccentColorUtil.MIN_COLOR_VALUE
					&& g < AccentColorUtil.MIN_COLOR_VALUE
					&& b < AccentColorUtil.MIN_COLOR_VALUE) {
				continue;
			}
			Integer counter = colors.get(c);
			if (counter != null) {
				colors.put(c, counter + 1);
			} else {
				colors.put(c, 1);
			}
		}

		LinkedHashMap<Color, Integer> processed = new LinkedHashMap<Color, Integer>();
		for (Map.Entry<Color, Integer> entry : colors.entrySet()) {
			if (entry.getValue() > AccentColorUtil.COLOR_WIDTH_MIN_PX
					&& entry.getValue() < AccentColorUtil.COLOR_WIDTH_MAX_PX) {
				log.debug("Processed: " + entry.getKey() + "="
						+ entry.getValue());
				processed.put(entry.getKey(), entry.getValue());
			}
		}

		fillColorsRGBValuesMap(new ArrayList<Color>(processed.keySet()));

		Color color = AccentColorUtil.findMainColor(processed);

		return AccentColorUtil.getAccentColorByColor(color);
	}

}
