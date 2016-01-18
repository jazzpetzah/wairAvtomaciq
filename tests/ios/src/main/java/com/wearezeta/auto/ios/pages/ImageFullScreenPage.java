package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ImageFullScreenPage extends IOSPage {
    private static final String nameImageFullScreenPage = "fullScreenPage";
    @FindBy(name = nameImageFullScreenPage)
    private WebElement imageFullScreen;

    private static final String nameFullScreenCloseButton = "fullScreenCloseButton";
    @FindBy(name = nameFullScreenCloseButton)
    private WebElement fullScreenCloseButton;

    private static final String nameFullScreenDownloadButton = "fullScreenDownloadButton";
    @FindBy(name = nameFullScreenDownloadButton)
    private WebElement fullScreenDownloadButton;

    private static final String nameFullScreenSenderName = "fullScreenSenderName";
    @FindBy(name = nameFullScreenSenderName)
    private WebElement fullScreenSenderName;

    private static final String nameFullScreenTimeStamp = "fullScreenTimeStamp";
    @FindBy(name = nameFullScreenTimeStamp)
    private WebElement fullScreenTimeStamp;

    private static final String nameFullScreenSketchButton = "sketchButton";
    @FindBy(name = nameFullScreenSketchButton)
    private WebElement fullScreenSketchButton;

    public ImageFullScreenPage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public boolean isImageFullScreenShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameImageFullScreenPage));
    }

    public void clickCloseButton() throws Exception {
        verifyLocatorPresence(By.name(nameFullScreenCloseButton), "Close button is not present in the view");
        fullScreenCloseButton.click();
    }

    public boolean isDownloadButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.name(nameFullScreenDownloadButton));
    }

    public void clickDownloadButton() {
        fullScreenDownloadButton.click();
    }

    public ImageFullScreenPage tapOnFullScreenPage() {
        imageFullScreen.click();
        return this;
    }

    public boolean isSenderNameVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameFullScreenSenderName));
    }

    public String getSenderName() {
        return fullScreenSenderName.getText();
    }

    public boolean isSentTimeVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameFullScreenTimeStamp));
    }

    public String getTimeStamp() {
        return fullScreenTimeStamp.getText();
    }

    public void clickSketchButton() {
        fullScreenSketchButton.click();
    }

}
