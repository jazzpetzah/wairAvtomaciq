package com.wearezeta.auto.android.pages;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.locators.AndroidLocators.CommonLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class ContactListPage extends AndroidPage {

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPeoplePickerClearbtn)
	private WebElement pickerClearBtn;

	@FindBy(id = AndroidLocators.ContactListPage.idConversationListFrame)
	private WebElement contactListFrame;

	@FindBy(id = AndroidLocators.ContactListPage.idMissedCallIcon)
	private WebElement missedCallIcon;

	@FindBy(id = AndroidLocators.ContactListPage.idContactListNames)
	private List<WebElement> contactListNames;

	@FindBy(id = AndroidLocators.CommonLocators.idEditText)
	private WebElement cursorInput;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameFrameLayout)
	private List<WebElement> frameLayout;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerSearch)
	private WebElement openStartUI;

	@FindBy(xpath = AndroidLocators.PersonalInfoPage.xpathNameField)
	private WebElement selfUserName;

	@FindBy(id = AndroidLocators.ContactListPage.idSelfUserAvatar)
	protected WebElement selfUserAvatar;

	@FindBy(id = AndroidLocators.ContactListPage.idConfirmCancelButton)
	private List<WebElement> laterBtn;

	@FindBy(id = AndroidLocators.ContactListPage.idConfirmCancelButtonPicker)
	private List<WebElement> laterBtnPicker;

	@FindBy(id = AndroidLocators.ContactListPage.idConvList)
	private WebElement convList;

	@FindBy(id = AndroidLocators.CommonLocators.idPager)
	private WebElement mainControl;

	@FindBy(id = AndroidLocators.ConnectToPage.idConnectToHeader)
	private WebElement connectToHeader;

	@FindBy(id = AndroidLocators.CommonLocators.idSearchHintClose)
	private WebElement closeHintBtn;

	@FindBy(xpath = AndroidLocators.CommonLocators.xpathGalleryCameraAlbum)
	private WebElement galleryCameraAlbumButton;

	@FindBy(id = AndroidLocators.CommonLocators.idConversationSendOption)
	private WebElement conversationShareOption;

	@FindBy(id = AndroidLocators.CommonLocators.idConfirmBtn)
	private WebElement confirmShareButton;

	@FindBy(id = AndroidLocators.ContactListPage.idSearchButton)
	private WebElement searchButton;

	private static final Logger log = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	public ContactListPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public AndroidPage tapOnName(String name) throws Exception {
		AndroidPage page = null;
		findInContactList(name, 5).click();
		this.verifyDriverIsAvailableAfterTimeout();
		try {
			page = detectCurrentPage();
		} catch (WebDriverException e) {
			// workaround for incorrect tap
			final WebElement el = findInContactList(name, 1);
			if (el != null && DriverUtils.isElementPresentAndDisplayed(el)) {
				this.restoreApplication();
				el.click();
				log.debug("tap on contact for the second time");
			}
			page = detectCurrentPage();
		}
		return page;
	}

	public void contactListSwipeUp(int time) {
		elementSwipeUp(contactListFrame, time);
	}

	public void waitForConversationListLoad() throws Exception {
		getWait().until(ExpectedConditions.visibilityOf(contactListFrame));
		verifyContactListIsFullyLoaded();
	}

	public AndroidPage tapOnContactByPosition(List<WebElement> contacts, int id)
			throws Exception {
		contacts.get(id).click();
		return new DialogPage(this.getLazyDriver());
	}

	public List<WebElement> GetVisibleContacts() throws Exception {
		return contactListNames;
	}

	public WebElement findInContactList(String name, int maxSwypesInList)
			throws Exception {
		final By nameLocator = By
				.xpath(AndroidLocators.ContactListPage.xpathContactByName
						.apply(name));
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

	public AndroidPage swipeRightOnContact(int durationMilliseconds,
			String contact) throws Exception {
		WebElement el = this.getDriver().findElementByXPath(
				AndroidLocators.ContactListPage.xpathContactByName
						.apply(contact));
		elementSwipeRight(el, durationMilliseconds);
		if (DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(AndroidLocators.CommonLocators.idEditText))) {
			return new ContactListPage(this.getLazyDriver());
		} else {
			return new DialogPage(this.getLazyDriver());
		}
	}

	public AndroidPage swipeOnArchiveUnarchive(String contact) throws Exception {
		WebElement el = getDriver().findElementByXPath(
				AndroidLocators.ContactListPage.xpathContactByName
						.apply(contact));
		DriverUtils.swipeRight(this.getDriver(), el, 1000);
		if (DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(AndroidLocators.CommonLocators.idEditText), 2)) {
			return new ContactListPage(this.getLazyDriver());
		} else {
			return new DialogPage(this.getLazyDriver());
		}
	}

	public boolean isContactMuted(String name) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.ContactListPage.xpathMutedIconByConvoName
						.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator);
	}

	public boolean waitUntilContactNotMuted(String name) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.ContactListPage.xpathMutedIconByConvoName
						.apply(name));
		return DriverUtils
				.waitUntilLocatorDissapears(this.getDriver(), locator);
	}

	public boolean isHintVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(CommonLocators.idSearchHintClose));
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

	public ContactListPage pressLaterButton() throws Exception {
		if (laterBtn.size() > 0) {
			laterBtn.get(0).click();
		} else if (laterBtnPicker.size() > 0) {
			laterBtnPicker.get(0).click();
		}

		DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(AndroidLocators.ContactListPage.idSimpleDialogPageText));
		// TODO: we need this as sometimes we see people picker after login
		PagesCollection.peoplePickerPage = new PeoplePickerPage(
				this.getLazyDriver());
		return this;
	}

	public boolean isContactExists(String name) throws Exception {
		return findInContactList(name, 0) != null;
	}

	public boolean waitUntilContactDisappears(String name) throws Exception {
		final By nameLocator = By
				.xpath(AndroidLocators.ContactListPage.xpathContactByName
						.apply(name));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameLocator);
	}

	public boolean isContactExists(String name, int cycles) throws Exception {
		return findInContactList(name, cycles) != null;
	}

	private AndroidPage detectCurrentPage() throws Exception {
		final Map<By, AndroidPage> pageMapping = new LinkedHashMap<By, AndroidPage>();
		pageMapping.put(By.id(AndroidLocators.CommonLocators.idEditText),
				new DialogPage(this.getLazyDriver()));
		pageMapping.put(
				By.xpath(AndroidLocators.PersonalInfoPage.xpathNameField),
				new PersonalInfoPage(this.getLazyDriver()));
		pageMapping.put(By.id(AndroidLocators.ConnectToPage.idConnectToHeader),
				new ConnectToPage(this.getLazyDriver()));
		for (Map.Entry<By, AndroidPage> entry : pageMapping.entrySet()) {
			if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
					entry.getKey(), 2)) {
				return entry.getValue();
			}
		}
		log.debug(getDriver().getPageSource());
		throw new WebDriverException(
				"Current page type cannot be detected. Please check locators");
	}

	public boolean isPlayPauseMediaButtonVisible(String convoName)
			throws Exception {
		final By locator = By
				.xpath(AndroidLocators.ContactListPage.xpathPlayPauseButtonByConvoName
						.apply(convoName));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	private static final int CONTACT_LIST_LOAD_TIMEOUT_SECONDS = 90;

	public void verifyContactListIsFullyLoaded() throws Exception {
		final By convoListLoadingProgressLocator = By
				.xpath(AndroidLocators.ContactListPage.xpathConversationListLoadingIndicator);
		if (!DriverUtils.waitUntilLocatorDissapears(getDriver(),
				convoListLoadingProgressLocator,
				CONTACT_LIST_LOAD_TIMEOUT_SECONDS)) {
			log.warn(String
					.format("It seems that conversation list has not been loaded within %s seconds (progress bar is still visible)",
							CONTACT_LIST_LOAD_TIMEOUT_SECONDS));
		}
		final By loadingItemLocator = By
				.xpath(AndroidLocators.ContactListPage.xpathLoadingContactListItem);
		assert DriverUtils.waitUntilLocatorDissapears(getDriver(),
				loadingItemLocator, CONTACT_LIST_LOAD_TIMEOUT_SECONDS) : String
				.format("Not all conversation list items were loaded within %s seconds",
						CONTACT_LIST_LOAD_TIMEOUT_SECONDS);
	}

	public boolean isVisibleMissedCallIcon() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(missedCallIcon);
	}

	public void shareImageToWireFromGallery() throws Exception {
		galleryCameraAlbumButton.click();
		List<WebElement> galleryImageViews = this.getDriver()
				.findElementsByClassName("android.widget.ImageView");
		for (WebElement imageView : galleryImageViews) {
			if (imageView.getAttribute("name").equals("Share with")) {
				imageView.click();
			}
		}
		List<WebElement> textViewElements = this.getDriver()
				.findElementsByClassName("android.widget.TextView");
		for (WebElement textView : textViewElements) {
			if (textView.getAttribute("text").equals("See all")) {
				textView.click();
			}
		}
		// find elements again
		textViewElements = this.getDriver().findElementsByClassName(
				"android.widget.TextView");
		for (WebElement textView : textViewElements) {
			if (textView.getAttribute("text").equals("Wire")) {
				textView.click();
			}
		}
		conversationShareOption.click();
		confirmShareButton.click();
	}

	public void shareURLFromNativeBrowser() throws Exception {
		List<WebElement> imageButtonElements = this.getDriver()
				.findElementsByClassName(
						AndroidLocators.Browsers.nameNativeBrowserMenuButton);
		for (WebElement imageButton : imageButtonElements) {
			if (imageButton.getAttribute("name").equals("More options")) {
				imageButton.click();
			}
		}
		List<WebElement> textViewElements = this
				.getDriver()
				.findElementsByClassName(
						AndroidLocators.Browsers.nameNativeBrowserMoreOptionsButton);
		for (WebElement textView : textViewElements) {
			if (textView.getAttribute("text").equals("Share page")) {
				textView.click();
				break;
			}
		}
		List<WebElement> textElements = this
				.getDriver()
				.findElementsByClassName(
						AndroidLocators.Browsers.nameNativeBrowserShareWireButton);
		for (WebElement textView : textElements) {
			if (textView.getAttribute("text").equals("Wire")) {
				textView.click();
			}
		}
		conversationShareOption.click();
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

}
