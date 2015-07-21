package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ConnectToPage extends AndroidPage {
	public static final String idConnectToHeader = "taet__participants__header";

	private static final Function<String, String> xpathConnectToHeaderByText = text -> String
			.format("//*[@id='taet__participants__header' and @value='%s']",
					text);

	private static final String idConnectRequestAccept = "zb__connect_request__accept_button";
	@FindBy(id = idConnectRequestAccept)
	private WebElement connectAcceptBtn;

	private static final String idConnectRequestIgnore = "zb__connect_request__ignore_button";
	@FindBy(id = idConnectRequestIgnore)
	private WebElement connectIgnoreBtn;

	private static final String idPaticipantsPendingLabel = "ttv__participants__left_label";
	@FindBy(id = idPaticipantsPendingLabel)
	private WebElement pendingText;

	@FindBy(id = PeoplePickerPage.idConnectionRequiesMessage)
	private WebElement connectionRequestMessage;

	@FindBy(id = PeoplePickerPage.idSendConnectionRequestButton)
	private WebElement sendConnectionRequestButton;

	private static final String idConnectToCharCounter = "ttv__send_connect_request__connect_button__character_counter";
	@FindBy(id = idConnectToCharCounter)
	private WebElement connectCharCounter;

	@FindBy(id = PeoplePickerPage.idParticipantsClose)
	private WebElement closeButton;

	@FindBy(id = OtherUserPersonalInfoPage.idRightActionButton)
	private WebElement blockButton;

	@FindBy(id = idConfirmBtn)
	private WebElement confirmBtn;

	// private final CommonSteps commonSteps = CommonSteps.getInstance();

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

	private static final int MAX_SCROLLS = 5;

	public boolean isConnectToHeaderVisible(String text) throws Exception {
		final By locator = By.xpath(xpathConnectToHeaderByText.apply(text));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void scrollToInboxContact(String contactName) throws Exception {
		scrollToInboxContact(contactName, MAX_SCROLLS);
	}

	public void scrollToInboxContact(String contactName, int maxScrolls)
			throws Exception {
		final By locator = By.xpath(xpathConnectToHeaderByText
				.apply(contactName));
		int ntry = 1;
		do {
			if (DriverUtils
					.waitUntilLocatorIsDisplayed(getDriver(), locator, 3)) {
				this.waitUntilIgnoreButtonIsVisible();
				this.swipeUpCoordinates(1000, 50);
			} else {
				this.waitUntilIgnoreButtonIsVisible();
				this.swipeDownCoordinates(1000, 50);
				return;
			}
			ntry++;
		} while (ntry <= maxScrolls);
		throw new RuntimeException(
				String.format(
						"Failed to find user %s in the inbox after scrolling %s users!",
						contactName, maxScrolls));
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
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				connectIgnoreBtn)
				&& DriverUtils.isElementPresentAndDisplayed(getDriver(),
						connectAcceptBtn);
	}

	public boolean isPending() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				pendingText);
	}

	public void tapEditConnectionRequest() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(PeoplePickerPage.idConnectionRequiesMessage)) : "The invitation input field is not visible";
		connectionRequestMessage.clear();
	}

	public void typeConnectionRequest(String message) throws Exception {
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

}
