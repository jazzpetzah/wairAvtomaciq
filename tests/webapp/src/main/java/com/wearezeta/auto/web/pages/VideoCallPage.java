package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.Future;

public class VideoCallPage extends WebPage {

    public VideoCallPage(Future<ZetaWebAppDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    @FindBy(css = WebAppLocators.VideoCallPage.cssEndVideoCallButton)
    public WebElement endVideoCallButton;

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
}
