package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.ios.locators.IOSLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PendingRequestsPage extends IOSPage {

	@FindBy(how = How.NAME, using = IOSLocators.namePendingRequestIgnoreButton)
	private WebElement ignoreRequestButton;

	@FindBy(how = How.NAME, using = IOSLocators.namePendingRequestConnectButton)
	private WebElement connectRequestButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPendingRequesterName)
	private WebElement requesterName;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPendingRequestMessage)
	private WebElement pendingMessage;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathYouBothKnowPeopleIcon)
	private WebElement youBothKnowPeopleIcon;

	public PendingRequestsPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void clickIgnoreButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), ignoreRequestButton);
		ignoreRequestButton.click();
	}

	public void clickIgnoreButtonMultiple(int clicks)
			throws Exception {
		for (int i = 0; i < clicks; i++) {
			DriverUtils.waitUntilLocatorAppears(this.getDriver(),
					By.name(IOSLocators.namePendingRequestIgnoreButton));
			DriverUtils.waitUntilElementClickable(getDriver(),
					ignoreRequestButton);
			ignoreRequestButton.click();
		}
	}

	public void clickConnectButton() throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				connectRequestButton);
		connectRequestButton.click();
	}

	public void clickConnectButtonMultiple(int clicks) throws Exception {
		for (int i = 0; i < clicks; i++) {
			DriverUtils.waitUntilLocatorAppears(this.getDriver(),
					By.name(IOSLocators.namePendingRequestConnectButton));
			this.getWait().until(
					ExpectedConditions
							.elementToBeClickable(connectRequestButton));
			connectRequestButton.click();
			DriverUtils.waitUntilLocatorAppears(this.getDriver(),
					By.name(IOSLocators.namePendingRequestConnectButton));
		}
	}

	public boolean isConnectButtonDisplayed() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.namePendingRequestConnectButton), 5);
	}

	public String getRequesterName() {
		final String CONNECT_TO = "Connect to ";
		return requesterName.getText().replace(CONNECT_TO, "");
	}

	public boolean isYouBothKnowDisplayed() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.nameYouBothKnowHeader), 5);
	}

	public void clickYouBothKnowPeopleIcon() {
		youBothKnowPeopleIcon.click();
	}

}
