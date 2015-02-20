package com.wearezeta.auto.ios.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class PendingRequestsPage extends IOSPage {

	@FindBy(how = How.NAME, using = IOSLocators.namePendingRequestIgnoreButton)
	private WebElement ignoreRequestButton;

	@FindBy(how = How.NAME, using = IOSLocators.namePendingRequestConnectButton)
	private WebElement connectRequestButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPendingRequesterName)
	private WebElement requesterName;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPendingRequestMessage)
	private WebElement pendingMessage;

	private String autoHelloMessage = CommonSteps.CONNECTION_MESSAGE;

	public PendingRequestsPage(ZetaIOSDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public ContactListPage clickIgnoreButton() throws Throwable {
		ContactListPage page = null;
		ignoreRequestButton.click();
		page = new ContactListPage(this.getDriver(), this.getWait());
		return page;
	}

	public ContactListPage clickIgnoreButtonMultiple(int clicks)
			throws Throwable {
		ContactListPage page = null;
		for (int i = 0; i < clicks; i++) {
			DriverUtils.waitUntilElementAppears(driver,
					By.name(IOSLocators.namePendingRequestIgnoreButton));
			this.getWait().until(
					ExpectedConditions
							.elementToBeClickable(ignoreRequestButton));
			ignoreRequestButton.click();
			DriverUtils.waitUntilElementAppears(driver,
					By.name(IOSLocators.namePendingRequestIgnoreButton));
		}
		page = new ContactListPage(this.getDriver(), this.getWait());
		return page;
	}

	public ContactListPage clickConnectButton() throws Throwable {
		ContactListPage page = null;
		DriverUtils.waitUntilElementClickable(driver, connectRequestButton);
		connectRequestButton.click();
		page = new ContactListPage(this.getDriver(), this.getWait());
		return page;
	}

	public ContactListPage clickConnectButtonMultiple(int clicks)
			throws Throwable {
		ContactListPage page = null;
		for (int i = 0; i < clicks; i++) {
			DriverUtils.waitUntilElementAppears(driver,
					By.name(IOSLocators.namePendingRequestConnectButton));
			this.getWait().until(
					ExpectedConditions
							.elementToBeClickable(connectRequestButton));
			connectRequestButton.click();
			DriverUtils.waitUntilElementAppears(driver,
					By.name(IOSLocators.namePendingRequestConnectButton));
		}
		page = new ContactListPage(this.getDriver(), this.getWait());
		return page;
	}

	public boolean isConnectButtonDisplayed() {
		return DriverUtils.isElementDisplayed(connectRequestButton);
	}

	public String getRequesterName() {
		return requesterName.getText();
	}

	public String getRequestMessage() {
		return pendingMessage.getText();
	}

	public boolean isAutoMessageCorrect() {
		return getRequestMessage().equals(autoHelloMessage);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {

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
			page = new ContactListPage(this.getDriver(), this.getWait());
			break;
		}
		}
		return page;
	}

}
