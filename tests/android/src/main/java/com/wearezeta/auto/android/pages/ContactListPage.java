package com.wearezeta.auto.android.pages;

import java.util.HashMap;
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

	@FindBy(id = AndroidLocators.PersonalInfoPage.idProfileOptionsButton)
	private WebElement laterButton;

	@FindBy(id = AndroidLocators.ContactListPage.idOpenStartUIButton)
	private WebElement openStartUIButton;

	@FindBy(id = AndroidLocators.PersonalInfoPage.idNameField)
	private WebElement selfUserName;

	@FindBy(id = AndroidLocators.ContactListPage.idSelfUserAvatar)
	protected WebElement selfUserAvatar;

	@FindBy(id = AndroidLocators.ContactListPage.idConfirmCancelButton)
	private List<WebElement> laterBtn;

	@FindBy(id = AndroidLocators.ContactListPage.idConfirmCancelButtonPicker)
	private List<WebElement> laterBtnPicker;

	@FindBy(id = AndroidLocators.ContactListPage.idPlayPauseMedia)
	private WebElement playPauseMedia;

	@FindBy(id = AndroidLocators.ContactListPage.idMutedIcon)
	private WebElement mutedIcon;

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

	private static final Logger log = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	public ContactListPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public AndroidPage tapOnName(String name) throws Exception {
		AndroidPage page = null;
		WebElement el = findInContactList(name, 5);
		this.getWait().until(ExpectedConditions.visibilityOf(el));
		el.click();
		try {
			page = detectCurrentPage();
		} catch (WebDriverException e) {
			// workaround for incorrect tap
			el = findInContactList(name, 1);
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

	public AndroidPage swipeRightOnContact(int time, String contact)
			throws Exception {
		WebElement el = this
				.getDriver()
				.findElementByXPath(
						AndroidLocators.ContactListPage.xpathContactListArchiveUnarchiveByName
								.apply(contact));
		elementSwipeRight(el, time);
		if (DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(AndroidLocators.CommonLocators.idEditText))) {
			return new ContactListPage(this.getLazyDriver());
		} else {
			return new DialogPage(this.getLazyDriver());
		}
	}

	public AndroidPage swipeOnArchiveUnarchive(String contact) throws Exception {
		WebElement el = getDriver()
				.findElementByXPath(
						AndroidLocators.ContactListPage.xpathContactListArchiveUnarchiveByName
								.apply(contact));
		DriverUtils.swipeRight(this.getDriver(), el, 1000);
		if (DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(AndroidLocators.CommonLocators.idEditText))) {
			return new ContactListPage(this.getLazyDriver());
		} else {
			return new DialogPage(this.getLazyDriver());
		}
	}

	public boolean isContactMuted() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.id(AndroidLocators.ContactListPage.idMutedIcon));
	}

	public boolean waitUntilContactNotMuted() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(AndroidLocators.ContactListPage.idMutedIcon));
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

	public PeoplePickerPage pressOpenStartUIButton() throws Exception {
		openStartUIButton.click();
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

	public Boolean isContactExists(String name) throws Exception {
		return findInContactList(name, 0) != null;
	}

	public Boolean isContactExists(String name, int cycles) throws Exception {
		return findInContactList(name, cycles) != null;
	}

	private AndroidPage detectCurrentPage() throws Exception {
		final Map<By, AndroidPage> pageMapping = new HashMap<By, AndroidPage>();
		pageMapping.put(By.id(AndroidLocators.ConnectToPage.idConnectToHeader),
				new ConnectToPage(this.getLazyDriver()));
		pageMapping.put(By.id(AndroidLocators.PersonalInfoPage.idNameField),
				new PersonalInfoPage(this.getLazyDriver()));
		pageMapping.put(By.id(AndroidLocators.CommonLocators.idEditText),
				new DialogPage(this.getLazyDriver()));
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

	public boolean isPlayPauseMediaButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.ContactListPage.idPlayPauseMedia));
	}

	public void waitForContactListLoadFinished() throws Exception {
		if (contactListNames.size() > 0) {
			waitForContacListLoading();
		}
	}

	private static final int CONTACT_LIST_ITEMS_LOAD_TIMEOUT_SECONDS = 60;

	private void waitForContacListLoading() throws Exception {
		final By locator = By
				.xpath(AndroidLocators.ContactListPage.xpathLoadingContactListItem);
		assert DriverUtils.waitUntilLocatorDissapears(getDriver(), locator,
				CONTACT_LIST_ITEMS_LOAD_TIMEOUT_SECONDS) : String
				.format("Not all conversation list items were loaded within %s seconds",
						CONTACT_LIST_ITEMS_LOAD_TIMEOUT_SECONDS);
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
		selfUserAvatar.click();
		return new PersonalInfoPage(getLazyDriver());
	}

}
