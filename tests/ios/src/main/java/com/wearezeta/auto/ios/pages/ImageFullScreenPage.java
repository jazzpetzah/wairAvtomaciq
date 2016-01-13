package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ImageFullScreenPage extends IOSPage {
	public static final String nameImageFullScreenPage = "fullScreenPage";
	@FindBy(name = nameImageFullScreenPage)
	private WebElement imageFullScreen;

	public static final String nameFullScreenCloseButton = "fullScreenCloseButton";
	@FindBy(name = nameFullScreenCloseButton)
	private WebElement fullScreenCloseButton;

    public static final String nameFullScreenDownloadButton = "fullScreenDownloadButton";
    @FindBy(name = nameFullScreenDownloadButton)
	private WebElement fullScreenDownloadButton;

    public static final String nameFullScreenSenderName = "fullScreenSenderName";
    @FindBy(name = nameFullScreenSenderName)
	private WebElement fullScreenSenderName;

    public static final String nameFullScreenTimeStamp = "fullScreenTimeStamp";
    @FindBy(name = nameFullScreenTimeStamp)
	private WebElement fullScreenTimeStamp;

    public static final String nameFullScreenSketchButton = "sketchButton";
	@FindBy(name = nameFullScreenSketchButton)
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
