package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.WebElement;

public class ImageFullScreenPage extends IOSPage {
    private static final By nameImageFullScreenPage = MobileBy.AccessibilityId("fullScreenPage");

    private static final By nameFullScreenCloseButton = MobileBy.AccessibilityId("fullScreenCloseButton");

    private static final By xpathFullScreenImage =
            By.xpath("//XCUIElementTypeScrollView[@name='fullScreenPage']/XCUIElementTypeImage");

    public ImageFullScreenPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isImageFullScreenShown() throws Exception {
        return isDisplayed(nameImageFullScreenPage);
    }

    public void tapCloseButton() throws Exception {
        final WebElement closeBtn = getElement(nameFullScreenCloseButton);
        closeBtn.click();
        if (!isInvisible(nameFullScreenCloseButton)) {
            closeBtn.click();
        }
    }

    public void tapOnFullScreenPage() throws Exception {
        getElement(nameImageFullScreenPage).click();
    }

    public Optional<BufferedImage> getPreviewPictureScreenshot() throws Exception {
        return getElementScreenshot(getElement(xpathFullScreenImage));
    }
}
