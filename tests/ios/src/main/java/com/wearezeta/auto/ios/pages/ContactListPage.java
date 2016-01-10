package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

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

	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.70;
	private final double MIN_ACCEPTABLE_IMAGE_SCORE = 0.80;
	private final int CONV_SWIPE_TIME = 500;

	@FindBy(how = How.NAME, using = IOSLocators.ContactListPage.nameSelfButton)
	private WebElement selfUserButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathNameContactList)
	private List<WebElement> contactListNames;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathContactListCells)
	private List<WebElement> contactListCells;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathFirstContactCell)
	private WebElement firstContactCell;

	@FindBy(how = How.NAME, using = IOSLocators.nameProfileName)
	private WebElement profileName;

	@FindBy(how = How.NAME, using = IOSLocators.ContactListPage.nameOpenStartUI)
	private WebElement openStartUI;

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

	@FindBy(how = How.NAME, using = IOSLocators.nameSelfButton)
	private WebElement selfButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathFirstInContactList)
	private WebElement firstContactInList;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathContactListContainer)
	private WebElement contactListContainer;

	// @FindBy(how = How.NAME, using = IOSLocators.nameArchiveButton)
	// private WebElement archiveButton;

	@FindBy(how = How.NAME, using = IOSLocators.ContactListPage.nameMuteCallButton)
	private WebElement muteCallButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameLeaveConversationButton)
	private WebElement leaveActionMenuButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameCancelButton)
	private WebElement cancelActionMenuButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameSendAnInviteButton)
	private WebElement inviteMorePeopleButton;

	private int oldLocation = 0;

	public ContactListPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isMyUserNameDisplayedFirstInContactList(String name)
			throws Exception {
		if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
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

	public PeoplePickerPage openSearch() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), openStartUI);
		openStartUI.click();
		return new PeoplePickerPage(getLazyDriver());
	}

	public boolean isContactMuted(String contact) throws Exception {
		// potential floating bug, the icon is always visible, but simply
		// changes x coordinate
		return this
				.getDriver()
				.findElementByXPath(
						String.format(IOSLocators.xpathMutedIcon, contact))
				.getLocation().x < oldLocation;
	}

	public boolean isPlayPauseButtonVisible(String contact) throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(), By
				.xpath(String.format(
						IOSLocators.xpathContactListPlayPauseButton, contact)));
	}

	public void tapPlayPauseButton() {
		playPauseButton.click();
	}

	public void tapPlayPauseButtonNextTo(String name) throws Exception {
		WebElement element = this.getDriver().findElement(
				By.xpath(String.format(
						IOSLocators.xpathContactListPlayPauseButton, name)));
		element.click();
	}

	public PersonalInfoPage tapOnMyName() throws Exception {
		selfButton.click();

		return new PersonalInfoPage(this.getLazyDriver());
	}

	public String getSelfButtonLabel() {
		String v = selfUserButton.getAttribute("label").toUpperCase();
		return v;
	}

	public boolean isSelfButtonContainingFirstNameLetter(String name) {
		String sub = name.substring(0, 1).toUpperCase();
		return sub.equals(getSelfButtonLabel());
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

		return new DialogPage(this.getLazyDriver());
	}

	public String getFirstDialogName() throws Exception {

		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(String.format(IOSLocators.xpathFirstInContactList)));
		WebElement contact = this.getDriver().findElement(
				By.xpath(String.format(IOSLocators.xpathFirstInContactList)));
		return contact.getText();
	}

	public String getDialogNameByIndex(int index) throws Exception {

		DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.xpath(String
				.format(IOSLocators.xpathContactListEntryWithIndex, index)));
		WebElement contact = this.getDriver().findElement(
				By.xpath(String.format(
						IOSLocators.xpathContactListEntryWithIndex, index)));
		return contact.getText();
	}

	private WebElement findNameInContactList(String name) throws Exception {
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
				this.getWait().until(ExpectedConditions.elementToBeClickable(el));
				this.getDriver().scrollToExact(el.getText());
			} else {
				break;
			}
		}
		return contact;
	}

	private WebElement findCellInContactList(String name) throws Exception {
		Boolean flag = true;
		WebElement contact = null;
		for (int i = 0; i < 5; i++) {
			for (WebElement listCell : contactListCells) {
				if (listCell.getAttribute("name").equals(name)) {
					contact = listCell;
					flag = false;
					break;
				}
			}
			if (flag) {
				WebElement el = contactListCells
						.get(contactListCells.size() - 1);
				this.getWait().until(ExpectedConditions.visibilityOf(el));
				this.getWait().until(ExpectedConditions.elementToBeClickable(el));
				this.getDriver().scrollToExact(el.getText());
			} else {
				break;
			}
		}
		return contact;
	}

	public boolean isChatInContactList(String name) throws Exception {
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

	public IOSPage swipeRightOnContact(String contact) throws Exception {
		DriverUtils.swipeRight(this.getDriver(),
				findNameInContactList(contact), CONV_SWIPE_TIME, 90, 50);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	public void swipeRightConversationToRevealArchiveButton(String conversation)
			throws Exception {
		int count = 0;

		do {
			swipeRightOnContact(conversation);
			count++;
		} while ((count < 5)
				&& !isArchiveConversationButtonVisible(conversation));

	}

	public void swipeRightConversationToRevealActionButtons(String conversation)
			throws Exception {
		int count = 0;

		do {
			swipeRightOnContact(conversation);
			count++;
		} while ((count < 5) && !isCancelActionButtonVisible());
	}

	public IOSPage longSwipeRightOnContact(int time, String contact)
			throws Exception {
		DriverUtils.swipeRight(this.getDriver(),
				findNameInContactList(contact), time);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	public String getFirstConversationName() throws Exception {
		if (DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.xpath(IOSLocators.xpathFirstChatInChatListTextField))) {
			String text = firstChatInChatListTextField.getText();
			return text;
		} else
			return null;
	}

	public boolean verifyChangedGroupNameInChatList() throws Exception {
		return firstChatInChatListTextField.getText().equals(
				new GroupChatInfoPage(getLazyDriver()).getConversationName());
	}

	public GroupChatPage tapOnUnnamedGroupChat(String contact1, String contact2)
			throws Exception {
		findChatInContactList(contact1, contact2).click();
		return new GroupChatPage(this.getLazyDriver());
	}

	public IOSPage tapOnGroupChat(String chatName) throws Exception {
		findNameInContactList(chatName).click();
		return new GroupChatPage(this.getLazyDriver());
	}

	public boolean waitForContactListToLoad() throws Exception {
		boolean waitForUser = DriverUtils.waitUntilLocatorAppears(
				this.getDriver(),
				By.xpath(IOSLocators.xpathMyUserInContactList));
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
			page = new PeoplePickerPage(this.getLazyDriver());
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
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(IOSLocators.xpathPendingRequest), 5);
	}

	public boolean pendingRequestInContactListIsNotShown() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(IOSLocators.xpathPendingRequest));
	}

	public PendingRequestsPage clickPendingRequest() throws Exception {
		pendingRequest.click();
		return new PendingRequestsPage(this.getLazyDriver());
	}

	public boolean isDisplayedInContactList(String name) throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(name), 5);
	}

	public boolean contactIsNotDisplayed(String name) throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.name(name), 5);
	}

	public boolean isTutorialShown() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.nameTutorialView), 10);
	}

	public void dismissTutorial() throws Exception {
		WebElement tutorialView = this.getDriver().findElement(
				By.name(IOSLocators.nameTutorialView));
		this.getDriver().tap(3, tutorialView, 1);
	}

	public List<WebElement> GetVisibleContacts() throws Exception {
		return this.getDriver().findElements(
				By.className(IOSLocators.classNameContactListNames));
	}

	public IOSPage tapOnContactByIndex(List<WebElement> contacts, int index)
			throws Exception {
		IOSPage page = null;
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				contacts.get(index));
		try {
			log.debug(contacts.get(index).getAttribute("name"));
			contacts.get(index).click();
		} catch (WebDriverException e) {
			BufferedImage im = DriverUtils.takeFullScreenShot(this.getDriver())
					.orElseThrow(IllegalStateException::new);
			ImageUtil.storeImageToFile(im, "/Project/ios_crash.jpg");
			log.debug("Can't select contact by index " + index
					+ ". Page source: " + this.getDriver().getPageSource());
			throw e;
		}
		page = new DialogPage(this.getLazyDriver());
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
			String name3) throws Exception {
		String firstChat = getFirstConversationName();
		boolean chatExist = firstChat.contains(name1)
				&& firstChat.contains(name2) && firstChat.contains(name3);
		return chatExist;
	}

	public void silenceConversation(String conversation) throws Exception {
		WebElement contact = findNameInContactList(conversation);
		DriverUtils.clickSilenceConversationButton(this.getDriver(), contact);
	}

	public void unsilenceConversation(String conversation) throws Exception {
		WebElement contact = findNameInContactList(conversation);
		DriverUtils.clickSilenceConversationButton(this.getDriver(), contact);
	}

	public boolean isConversationSilenced(String conversation,
			boolean isSilenced) throws Exception {
		String deviceType = CommonUtils.getDeviceName(this.getClass());
		BufferedImage silencedConversation = null;

		BufferedImage referenceImage = null;
		WebElement element = findCellInContactList(conversation);
		silencedConversation = CommonUtils.getElementScreenshot(element,
				this.getDriver(), CommonUtils.getDeviceName(this.getClass()))
				.orElseThrow(IllegalStateException::new);
		if (deviceType.equals("iPhone 6 Plus") && isSilenced) {
			referenceImage = ImageUtil.readImageFromFile(IOSPage
					.getImagesPath() + "silenceiPhone6plus.png");
		} else if (deviceType.equals("iPhone 6 Plus") && !isSilenced) {
			referenceImage = ImageUtil.readImageFromFile(IOSPage
					.getImagesPath() + "verifyUnsilenceIphone6plus.png");
		} else if (deviceType.equals("iPhone 6") && isSilenced) {
			referenceImage = ImageUtil.readImageFromFile(IOSPage
					.getImagesPath() + "silenceiPhone6.png");
		} else if (deviceType.equals("iPhone 6") && !isSilenced) {
			referenceImage = ImageUtil.readImageFromFile(IOSPage
					.getImagesPath() + "verifyUnsilenceTestIphone6.png");
		} else if (deviceType.equals("iPad Air") && isSilenced) {
			referenceImage = ImageUtil.readImageFromFile(IOSPage
					.getImagesPath()
					+ "verifySilenceiPadAir_"
					+ getOrientation().toString() + ".png");
		} else if (deviceType.equals("iPad Air") && !isSilenced) {
			referenceImage = ImageUtil.readImageFromFile(IOSPage
					.getImagesPath()
					+ "verifyUnsilenceTestiPadAir_"
					+ getOrientation().toString() + ".png");
		}
		double score = ImageUtil.getOverlapScore(silencedConversation,
				referenceImage, 0);
		if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
			return false;
		}
		return true;
	}

	public boolean isConversationSilencedBefore(String conversation)
			throws Exception {
		String deviceType = CommonUtils.getDeviceName(this.getClass());
		BufferedImage silencedConversation = null;
		BufferedImage referenceImage = null;
		WebElement element = findCellInContactList(conversation);
		silencedConversation = CommonUtils.getElementScreenshot(element,
				this.getDriver(), CommonUtils.getDeviceName(this.getClass()))
				.orElseThrow(IllegalStateException::new);
		if (deviceType.equals("iPhone 6 Plus")) {
			referenceImage = ImageUtil.readImageFromFile(IOSPage
					.getImagesPath() + "unsilenceTestiPhone6plus.png");
		} else if (deviceType.equals("iPhone 6")) {
			referenceImage = ImageUtil.readImageFromFile(IOSPage
					.getImagesPath() + "unsilenceTestiPhone6.png");
		} else if (deviceType.equals("iPad Air")) {
			referenceImage = ImageUtil.readImageFromFile(IOSPage
					.getImagesPath() + "unsilenceTestiPadAir.png");
		}

		double score = ImageUtil.getOverlapScore(silencedConversation,
				referenceImage, 0);
		if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
			return false;
		}
		return true;
	}

	private boolean isCancelActionButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				cancelActionMenuButton);
	}

	private boolean isArchiveConversationButtonVisible(String conversation)
			throws Exception {
		if (DriverUtils
				.waitUntilLocatorAppears(
						getDriver(),
						By.xpath(IOSLocators.ContactListPage.xpathArchiveConversationButton),
						3)) {
			WebElement archiveButton = this
					.getDriver()
					.findElement(
							By.xpath(IOSLocators.ContactListPage.xpathArchiveConversationButton));
			return DriverUtils.waitUntilElementClickable(getDriver(),
					archiveButton, 3);
		} else {
			return false;
		}
	}

	public void clickArchiveConversationButton(String conversation)
			throws Exception {
		WebElement archiveButton = this
				.getDriver()
				.findElement(
						By.xpath(IOSLocators.ContactListPage.xpathArchiveConversationButton));
		DriverUtils.mobileTapByCoordinates(getDriver(), archiveButton);
	}

	public void archiveConversation(String conversation) throws Exception {
		WebElement contact = findNameInContactList(conversation);
		DriverUtils.clickArchiveConversationButton(this.getDriver(), contact);
		// DriverUtils.mobileTapByCoordinates(getDriver(), archiveButton);
	}

	public BufferedImage getScreenshotFirstContact() throws Exception {
		// This takes a screenshot of the area to the left of a contact where
		// ping and unread dot notifications are visible
		WebElement contact = firstContactCell;
		return getScreenshotByCoordinates(contact.getLocation().x,
				contact.getLocation().y + contactListContainer.getLocation().y,
				contact.getSize().width / 4, contact.getSize().height * 2)
				.orElseThrow(IllegalStateException::new);
	}

	public boolean missedCallIndicatorIsVisible(String conversation)
			throws Exception {
		BufferedImage missedCallIndicator = null;
		BufferedImage referenceImage = null;
		double score = 0;
		WebElement contact = findCellInContactList(conversation);

		missedCallIndicator = getElementScreenshot(contact).orElseThrow(
				IllegalStateException::new).getSubimage(0, 0,
				2 * contact.getSize().height, 2 * contact.getSize().height);

		referenceImage = ImageUtil.readImageFromFile(IOSPage.getImagesPath()
				+ "missedCallIndicator.png");

		score = ImageUtil.getOverlapScore(referenceImage, missedCallIndicator,
				ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);

		if (score <= MIN_ACCEPTABLE_IMAGE_SCORE) {
			log.debug("Overlap Score is " + score
					+ ". And minimal expected is " + MIN_ACCEPTABLE_IMAGE_SCORE);
			return false;

		}
		return true;
	}

	public boolean unreadMessageIndicatorIsVisible(int numberOfMessages,
			String conversation) throws Exception {
		BufferedImage unreadMessageIndicator = null;
		BufferedImage referenceImage = null;
		double score = 0;
		WebElement contact = findCellInContactList(conversation);

		unreadMessageIndicator = getElementScreenshot(contact).orElseThrow(
				IllegalStateException::new).getSubimage(0, 0,
				2 * contact.getSize().height, 2 * contact.getSize().height);

		if (numberOfMessages == 0) {
			if (CommonUtils.getDeviceName(this.getClass()).equals("iPad Air")) {
				if (getOrientation() == ScreenOrientation.LANDSCAPE) {
					referenceImage = ImageUtil.readImageFromFile(IOSPage
							.getImagesPath()
							+ "unreadMessageIndicator0_iPad_landscape.png");
				} else {
					referenceImage = ImageUtil.readImageFromFile(IOSPage
							.getImagesPath()
							+ "unreadMessageIndicator0_iPad.png");
				}

			} else if (CommonUtils.getDeviceName(this.getClass()).equals(
					"iPhone 6")) {
				referenceImage = ImageUtil.readImageFromFile(IOSPage
						.getImagesPath()
						+ "unreadMessageIndicator0_iPhone6.png");
			} else if (CommonUtils.getDeviceName(this.getClass()).equals(
					"iPhone 6 Plus")) {
				referenceImage = ImageUtil.readImageFromFile(IOSPage
						.getImagesPath()
						+ "unreadMessageIndicator0_iPhone6Plus.png");
			}
		} else if (numberOfMessages == 1) {
			if (CommonUtils.getDeviceName(this.getClass()).equals("iPhone 6")) {
				referenceImage = ImageUtil.readImageFromFile(IOSPage
						.getImagesPath()
						+ "unreadMessageIndicator1_iPhone6.png");
			} else if (CommonUtils.getDeviceName(this.getClass()).equals(
					"iPhone 6 Plus")) {
				referenceImage = ImageUtil.readImageFromFile(IOSPage
						.getImagesPath()
						+ "unreadMessageIndicator1_iPhone6Plus.png");
			} else {
				referenceImage = ImageUtil.readImageFromFile(IOSPage
						.getImagesPath() + "unreadMessageIndicator1.png");
			}
		} else if (numberOfMessages > 1 && numberOfMessages < 10) {
			if (CommonUtils.getDeviceName(this.getClass()).equals("iPhone 6")) {
				referenceImage = ImageUtil.readImageFromFile(IOSPage
						.getImagesPath()
						+ "unreadMessageIndicator5_iPhone6.png");
			} else if (CommonUtils.getDeviceName(this.getClass()).equals(
					"iPhone 6 Plus")) {
				referenceImage = ImageUtil.readImageFromFile(IOSPage
						.getImagesPath()
						+ "unreadMessageIndicator5_iPhone6Plus.png");
			} else {
				referenceImage = ImageUtil.readImageFromFile(IOSPage
						.getImagesPath() + "unreadMessageIndicator5.png");
			}
		} else if (numberOfMessages >= 10) {
			if (CommonUtils.getDeviceName(this.getClass()).equals("iPhone 6")) {
				referenceImage = ImageUtil.readImageFromFile(IOSPage
						.getImagesPath()
						+ "unreadMessageIndicator10_iPhone6.png");
			} else if (CommonUtils.getDeviceName(this.getClass()).equals(
					"iPhone 6 Plus")) {
				referenceImage = ImageUtil.readImageFromFile(IOSPage
						.getImagesPath()
						+ "unreadMessageIndicator10_iPhone6Plus.png");
			} else {
				referenceImage = ImageUtil.readImageFromFile(IOSPage
						.getImagesPath() + "unreadMessageIndicator10.png");
			}
		}

		score = ImageUtil.getOverlapScore(referenceImage,
				unreadMessageIndicator,
				ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);

		if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
			log.debug("Overlap Score is " + score
					+ ". And minimal expected is " + MIN_ACCEPTABLE_IMAGE_VALUE);
			return false;

		}
		return true;
	}

	public boolean changeOfAccentColorIsVisible(String name) throws Exception {

		return false; // Needs refactoring, UI have changed
		// BufferedImage changedAccentColorImage = null;
		// BufferedImage referenceImage = null;
		// double score = 0;
		// WebElement el = this.getDriver().findElementByXPath(
		// String.format(IOSLocators.xpathSelfName, name));
		// changedAccentColorImage = getElementScreenshot(el).orElseThrow(
		// IllegalStateException::new);
		// referenceImage = ImageUtil.readImageFromFile(IOSPage.getImagesPath()
		// + "changedAccentColor.png");
		// score = ImageUtil.getOverlapScore(referenceImage,
		// changedAccentColorImage,
		// ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
		// if (score >= MIN_ACCEPTABLE_IMAGE_ACCENTCOLOR_VALUE) {
		// return true;
		// }
		// return false;
	}

	public boolean isMuteCallButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				muteCallButton);
	}

	public void clickMuteCallButton() {
		muteCallButton.click();
	}

	public boolean isPauseButtonVisible() throws IllegalStateException,
			Exception {
		BufferedImage pauseMediaButtonIcon = null;
		BufferedImage referenceImage = null;
		double score = 0;

		pauseMediaButtonIcon = getElementScreenshot(playPauseButton)
				.orElseThrow(IllegalStateException::new);

		referenceImage = ImageUtil.readImageFromFile(IOSPage.getImagesPath()
				+ "pauseMediaButtonIcon.png");

		score = ImageUtil.getOverlapScore(referenceImage, pauseMediaButtonIcon,
				ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);

		if (score <= MIN_ACCEPTABLE_IMAGE_SCORE) {
			log.debug("Overlap Score is " + score
					+ ". And minimal expected is " + MIN_ACCEPTABLE_IMAGE_SCORE);
			return false;
		}

		return true;
	}

	public boolean isPlayButtonVisible() throws IllegalStateException,
			Exception {
		BufferedImage playMediaButtonIcon = null;
		BufferedImage referenceImage = null;
		double score = 0;

		playMediaButtonIcon = getElementScreenshot(playPauseButton)
				.orElseThrow(IllegalStateException::new);

		referenceImage = ImageUtil.readImageFromFile(IOSPage.getImagesPath()
				+ "playMediaButtonIcon.png");

		score = ImageUtil.getOverlapScore(referenceImage, playMediaButtonIcon,
				ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);

		if (score <= MIN_ACCEPTABLE_IMAGE_SCORE) {
			log.debug("Overlap Score is " + score
					+ ". And minimal expected is " + MIN_ACCEPTABLE_IMAGE_SCORE);
			return false;
		}

		return true;
	}

	public boolean isActionMenuVisibleForConversation(String conversation)
			throws Exception {
		String xpath = String
				.format(IOSLocators.ContactListPage.xpathFormatActionMenuConversationName,
						conversation.toUpperCase());
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpath));
	}

	public boolean isButtonVisibleInActionMenu(String buttonTitle)
			throws Exception {
		String xpath = String.format(
				IOSLocators.ContactListPage.xpathFormatActionMenuXButton,
				buttonTitle.toUpperCase());
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpath));
	}

	public void clickArchiveButtonInActionMenu() throws Exception {
		WebElement archiveButton = this
				.getDriver()
				.findElement(
						By.xpath(IOSLocators.ContactListPage.xpathArchiveConversationButton));
		DriverUtils.mobileTapByCoordinates(getDriver(), archiveButton);
	}

	public void clickLeaveButtonInActionMenu() {
		leaveActionMenuButton.click();
	}

	public void clickCancelButtonInActionMenu() {
		cancelActionMenuButton.click();
	}

	public String getSelectedConversationCellValue(String conversation)
			throws Exception {
		WebElement conversationCell = this
				.getDriver()
				.findElement(
						By.xpath(String
								.format(IOSLocators.ContactListPage.xpathSpecificContactListCell,
										conversation)));
		return conversationCell.getAttribute("value");
	}

	public boolean isInviteMorePeopleButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				inviteMorePeopleButton);
	}

	public boolean isInviteMorePeopleButtonNotVisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.name(IOSLocators.nameSendAnInviteButton));
	}

	public boolean waitUntilSelfButtonIsDisplayed() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.name(IOSLocators.ContactListPage.nameSelfButton));
	}

}
