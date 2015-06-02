package com.wearezeta.auto.android.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPeoplePickerSerchConversations)
	private List<WebElement> pickerSearchConversations;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPeoplePickerClearbtn)
	private WebElement pickerClearBtn;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerRows)
	private List<WebElement> pickerSearchRows;

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

	public boolean isTopPeopleHeaderVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(pickerTopPeopleHeader);
	}

	public boolean waitUntilTopPeopleHeaderInvisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorDissapears(
						getDriver(),
						By.id(AndroidLocators.PeoplePickerPage.idPickerTopPeopleHeader));
	}

	public AndroidPage selectContact(String contactName) throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				pickerSearchUser);
		pickerSearchUser.click();
		final Map<By, AndroidPage> pagesMapping = new HashMap<By, AndroidPage>();
		pagesMapping.put(
				By.id(AndroidLocators.OtherUserPersonalInfoPage.idUnblockBtn),
				new OtherUserPersonalInfoPage(this.getLazyDriver()));
		pagesMapping.put(
				By.id(AndroidLocators.ConnectToPage.idConnectToHeader),
				new ConnectToPage(this.getLazyDriver()));
		pagesMapping.put(
				By.id(AndroidLocators.PeoplePickerPage.idPickerBtnDone), this);
		final int maxScanTries = 3;
		int scanTry = 1;
		while (scanTry <= maxScanTries) {
			for (Map.Entry<By, AndroidPage> entry : pagesMapping.entrySet()) {
				if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
						entry.getKey(), 1)) {
					return entry.getValue();
				}
			}
			scanTry++;
		}
		return new DialogPage(this.getLazyDriver());
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

	// !!! Indexing starts from 1

	public String getPYMKItemName(int index) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.PeoplePickerPage.xpathPYMKItemByIdxLabel
						.apply(index));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		return getDriver().findElement(locator).getText();
	}

	public void clickPlusOnPYMKItem(int index) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.PeoplePickerPage.xpathPYMKItemByIdxPlusButton
						.apply(index));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		this.getDriver().findElement(locator).click();
	}

	public void longSwipeRigthOnPYMKItem(int index) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.PeoplePickerPage.xpathPYMKItemByIdx
						.apply(index));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		DriverUtils.swipeRight(getDriver(), getDriver().findElement(locator),
				1000, 10, 50, 75, 50);
	}

	public void shortSwipeRigthOnPYMKItem(int index) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.PeoplePickerPage.xpathPYMKItemByIdx
						.apply(index));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		DriverUtils.swipeRight(getDriver(), getDriver().findElement(locator),
				1000, 10, 50, 50, 50);
	}

	public void clickHideButtonOnPYMKItem(int index) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.PeoplePickerPage.xpathPYMKItemByIdxHideButton
						.apply(index));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		this.getDriver().findElement(locator).click();
	}

	public boolean waitUntilPYMKItemIsInvisible(String name) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.PeoplePickerPage.xpathPYMKItemByName
						.apply(name));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
	}

	public boolean waitUntilPYMKItemIsVisible(int index) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.PeoplePickerPage.xpathPYMKItemByIdx
						.apply(index));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}
}
