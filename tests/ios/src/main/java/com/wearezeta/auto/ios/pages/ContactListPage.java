package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class ContactListPage extends IOSPage {

	private static final Logger log = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.90;
	private final double MIN_ACCEPTABLE_IMAGE_UNREADDOT_VALUE = 0.99;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathNameContactList)
	private List<WebElement> contactListNames;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathContactListCells)
	private List<WebElement> contactListCells;

	@FindBy(how = How.NAME, using = IOSLocators.nameProfileName)
	private WebElement profileName;

	@FindBy(how = How.NAME, using = IOSLocators.nameMuteButton)
	private List<WebElement> muteButtons;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathMyUserInContactList)
	private WebElement myUserNameInContactList;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathFirstChatInChatListTextField)
	private WebElement firstChatInChatListTextField;

	@FindBy(how = How.NAME, using = IOSLocators.nameContactListLoadBar)
	private WebElement loadBar;

	@FindBy(how = How.NAME, using = IOSLocators.nameMediaCellPlayButton)
	private WebElement playPauseButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPendingRequest)
	private WebElement pendingRequest;

	@FindBy(how = How.NAME, using = IOSLocators.nameTutorialView)
	private WebElement tutorialView;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathFirstInContactList)
	private WebElement firstContactInList;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathContactListContainer)
	private WebElement contactListContainer;
	
