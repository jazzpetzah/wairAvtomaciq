package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ConnectToPage extends AndroidPage {
	public static final String idConnectToHeader = "taet__participants__header";
	@FindBy(id = idConnectToHeader)
	private WebElement connectToHeader;

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

	private static final int MAX_SCROLLS = 8;

	public boolean isConnectToHeaderVisible(String text) throws Exception {
		final By locator = By.xpath(xpathConnectToHeaderByText.apply(text));
		boolean result = DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				locator);
		if (!result)
			log.debug("Can't find correct connect to header. Page source: "
					+ getDriver().getPageSource());
		return result;
	}

	public void scrollToInboxContact(String contactName) throws Exception {
		scrollToInboxContact(contactName, MAX_SCROLLS);
	}

	public void scrollToInboxContact(String contactName, int maxScrolls)
			throws Exception {
		final By locator = By.xpath(xpathConnectToHeaderByText
				.apply(contactName));
		int ntry = 1;
		Boolean swipeUp = true;
		String currentContact = "";
		String latestContact = "";
		do {
			currentContact = connectToHeader.getText();
			int location = connectToHeader.getLocation().y;
			log.debug("Looking for: " + contactName + "; Current contact: "
					+ currentContact);
			if (DriverUtils.waitUntilLocatorAppears(getDriver(), locator, 3)
					&& currentContact.equals(contactName)) {
				log.debug("User had appeared.");
				tapOnCenterOfScreen();
				this.waitUntilIgnoreButtonIsClickable();
				return;
			} else if (location < 0) {
				tapOnCenterOfScreen();
				this.waitUntilIgnoreButtonIsClickable();
				return;
			} else {
				log.debug("User still invisible. Swipe #" + ntry);
				if (latestContact.equals(currentContact)) {
					swipeUp = false;
					ntry = 1;
				}
				if (swipeUp)
					this.swipeUpCoordinates(1000, 50);
				else
					this.swipeDownCoordinates(1000, 50);
			}
			latestContact = currentContact;
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
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				connectIgnoreBtn, 3);
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

	public void waitUntilIgnoreButtonIsClickable() throws Exception {
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
