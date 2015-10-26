package com.wearezeta.auto.win.locators;

import static com.wearezeta.auto.win.locators.WinLocators.MainWirePage.xpathWindow;
import com.wearezeta.auto.win.steps.CommonWinSteps;
import java.util.function.Function;

public final class WinLocators {

	public static final class AppMenu {

		public static final Function<String, String> xpathMenuBarItem = (name) -> String
				.format(xpathWindow+"//*[@ControlType='ControlType.MenuItem' and contains(@Name, '%s')]", name);

		public static final Function<String, String> xpathMenuItem = (name) -> String
				.format(xpathWindow+"//*[@ControlType='ControlType.MenuItem' and contains(@Name, '%s')]", name);
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

	public static final class ContactListContextMenuPage {

		public static final String xpathContextArchive = xpathWindow
				+ "//AXMenuItem[@AXTitle='Archive']";
		public static final String xpathContextSilence = xpathWindow
				+ "//AXMenuItem[@AXTitle='Silence']";
		public static final String xpathContextDelete = xpathWindow
				+ "//AXMenuItem[@AXTitle='Delete']";
		public static final String xpathContextBlock = "//AXApplication[@AXTitle='Wire']//AXMenuItem[@AXTitle='Block']";
	}

	public static final class ChoosePicturePage {

		public static final String idOpenButton = "_NS:55";

		public static final String idCancelButton = "_NS:53";

		public static final String xpathFileListScrollArea = "//AXScrollArea";

		public static final String xpathSelectColumnViewButton = "//AXRadioButton[@AXDescription='column view' or @AXLabel='column view']";

		public static final String xpathFormatFinderImageFile = "//AXTextField[@AXValue='%s']";

		public static final String xpathFormatFavoritesFolderPopUp = "//AXStaticText[@AXValue='%s']";
	}
}
