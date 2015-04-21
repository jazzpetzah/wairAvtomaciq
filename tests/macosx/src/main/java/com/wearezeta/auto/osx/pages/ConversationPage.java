package com.wearezeta.auto.osx.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jboss.netty.handler.timeout.TimeoutException;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.util.NSPoint;

public class ConversationPage extends MainWirePage {

	private static final Logger log = ZetaLogger.getLog(ConversationPage.class
			.getSimpleName());

	@FindBy(how = How.ID, using = OSXLocators.ConversationPage.idConversationScrollArea)
	private WebElement conversationScrollArea;

	@FindBy(how = How.ID, using = OSXLocators.ConversationPage.idOpenPeoplePopoverButton)
	private WebElement peoplePopoverButton;

	@FindBy(how = How.ID, using = OSXLocators.ConversationPage.idSendImageButton)
	private WebElement sendImageButton;

	@FindBy(how = How.ID, using = OSXLocators.ConversationPage.idPingButton)
	private WebElement pingButton;

	@FindBy(how = How.ID, using = OSXLocators.ConversationPage.idCallButton)
	private WebElement callButton;

	static final String SOUNDCLOUD_BUTTON_ATT_TITLE = "AXDescription";
	static final String SOUNDCLOUD_BUTTON_ATT_TITLE_9 = "AXTitle";

	static String SOUNDCLOUD_BUTTON_STATE;
	int numberSoundCloudButtons;

	private WebElement newMessageTextArea = findNewMessageTextArea();

	@FindBy(how = How.XPATH, using = OSXLocators.xpathMessageEntry)
	private List<WebElement> messageEntries;

	@FindBy(how = How.NAME, using = OSXLocators.namePingMenuItem)
	private WebElement pingMenuItem;

