package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.*;

import com.wearezeta.auto.common.locators.ZetaHow;

import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;

public class ConnectToPage extends AndroidPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectToHeader")
	protected WebElement connectToHeader;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectRequestAccept")
	private WebElement connectAcceptBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectRequestIgnore")
	private WebElement connectIgnoreBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idPaticipantsPendingLabel")
	private WebElement pendingText;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idConnectionRequiesMessage")
	private WebElement connectionRequestMessage;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idSendConnectionRequestButton")
	private WebElement sendConnectionRequestButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectToCharCounter")
	private WebElement connectCharCounter;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idParticipantsClose")
	private WebElement closeButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idRightActionButton")
	private WebElement blockButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idConfirmBtn")
	private WebElement confirmBtn;

	public ConnectToPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		return null;
	}

	public void clickBlockBtn() {
		blockButton.click();
	}

	public void pressConfirmBtn() throws Exception {
		refreshUITree();
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(confirmBtn));
		confirmBtn.click();
	}

	public ContactListPage navigateBack() throws Exception {
		refreshUITree();
		// driver.navigate().back();
		swipeRightCoordinates(1000);
		return new ContactListPage(this.getLazyDriver());
	}

	public String getConnectToHeader() throws Exception {
		refreshUITree();
		this.getWait().until(ExpectedConditions.visibilityOf(connectToHeader));
		return connectToHeader.getText().toLowerCase();
	}

	public DialogPage pressAcceptConnectButton() throws Exception {
		connectAcceptBtn.click();
		return new DialogPage(this.getLazyDriver());
	}

	public ContactListPage pressIgnoreButton() throws Exception {
		refreshUITree();
		connectIgnoreBtn.click();
		return new ContactListPage(this.getLazyDriver());
	}

	public boolean isIgnoreConnectButtonVisible() throws Exception {

		return connectIgnoreBtn.isDisplayed() && connectAcceptBtn.isDisplayed();
	}

	public boolean isPending() throws NumberFormatException, Exception {
		return isVisible(pendingText);
	}

	public void tapEditConnectionRequest() throws Exception {
		refreshUITree();
		connectionRequestMessage.clear();
	}

	public void typeConnectionRequies(String message) throws Exception {
		refreshUITree();
		connectionRequestMessage.sendKeys(message);
	}

	public ContactListPage pressConnectButton() throws Exception {
		refreshUITree();
		this.getWait().until(
				ExpectedConditions
						.elementToBeClickable(sendConnectionRequestButton));
		sendConnectionRequestButton.click();
		return new ContactListPage(this.getLazyDriver());
	}

	public boolean getConnectButtonState() {
		String state = sendConnectionRequestButton.getAttribute("enabled");
		return Boolean.parseBoolean(state);
	}

	public int getCharCounterValue() {
		return Integer.parseInt(connectCharCounter.getText());
	}

	public PeoplePickerPage clickCloseButton() throws Exception {
		closeButton.click();
		return new PeoplePickerPage(this.getLazyDriver());
	}

}
