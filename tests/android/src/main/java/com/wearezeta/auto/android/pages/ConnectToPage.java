package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ConnectToPage extends AndroidPage {
	@FindBy(id = AndroidLocators.ConnectToPage.idConnectRequestAccept)
	private WebElement connectAcceptBtn;

	@FindBy(id = AndroidLocators.ConnectToPage.idConnectRequestIgnore)
	private WebElement connectIgnoreBtn;

	@FindBy(id = AndroidLocators.ConnectToPage.idPaticipantsPendingLabel)
	private WebElement pendingText;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idConnectionRequiesMessage)
	private WebElement connectionRequestMessage;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idSendConnectionRequestButton)
	private WebElement sendConnectionRequestButton;

	@FindBy(id = AndroidLocators.ConnectToPage.idConnectToCharCounter)
	private WebElement connectCharCounter;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idParticipantsClose)
	private WebElement closeButton;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idRightActionButton)
	private WebElement blockButton;

	@FindBy(id = AndroidLocators.CommonLocators.idConfirmBtn)
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
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(confirmBtn));
		confirmBtn.click();
	}

	public ContactListPage navigateBack() throws Exception {
		// driver.navigate().back();
		swipeRightCoordinates(1000);
		return new ContactListPage(this.getLazyDriver());
	}

	public boolean isConnectToHeaderVisible(String text) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.ConnectToPage.xpathConnectToHeaderByText
						.apply(text));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public DialogPage pressAcceptConnectButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				connectAcceptBtn);
		connectAcceptBtn.click();
		return new DialogPage(this.getLazyDriver());
	}

	public ContactListPage pressIgnoreButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				connectIgnoreBtn);
		connectIgnoreBtn.click();
		return new ContactListPage(this.getLazyDriver());
	}

	public boolean isIgnoreConnectButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(connectIgnoreBtn)
				&& DriverUtils.isElementPresentAndDisplayed(connectAcceptBtn);
	}

	public boolean isPending() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(pendingText);
	}

	public void tapEditConnectionRequest() throws Exception {
		connectionRequestMessage.clear();
	}

	public void typeConnectionRequies(String message) throws Exception {
		connectionRequestMessage.sendKeys(message);
	}

	public ContactListPage pressConnectButton() throws Exception {
		this.getWait().until(
				ExpectedConditions
						.elementToBeClickable(sendConnectionRequestButton));
		sendConnectionRequestButton.click();
		return new ContactListPage(this.getLazyDriver());
	}

	public void waitUntilIgnoreButtonIsVisible() throws Exception {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(connectIgnoreBtn));
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

	public ContactListPage tapConnectNotification() throws Exception {
		final Dimension screenDimension = getDriver().manage().window()
				.getSize();
		AndroidCommonUtils.genericScreenTap(screenDimension.getWidth() / 2,
				screenDimension.getHeight() / 20);
		return new ContactListPage(this.getLazyDriver());
	}

}
