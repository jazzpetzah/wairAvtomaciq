package com.wearezeta.auto.ios.pages;

import java.io.IOException;

import javax.script.ScriptException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

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

	public ImageFullScreenPage(ZetaIOSDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isImageFullScreenShown() {
		return imageFullScreen.isDisplayed();
	}

	public DialogPage clickCloseButton() throws Exception {
		DialogPage page = null;
		fullScreenCloseButton.click();
		page = new DialogPage(this.getDriver(), this.getWait());
		return page;
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
	
	public void rotateSimulatorLeft() throws ScriptException{
		cmdVscript(rotateLeftScript);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		IOSPage page = null;
		switch (direction) {
		case DOWN: {
			break;
		}
		case UP: {
			break;
		}
		case LEFT: {
			break;
		}
		case RIGHT: {
			break;
		}
		}
		return page;
	}

}
