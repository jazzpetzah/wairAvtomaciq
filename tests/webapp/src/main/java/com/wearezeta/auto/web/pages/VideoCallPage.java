package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.concurrent.Future;

public class VideoCallPage extends WebPage {

	public VideoCallPage (Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
        super(lazyDriver);
    }

	@FindBy(css = WebAppLocators.VideoCallPage.cssEndVideoCallButton)
	public WebElement endVideoCallButton;

	public void clickEndVideoCallButton() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), endVideoCallButton);
        endVideoCallButton.click();

	}

    public boolean isEndVideoCallButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssEndVideoCallButton), 7000);
    }
}
