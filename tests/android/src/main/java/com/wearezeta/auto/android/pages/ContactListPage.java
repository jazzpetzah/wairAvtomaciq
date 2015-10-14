package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.android.pages.registration.EmailSignInPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class ContactListPage extends AndroidPage {

	private static final String xpathLoadingContactListItem = "//*[@id='tv_conv_list_topic' and contains(@value, '…')]";

	public static final Function<String, String> xpathContactByName = name -> String
			.format("//*[@id='tv_conv_list_topic' and @value='%s' and @shown='true']",
					name);

	public static final Function<Integer, String> xpathContactByIndex = index -> String
			.format("(//*[@id='tv_conv_list_topic'])[%s]", index);

	public static final String xpathLastContact = "(//*[@id='tv_conv_list_topic'])[last()]";

	public static final Function<String, String> xpathMutedIconByConvoName = convoName -> String
			.format("%s/parent::*//*[@id='tv_conv_list_voice_muted']",
					xpathContactByName.apply(convoName));

	private static final Function<String, String> xpathPlayPauseButtonByConvoName = convoName -> String
			.format("%s/parent::*//*[@id='tv_conv_list_media_player']",
					xpathContactByName.apply(convoName));

	private static final Function<String, String> xpathMissedCallNotificationByConvoName = convoName -> String
			.format("%s/parent::*//*[@id='sci__list__missed_call']",
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

	private static final String xpathNonEmptyContacts = "//*[@id='"
			+ idContactListNames
			+ "' and @value and string-length(@value) > 0 and not(starts-with(@value, '…'))]";
	private static final Function<Integer, String> xpathNonEmptyContactByIdx = idx -> String
			.format("(%s)[%d]", xpathNonEmptyContacts, idx);

	@FindBy(id = idEditText)
	private WebElement cursorInput;

	@FindBy(how = How.CLASS_NAME, using = classNameFrameLayout)
	private List<WebElement> frameLayout;

	@FindBy(id = PeoplePickerPage.idPickerSearch)
	private WebElement searchBox;

	public static final String idSelfUserAvatar = "gtv__searchbox__self_user_avatar";
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

	private static final Function<String, String> xpathConvoSettingsMenuItemByName = name -> String
			.format("//*[starts-with(@id, 'ttv__settings_box__item') and @value='%s']",
					name.toUpperCase());

	private static final String xpathSpinnerConversationsListLoadingIndicator = "//*[@id='liv__conversations__loading_indicator']/*";

	private static final Function<String, String> xpathConversationListEntry = name -> String
			.format("//*[@id='tv_conv_list_topic' and @value='%s']/parent::*//*[@id='civ__list_row']",
					name);

	private static final Logger log = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	public ContactListPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public String getFirstVisibleConversationName() throws Exception {
		final int maxTries = 20;
		final long millisecondsDelay = 20000;
		int ntry = 1;
		do {
			try {
				final int itemsCount = getDriver().findElements(
						By.xpath(xpathNonEmptyContacts)).size();
				for (int i = 1; i <= itemsCount; i++) {
					final By locator = By.xpath(xpathNonEmptyContactByIdx
							.apply(i));
					if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
							locator, 1)) {
						final WebElement elem = getDriver()
								.findElement(locator);
						final String name = elem.getText();
						if ((name instanceof String) && name.length() > 0) {
							if (DriverUtils.waitUntilElementClickable(
									getDriver(), elem)) {
								return name;
							} else {
								break;
							}
						}
					}
				}
			} catch (WebDriverException e) {
				e.printStackTrace();
				// Ignore silently
			}
			Thread.sleep(millisecondsDelay);
			ntry++;
		} while (ntry <= maxTries);
		throw new AssertionError(
				"There are no visible conversations in the list after "
						+ millisecondsDelay * maxTries / 1000 + " seconds");
	}

	public void tapOnName(final String name) throws Exception {
		findInContactList(name, 5)
				.orElseThrow(
						() -> new IllegalStateException(
								String.format(
										"The conversation '%s' does not exist in the conversations list",
										name))).click();
	}

	public void tapOnName(final String name, int maxSwipesInList)
			throws Exception {
		findInContactList(name, maxSwipesInList)
				.orElseThrow(
						() -> new IllegalStateException(
								String.format(
										"The conversation '%s' does not exist in the conversations list",
										name))).click();
	}

	public void doLongSwipeUp() throws Exception {
		DriverUtils.swipeElementPointToPoint(getDriver(), contactListFrame,
				4000, 50, 80, 50, 10);
	}

	public void workaroundConvoListItemsLoad() throws Exception {
		for (int i = 1; i <= contactListNames.size(); i++) {
			final By locator = By.xpath(xpathContactByIndex.apply(i));
			try {
				if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
						locator, 1)) {
					final WebElement elem = getDriver().findElement(locator);
					if (DriverUtils.waitUntilElementClickable(getDriver(),
							elem, 1)) {
						elem.click();
						// Wait for animation
						Thread.sleep(1000);
						DriverUtils
								.swipeRightCoordinates(getDriver(), 1000, 50);
						return;
					}
				}
			} catch (WebDriverException e) {
				e.printStackTrace();
			}
		}
	}

	public List<WebElement> GetVisibleContacts() throws Exception {
		return contactListNames;
	}

	public Optional<WebElement> findInContactList(String name,
			int maxSwypesInList) throws Exception {
		final By nameLocator = By.xpath(xpathContactByName.apply(name));
		if (DriverUtils
				.waitUntilLocatorIsDisplayed(getDriver(), nameLocator, 1)) {
			return Optional.of(this.getDriver().findElement(nameLocator));
		} else {
			if (maxSwypesInList > 0) {
				maxSwypesInList--;
				DriverUtils.swipeUp(this.getDriver(), mainControl, 500, 50, 90);
				return findInContactList(name, maxSwypesInList);
			}
		}
		return Optional.empty();
	}

	public void swipeRightOnConversation(int durationMilliseconds, String name)
			throws Exception {
		final By locator = By.xpath(xpathContactByName.apply(name));
		DriverUtils.swipeRight(this.getDriver(),
				this.getDriver().findElement(locator), durationMilliseconds,
				20, 50, 90, 50);
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

	public PeoplePickerPage tapOnSearchBox() throws Exception {
		searchBox.click();
		return new PeoplePickerPage(this.getLazyDriver());
	}

	public boolean isContactExists(String name) throws Exception {
		return findInContactList(name, 0).isPresent();
	}

	public boolean waitUntilContactDisappears(String name) throws Exception {
		final By nameLocator = By.xpath(xpathContactByName.apply(name));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameLocator);
	}

	public boolean isContactExists(String name, int cycles) throws Exception {
		return findInContactList(name, cycles).isPresent();
	}

	public boolean isPlayPauseMediaButtonVisible(String convoName)
			throws Exception {
		final By locator = By.xpath(xpathPlayPauseButtonByConvoName
				.apply(convoName));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	private static final int CONTACT_LIST_LOAD_TIMEOUT_SECONDS = 60;
	private static final int CONVERSATIONS_INFO_LOAD_TIMEOUT_SECONDS = CONTACT_LIST_LOAD_TIMEOUT_SECONDS * 2;

	public void verifyContactListIsFullyLoaded() throws Exception {
		Thread.sleep(1000);
		if (!DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(EmailSignInPage.idLoginButton),
				CONTACT_LIST_LOAD_TIMEOUT_SECONDS)) {
			throw new IllegalStateException(
					String.format(
							"It seems that conversations list has not been loaded within %s seconds (login button is still visible)",
							CONTACT_LIST_LOAD_TIMEOUT_SECONDS));
		}

		final By selfAvatarLocator = By.id(idSelfUserAvatar);
		if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				selfAvatarLocator, 5)) {
			log.warn("Self avatar is not detected on top of conversations list");
		}

		final By spinnerConvoListLoadingProgressLocator = By
				.xpath(xpathSpinnerConversationsListLoadingIndicator);
		if (!DriverUtils.waitUntilLocatorDissapears(getDriver(),
				spinnerConvoListLoadingProgressLocator,
				CONTACT_LIST_LOAD_TIMEOUT_SECONDS / 2)) {
			log.warn(String
					.format("It seems that conversations list has not been loaded within %s seconds (the spinner is still visible)",
							CONTACT_LIST_LOAD_TIMEOUT_SECONDS / 2));
		}

		if (!this.waitUntilConversationsInfoIsLoaded()) {
			throw new IllegalStateException(
					String.format(
							"Not all conversations list items were loaded within %s seconds",
							CONVERSATIONS_INFO_LOAD_TIMEOUT_SECONDS));
		}
	}

	private boolean waitUntilConversationsInfoIsLoaded() throws Exception {
		final By loadingItemLocator = By.xpath(xpathLoadingContactListItem);
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				loadingItemLocator, CONVERSATIONS_INFO_LOAD_TIMEOUT_SECONDS);
	}

	public boolean isVisibleMissedCallIcon() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				missedCallIcon);
	}

	public PersonalInfoPage tapOnMyAvatar() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				selfUserAvatar);
		selfUserAvatar.click();
		return new PersonalInfoPage(getLazyDriver());
	}

	public PeoplePickerPage tapOnSearchButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), searchButton);
		searchButton.click();
		return new PeoplePickerPage(getLazyDriver());
	}

	public boolean isAnyConversationVisible() throws Exception {
		for (int i = contactListNames.size(); i >= 1; i--) {
			final By locator = By.xpath(xpathContactByIndex.apply(i));
			if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator)) {
				if (!waitUntilConversationsInfoIsLoaded()) {
					throw new IllegalStateException(
							String.format(
									"Not all conversations list items were loaded within %s seconds",
									CONVERSATIONS_INFO_LOAD_TIMEOUT_SECONDS));
				}
				return true;
			}
		}
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathLastContact), CONTACT_LIST_LOAD_TIMEOUT_SECONDS);
	}

	public boolean isNoConversationsVisible() throws Exception {
		for (int i = contactListNames.size(); i >= 1; i--) {
			final By locator = By.xpath(xpathContactByIndex.apply(i));
			if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), locator)) {
				return false;
			}
		}
		return true;
	}

	public void selectConvoSettingsMenuItem(String itemName) throws Exception {
		final By locator = By.xpath(xpathConvoSettingsMenuItemByName
				.apply(itemName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("Conversation menu item '%s' could not be found on the current screen");
		getDriver().findElement(locator).click();
	}

	public boolean waitUntilMissedCallNotificationVisible(String convoName)
			throws Exception {
		final By locator = By.xpath(xpathMissedCallNotificationByConvoName
				.apply(convoName));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitUntilMissedCallNotificationInvisible(String convoName)
			throws Exception {
		final By locator = By.xpath(xpathMissedCallNotificationByConvoName
				.apply(convoName));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
	}

	public void doShortSwipeDown() throws Exception {
		DriverUtils.swipeElementPointToPoint(getDriver(), contactListFrame,
				500, 15, 15, 15, 20);
	}

	public void doLongSwipeDown() throws Exception {
		DriverUtils.swipeElementPointToPoint(getDriver(), contactListFrame,
				1000, 15, 15, 15, 180);
	}

	public Optional<BufferedImage> getScreenshotOfPlayPauseButtonNextTo(
			String convoName) throws Exception {
		final By locator = By.xpath(xpathPlayPauseButtonByConvoName
				.apply(convoName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("PlayPause button is not visible next to the '%s' conversation item",
						convoName);
		return this.getElementScreenshot(this.getDriver().findElement(locator));
	}

	public void tapPlayPauseMediaButton(String convoName) throws Exception {
		final By locator = By.xpath(xpathPlayPauseButtonByConvoName
				.apply(convoName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("PlayPause button is not visible next to the '%s' conversation item",
						convoName);
		this.getDriver().findElement(locator).click();
	}

	public Optional<BufferedImage> getMessageIndicatorScreenshot(String name)
			throws Exception {
		final By locator = By.xpath(xpathConversationListEntry.apply(name));
		return this.getElementScreenshot(this.getDriver().findElement(locator));
	}
}
