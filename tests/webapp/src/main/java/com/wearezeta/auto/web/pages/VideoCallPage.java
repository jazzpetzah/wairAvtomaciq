package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
//import org.openqa.selenium.support.FindBy;

import java.util.concurrent.Future;

public class VideoCallPage extends WebPage {

	public VideoCallPage (Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
        super(lazyDriver);
    }

	@FindBy(css = WebAppLocators.VideoCallPage.cssEndVideoCallButton)
	public WebElement endVideoCallButton;

    public void clickEndVideoCallButton() throws Exception {
        Actions builder = new Actions(this.getDriver());
        builder.moveToElement(endVideoCallButton, -10, -10).click().build().perform();
    }

//	public void clickEndVideoCallButton() throws Exception {
////        Thread.sleep(10000);
////        DriverUtils.waitUntilElementClickable(this.getDriver(), endVideoCallButton);
//        endVideoCallButton.click();
//	}

    public boolean isEndVideoCallButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.VideoCallPage.cssEndVideoCallButton));
    }
}
