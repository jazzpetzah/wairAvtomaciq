package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class ConnectToPage extends IOSPage {

	private static final int MAX_MESSAGE_CHARACTERS = 140;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathConnectCloseButton)
	private WebElement closeConnectDialoButon;

	@FindBy(how = How.NAME, using = IOSLocators.nameSendConnectButton)
	private WebElement sendConnectButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathConnectOtherUserButton)
	private WebElement connectOtherUserButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameIgnoreOtherUserButton)
	private WebElement ignoreOtherUserButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameSendConnectionInputField)
	private WebElement sendConnectionInput;
	
	private static final Logger log = ZetaLogger.getLog(ConnectToPage.class
			.getSimpleName());

	private String inviteMessage = CommonSteps.CONNECTION_MESSAGE;

	public ConnectToPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public Boolean isConnectToUserDialogVisible() {
		return sendConnectionInput.isDisplayed();
	}

	public String getConnectToUserLabelValue() {
		return sendConnectionInput.getText();
	}
	
	public void fillTextInConnectDialogWithLengh(int numberOfCharacters) {
		String text = CommonUtils.generateRandomStringFromAlphanumericPlusSymbolsWithLengh(numberOfCharacters);
		sendConnectionInput.sendKeys(text);
	}

	public void fillHelloTextInConnectDialog() {
		sendConnectionInput.sendKeys(inviteMessage);
	}
	
	public void inputCharactersIntoConnectDialogByScript(int numberOfCharacters) throws Exception {
		String text = CommonUtils.generateRandomStringFromAlphanumericPlusSymbolsWithLengh(numberOfCharacters);
		scriptInputConnectDialog(text);
	}
	
	private void scriptInputConnectDialog(String text) throws Exception {
		getWait().until(ExpectedConditions.elementToBeClickable(sendConnectionInput));
		String script = String.format(IOSLocators.scriptSendConnectionInput
				+ ".setValue(\"%s\")", text);
		try {
			this.getDriver().executeScript(script);
		} catch (WebDriverException ex) {
			log.debug("Appium execute script fail. " + ex.getMessage());
		}
	}

	public boolean isMaxCharactersInMessage() {
		int messageCharCount = sendConnectionInput.getText().length();
		if (messageCharCount != MAX_MESSAGE_CHARACTERS) {
			return false;
		} else {
			return true;
		}
	}

	public void deleteTextInConnectDialog() {
		sendConnectionInput.clear();
		// additional steps required because clear() does not disable the
		// connect button
		sendConnectionInput.sendKeys("a");
		clickKeyboardDeleteButton();
	}

	public PeoplePickerPage clickSendButton() throws Throwable {
		sendConnectButton.click();
		return new PeoplePickerPage(this.getLazyDriver());
	}

	public ContactListPage sendInvitation(String name) throws Exception {
		ContactListPage page = null;
		fillHelloTextInConnectDialog();
		sendConnectButton.click();
		page = new ContactListPage(this.getLazyDriver());
		return page;
	}

	public PeoplePickerPage closeConnectDialog() throws Exception {
		closeConnectDialoButon.click();
		return new PeoplePickerPage(this.getLazyDriver());
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean waitForConnectDialog() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.className(IOSLocators.clasNameConnectDialogLabel));
	}

	public boolean isConnectButtonVisible() {
		return connectOtherUserButton.isDisplayed();
	}

	public PeoplePickerPage sendInvitation() throws Exception {
		connectOtherUserButton.click();
		return new PeoplePickerPage(this.getLazyDriver());
	}

	public void acceptInvitation() {
		connectOtherUserButton.click();
	}

	public boolean isSendButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.nameSendConnectButton));
	}

	public boolean isSendConnectionInputVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.nameSendConnectionInputField));
	}

}
