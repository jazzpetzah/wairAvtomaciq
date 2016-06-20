package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.Future;

public class VideoCallPage extends WebPage {

    @FindBy(css = WebAppLocators.VideoCallPage.cssEndVideoCallButton)
    private WebElement endVideoCallButton;

    @FindBy(css = WebAppLocators.VideoCallPage.cssMuteCallButton)
    private WebElement muteVideoCallButton;

    public VideoCallPage(Future<ZetaWebAppDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void clickEndVideoCallButton() throws Exception {
        this.getDriver().executeScript("arguments[0].click();", endVideoCallButton);
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
        this.getDriver().executeScript("arguments[0].click();", muteVideoCallButton);
    }

    public boolean isMuteCallButtonPressed() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(WebAppLocators.VideoCallPage.xpathMuteCallButtonPressed));
    }

    public boolean isMuteCallButtonNotPressed() throws Exception {
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
}