	@FindBy(how = How.NAME, using = OSXLocators.namePingAgainMenuItem)
	private WebElement pingAgainMenuItem;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathSoundCloudLinkButton)
	private List<WebElement> soundCloudButtons;

	private WebElement soundCloudLinkButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathSoundCloudMediaContainer)
	private WebElement soundCloudMediaContainer;

	@FindBy(how = How.ID, using = OSXLocators.idMediaBarPlayPauseButton)
	private WebElement mediabarPlayPauseButton;

	@FindBy(how = How.ID, using = OSXLocators.idMediaBarCloseButton)
	private WebElement mediabarStopCloseButton;

	@FindBy(how = How.ID, using = OSXLocators.idMediaBarTitelButton)
	private WebElement mediabarBarTitle;

	public String currentConversationName;

	public ConversationPage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		this(driver, wait, null);
	}

	public ConversationPage(ZetaOSXDriver driver, WebDriverWait wait,
			String conversationName) throws Exception {
		super(driver, wait);
		this.currentConversationName = conversationName;
	}

	public void focusOnConversation() {
		conversationScrollArea.click();
	}

	public WebElement findNewMessageTextArea() {
		List<WebElement> rows = driver.findElements(By
				.xpath(OSXLocators.xpathNewMessageTextArea));
		for (WebElement row : rows) {
			if (row.getText().equals("")) {
				return row;
			}
		}
		return null;
	}

	public boolean isMessageTextAreaVisible() {
		WebElement newMessageTextArea = findNewMessageTextArea();
		if (newMessageTextArea != null) {
			return true;
		}
		return false;
	}

	public void ping() {
		pingMenuItem.click();
	}

	public void pingAgain() {
		pingAgainMenuItem.click();
	}

	public boolean isMessageExist(String message) throws InterruptedException {
		String messageText = "";

		boolean isSystemMessage = true;
		if (message.contains(OSXLocators.YOU_ADDED_MESSAGE)) {
			messageText = OSXLocators.YOU_ADDED_MESSAGE;
		} else if (message.contains(OSXLocators.USER_ADDED_MESSAGE_FORMAT)) {
			messageText = OSXLocators.USER_ADDED_MESSAGE_FORMAT;
		} else if (message
				.contains(OSXLocators.YOU_STARTED_CONVERSATION_MESSAGE)) {
			messageText = OSXLocators.YOU_STARTED_CONVERSATION_MESSAGE;
		} else if (message
				.contains(OSXLocators.USER_STARTED_CONVERSATION_MESSAGE_FORMAT)) {
			messageText = OSXLocators.USER_STARTED_CONVERSATION_MESSAGE_FORMAT;
		} else {
			isSystemMessage = false;
		}

		if (isSystemMessage) {
			for (int i = 0; i < 10; i++) {
				List<WebElement> els = driver.findElements(By
						.xpath(OSXLocators.xpathMessageEntry));
				Collections.reverse(els);
				for (WebElement el : els) {
					if (el.getText().contains(messageText)) {
						return true;
					}
				}
				Thread.sleep(1000);
			}
		} else {
			String xpath = String.format(
					OSXLocators.xpathFormatSpecificMessageEntry, message);
			WebElement el = driver.findElement(By.xpath(xpath));
			return (el != null);
		}
		return false;
	}

	public void writeNewMessage(String message) {
		int i = 0;
		while (newMessageTextArea == null) {
			newMessageTextArea = findNewMessageTextArea();
			if (++i > 10) {
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		newMessageTextArea.sendKeys(message + "\\n");
	}

	public void sendNewMessage() {
		newMessageTextArea.submit();
	}

	public void openPeoplePopover() {
		peoplePopoverButton.click();
	}

	public void openChooseImageDialog() throws IOException {
		sendImageButton.click();
	}

	public int getNumberOfYouPingedMessages(String xpath) {
		List<WebElement> youPingedMessages = driver.findElements(By
				.xpath(xpath));
		log.debug("Retrieved number of Pings in conversation: "
				+ youPingedMessages.size());
		return youPingedMessages.size();
	}

	public int getNumberOfMessageEntries(String message) {
		String xpath = String.format(
				OSXLocators.xpathFormatSpecificMessageEntry, message);
		List<WebElement> messageEntries = driver.findElements(By.xpath(xpath));
		return messageEntries.size();
	}

	public int getNumberOfImageEntries() throws Exception {
		DriverUtils.setImplicitWaitValue(driver, 1);
		List<WebElement> conversationImages = driver.findElements(By
				.xpath(OSXLocators.xpathConversationImageEntry));
		DriverUtils.setDefaultImplicitWait(driver);
		return conversationImages.size();
	}

	public boolean isMessageSent(String message) throws Exception {
		boolean isSend = false;
		String xpath = String.format(
				OSXLocators.xpathFormatSpecificMessageEntry, message);
		DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath));
		WebElement element = driver.findElement(By.xpath(xpath));
		if (element != null) {
			isSend = true;
		}
		return isSend;
	}

	public void setLastSoundCloudButtonToUse() {
		numberSoundCloudButtons = soundCloudButtons.size();
		soundCloudLinkButton = soundCloudButtons
				.get(numberSoundCloudButtons - 1);
	}

	public void tapOnSoundCloudMessage() {
		setLastSoundCloudButtonToUse();
		soundCloudLinkButton.click();
	}

	public boolean isSoundCloudContainerVisible() throws Exception {
		return DriverUtils
				.waitUntilElementAppears(
						driver,
						By.xpath(OSXLocators.xpathSoundCloudMediaContainerWithoutImage));
	}

	public String getSoundCloudButtonState() {
		setLastSoundCloudButtonToUse();
		SOUNDCLOUD_BUTTON_STATE = soundCloudLinkButton
				.getAttribute(SOUNDCLOUD_BUTTON_ATT_TITLE);
		if (SOUNDCLOUD_BUTTON_STATE.trim().isEmpty()) {
			SOUNDCLOUD_BUTTON_STATE = soundCloudLinkButton
					.getAttribute(SOUNDCLOUD_BUTTON_ATT_TITLE_9);
		}
		return SOUNDCLOUD_BUTTON_STATE;
	}

	public void scrollDownTillMediaBarAppears() throws Exception {
		NSPoint mediaBarPosition = NSPoint.fromString(mediabarBarTitle
				.getAttribute(OSXConstants.Attributes.AXPOSITION));
		NSPoint conversationPosition = NSPoint
				.fromString(conversationScrollArea
						.getAttribute(OSXConstants.Attributes.AXPOSITION));

		NSPoint windowSize = NSPoint.fromString(window
				.getAttribute(OSXConstants.Attributes.AXSIZE));
		log.debug("Window size: " + windowSize);

		log.debug("Window position: " + conversationPosition);
		log.debug("Initial media bar position: " + mediaBarPosition);

		// get scrollbar for conversation view
		WebElement conversationDecrementSB = null;

		WebElement scrollArea = driver.findElement(By
				.id(OSXLocators.ConversationPage.idConversationScrollArea));

		if (mediaBarPosition.y() < conversationPosition.y()) {
			WebElement scrollBar = scrollArea.findElement(By
					.xpath("//AXScrollBar[1]"));
			List<WebElement> scrollButtons = scrollBar.findElements(By
					.xpath("//AXButton"));
			for (WebElement scrollButton : scrollButtons) {
				String subrole = scrollButton.getAttribute("AXSubrole");
				if (subrole.equals("AXDecrementPage")) {
					conversationDecrementSB = scrollButton;
				}
			}
			long TIMEOUT_MINUTES = 1;
			long startDate = new Date().getTime();
			while (mediaBarPosition.y() < conversationPosition.y()) {
				conversationDecrementSB.click();
				mediaBarPosition = NSPoint.fromString(mediabarBarTitle
						.getAttribute(OSXConstants.Attributes.AXPOSITION));
				log.debug("Current media bar position: " + mediaBarPosition);
				log.debug("Media play state: " + getSoundCloudButtonState());
				long endDate = new Date().getTime();
				if (endDate - startDate > TIMEOUT_MINUTES * 60 * 1000)
					break;
			}
		}
	}

	public void scrollDownToLastMessage() throws Exception {
		NSPoint lastGroupPosition = null;
		WebElement lastGroup = null;
		List<WebElement> groups = driver.findElements(By
				.xpath(OSXLocators.xpathConversationMessageGroup));
		int lastPosition = 0;
		for (WebElement group : groups) {
			NSPoint position = NSPoint.fromString(group
					.getAttribute(OSXConstants.Attributes.AXPOSITION));
			NSPoint size = NSPoint.fromString(group
					.getAttribute(OSXConstants.Attributes.AXSIZE));
			if (position == null || size == null) {
				log.debug("Can't get position or size for current element. Position: "
						+ position + ", size: " + size);
				continue;
			}
			if (position.y() + size.y() > lastPosition) {
				lastPosition = position.y() + size.y();
				lastGroupPosition = new NSPoint(position.x(), position.y()
						+ size.y());
				lastGroup = group;
			}
		}

		NSPoint textInputPosition = NSPoint.fromString(newMessageTextArea
				.getAttribute(OSXConstants.Attributes.AXPOSITION));

		// get scrollbar for conversation view
		WebElement conversationIncrementSB = null;

		WebElement scrollArea = driver.findElement(By
				.id(OSXLocators.ConversationPage.idConversationScrollArea));

		if (lastGroupPosition == null || textInputPosition == null) {
			log.debug("No scroll, last group were not found.");
			return;
		}

		if (lastGroupPosition.y() > textInputPosition.y()) {
			WebElement scrollBar = scrollArea.findElement(By
					.xpath("//AXScrollBar[2]"));
			List<WebElement> scrollButtons = scrollBar.findElements(By
					.xpath("//AXButton"));
			for (WebElement scrollButton : scrollButtons) {
				String subrole = scrollButton.getAttribute("AXSubrole");
				if (subrole.equals("AXIncrementPage")) {
					conversationIncrementSB = scrollButton;
				}
			}
			long TIMEOUT_MINUTES = 2;
			long startDate = new Date().getTime();
			while (lastGroupPosition.y() > textInputPosition.y()) {
				conversationIncrementSB.click();
				lastGroupPosition = NSPoint.fromString(lastGroup
						.getAttribute(OSXConstants.Attributes.AXPOSITION));
				long endDate = new Date().getTime();
				if (endDate - startDate > TIMEOUT_MINUTES * 60 * 1000)
					break;
			}
		}
	}

	public void pressPlayPauseButton() {
		mediabarPlayPauseButton.click();
	}

	public void pressStopButton() {
		mediabarStopCloseButton.click();
	}

	public void pressMediaTitle() {
		mediabarBarTitle.click();
	}

	public String getLastConversationNameChangeMessage() {
		WebElement el = driver.findElement(By
				.xpath(OSXLocators.xpathConversationLastNewNameEntry));
		return el.getAttribute(OSXConstants.Attributes.AXVALUE);
	}

	public boolean isMediaBarVisible() {
		NSPoint mediaBarPosition = NSPoint.fromString(mediabarBarTitle
				.getAttribute(OSXConstants.Attributes.AXPOSITION));
		NSPoint conversationPosition = NSPoint
				.fromString(conversationScrollArea
						.getAttribute(OSXConstants.Attributes.AXPOSITION));
		if (mediaBarPosition.y() >= conversationPosition.y())
			return true;
		else
			return false;
	}

	private static int STATE_CHANGE_TIMEOUT = 60 * 2;

	public String getCurrentPlaybackTime() {
		String time = "";
		try {
			WebElement el = driver.findElement(By
					.xpath(OSXLocators.xpathSoundCloudCurrentPlaybackTime));
			time = el.getAttribute(OSXConstants.Attributes.AXVALUE);
		} catch (NoSuchElementException e) {
			log.error("No element that contains playback time");
		}
		return time;
	}

	public void waitForSoundcloudButtonState(String currentState,
			String wantedState) throws InterruptedException {
		Thread.sleep(1000);
		int secondsElapsed = 0;
		while (!currentState.equals(wantedState)
				&& secondsElapsed < STATE_CHANGE_TIMEOUT) {
			Assert.assertEquals(currentState, currentState);
			Thread.sleep(1000);
			currentState = getSoundCloudButtonState();
			secondsElapsed++;
		}
		if (secondsElapsed >= STATE_CHANGE_TIMEOUT) {
			throw new TimeoutException(
					String.format(
							"The Soundcloud button have not changed its state from \"%s\" to \"%s\" within %d seconds timeout",
							currentState, wantedState, STATE_CHANGE_TIMEOUT));
		}
	}

	public boolean isMediaLinkAppearsInDialog(String link) throws Exception {
		return DriverUtils.waitUntilElementAppears(driver, By.name(link));
	}

	private static final String UUID_TEXT_MESSAGE_PATTERN = "<AXGroup[^>]*>\\s*<AXStaticText[^>]*AXValue=\"([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*/>\\s*</AXGroup>";

	public ArrayList<MessageEntry> listAllMessages(boolean checkTime) {
		long startDate = new Date().getTime();
		Date receivedDate = new Date();
		String source = driver.getPageSource();
		long endDate = new Date().getTime();
		log.debug("Time to get page source: " + (endDate - startDate) + "ms");
		ArrayList<MessageEntry> listResult = new ArrayList<MessageEntry>();
		Pattern pattern = Pattern.compile(UUID_TEXT_MESSAGE_PATTERN);
		Matcher matcher = pattern.matcher(source);
		while (matcher.find()) {
			listResult.add(new MessageEntry("text", matcher.group(1),
					receivedDate, checkTime));
		}
		return listResult;
	}

	public MessageEntry receiveMessage(String message, boolean checkTime)
			throws Exception {
		DriverUtils.setImplicitWaitValue(driver, 120);
		try {
			Date receivedDate = new Date();
			long startDate = new Date().getTime();
			WebElement messageElement = driver.findElement(By.xpath(String
					.format(OSXLocators.xpathFormatSpecificMessageEntry,
							message)));
			long endDate = new Date().getTime();
			long time = endDate - startDate;
			if (messageElement != null) {
				return new MessageEntry("text", message, new Date(
						receivedDate.getTime() + time / 2), checkTime);
			}
		} finally {
			DriverUtils.setDefaultImplicitWait(driver);
		}
		return null;

	}

	public void openFinder() {
		String scr0 = "tell application \"Finder\" to close every window\n"
				+ "tell application \"Finder\" to open folder \"Documents\" of home\n"
				+ "tell application \"System Events\" to tell application process \"Finder\"\n"
				+ "set frontmost to true\n"
				+ "end tell\n"
				+ "tell application \"System Events\" to tell application process \"Finder\"\n"
				+ "set position of window 1 to {0, 0}\n"
				+ "end tell\n"
				+ "tell application \"Finder\" to set the current view of the front Finder window to icon view";
		driver.executeScript(scr0);
	}

	public void dragPictureToConversation(String picture) throws Exception {
		WebElement target = driver.findElement(By
				.id(OSXLocators.ConversationPage.idConversationScrollArea));

		NSPoint targetLocation = NSPoint.fromString(target
				.getAttribute(OSXConstants.Attributes.AXPOSITION));
		NSPoint targetSize = NSPoint.fromString(target
				.getAttribute(OSXConstants.Attributes.AXSIZE));

		int xLoc = targetLocation.x() + targetSize.x() / 2;
		int yLoc = targetLocation.y() + targetSize.y() / 2;

		String scr0 = "tell application \"Finder\" "
				+ "to set the bounds of the front Finder window "
				+ "to {0, 0, " + (xLoc - 20) + ", " + (yLoc - 20) + "}";
		driver.executeScript(scr0);

		driver.navigate().to(OSXConstants.Apps.FINDER);
		try {
			WebElement element = driver.findElement(By.name(picture));
			Actions builder = new Actions(driver);

			Action dragAndDrop = builder.clickAndHold(element)
					.moveToElement(target).release(target).build();

			dragAndDrop.perform();
		} finally {
			driver.navigate()
					.to(CommonUtils
							.getOsxApplicationPathFromConfig(ConversationPage.class));
		}
	}

	public String getCurrentConversationName() {
		return currentConversationName;
	}

	public void setCurrentConversationName(String name) {
		currentConversationName = name;
	}
}
