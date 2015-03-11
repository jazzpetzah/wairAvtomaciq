package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.*;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.locators.ZetaHow;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.log.ZetaLogger;

public class ContactListPage extends AndroidPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPeoplePickerClearbtn")
	private WebElement pickerClearBtn;

	@FindBy(className = AndroidLocators.CommonLocators.classNameLoginPage)
	private WebElement content;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idConversationListFrame")
	private WebElement contactListFrame;	

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idContactListNames")
	private List<WebElement> contactListNames;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classEditText)
	private WebElement cursorInput;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameFrameLayout)
	private List<WebElement> frameLayout;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idProfileOptionsButton")
	private WebElement laterButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idOpenStartUIButton")
	private WebElement openStartUIButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idNameField")
	private WebElement selfUserName;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idConfirmCancelButton")
	private List<WebElement> laterBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idConfirmCancelButtonPicker")
	private List<WebElement> laterBtnPicker;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idPlayPauseMedia")
	private WebElement playPauseMedia;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idMutedIcon")
	private WebElement mutedIcon;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idConvList")
	private WebElement convList;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameLoginPage)
	private WebElement mainControl;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectToHeader")
	private WebElement connectToHeader;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idSearchHintClose")
	private WebElement closeHintBtn;

	private static final Logger log = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	public ContactListPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public AndroidPage tapOnName(String name) throws Exception {
		AndroidPage page = null;
		WebElement el = findInContactList(name, 5);
		this.getWait().until(ExpectedConditions.visibilityOf(el));
		el.click();
		refreshUITree();
		page = getPages();
		// workaround for incorrect tap
		if (page == null) {
			el = findInContactList(name, 1);
			if (el != null
					&& DriverUtils.isElementDisplayed(this.getDriver(), el)) {
				this.restoreApplication();
				el.click();
				log.debug("tap on contact for the second time");
			}
		}
		return page;
	}

	public void contactListSwipeUp(int time) {
		elementSwipeUp(contactListFrame, time);
	}
	
	public AndroidPage tapOnContactByPosition(List<WebElement> contacts, int id)
			throws Exception {
		AndroidPage page = null;
		refreshUITree();
		contacts.get(id).click();
		page = new DialogPage(this.getDriver(), this.getWait());
		return page;
	}

	public List<WebElement> GetVisibleContacts() {
		refreshUITree();
		return contactListNames;
	}

	private WebElement findInContactList(String name, int cyclesNumber)
			throws Exception {
		WebElement contact = null;
		Boolean flag = false;
		refreshUITree();
		if (CommonUtils.getAndroidApiLvl(ContactListPage.class) > 42) {
			if (isVisible(convList)) {
				flag = true;
			}
		} else if (!isVisible(cursorInput) && !isVisible(selfUserName)) {
			flag = true;
		}
		if (flag) {
			List<WebElement> contactsList = driver
					.findElements(By.xpath(String
							.format(AndroidLocators.ContactListPage.xpathContacts,
									name)));
			if (contactsList.size() > 0) {
				contact = contactsList.get(0);
			} else {
				if (cyclesNumber > 0) {
					cyclesNumber--;
					DriverUtils.swipeUp(this.getDriver(), mainControl, 500);
					contact = findInContactList(name, cyclesNumber);
				}
			}
		}
		return contact;
	}

	public AndroidPage swipeRightOnContact(int time, String contact)
			throws Exception {
		refreshUITree();
		AndroidPage page = null;
		WebElement el = driver.findElementByXPath(String.format(
				AndroidLocators.ContactListPage.xpathContactFrame, contact));
		elementSwipeRight(el, time);
		if (!isVisible(cursorInput)) {
			page = new ContactListPage(this.getDriver(), this.getWait());
		} else if (isVisible(cursorInput)) {
			page = new DialogPage(this.getDriver(), this.getWait());
		}
		return page;
	}

	public AndroidPage swipeOnArchiveUnarchive(String contact) throws Exception {
		WebElement el = driver
				.findElementByXPath(String
						.format(AndroidLocators.ContactListPage.xpathContactListArchiveUnarchive,
								contact));
		DriverUtils.swipeRight(this.getDriver(), el, 1000);
		AndroidPage page = null;
		refreshUITree();
		if (!isVisible(cursorInput)) {
			page = new ContactListPage(this.getDriver(), this.getWait());
		} else if (isVisible(cursorInput)) {
			page = new DialogPage(this.getDriver(), this.getWait());
		}
		return page;
	}

	public boolean isContactMuted() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.id(AndroidLocators.ContactListPage.idMutedIcon));
	}

	public boolean isHintVisible() throws InterruptedException, IOException {
		refreshUITree();// TODO workaround
		try {
			this.getWait().until(
					ExpectedConditions.elementToBeClickable(closeHintBtn));
		} catch (NoSuchElementException e) {
			return false;
		} catch (TimeoutException e) {
			return false;
		}
		return closeHintBtn.isEnabled();
	}

	public void closeHint() {
		closeHintBtn.click();
	}

	@Override
	public AndroidPage swipeDown(int time) throws Exception {
		refreshUITree();
		DriverUtils.swipeDown(this.getDriver(), content, time);
		Thread.sleep(2000);
		if (!isVisible(pickerClearBtn) && isVisible(openStartUIButton)) {
			refreshUITree();
			try {
				openStartUIButton.click();
			} catch (NoSuchElementException ex) {

			}
		}
		return returnBySwipe(SwipeDirection.DOWN);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {

		AndroidPage page = null;
		switch (direction) {
		case DOWN: {
			page = new PeoplePickerPage(this.getDriver(), this.getWait());
			break;
		}
		case UP: {
			break;
		}
		case LEFT: {
			break;
		}
		case RIGHT: {
			break;
		}
		}
		return page;
	}

	public ContactListPage pressLaterButton() throws Exception {
		/*
		 * try {
		 * wait.until(ExpectedConditions.elementToBeClickable(laterButton)); }
		 * catch (NoSuchElementException e) {
		 * 
		 * } catch (TimeoutException e) {
		 * 
		 * }
		 */
		// DriverUtils.waitUntilElementDissapear(driver,
		// By.id(AndroidLocators.PersonalInfoPage.idProfileOptionsButton));

		refreshUITree();
		if (laterBtn.size() > 0) {
			laterBtn.get(0).click();
		} else if (laterBtnPicker.size() > 0) {
			laterBtnPicker.get(0).click();
		}

		DriverUtils.waitUntilElementDissapear(driver,
				By.id(AndroidLocators.ContactListPage.idSimpleDialogPageText));
		// TODO: we need this as sometimes we see people picker after login
		PagesCollection.peoplePickerPage = new PeoplePickerPage(
				this.getDriver(), this.getWait());
		return this;
	}

	public Boolean isContactExists(String name) throws Exception {
		return findInContactList(name, 0) != null;
	}

	private AndroidPage getPages() throws Exception {
		AndroidPage page = null;
		if (isVisible(connectToHeader)) {
			page = new ConnectToPage(this.getDriver(), this.getWait());
		} else if (isVisible(selfUserName)) {
			page = new PersonalInfoPage(this.getDriver(), this.getWait());
		} else if (isVisible(cursorInput)) {
			page = new DialogPage(this.getDriver(), this.getWait());
		}

		return page;
	}

	public boolean isPlayPauseMediaButtonVisible()
			throws NumberFormatException, Exception {
		DriverUtils.waitUntilElementAppears(driver,
				By.id(AndroidLocators.ContactListPage.idPlayPauseMedia));
		return isVisible(playPauseMedia);
	}
}