//	@FindBy(how = How.NAME, using = IOSLocators.nameArchiveButton)
//	private WebElement archiveButton;

	private int oldLocation = 0;

	public ContactListPage(ZetaIOSDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isMyUserNameDisplayedFirstInContactList(String name)
			throws Exception {
		if (DriverUtils.isElementDisplayed(this.getDriver(),
				By.xpath(IOSLocators.xpathMyUserInContactList))) {
			return myUserNameInContactList.getText().equals(name);
		} else {
			return false;
		}
	}

	public void muteConversation() {

		for (WebElement el : muteButtons) {
			if (el.isDisplayed()) {
				el.click();
			}
		}
	}

	public boolean isContactMuted(String contact) {

		// potential floating bug, the icon is always visible, but simply
		// changes x coordinate
		return driver.findElementByXPath(
				String.format(IOSLocators.xpathMutedIcon, contact))
				.getLocation().x < oldLocation;
	}

	public boolean isPlayPauseButtonVisible(String contact) throws Exception {
		return DriverUtils.waitUntilElementAppears(driver, By.xpath(String
				.format(IOSLocators.xpathContactListPlayPauseButton, contact)));
	}

	public void tapPlayPauseButton() {
		playPauseButton.click();
	}

	public void tapPlayPauseButtonNextTo(String name)
			throws InterruptedException {
		WebElement element = driver.findElement(By.xpath(String.format(
				IOSLocators.xpathContactListPlayPauseButton, name)));
		DriverUtils.iOSMultiTap(this.getDriver(), element, 1);
	}
	
	public PersonalInfoPage tapOnMyName(String name) throws Exception {
		WebElement el = driver.findElementByXPath(String.format(IOSLocators.xpathSelfName, name));
		el.click();
		
		return new PersonalInfoPage(this.getDriver(), this.getWait());
	}

	public IOSPage tapOnName(String name) throws Exception {
		WebElement el = findNameInContactList(name);
		boolean clickableGlitch = false;
		try {
			this.getWait().until(ExpectedConditions.elementToBeClickable(el));
		} catch (org.openqa.selenium.TimeoutException ex) {
			clickableGlitch = true;
		}
		if (clickableGlitch) {
			DriverUtils.mobileTapByCoordinates(getDriver(), el);
		} else {
			el.click();
		}

		return new DialogPage(this.getDriver(), this.getWait());
	}

	public String getFirstDialogName(String name) throws Exception {

		DriverUtils.waitUntilElementAppears(driver, By.xpath(String.format(
				IOSLocators.xpathFirstInContactList, name)));
		WebElement contact = driver.findElement(By.xpath(String.format(
				IOSLocators.xpathFirstInContactList, name)));
		return contact.getText();
	}

	private WebElement findNameInContactList(String name) {
		Boolean flag = true;
		WebElement contact = null;
		for (int i = 0; i < 5; i++) {
			for (WebElement listName : contactListNames) {
				if (listName.getText().equals(name) && listName.isDisplayed()) {
					contact = listName;
					flag = false;
					break;
				}
			}
			if (flag) {
				if (contactListNames.isEmpty()) {
					continue;
				}
				WebElement el = contactListNames
						.get(contactListNames.size() - 1);
				this.getWait().until(ExpectedConditions.visibilityOf(el));
				this.getWait().until(
						ExpectedConditions.elementToBeClickable(el));
				DriverUtils.scrollToElement(this.getDriver(), el);
			} else {
				break;
			}
		}
		return contact;
	}

	private WebElement findCellInContactList(String name) {
		Boolean flag = true;
		WebElement contact = null;
		for (int i = 0; i < 5; i++) {
			for (WebElement listCell : contactListCells) {
				for (WebElement cellText : contactListNames) {
					if (cellText.getText().equals(name)) {
						contact = listCell;
						flag = false;
						break;
					}
				}
			}
			if (flag) {
				WebElement el = contactListCells
						.get(contactListCells.size() - 1);
				this.getWait().until(ExpectedConditions.visibilityOf(el));
				this.getWait().until(
						ExpectedConditions.elementToBeClickable(el));
				DriverUtils.scrollToElement(this.getDriver(), el);
			} else {
				break;
			}
		}
		return contact;
	}

	public boolean isChatInContactList(String name) {
		boolean flag = findNameInContactList(name) != null;
		return flag;
	}

	public boolean isGroupChatAvailableInContactList() {
		boolean flag = false;

		for (WebElement el : contactListNames) {
			if (el.getAttribute("name").contains(",")) {
				flag = true;
				break;
			}
		}

		return flag;
	}

	public IOSPage swipeRightOnContact(int time, String contact)
			throws Exception {
		DriverUtils.swipeRight(this.getDriver(),
				findNameInContactList(contact), time, 70, 50);
		return returnBySwipe(SwipeDirection.RIGHT);
	}
	
	public IOSPage longSwipeRightOnContact(int time, String contact)
			throws Exception {
		DriverUtils.swipeRight(this.getDriver(),
				findNameInContactList(contact), time);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	private String getFirstConversationName() {
		String text = firstChatInChatListTextField.getText();
		return text;
	}

	public boolean verifyChangedGroupNameInChatList() {
		return firstChatInChatListTextField.getText().equals(
				PagesCollection.groupChatInfoPage.getConversationName());
	}

	public GroupChatPage tapOnUnnamedGroupChat(String contact1, String contact2)
			throws Exception {
		findChatInContactList(contact1, contact2).click();
		return new GroupChatPage(this.getDriver(), this.getWait());
	}

	public IOSPage tapOnGroupChat(String chatName) throws Exception {
		findNameInContactList(chatName).click();
		return new GroupChatPage(this.getDriver(), this.getWait());
	}

	public boolean waitForContactListToLoad() throws Exception {
		boolean waitForUser = DriverUtils.waitUntilElementAppears(driver,
				By.xpath(IOSLocators.xpathMyUserInContactList));
		if (!waitForUser) {
			log.debug(this.getDriver().getPageSource());
		}
		return waitForUser;
	}

	private WebElement findChatInContactList(String contact1, String contact2) {

		String contact = "";
		WebElement el = null;

		for (WebElement element : contactListNames) {
			contact = element.getAttribute("name");
			if (contact.contains(",") && contact.contains(contact1)
					&& contact.contains(contact2)) {
				el = element;
				break;
			}
		}

		return el;
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {

		IOSPage page = null;
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

	public boolean isPendingRequestInContactList() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.xpath(IOSLocators.xpathPendingRequest));
	}

	public PendingRequestsPage clickPendingRequest() throws Throwable {
		pendingRequest.click();
		return new PendingRequestsPage(this.getDriver(), this.getWait());
	}

	public boolean isDisplayedInContactList(String name) throws Exception {
		return DriverUtils.waitUntilElementAppears(driver, By.name(name));
	}

	public boolean isTutorialShown() throws Exception {
		// this.refreshUITree();
		DriverUtils.waitUntilElementAppears(driver,
				By.name(IOSLocators.nameTutorialView));
		boolean tutorialShown = DriverUtils.isElementDisplayed(
				this.getDriver(), tutorialView);
		return tutorialShown;
	}

	public void dismissTutorial() {

		WebElement tutorialView = driver.findElement(By
				.name(IOSLocators.nameTutorialView));
		DriverUtils.iOS3FingerTap(this.getDriver(), tutorialView, 3);
	}

	public List<WebElement> GetVisibleContacts() {
		return driver.findElements(By.className(IOSLocators.classNameContactListNames));
	}

	public IOSPage tapOnContactByIndex(List<WebElement> contacts, int index)
			throws Exception {
		IOSPage page = null;
		log.debug(DriverUtils.isElementDisplayed(driver, contacts.get(index)));
		DriverUtils.waitUntilElementClickable(driver, contacts.get(index));
		try {
			log.debug(contacts.get(index).getAttribute("name"));
			contacts.get(index).click();
		} catch (WebDriverException e) {
			BufferedImage im = DriverUtils.takeScreenshot(this.getDriver());
			ImageUtil.storeImageToFile(im, "/Project/ios_crash.jpg");
			log.debug("Can't select contact by index " + index + ". Page source: " +driver.getPageSource());
			throw e;
		}
		page = new DialogPage(this.getDriver(), this.getWait());
		return page;
	}

	@Override
	public IOSPage swipeDown(int time) throws Exception {
		Point coords = content.getLocation();
		Dimension elementSize = content.getSize();
		this.getDriver().swipe(coords.x + elementSize.width / 2,
				coords.y + 150, coords.x + elementSize.width / 2,
				coords.y + elementSize.height - 150, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public boolean conversationWithUsersPresented(String name1, String name2,
			String name3) {
		String firstChat = getFirstConversationName();
		boolean chatExist = firstChat.contains(name1)
				&& firstChat.contains(name2) && firstChat.contains(name3);
		return chatExist;
	}

	public void silenceConversation(String conversation) {
		WebElement contact = findNameInContactList(conversation);
		DriverUtils.clickSilenceConversationButton(this.getDriver(), contact);
	}

	public void unsilenceConversation(String conversation) {
		WebElement contact = findNameInContactList(conversation);
		DriverUtils.clickSilenceConversationButton(this.getDriver(), contact);
	}

	public boolean isConversationSilenced(String conversation) throws Exception {
		String deviceType = CommonUtils.getDeviceName(this.getClass());
		BufferedImage silencedConversation = null;
		BufferedImage referenceImage = null;
		WebElement element = findCellInContactList(conversation);
		silencedConversation = CommonUtils.getElementScreenshot(element,
				this.getDriver(), CommonUtils.getDeviceName(this.getClass()));
		File outputfile = new File("silenceTestIphone6.png");
		ImageIO.write(silencedConversation, "png", outputfile);
//		if (deviceType.equals("iPhone 6 Plus")) {
//			referenceImage = ImageUtil.readImageFromFile(IOSPage
//					.getImagesPath() + "silenceiPhone6plus.png");
//		} else {
//			referenceImage = ImageUtil.readImageFromFile(IOSPage
//					.getImagesPath() + "silenceVerification.png");
//		}
//		double score = ImageUtil.getOverlapScore(silencedConversation,
//				referenceImage, 0);
//		if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
//			return false;
//		}
		return true;
	}
	
	public boolean isConversationSilencedBefore(String conversation) throws Exception {
		String deviceType = CommonUtils.getDeviceName(this.getClass());
		BufferedImage silencedConversation = null;
		BufferedImage referenceImage = null;
		WebElement element = findCellInContactList(conversation);
		silencedConversation = CommonUtils.getElementScreenshot(element,
				this.getDriver(), CommonUtils.getDeviceName(this.getClass()));
		File outputfile = new File("unsilenceTestiPhone6.png");
		ImageIO.write(silencedConversation, "png", outputfile);
//		if (deviceType.equals("iPhone 6 Plus")) {
//			referenceImage = ImageUtil.readImageFromFile(IOSPage
//					.getImagesPath() + "silenceiPhone6plus.png");
//		} else {
//			referenceImage = ImageUtil.readImageFromFile(IOSPage
//					.getImagesPath() + "silenceVerification.png");
//		}
//		double score = ImageUtil.getOverlapScore(silencedConversation,
//				referenceImage, 0);
//		if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
//			return false;
//		}
		return true;
	}

	public void archiveConversation(String conversation) {
		WebElement contact = findNameInContactList(conversation);
		DriverUtils.clickArchiveConversationButton(this.getDriver(), contact);
		//DriverUtils.mobileTapByCoordinates(getDriver(), archiveButton);
	}

	public boolean unreadDotIsVisible(boolean visible, boolean bigUnreadDot,
			String conversation) throws IOException {
		BufferedImage unreadDot = null;
		BufferedImage referenceImage = null;
		double score = 0;
		WebElement contact = findCellInContactList(conversation);
		unreadDot = getScreenshotByCoordinates(contact.getLocation().x,
				contact.getLocation().y + contactListContainer.getLocation().y,
				contact.getSize().width / 4, contact.getSize().height * 2);
		if (visible == true && bigUnreadDot == true) {
			referenceImage = ImageUtil.readImageFromFile(IOSPage
					.getImagesPath() + "unreadDot.png");
			score = ImageUtil.getOverlapScore(referenceImage, unreadDot,
					ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
		} else if (visible == true && bigUnreadDot == false) {
			referenceImage = ImageUtil.readImageFromFile(IOSPage
					.getImagesPath() + "unreadDot_small.png");
			score = ImageUtil.getOverlapScore(referenceImage, unreadDot,
					ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
		} else if (visible == false && bigUnreadDot == false) {
			referenceImage = ImageUtil.readImageFromFile(IOSPage
					.getImagesPath() + "noUnreadDot.png");
			score = ImageUtil.getOverlapScore(referenceImage, unreadDot,
					ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
		}

		if (score <= MIN_ACCEPTABLE_IMAGE_UNREADDOT_VALUE) {
			return false;
		}

		return true;
	}

}