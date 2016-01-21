package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ImageFullScreenPage extends IOSPage {
    private static final By nameImageFullScreenPage = By.name("fullScreenPage");

    private static final By nameFullScreenCloseButton = By.name("fullScreenCloseButton");

    private static final By nameFullScreenDownloadButton = By.name("fullScreenDownloadButton");

    private static final Function<String, String> xpathStrFullScreenSenderByName = name ->
            String.format("//*[@name='fullScreenSenderName' and contains(@value, '%s')]", name);

    private static final By nameFullScreenTimeStamp = By.name("fullScreenTimeStamp");

    private static final By nameFullScreenSketchButton = By.name("sketchButton");

    public ImageFullScreenPage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public boolean isImageFullScreenShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameImageFullScreenPage);
    }

    public void clickCloseButton() throws Exception {
        getElement(nameFullScreenCloseButton, "Close button is not present in the view").click();
    }

    public boolean isDownloadButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameFullScreenDownloadButton);
    }

    public void clickDownloadButton() throws Exception {
        getElement(nameFullScreenDownloadButton).click();
    }

    public void tapOnFullScreenPage() throws Exception {
        getElement(nameImageFullScreenPage).click();
    }

    public boolean isSenderNameVisible(String expectedName) throws Exception {
        final By locator = By.xpath(xpathStrFullScreenSenderByName.apply(expectedName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isSentTimeVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameFullScreenTimeStamp);
    }

    public String getTimeStamp() throws Exception {
        return getElement(nameFullScreenTimeStamp).getText();
    }

    public void clickSketchButton() throws Exception {
        getElement(nameFullScreenSketchButton).click();
    }

}
