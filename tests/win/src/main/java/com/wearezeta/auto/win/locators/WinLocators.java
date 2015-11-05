package com.wearezeta.auto.win.locators;

import static com.wearezeta.auto.win.locators.WinLocators.MainWirePage.xpathWindow;
import java.util.function.Function;

public final class WinLocators {

	public static final class AppMenu {

		public static final Function<String, String> xpathMenuBarItem = (name) -> String
				.format(xpathWindow
						+ "//*[@ControlType='ControlType.MenuItem' and contains(@Name, '%s')]",
						name);

		public static final Function<String, String> xpathMenuItem = (name) -> String
				.format(xpathWindow
						+ "//*[@ControlType='ControlType.MenuItem' and contains(@Name, '%s')]",
						name);
	}

	public static final class MainWirePage {

		public static final String xpathWindow = "/*[@ClassName='Chrome_WidgetWin_1' and @ControlType='ControlType.Window' and contains(@Name,'Wire')]";

		public static final String xpathCloseButton = xpathWindow
				+ "/*[@Name='Close']";

		public static final String xpathMinimizeButton = xpathWindow
				+ "//*[@Name='minimize']";

		public static final String xpathZoomButton = xpathWindow
				+ "//*[@Name='Maximize']";
	}

	public static final class AboutPage {

		public static final String xpathWindow = "/*[@ClassName='Chrome_WidgetWin_1' and @ControlType='ControlType.Window' and @Name='']";

		public static final String xpathCloseButton = xpathWindow
				+ "/*[@ControlType='ControlType.TitleBar']/*[@ControlType='ControlType.Button' and @Name='Close']";

		public static final String xpathMinimizeButton = xpathWindow
				+ "//*[@Name='Minimize']";
	}

}
