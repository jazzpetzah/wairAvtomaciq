package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

public class VideoCallPage extends WebPage {

    @FindBy(css = WebAppLocators.VideoCallPage.cssEndVideoCallButton)
    private WebElement endVideoCallButton;

    @FindBy(css = WebAppLocators.VideoCallPage.cssMuteCallButton)
    private WebElement muteVideoCallButton;

    @FindBy(css = WebAppLocators.VideoCallPage.cssMinimizeVideoCallButton)
    private WebElement minimizeVideoCallButton;

    @FindBy(css = WebAppLocators.VideoCallPage.cssMaximizeVideoCallButton)
    private WebElement maximizeVideoCallButton;

    @FindBy(css = WebAppLocators.VideoCallPage.cssCameraButton)
    private WebElement cameraButton;

    @FindBy(css = WebAppLocators.VideoCallPage.cssSelfVideo)
    private WebElement selfVideo;

    @FindBy(css = WebAppLocators.VideoCallPage.cssMinimizedRemoteVideo)
    private WebElement minimizedRemoteVideo;

    @FindBy(css = WebAppLocators.VideoCallPage.cssMaximizedRemoteVideo)
    private WebElement maximizedRemoteVideo;

    @FindBy(css = WebAppLocators.VideoCallPage.cssScreenShareButton)
    private WebElement screenShareButton;

    @FindBy(css = WebAppLocators.VideoCallPage.cssLocalScreenShareVideo)
    private WebElement localScreenShareVideo;

    public VideoCallPage(Future<ZetaWebAppDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void clickEndVideoCallButton() throws Exception {
        WebCommonUtils.hoverOverElement(getDriver(), endVideoCallButton);
        endVideoCallButton.click();
    }

    public boolean isEndVideoCallButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssEndVideoCallButton));
    }

    public boolean isEndVideoCallButtonNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssEndVideoCallButton));
    }

    public boolean isMuteCallButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssMuteCallButton));
    }

    public void clickMuteCallButton() throws Exception {
        WebCommonUtils.hoverOverElement(getDriver(), muteVideoCallButton);
        muteVideoCallButton.click();
    }

    public boolean isMuteCallButtonPressed() throws Exception {
        WebCommonUtils.hoverOverElement(getDriver(), muteVideoCallButton);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(WebAppLocators.VideoCallPage.xpathMuteCallButtonPressed));
    }

    public boolean isMuteCallButtonNotPressed() throws Exception {
        WebCommonUtils.hoverOverElement(getDriver(), muteVideoCallButton);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(WebAppLocators.VideoCallPage.xpathMuteCallButtonNotPressed));
    }

    public boolean isDurationTimerVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssDurationTimer));
    }

    public boolean isDurationTimerNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssDurationTimer));
    }

    public void clickMinimizeVideoCallButton() throws Exception {
        WebCommonUtils.hoverOverElement(getDriver(), minimizeVideoCallButton);
        minimizeVideoCallButton.click();
    }

    public boolean isMinimizeVideoCallButtonVisible() throws Exception {
        WebCommonUtils.hoverOverElement(getDriver(), minimizeVideoCallButton);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssMinimizeVideoCallButton));
    }

    public boolean isMinimizeVideoCallButtonNotVisible() throws Exception {
        WebCommonUtils.hoverOverElement(getDriver(), minimizeVideoCallButton);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssMinimizeVideoCallButton));
    }

    public boolean isMaximizeVideoCallButtonVisible() throws Exception {
        WebCommonUtils.hoverOverElement(getDriver(), maximizeVideoCallButton);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssMaximizeVideoCallButton));
    }

    public boolean isMaximizeVideoCallButtonNotVisible() throws Exception {
        WebCommonUtils.hoverOverElement(getDriver(), maximizeVideoCallButton);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssMaximizeVideoCallButton));
    }

    public void clickMaximizeVideoCallButton() throws Exception {
        WebCommonUtils.hoverOverElement(getDriver(), maximizeVideoCallButton);
        maximizeVideoCallButton.click();
    }

    public boolean isVideoInPortrait() throws Exception {
        By locator = By.cssSelector(WebAppLocators.VideoCallPage.cssVideoPortrait);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isVideoNotInPortrait() throws Exception {
        By locator = By.cssSelector(WebAppLocators.VideoCallPage.cssVideoPortrait);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void clickVideoButton() throws Exception {
        WebCommonUtils.hoverOverElement(getDriver(), cameraButton);
        getDriver().executeScript("arguments[0].click()", cameraButton);
    }

    public boolean isVideoButtonPressed() throws Exception {
        WebCommonUtils.hoverOverElement(getDriver(), cameraButton);
        By locator = By.cssSelector(WebAppLocators.VideoCallPage.cssCameraButtonPressed);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isVideoButtonUnPressed() throws Exception {
        WebCommonUtils.hoverOverElement(getDriver(), cameraButton);
        By locator = By.cssSelector(WebAppLocators.VideoCallPage.cssCameraButtonNotPressed);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public Optional<BufferedImage> getSelfVideo() throws Exception {
        return getElementScreenshot(selfVideo);
    }

    public Optional<BufferedImage> getMinimizedRemoteVideo() throws Exception {
        return getElementScreenshot(minimizedRemoteVideo);
    }

    public Optional<BufferedImage> getMaximizedRemoteVideo() throws Exception {
        return getElementScreenshot(maximizedRemoteVideo);
    }

    public void clickScreenShareButton() throws Exception {
        WebCommonUtils.hoverOverElement(getDriver(), screenShareButton);
        getDriver().executeScript("arguments[0].click()", screenShareButton);
    }

    public WebElement getLocalScreenShareVideoElement() throws Exception {
        return localScreenShareVideo;
    }
}
