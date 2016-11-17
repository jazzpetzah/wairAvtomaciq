package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class VideoPlayerPage extends IOSPage {
    private static final By xpathVideoMainPage = By.className("XCUIElementTypeWebView");

    private static final By nameVideoDoneButton = MobileBy.AccessibilityId("Done");

    public VideoPlayerPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVideoPlayerPageOpened() throws Exception {
        return isLocatorExist(xpathVideoMainPage);
    }

    public boolean isVideoMessagePlayerPageDoneButtonVisible() throws Exception {
        return isLocatorExist(nameVideoDoneButton, 20);
    }
}
