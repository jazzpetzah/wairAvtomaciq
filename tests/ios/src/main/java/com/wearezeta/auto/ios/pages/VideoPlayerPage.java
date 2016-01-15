package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class VideoPlayerPage extends IOSPage {

	private static final String xpathVideoMainPage = "//UIAWebView/UIAButton[@name='Home']";
	@FindBy(xpath = xpathVideoMainPage)
	private WebElement videoPlayerMainWindow;

	private static final String nameVideoDoneButton = "Done";
    @FindBy(name = nameVideoDoneButton)
	private WebElement videoDoneButton;

	private static final String nameVideoSlider = "Track position";
    @FindBy(name = nameVideoSlider)
	private WebElement videoSlider;

	private static final String nameVideoFullScreenButton = "Full screen";
    @FindBy(name = nameVideoFullScreenButton)
	private WebElement videoFullScreenButton;

	private static final String nameVideoPreviousButton = "Previous track";
    @FindBy(name = nameVideoPreviousButton)
	private WebElement videoPreviousButton;

	private static final String nameVideoPauseButton = "PauseButton";
    @FindBy(name = nameVideoPauseButton)
	private WebElement videoPauseButton;

	private static final String nameVideoNextButton = "Next track";
    @FindBy(name = nameVideoNextButton)
	private WebElement videoNextButton;

	public VideoPlayerPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void waitForVideoPlayerPage() throws Exception {
		DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(nameVideoFullScreenButton));
	}

	public boolean isVideoPlayerPageOpened() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.xpath(xpathVideoMainPage));
	}

	public void tapVideoPage() {
		videoPlayerMainWindow.click();
	}

	public void clickVideoDoneButton() throws Exception {
		DriverUtils.tapByCoordinates(this.getDriver(), videoDoneButton);
		try {
			DriverUtils.tapByCoordinates(this.getDriver(), videoDoneButton);
		} catch (WebDriverException e) {
			// ignore silently
		}
	}

	public void clickPauseButton() throws Exception {
		if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(nameVideoPauseButton))) {
			videoPauseButton.click();
		} else {
			tapVideoPage();
			videoPauseButton.click();
		}
	}
}
