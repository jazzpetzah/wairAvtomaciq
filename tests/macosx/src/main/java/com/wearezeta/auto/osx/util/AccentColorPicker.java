package com.wearezeta.auto.osx.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.common.OSXConstants;

public class AccentColorPicker {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(AccentColorPicker.class
			.getSimpleName());

	public static final int NUMBER_OF_COLORS = 7;

	private NSPoint windowOffset;

	private ElementRectangle pickerArea;

	private AccentColor currentColor = AccentColor.Undefined;

	private HashMap<AccentColor, ElementRectangle> colorsAreas;

	private ZetaOSXDriver driver;

	private NSPoint getRectangleMiddlePoint(ElementRectangle rect)
			throws IOException {
		int multiply = OSXCommonUtils.screenPixelsMultiplier(driver);
		
		return new NSPoint(
				(windowOffset.x() * multiply + rect.x() + rect.width() / 2)
						/ multiply,
				(windowOffset.y() * multiply + rect.y() + rect.height() / 2)
						/ multiply);
	}

	public void changeAccentColor(AccentColor newColor) throws IOException {
		ElementRectangle newColorArea = colorsAreas.get(newColor);
		NSPoint clickPoint = getRectangleMiddlePoint(newColorArea);
		Actions builder = new Actions(driver);
		Action pickColor = builder.moveByOffset(clickPoint.x(), clickPoint.y())
				.click().build();
		pickColor.perform();
		currentColor = newColor;
	}

	public AccentColor getCurrentColor() {
		return currentColor;
	}

	public static AccentColorPicker findColorPickerInWindow(
			ZetaOSXDriver driver, WebElement window) throws IOException {
		BufferedImage windowScreen = OSXCommonUtils.takeElementScreenshot(
				window, driver);
		NSPoint windowPosition = NSPoint.fromString(window
				.getAttribute(OSXConstants.Attributes.AXPOSITION));
		AccentColorPicker result = findColorPickerInWindow(windowScreen);
		result.driver = driver;
		result.windowOffset = windowPosition;
		return result;
	}

	public static AccentColorPicker findColorPickerInWindow(BufferedImage image) {
		AccentColorPicker result = new AccentColorPicker();
		int width = image.getWidth();
		int height = image.getHeight();

		result.pickerArea = new ElementRectangle();

		int tempSize = 0;
		boolean rowStarted = false;

		ArrayList<Color> accentColors = new ArrayList<Color>();

		// go through the window screenshot and find all lines which contain
		// more than COLOR_WIDTH_MIN_PX pixels for more than NUMBER_OF_COLORS
		for (int j = 0; j < height; j++) {
			LinkedHashMap<Color, Integer> colors = new LinkedHashMap<Color, Integer>();
			for (int i = 0; i < width; i++) {
				Color c = new Color(image.getRGB(i, j));
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				// filter all not relevant colors (at least one channel in
				// required color will be more than MIN_COLOR_VALUE)
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

			// filter colors with number of pixels between COLOR_WIDTH_MIN_PX
			// and COLOR_WIDTH_MAX_PX
			LinkedHashMap<Color, Integer> processed = new LinkedHashMap<Color, Integer>();
			for (Map.Entry<Color, Integer> entry : colors.entrySet()) {
				if (entry.getValue() > AccentColorUtil.COLOR_WIDTH_MIN_PX
						&& entry.getValue() < AccentColorUtil.COLOR_WIDTH_MAX_PX) {
					processed.put(entry.getKey(), entry.getValue());
				}
			}

			if (processed.size() == NUMBER_OF_COLORS) {
				tempSize++;
				if (result.pickerArea.y() < 0 && !rowStarted) {
					result.pickerArea.setY(j);
					rowStarted = true;
					accentColors = new ArrayList<Color>(processed.keySet());
				}
			} else {
				if (tempSize > 0 && result.pickerArea.height() < tempSize) {
					result.pickerArea.setHeight(tempSize);
				}
			}
		}

		fillColorsRGBValuesMap(accentColors);

		// go through first line for found element and store all required info
		for (int i = 0; i < width; i++) {
			if (new Color(image.getRGB(i, result.pickerArea.y()))
					.equals(accentColors.get(0))) {
				result.pickerArea.setX(i);
				break;
			}
		}

		// go through first line to found end of element
		for (int i = width; i > 0; i--) {
			if (new Color(image.getRGB(i - 1, result.pickerArea.y()))
					.equals(accentColors.get(NUMBER_OF_COLORS - 1))) {
				result.pickerArea.setWidth(i - result.pickerArea.x());
				break;
			}
		}

		// locate all colors
		result.colorsAreas = locateColors(image, result.pickerArea,
				accentColors);

		return result;
	}

	public static AccentColor findSelectedAccentColor(ZetaOSXDriver driver,
			WebElement window) throws IOException {
		BufferedImage windowScreen = OSXCommonUtils.takeElementScreenshot(
				window, driver);
		return findSelectedAccentColor(windowScreen);
	}

	private static AccentColor findSelectedAccentColor(BufferedImage screen) {
		AccentColorPicker picker = findColorPickerInWindow(screen);

		LinkedHashMap<Color, Integer> colors = new LinkedHashMap<Color, Integer>();

		for (int i = picker.pickerArea.x(); i < picker.pickerArea.x()
				+ picker.pickerArea.width(); i++) {
			Color c = new Color(screen.getRGB(i, picker.pickerArea.y() - 1));
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
				processed.put(entry.getKey(), entry.getValue());
			}
		}

		Color color = AccentColorUtil.findMainColor(processed);

		return AccentColorUtil.getAccentColorByColor(color);
	}

	private static void fillColorsRGBValuesMap(ArrayList<Color> accentColors) {
		AccentColorUtil.accentColorsRGBMap = new LinkedHashMap<Color, AccentColor>();
		for (int i = 0; i < AccentColorUtil.accentColorsList.length; i++) {
			if (AccentColorUtil.accentColorsList[i] == AccentColor.Undefined) {
				i--;
				continue;
			}
			AccentColorUtil.accentColorsRGBMap.put(accentColors.get(i),
					AccentColorUtil.accentColorsList[i]);
		}
	}

	private static LinkedHashMap<AccentColor, ElementRectangle> locateColors(
			BufferedImage window, ElementRectangle pickerArea,
			ArrayList<Color> realColors) {
		LinkedHashMap<AccentColor, ElementRectangle> colors = new LinkedHashMap<AccentColor, ElementRectangle>();

		int x = pickerArea.x();
		final int y = pickerArea.y();
		int colorIndex = 0;
		for (AccentColor color : AccentColorUtil.accentColorsList) {
			// skip pixels to the next color
			while (!new Color(window.getRGB(x, y)).equals(realColors
					.get(colorIndex)))
				x++;
			// init next color rectangle
			ElementRectangle currentColor = new ElementRectangle();
			currentColor.setX(x);
			currentColor.setY(y);
			currentColor.setHeight(pickerArea.height());
			// start calculating color width
			int width = 0;
			while (new Color(window.getRGB(x, y)).equals(realColors
					.get(colorIndex))) {
				width++;
				x++;
			}
			currentColor.setWidth(width);
			colors.put(color, currentColor);
			colorIndex++;
		}

		return colors;
	}
}
