package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.concurrent.Future;

import javax.script.ScriptException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class ImageFullScreenPage extends IOSPage {

	final String[] rotateLeftScript = new String[] {
			"tell application \"System Events\"",
			"tell application \"iOS Simulator\" to activate",
			"tell application \"System Events\" to keystroke (ASCII character 28) using {command down}",
			"end tell" };

	@FindBy(how = How.NAME, using = IOSLocators.nameImageFullScreenPage)
	private WebElement imageFullScreen;

	@FindBy(how = How.NAME, using = IOSLocators.nameFullScreenCloseButton)
	private WebElement fullScreenCloseButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameFullScreenDownloadButton)
	private WebElement fullScreenDownloadButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameFullScreenSenderName)
	private WebElement fullScreenSenderName;

	@FindBy(how = How.NAME, using = IOSLocators.nameFullScreenTimeStamp)
	private WebElement fullScreenTimeStamp;

	@FindBy(how = How.NAME, using = IOSLocators.nameFullScreenSketchButton)
	private WebElement fullScreenSketchButton;

	public ImageFullScreenPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isImageFullScreenShown() {
		return imageFullScreen.isDisplayed();
	}

	public void clickCloseButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(),
				fullScreenCloseButton);
		fullScreenCloseButton.click();
	}

	public boolean isDownloadButtonVisible() {
		return fullScreenDownloadButton.isDisplayed();
	}

	public void clickDownloadButton() {
		fullScreenDownloadButton.click();
	}

	public ImageFullScreenPage tapOnFullScreenPage() {
		imageFullScreen.click();
		return this;
	}

	public boolean isSenderNameVisible() {
		return fullScreenSenderName.isDisplayed();
	}

	public String getSenderName() {
		return fullScreenSenderName.getText();
	}

	public boolean isSentTimeVisible() {
		return fullScreenTimeStamp.isDisplayed();
	}

	public String getTimeStamp() {
		return fullScreenTimeStamp.getText();
	}

	public void clickSketchButton() {
		fullScreenSketchButton.click();
	}

}
