package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.common.KeyboardMapper;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class PeoplePickerPage extends AndroidPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(PeoplePickerPage.class
			.getSimpleName());

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerSearchUsers)
	private List<WebElement> pickerSearchUsers;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerSearchUsers)
	private WebElement pickerSearchUser;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerTopPeopleHeader)
	private WebElement pickerTopPeopleHeader;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerUserSlidingRow)
	private List<WebElement> pickerUserSlidingRow;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPeoplePickerSerchConversations)
	private List<WebElement> pickerSearchConversations;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPeoplePickerClearbtn)
	private WebElement pickerClearBtn;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerRows)
	private List<WebElement> pickerSearchRows;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerUserHideMenu)
	private WebElement pickerUserHideMenu;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerUsersUnselected)
	private List<WebElement> pickerUsersUnselected;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerSearch)
	private WebElement pickerSearch;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerSearch)
	private List<WebElement> pickerSearchList;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerGrid)
	private WebElement pickerGrid;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerBtnDone)
	private WebElement addToConversationsButton;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idCreateConversationIcon)
	private WebElement createConversation;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idNoResultsFound)
	private WebElement noResults;

	@FindBy(xpath = AndroidLocators.PeoplePickerPage.xpathSendInvitationFrame)
	private WebElement sendInvitationFrame;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idSendInvitationBubble)
	private WebElement sendInvitationBubble;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerListContainer)
	private WebElement content;

	@FindBy(id = AndroidLocators.ConnectToPage.idConnectToHeader)
	private List<WebElement> connectToHeader;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerRecomendedName)
	private WebElement recommendedName;

	@FindBy(xpath = AndroidLocators.PeoplePickerPage.xpathGmailLink)
	private WebElement gmailLink;

	public PeoplePickerPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void tapPeopleSearch() {
		pickerSearch.click();
	}

	public void tapOnContactInTopPeoples(String name) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.PeoplePickerPage.xpathTopConversationContactByName
						.apply(name));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		this.getDriver().findElement(locator).click();
	}

	public void typeTextInPeopleSearch(String contactName) throws Exception {
		for (char ch : contactName.toCharArray()) {
			int keyCode = KeyboardMapper.getPrimaryKeyCode(ch);
			this.getDriver().sendKeyEvent(keyCode);
		}
		this.getDriver().sendKeyEvent(66);
	}

	public void addTextToPeopleSearch(String contactName) throws Exception {
		for (char ch : contactName.toCharArray()) {
			int keyCode = KeyboardMapper.getPrimaryKeyCode(ch);
			this.getDriver().sendKeyEvent(keyCode);
		}
	}

	public boolean isNoResultsFoundVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(noResults);
	}

	public Boolean ispTopPeopleHeaderVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(pickerTopPeopleHeader);
	}

	public AndroidPage selectContact(String contactName) throws Exception {
		pickerSearchUser.click();
		DriverUtils.turnOffImplicitWait(this.getDriver());
		try {
			if (this.getDriver()
					.findElements(
							By.id(AndroidLocators.OtherUserPersonalInfoPage.idUnblockBtn))
					.size() > 0) {
				return new OtherUserPersonalInfoPage(this.getLazyDriver());

			} else if (this
					.getDriver()
					.findElements(
							By.id(AndroidLocators.ConnectToPage.idConnectToHeader))
					.size() > 0) {
				return new ConnectToPage(this.getLazyDriver());
			} else if (DriverUtils
					.isElementPresentAndDisplayed(addToConversationsButton)) {
				return this;
			} else {
				return new DialogPage(this.getLazyDriver());
			}
		} finally {
			DriverUtils.restoreImplicitWait(this.getDriver());
		}
	}

	public AndroidPage selectGroup(String contactName) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.PeoplePickerPage.xpathPeoplePickerGroupByName
						.apply(contactName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		this.getDriver().findElement(locator).click();

		if (DriverUtils.isElementPresentAndDisplayed(addToConversationsButton)) {
			return this;
		} else {
			return new DialogPage(this.getLazyDriver());
		}
	}

	@Override
	public AndroidPage swipeDown(int time) throws Exception {
		elementSwipeDown(content, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		switch (direction) {
		case DOWN: {
			return new ContactListPage(this.getLazyDriver());
		}
		default:
			return null;
		}
	}

	public boolean isPeoplePickerPageVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(pickerSearch);
	}

	// FIXME: find better locator
	public void waitUserPickerFindUser(String contactName) throws Exception {
		for (int i = 0; i < 50; i++) {
			List<WebElement> elements = pickerSearchUsers;
			for (WebElement element : elements) {
				try {
					if (element.getText().toLowerCase()
							.equals(contactName.toLowerCase())) {
						return;
					}
				} catch (Exception ex) {
					continue;
				}
			}
			Thread.sleep(100);
		}
	}

	public ContactListPage navigateBack() throws Exception {
		pickerClearBtn.click();
		return new ContactListPage(this.getLazyDriver());
	}

	public boolean isAddToConversationBtnVisible() {
		return DriverUtils
				.isElementPresentAndDisplayed(addToConversationsButton);
	}

	public DialogPage clickOnAddToCoversationButton() throws Exception {
		this.getDriver().navigate().back();
		addToConversationsButton.click();
		return new DialogPage(this.getLazyDriver());
	}

	// TODO: move this to some base page

	public AndroidPage tapCreateConversation() throws Exception {
		final By locator = By
				.id(AndroidLocators.PeoplePickerPage.idCreateConversationIcon);
		this.hideKeyboard();
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		createConversation.click();
		return new DialogPage(this.getLazyDriver());
	}

	public ContactListPage tapClearButton() throws Exception {
		pickerClearBtn.click();
		return new ContactListPage(this.getLazyDriver());
	}

	public boolean userIsVisible(String contact) throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.xpath(AndroidLocators.PeoplePickerPage.xpathPeoplePickerContactByName
								.apply(contact)));
	}

	public boolean groupIsVisible(String contact) throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.xpath(AndroidLocators.PeoplePickerPage.xpathPeoplePickerGroupByName
								.apply(contact)));
	}

	public PeoplePickerPage selectContactByLongTap(String contact)
			throws Exception {
		final By locator = By
				.xpath(AndroidLocators.PeoplePickerPage.xpathPeoplePickerContactByName
						.apply(contact));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		DriverUtils.androidLongClick(this.getDriver(),
				getDriver().findElement(locator));
		return this;
	}

	public void tapOnSendInvitation() throws Exception {
		try {
			sendInvitationBubble.click();
		} catch (NoSuchElementException ex) {
			sendInvitationFrame.click();
		}
	}

	// FIXME: Unexpected method behavior
	public CommonAndroidPage tapOnGmailLink() throws Exception {
		if (DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(AndroidLocators.PeoplePickerPage.xpathGmailLink))) {
			DriverUtils
					.swipeUp(
							this.getDriver(),
							this.getDriver()
									.findElementByXPath(
											AndroidLocators.PeoplePickerPage.xpathDestinationFrame),
							500, 50, 50);
			this.getWait().until(
					ExpectedConditions.elementToBeClickable(gmailLink));
		}
		gmailLink.click();
		return new CommonAndroidPage(this.getLazyDriver());
	}

	public WebElement selectRandomConnect() throws Exception {
		Random rand = new Random();
		int n = rand.nextInt(pickerUserSlidingRow.size() - 1);
		return pickerUserSlidingRow.get(n);
	}

	public String pressPlusOnContact(WebElement randomConnect) throws Exception {
		String name = randomConnect.findElement(
				By.id(AndroidLocators.PeoplePickerPage.idPickerRecomendedName))
				.getText();
		randomConnect
				.findElement(
						By.id(AndroidLocators.PeoplePickerPage.idPickerRecomendedQuickAdd))
				.click();
		return name;
	}

	public String swipePYMKContact(WebElement randomConnect) throws Exception {
		WebElement element = randomConnect.findElement(By
				.id(AndroidLocators.PeoplePickerPage.idPickerRecomendedName));
		String name = element.getText();
		DriverUtils.swipeRight(getDriver(), randomConnect, 1000, 90, 50);
		return name;
	}

	public void clickPYMKHideButton() throws Exception {
		WebElement hideButton = pickerUserHideMenu.findElement(By
				.className("android.widget.TextView"));
		hideButton.click();
	}

	public boolean pYMKcontactIsVisible(String randomConnectName,
			WebElement element) throws Exception {
		return (element.findElement(By
				.id(AndroidLocators.PeoplePickerPage.idPickerRecomendedName))
				.getText()).equals(randomConnectName);
	}

	public void swipeRightPYMKHideMenu() throws Exception {
		DriverUtils.swipeRight(getDriver(), pickerUserHideMenu, 1500, 30, 50,
				90, 50);
	}

	public boolean waitForPYMKForSecs(int time) throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.PeoplePickerPage.idPickerRecomendedName),
				time);
	}

	public String getPYMKContactName(WebElement randomConnect) throws Exception {
		String name = randomConnect.findElement(
				By.id(AndroidLocators.PeoplePickerPage.idPickerRecomendedName))
				.getText();
		return name;
	}

	public ConnectToPage tapOnPYMKContact(WebElement randomConnect)
			throws Exception {
		randomConnect.click();
		return new ConnectToPage(getLazyDriver());
	}
}
