package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.android.pages.registration.EmailSignInPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class ContactListPage extends AndroidPage {

	private static final String xpathLoadingContactListItem = "//*[@id='tv_conv_list_topic' and contains(@value, '…')]";

	public static final Function<String, String> xpathContactByName = name -> String
			.format("//*[@id='tv_conv_list_topic' and @value='%s']", name);

	public static final Function<Integer, String> xpathContactByIndex = index -> String
			.format("(//*[@id='tv_conv_list_topic'])[%s]", index);

	public static final Function<String, String> xpathMutedIconByConvoName = convoName -> String
			.format("%s/parent::*//*[@id='tv_conv_list_voice_muted']",
					xpathContactByName.apply(convoName));

	private static final Function<String, String> xpathPlayPauseButtonByConvoName = convoName -> String
			.format("%s/parent::*//*[@id='tv_conv_list_media_player']",
					xpathContactByName.apply(convoName));

	@FindBy(id = PeoplePickerPage.idPeoplePickerClearbtn)
	private WebElement pickerClearBtn;

	private static final String idConversationListFrame = "pfac__conversation_list";
	@FindBy(id = idConversationListFrame)
	private WebElement contactListFrame;

	private static final String idMissedCallIcon = "sci__list__missed_call";
	@FindBy(id = idMissedCallIcon)
	private WebElement missedCallIcon;

	private static final String idContactListNames = "tv_conv_list_topic";
	@FindBy(id = idContactListNames)
	private List<WebElement> contactListNames;

	@FindBy(id = idEditText)
	private WebElement cursorInput;

	@FindBy(how = How.CLASS_NAME, using = classNameFrameLayout)
	private List<WebElement> frameLayout;

	@FindBy(id = PeoplePickerPage.idPickerSearch)
	private WebElement openStartUI;

	@FindBy(xpath = PersonalInfoPage.xpathNameField)
	private WebElement selfUserName;

	private static final String idSelfUserAvatar = "civ__searchbox__self_user_avatar";
	@FindBy(id = idSelfUserAvatar)
	protected WebElement selfUserAvatar;

	public static final String idConfirmCancelButton = "cancel";
	@FindBy(id = idConfirmCancelButton)
	private List<WebElement> laterBtn;

	private static final String idConfirmCancelButtonPicker = "zb__confirm_dialog__cancel_button";
	@FindBy(id = idConfirmCancelButtonPicker)
	private List<WebElement> laterBtnPicker;

	private static final String idConvList = "pv__conv_list";
	@FindBy(id = idConvList)
	private WebElement convList;

	@FindBy(id = idPager)
	private WebElement mainControl;

	@FindBy(id = ConnectToPage.idConnectToHeader)
	private WebElement connectToHeader;

	@FindBy(id = idSearchHintClose)
	private WebElement closeHintBtn;

	@FindBy(id = idConversationSendOption)
	private WebElement conversationShareOption;

	private static final String idSearchButton = "gtv_pickuser__searchbutton";
	@FindBy(id = idSearchButton)
	private WebElement searchButton;

	private static final String xpathTopConversationsListLoadingIndicator = "//*[@id='lbv__conversation_list__loading_indicator']/*";
	private static final String xpathSpinnerConversationsListLoadingIndicator = "//*[@id='liv__conversations__loading_indicator']/*";

	private static final Logger log = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	public ContactListPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void tapOnName(String name) throws Exception {
		findInContactList(name, 5).click();
	}

	public void contactListSwipeUp(int time) {
		elementSwipeUp(contactListFrame, time);
	}

	public void waitForConversationListLoad() throws Exception {
		verifyContactListIsFullyLoaded();
	}

	public AndroidPage tapOnContactByPosition(List<WebElement> contacts, int id)
			throws Exception {
		try {
			contacts.get(id).click();
			log.debug("Trying to open contact " + id + ". It's name: "
					+ contacts.get(id).getAttribute("value"));
		} catch (Exception e) {
			log.debug("Failed to find element in contact list.\nPage source: "
					+ getDriver().getPageSource());
			throw e;
		}
		return new DialogPage(this.getLazyDriver());
	}

	public List<WebElement> GetVisibleContacts() throws Exception {
		return contactListNames;
	}

	public WebElement findInContactList(String name, int maxSwypesInList)
			throws Exception {
		final By nameLocator = By.xpath(xpathContactByName.apply(name));
		if (DriverUtils
				.waitUntilLocatorIsDisplayed(getDriver(), nameLocator, 1)) {
			return this.getDriver().findElement(nameLocator);
		} else {
			if (maxSwypesInList > 0) {
				maxSwypesInList--;
				DriverUtils.swipeUp(this.getDriver(), mainControl, 500);
				return findInContactList(name, maxSwypesInList);
			}
		}
		return null;
	}

	public void swipeRightOnContact(int durationMilliseconds, String contact)
			throws Exception {
		WebElement el = this.getDriver().findElementByXPath(
				xpathContactByName.apply(contact));
		elementSwipeRight(el, durationMilliseconds);
	}

	public void swipeOnArchiveUnarchive(String contact) throws Exception {
		WebElement el = getDriver().findElementByXPath(
				xpathContactByName.apply(contact));
		DriverUtils.swipeRight(this.getDriver(), el, 1000);
	}

	public boolean isContactMuted(String name) throws Exception {
		final By locator = By.xpath(xpathMutedIconByConvoName.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator);
	}

	public boolean waitUntilContactNotMuted(String name) throws Exception {
		final By locator = By.xpath(xpathMutedIconByConvoName.apply(name));
		return DriverUtils
				.waitUntilLocatorDissapears(this.getDriver(), locator);
	}

	public boolean isHintVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idSearchHintClose));
	}

	public void closeHint() {
		closeHintBtn.click();
	}

	@Override
	public AndroidPage swipeDown(int time) throws Exception {
		elementSwipeDown(contactListFrame, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public PeoplePickerPage pressOpenStartUI() throws Exception {
		openStartUI.click();
		return new PeoplePickerPage(this.getLazyDriver());
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		switch (direction) {
		case DOWN: {
			return new PeoplePickerPage(this.getLazyDriver());
		}
		default:
			return null;
		}
	}

	public boolean isContactExists(String name) throws Exception {
		return findInContactList(name, 0) != null;
	}

	public boolean waitUntilContactDisappears(String name) throws Exception {
		final By nameLocator = By.xpath(xpathContactByName.apply(name));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameLocator);
	}

	public boolean isContactExists(String name, int cycles) throws Exception {
		return findInContactList(name, cycles) != null;
	}

	public boolean isPlayPauseMediaButtonVisible(String convoName)
			throws Exception {
		final By locator = By.xpath(xpathPlayPauseButtonByConvoName
				.apply(convoName));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	private static final int CONTACT_LIST_LOAD_TIMEOUT_SECONDS = 60;

	public void verifyContactListIsFullyLoaded() throws Exception {
		assert DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(EmailSignInPage.idLoginButton),
				CONTACT_LIST_LOAD_TIMEOUT_SECONDS) : String
				.format("It seems that conversation list has not been loaded within %s seconds (login button is still visible)",
						CONTACT_LIST_LOAD_TIMEOUT_SECONDS);

		final By topConvoListLoadingProgressLocator = By
				.xpath(xpathTopConversationsListLoadingIndicator);
		if (!DriverUtils.waitUntilLocatorDissapears(getDriver(),
				topConvoListLoadingProgressLocator,
				CONTACT_LIST_LOAD_TIMEOUT_SECONDS)) {
			log.warn(String
					.format("It seems that conversation list has not been loaded within %s seconds (the progress bar is still visible)",
							CONTACT_LIST_LOAD_TIMEOUT_SECONDS));
		}

		final By selfAvatarLocator = By.id(idSelfUserAvatar);
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				selfAvatarLocator, CONTACT_LIST_LOAD_TIMEOUT_SECONDS) : "Self avatar is not visible on top of conversations list";

		final By spinnerConvoListLoadingProgressLocator = By
				.xpath(xpathSpinnerConversationsListLoadingIndicator);
		if (!DriverUtils.waitUntilLocatorDissapears(getDriver(),
				spinnerConvoListLoadingProgressLocator,
				CONTACT_LIST_LOAD_TIMEOUT_SECONDS)) {
			log.warn(String
					.format("It seems that conversation list has not been loaded within %s seconds (the spinner is still visible)",
							CONTACT_LIST_LOAD_TIMEOUT_SECONDS));
		}

		final By loadingItemLocator = By.xpath(xpathLoadingContactListItem);
		assert DriverUtils.waitUntilLocatorDissapears(getDriver(),
				loadingItemLocator, CONTACT_LIST_LOAD_TIMEOUT_SECONDS) : String
				.format("Not all conversation list items were loaded within %s seconds",
						CONTACT_LIST_LOAD_TIMEOUT_SECONDS);
	}

	public boolean isVisibleMissedCallIcon() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(missedCallIcon);
	}

	public PersonalInfoPage tapOnMyAvatar() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				selfUserAvatar);
		selfUserAvatar.click();
		return new PersonalInfoPage(getLazyDriver());
	}

	public PeoplePickerPage openPeoplePicker() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), searchButton);
		searchButton.click();
		return new PeoplePickerPage(getLazyDriver());
	}

	public boolean isAnyConversationVisible() throws Exception {
		for (int i = 1; i <= contactListNames.size(); i++) {
			final By locator = By.xpath(xpathContactByIndex.apply(i));
			if (DriverUtils
					.waitUntilLocatorIsDisplayed(getDriver(), locator, 1)) {
				return true;
			}
		}
		return false;
	}
}
