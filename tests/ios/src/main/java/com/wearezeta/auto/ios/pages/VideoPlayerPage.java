package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class VideoPlayerPage extends IOSPage {

	public static final String xpathVideoMainPage = "//UIAWebView/UIAButton[@name='Home']";
	@FindBy(xpath = xpathVideoMainPage)
	private WebElement videoPlayerMainWindow;

    public static final String nameVideoDoneButton = "Done";
    @FindBy(name = nameVideoDoneButton)
	private WebElement videoDoneButton;

    public static final String nameVideoSlider = "Track position";
    @FindBy(name = nameVideoSlider)
	private WebElement videoSlider;

    public static final String nameVideoFullScreenButton = "Full screen";
    @FindBy(name = nameVideoFullScreenButton)
	private WebElement videoFullScreenButton;

    public static final String nameVideoPreviousButton = "Previous track";
    @FindBy(name = nameVideoPreviousButton)
	private WebElement videoPreviousButton;

    public static final String nameVideoPauseButton = "PauseButton";
    @FindBy(name = nameVideoPauseButton)
	private WebElement videoPauseButton;

    public static final String nameVideoNextButton = "Next track";
    @FindBy(name = nameVideoNextButton)
	private WebElement videoNextButton;

	public VideoPlayerPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void waitForVideoPlayerPage() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(nameVideoFullScreenButton));
	}

	public boolean isVideoPlayerPageOpened() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(xpathVideoMainPage));
	}

	public void tapVideoPage() {
		videoPlayerMainWindow.click();
	}

	public void clickVideoDoneButton() throws Exception {
		DriverUtils.tapByCoordinates(this.getDriver(), videoDoneButton);
		try {
			DriverUtils.tapByCoordinates(this.getDriver(),
					videoDoneButton);
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
