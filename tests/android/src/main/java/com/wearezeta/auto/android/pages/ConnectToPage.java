package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ConnectToPage extends AndroidPage {
	public static final String idConnectToHeader = "taet__participants__header";
	@FindBy(id = idConnectToHeader)
	private WebElement connectToHeader;

	@FindBy(id = idConnectToHeader)
	private List<WebElement> connectToHeaders;

	private static final Function<String, String> xpathConnectToHeaderByText = text -> String
			.format("//*[@id='taet__participants__header' and @value='%s']",
					text);

	private static final String idConnectRequestAccept = "zb__connect_request__accept_button";
	@FindBy(id = idConnectRequestAccept)
	private WebElement connectAcceptBtn;

	private static final Function<String, String> xpathAcceptButtonByHeaderText = text -> String
			.format("//*[@id='ll__connect_request__main_container' and .%s]//*[@id='%s']",
					xpathConnectToHeaderByText.apply(text),
					idConnectRequestAccept);

	@FindBy(id = idConnectRequestAccept)
	private List<WebElement> connectAcceptBtns;

	private static final String idConnectRequestIgnore = "zb__connect_request__ignore_button";
	@FindBy(id = idConnectRequestIgnore)
	private WebElement connectIgnoreBtn;

	private static final Function<String, String> xpathUserDetailsLeftButton = label -> String
			.format("//*[@id='ttv__participants__left_label' and @value='%s']",
					label);

	@FindBy(id = PeoplePickerPage.idSendConnectionRequestButton)
	private WebElement sendConnectionRequestButton;

	private static final String idConnectToCharCounter = "ttv__send_connect_request__connect_button__character_counter";
	@FindBy(id = idConnectToCharCounter)
	private WebElement connectCharCounter;

	@FindBy(id = PeoplePickerPage.idParticipantsClose)
	private WebElement closeButton;

	@FindBy(id = OtherUserPersonalInfoPage.idRightActionButton)
	private WebElement blockButton;

	@FindBy(id = OtherUserPersonalInfoPage.idUnblockBtn)
	private WebElement unblockButton;

	@FindBy(xpath = xpathConfirmBtn)
	private WebElement confirmBtn;

	private final CommonSteps commonSteps = CommonSteps.getInstance();

	public ConnectToPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		return null;
	}

	public void clickBlockBtn() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), blockButton);
		blockButton.click();
	}

	public void clickUnblockBtn() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), unblockButton);
		unblockButton.click();
	}

	public void pressConfirmBtn() throws Exception {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(confirmBtn));
		confirmBtn.click();
	}

	public ContactListPage navigateBack() throws Exception {
		AndroidCommonUtils.tapBackButton();
		commonSteps.WaitForTime(0.5);
		return new ContactListPage(this.getLazyDriver());
	}

	public boolean isConnectToHeaderVisible(String name) throws Exception {
		final By locator = By.xpath(xpathConnectToHeaderByText.apply(name));
		boolean result = DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				locator);
		if (!result)
			log.debug("Can't find correct connect to header. Page source: "
					+ getDriver().getPageSource());
		return result;
	}

	public void scrollToInboxContact(String contactName, int maxScrolls)
			throws Exception {
		final By locator = By.xpath(xpathAcceptButtonByHeaderText
				.apply(contactName));
		int ntry = 1;
		do {
			if (DriverUtils
					.waitUntilLocatorIsDisplayed(getDriver(), locator, 1)) {
				return;
			}
			this.swipeUpCoordinates(1000, 50);
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
		if (connectAcceptBtn.getLocation().getY() > this.getDriver().manage()
				.window().getSize().getHeight() / 2
				&& connectAcceptBtn.getLocation().getY() < this.getDriver()
						.manage().window().getSize().getHeight()) {
			connectAcceptBtn.click();
		} else {
			connectAcceptBtns.get(1).click();
		}
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
		return DriverUtils.isElementPresentAndDisplayed(
				getDriver(),
				getDriver().findElement(
						By.xpath(xpathUserDetailsLeftButton.apply("Pending"))));
	}

	public void pressLeftConnectButton() throws Exception {
		final WebElement leftConnectBtn = getDriver().findElement(
				By.xpath(xpathUserDetailsLeftButton.apply("Connect")));
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(leftConnectBtn));
		leftConnectBtn.click();

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
