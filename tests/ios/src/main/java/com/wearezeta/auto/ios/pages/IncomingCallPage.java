package com.wearezeta.auto.ios.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class IncomingCallPage extends CallPage {

	@FindBy(how = How.NAME, using = IOSLocators.IncomingCallPage.nameAcceptCallButton)
	private WebElement acceptCallButton;

	@FindBy(how = How.NAME, using = IOSLocators.IncomingCallPage.nameEndCallButton)
	private WebElement endCallButton;

	@FindBy(how = How.XPATH, using = IOSLocators.IncomingCallPage.xpathCallingMessage)
	private WebElement callingMessage;

	@FindBy(how = How.NAME, using = IOSLocators.IncomingCallPage.nameIgnoreCallButton)
	private WebElement ignoreCallButton;

	@FindBy(how = How.NAME, using = IOSLocators.IncomingCallPage.nameCallingMessageUser)
	private WebElement callingMessageUser;

	@FindBy(how = How.NAME, using = IOSLocators.IncomingCallPage.nameJoinCallButton)
	private WebElement joinCallButton;

	@FindBy(how = How.NAME, using = IOSLocators.IncomingCallPage.nameSecondCallAlert)
	private WebElement secondCallAlert;

	@FindBy(how = How.NAME, using = IOSLocators.IncomingCallPage.nameAnswerCallAlertButton)
	private WebElement answerCallAlertButton;

	@FindBy(how = How.XPATH, using = IOSLocators.IncomingCallPage.xpathGroupCallAvatars)
	private List<WebElement> numberOfGroupCallAvatars;

	@FindBy(how = How.XPATH, using = IOSLocators.IncomingCallPage.xpathGroupCallFullMessage)
	private WebElement groupCallFullMessage;

	@FindBy(how = How.XPATH, using = IOSLocators.IncomingCallPage.xpathUserInCallContactListCell)
	private List<WebElement> contactListNamesInACall;

	public IncomingCallPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isCallingMessageVisible(String contact) throws Exception {
		return getDriver().findElementByXPath(
				String.format(
						IOSLocators.IncomingCallPage.xpathCallingMessage,
						contact)).isDisplayed();
	}

	public boolean isUserCallingMessageShown(String contact) throws Exception {

		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.name(IOSLocators.IncomingCallPage.nameCallingMessageUser));
	}

	public StartedCallPage acceptIncomingCallClick() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), acceptCallButton);
		acceptCallButton.click();
		return new StartedCallPage(getLazyDriver());
	}

	public void ignoreIncomingCallClick() {
		ignoreCallButton.click();
	}

	public boolean isCallingMessageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(IOSLocators.IncomingCallPage.xpathCallingMessage), 15);
	}

	public boolean isGroupCallingMessageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.IncomingCallPage.nameCallingMessageUser), 15);
	}

	public boolean isJoinCallBarVisible() {
		return joinCallButton.isDisplayed();
	}

	public boolean isSecondCallAlertVisible() {
		return secondCallAlert.isDisplayed();
	}

	public void pressAnswerCallAlertButton() {
		answerCallAlertButton.click();

	}

	public int getNumberOfGroupCallAvatar() throws Exception {
		return numberOfGroupCallAvatars.size();
	}

	public void clickJoinCallButton() {
		joinCallButton.click();
	}

	public boolean isGroupCallFullMessageShown() {
		return groupCallFullMessage.isDisplayed();
	}


	public void tapOnNameInCallWith(String name) throws Exception {
		WebElement el = findNameInContactListWhoIsInACall(name);
		boolean clickableGlitch = false;
		try {
			this.getWait().until(ExpectedConditions.elementToBeClickable(el));
		} catch (org.openqa.selenium.TimeoutException ex) {
			clickableGlitch = true;
		}
		if (clickableGlitch) {
			DriverUtils.tapByCoordinates(getDriver(), el);
		} else {
			el.click();
		}
	}

	private WebElement findNameInContactListWhoIsInACall(String name) throws Exception {
		Boolean flag = true;
		WebElement contact = null;
		for (int i = 0; i < 5; i++) {
			for (WebElement listName : contactListNamesInACall) {
				if (listName.getText().equals(name) && listName.isDisplayed()) {
					contact = listName;
					flag = false;
					break;
				}
			}
			if (flag) {
				if (contactListNamesInACall.isEmpty()) {
					continue;
				}
				WebElement el = contactListNamesInACall
						.get(contactListNamesInACall.size() - 1);
				this.getWait().until(ExpectedConditions.visibilityOf(el));
				this.getWait().until(
						ExpectedConditions.elementToBeClickable(el));
				this.getDriver().scrollToExact(el.getText());
			} else {
				break;
			}
		}
		return contact;
	}
}



