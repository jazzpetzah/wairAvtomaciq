package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class StartedCallPage extends CallPage {

	private static final Logger log = ZetaLogger.getLog(StartedCallPage.class
			.getSimpleName());

	private final double MIN_ACCEPTABLE_SCORE = 0.9;

	@FindBy(how = How.XPATH, using = IOSLocators.StartedCallPage.xpathCallingMessage)
	private WebElement callingMessage;

	@FindBy(how = How.NAME, using = IOSLocators.StartedCallPage.nameEndCallButton)
	private WebElement endCallButton;

	@FindBy(how = How.NAME, using = IOSLocators.StartedCallPage.nameSpeakersButton)
	private WebElement speakersButton;

	@FindBy(how = How.NAME, using = IOSLocators.StartedCallPage.nameMuteCallButton)
	private WebElement muteCallButton;

	@FindBy(how = How.NAME, using = IOSLocators.IncomingCallPage.nameCallingMessageUser)
	private WebElement callingMessageUser;

	public boolean isCallingMessageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(IOSLocators.StartedCallPage.xpathCallingMessage));
	}

	public boolean waitCallingMessageDisappear() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(IOSLocators.StartedCallPage.xpathCallingMessage));
	}

	public boolean isStartedCallMessageVisible(String contact) throws Exception {
		return getDriver()
				.findElementByXPath(
						String.format(
								IOSLocators.StartedCallPage.xpathStartedCallMessageUser,
								contact)).isDisplayed();
	}

	public StartedCallPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isEndCallVisible() throws Exception {
		DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.StartedCallPage.nameEndCallButton));
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				endCallButton);
	}

	public boolean isSpeakersVisible() throws Exception {
		DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.StartedCallPage.nameSpeakersButton));
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				speakersButton);
	}

	public boolean isMuteCallVisible() throws Exception {
		DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.StartedCallPage.nameMuteCallButton));
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				muteCallButton);
	}

	public void clickEndCallButton() {
		endCallButton.click();
	}

	public boolean isMuteCallButtonSelected() throws Exception {
		BufferedImage muteCallButtonIcon = getElementScreenshot(muteCallButton).orElseThrow(
				IllegalStateException::new);
		BufferedImage referenceImage = ImageUtil.readImageFromFile(IOSPage.getImagesPath()
				+ "selectedMuteCallButton.png");

		double score = ImageUtil.getOverlapScore(referenceImage, muteCallButtonIcon,
				ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
		return score > MIN_ACCEPTABLE_SCORE;
	}
}
