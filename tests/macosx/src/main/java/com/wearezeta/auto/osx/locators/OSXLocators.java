package com.wearezeta.auto.osx.locators;

import static com.wearezeta.auto.osx.locators.OSXLocators.MainWirePage.xpathWindow;
import java.util.function.Function;

public final class OSXLocators {

    public static final class AppMenu {

        public static final Function<String, String> xpathMenuBarItem = (name) -> String
                .format("//AXMenuBarItem[contains(@AXTitle, '%s')]", name);

        public static final Function<String, String> xpathMenuItem = (name) -> String
                .format("/AXMenu/AXMenuItem[contains(@AXTitle, '%s')]", name);
    }

    public static final class NoInternetConnectionPage {

        public static final String idOKButton = "_NS:14";

        public static final String xpathNoInternetMessage = "//AXStaticText[contains(@AXValue,'No Internet Connection')]";
    }

    public static final class ProblemReportPage {

        public static final String idWindow = "_NS:162";

        public static final String nameCancelButton = "Cancel";

        public static final String nameSendButton = "Send";
    }

    public static final class MainWirePage {

        public static final String xpathWindow = "//AXApplication[contains(@AXTitle,'Wire')]//AXWindow[contains(@AXTitle,'Wire')]";

        public static final String xpathCloseButton = xpathWindow
                + "//AXButton[@AXRoleDescription='close button']";

        public static final String xpathMinimizeButton = xpathWindow
                + "//AXButton[@AXRoleDescription='minimize button']";

        public static final String xpathZoomButton = xpathWindow
                + "//AXButton[@AXRoleDescription='zoom button']";
    }

    public static final class AboutPage {

        public static final String xpathWindow = "//AXApplication[contains(@AXTitle,'Wire')]//AXWindow[@AXTitle='']";

        public static final String xpathCloseButton = xpathWindow
                + "//AXButton[@AXRoleDescription='close button']";

        public static final String xpathMinimizeButton = xpathWindow
                + "//AXButton[@AXRoleDescription='minimize button']";
    }

    public static final class ContactListContextMenuPage {

        public static final String xpathContextArchive = xpathWindow
                + "//AXMenuItem[@AXTitle='Archive']";
        public static final String xpathContextSilence = xpathWindow
                + "//AXMenuItem[@AXTitle='Silence']";
        public static final String xpathContextDelete = xpathWindow
                + "//AXMenuItem[@AXTitle='Delete']";
        public static final String xpathContextBlock = xpathWindow
                + "//AXMenuItem[@AXTitle='Block']";
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
