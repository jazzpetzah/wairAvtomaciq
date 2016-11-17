package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.WebElement;

public class ImageFullScreenPage extends IOSPage {
    private static final By nameImageFullScreenPage = MobileBy.AccessibilityId("fullScreenPage");

    private static final By nameFullScreenCloseButton = MobileBy.AccessibilityId("fullScreenCloseButton");

    private static final By nameImageContainer = MobileBy.AccessibilityId("fullScreenPage");
    private static final By classImage = By.className("XCUIElementTypeImage");

    public ImageFullScreenPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isImageFullScreenShown() throws Exception {
        return isLocatorDisplayed(nameImageFullScreenPage);
    }

    public void tapCloseButton() throws Exception {
        final WebElement closeBtn = getElement(nameFullScreenCloseButton);
        closeBtn.click();
        if (!isLocatorInvisible(nameFullScreenCloseButton)) {
            closeBtn.click();
        }
    }

    public Optional<BufferedImage> getPreviewPictureScreenshot() throws Exception {
        final List<WebElement> visibleImages = getElement(nameImageContainer).findElements(classImage).
                stream().filter(WebElement::isDisplayed).collect(Collectors.toList());
        if (visibleImages.size() > 0) {
            return getElementScreenshot(visibleImages.get(0));
        }
        throw new IllegalStateException("No visible images are detected in fullscreen mode");
    }
}
