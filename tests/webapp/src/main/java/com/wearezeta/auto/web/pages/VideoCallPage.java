package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.*;

import java.util.concurrent.Future;

public class VideoCallPage extends WebPage {

    public VideoCallPage(Future<ZetaWebAppDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void clickEndVideoCallButton() throws Exception {
        this.getDriver()
                .executeScript(
                        String.format("$(document).find(\"%s\").click();",
                                WebAppLocators.VideoCallPage.cssEndVideoCallButton));
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
        this.getDriver()
                .executeScript(
                        String.format("$(document).find(\"%s\").click();",
                                WebAppLocators.VideoCallPage.cssMuteCallButton));
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

    public void clickMinimizeVideoCallButton() throws Exception {
        this.getDriver()
                .executeScript(
                        String.format("$(document).find(\"%s\").click();",
                                WebAppLocators.VideoCallPage.cssMinimizeVideoCallButton));
    }

    public boolean isMinimizeVideoCallButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssMinimizeVideoCallButton));
    }

    public boolean isMinimizeVideoCallButtonNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssMinimizeVideoCallButton));
    }

    public boolean isMaximizeVideoCallButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssMaximizeVideoCallButton));
    }

    public boolean isMaximizeVideoCallButtonNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssMaximizeVideoCallButton));
    }

    public void clickMaximizeVideoCallButton() throws Exception {
        this.getDriver()
                .executeScript(
                        String.format("$(document).find(\"%s\").click();",
                                WebAppLocators.VideoCallPage.cssMaximizeVideoCallButton));
    }
}
