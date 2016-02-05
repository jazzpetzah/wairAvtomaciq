package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.concurrent.Future;

public class VideoCallPage extends WebPage {


	private static final int TIMEOUT_END_VIDEO_CALL_BUTTON = 10; // seconds

	private final Future<ZetaWebAppDriver> lazyDriver;
	@FindBy(how = How.CSS, using = WebAppLocators.VideoCallPage.cssEndVideoCallButton)
	private WebElement endVideoCallButton;

	public VideoCallPage(Future<ZetaWebAppDriver> lazyDriver, WebElement endVideoCallButton)
			throws Exception {
		super(lazyDriver);
		this.lazyDriver = lazyDriver;
		this.endVideoCallButton = endVideoCallButton;
	}

	public void clickEndVideoCallButton() throws Exception {
		final By locator = By
				.cssSelector(WebAppLocators.VideoCallPage.cssEndVideoCallButton);
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator) : "End video call button has not been shown after "
				 + TIMEOUT_END_VIDEO_CALL_BUTTON +" seconds";
		getDriver().findElement(locator).click();
	}
}
