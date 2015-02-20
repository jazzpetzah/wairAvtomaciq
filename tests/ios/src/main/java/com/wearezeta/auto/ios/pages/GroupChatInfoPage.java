package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class GroupChatInfoPage extends IOSPage {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.80;

	private final String AQA_PICTURE_CONTACT = "AQAPICTURECONTACT";
	private final String AQA_AVATAR_CONTACT = "AQAAVATAR";

	private String conversationName = null;

	@FindBy(how = How.NAME, using = IOSLocators.nameLeaveConversation)
	private WebElement leaveChat;

	@FindBy(how = How.NAME, using = IOSLocators.nameLeaveConversationButton)
	private WebElement leaveChatButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameConversationNameTextField)
	private WebElement conversationNameTextField;

	@FindBy(how = How.NAME, using = IOSLocators.nameExitGroupInfoPageButton)
	private WebElement exitGroupInfoPageButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathNumberOfParticipantsText)
	private WebElement numberOfParticipantsText;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathAvatarCollectionView)
	private WebElement avatarCollectionView;

	@FindBy(how = How.NAME, using = IOSLocators.nameAddContactToChatButton)
	private WebElement addContactButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameAddPeopleDialogHeader)
	private WebElement addDialogHeader;

	@FindBy(how = How.NAME, using = IOSLocators.nameAddPeopleCancelButton)
	private WebElement addDialogCancelButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameAddPeopleContinueButton)
	private WebElement addDialogContinueButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameOtherUserProfilePageCloseButton)
	private WebElement closeButton;

	public GroupChatInfoPage(ZetaIOSDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public String getGroupChatName() {
		return conversationNameTextField.getText();
	}

	public void changeConversationNameToRandom() {
		conversationName = CommonUtils.generateGUID().substring(0, 15);
		conversationNameTextField.sendKeys(conversationName + "\n");
	}

	public void changeConversationName(String name) {
		conversationNameTextField.clear();
		conversationNameTextField.sendKeys(name + "\n");
	}

	public boolean isNumberOfParticipants(int correctNumber) throws Exception {
		DriverUtils.waitUntilElementAppears(driver,
				By.xpath(IOSLocators.xpathNumberOfParticipantsText));
		int givenNumberOfParticipants = Integer
				.parseInt(numberOfParticipantsText.getText().replaceAll("\\D+",
						""));
		return givenNumberOfParticipants == correctNumber;
	}

	public GroupChatPage closeGroupChatInfoPage() throws Exception {
		closeButton.click();
		return new GroupChatPage(this.getDriver(), this.getWait());
	}

	public boolean areParticipantAvatarCorrect(String contact)
			throws IOException {

		String name = "", picture = "";
		if (contact.toLowerCase().contains(AQA_PICTURE_CONTACT.toLowerCase())) {
			name = AQA_PICTURE_CONTACT;
			picture = "avatarPictureTest.png";
		} else {
			name = AQA_AVATAR_CONTACT;
			picture = "avatarTest.png";
		}
		List<WebElement> participantAvatars = getCurrentParticipants();
		BufferedImage avatarIcon = null;
		boolean flag = false;
		for (WebElement avatar : participantAvatars) {
			avatarIcon = CommonUtils.getElementScreenshot(avatar,
					this.getDriver());
			String avatarName = avatar.getAttribute("name");
			if (avatarName.equalsIgnoreCase(name)) {
				BufferedImage realImage = ImageUtil.readImageFromFile(IOSPage
						.getImagesPath() + picture);
				double score = ImageUtil.getOverlapScore(realImage, avatarIcon);
				if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
					return false;
				} else {
					flag = true;
				}
			}
		}
		return flag;
	}

	public void tapAndCheckAllParticipants(String user, boolean checkEmail)
			throws Exception {

		List<WebElement> participants = getCurrentParticipants();
		String participantNameTextFieldValue = "";
		String participantName = "";
		String participantEmailTextFieldValue = "";

		user = usrMgr.findUserByNameOrNameAlias(user).getName();
		String email = usrMgr.findUserByNameOrNameAlias(user).getEmail();

		for (WebElement participant : participants) {
			ClientUser participantUser = getParticipantUser(participant);
			participantName = participantUser.getName();
			if (!participantName.equalsIgnoreCase(user)) {
				continue;
			}
			PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) tapOnParticipant(getParticipantName(participant));
			participantNameTextFieldValue = PagesCollection.otherUserPersonalInfoPage
					.getNameFieldValue();
			participantEmailTextFieldValue = PagesCollection.otherUserPersonalInfoPage
					.getEmailFieldValue();
			Assert.assertTrue(
					"Participant Name is incorrect and/or not displayed",
					participantNameTextFieldValue.equalsIgnoreCase(user));
			if (checkEmail) {
				Assert.assertTrue("User's email is not displayed",
						participantEmailTextFieldValue.equalsIgnoreCase(email));
			} else {
				Assert.assertFalse("User's email is displayed",
						participantEmailTextFieldValue.equalsIgnoreCase(email));
			}
		}
		PagesCollection.groupChatInfoPage = (GroupChatInfoPage) PagesCollection.otherUserPersonalInfoPage
				.leavePageToGroupInfoPage();
	}

	public String getParticipantName(WebElement participant) {
		String firstElementName = participant
				.findElements(By.className("UIAStaticText")).get(0)
				.getAttribute("name");
		try {
			return participant.findElements(By.className("UIAStaticText"))
					.get(1).getAttribute("name");
		} catch (IndexOutOfBoundsException e) {
			return firstElementName;
		}
	}

	public ClientUser getParticipantUser(WebElement participant)
			throws NoSuchUserException {
		return usrMgr
				.findUserByNameOrNameAlias(getParticipantName(participant));
	}

	public IOSPage tapOnParticipant(String participantName) throws Exception {
		IOSPage page = null;
		participantName = usrMgr.findUserByNameOrNameAlias(participantName)
				.getName();
		List<WebElement> participants = getCurrentParticipants();
		for (WebElement participant : participants) {
			if (getParticipantName(participant).equalsIgnoreCase(
					participantName)) {
				participant.click();
				page = new OtherUserPersonalInfoPage(this.getDriver(),
						this.getWait());
				return page;
			}
		}
		throw new NoSuchElementException(
				"No participant was found with the name: " + participantName);
	}

	public boolean isCorrectConversationName(String contact1, String contact2)
			throws Exception {
		if (conversationNameTextField.getText().equals(conversationName)) {
			return true;
		} else {
			contact1 = usrMgr.findUserByNameOrNameAlias(contact1).getName();
			contact2 = usrMgr.findUserByNameOrNameAlias(contact2).getName();
			;
			if (contact1.contains(" ")) {
				contact1 = contact1.substring(0, contact1.indexOf(" "));
			}
			if (contact2.contains(" ")) {
				contact2 = contact2.substring(0, contact2.indexOf(" "));
			}
			String currentConversationName = conversationNameTextField
					.getText();
			return currentConversationName.contains(contact1)
					&& currentConversationName.contains(contact2)
					&& currentConversationName.contains(", ");
		}
	}

	public int numberOfPeopleInConversation() {
		int result = -1;
		List<WebElement> elements = driver.findElements(By
				.xpath(IOSLocators.xpathNumberPeopleText));
		for (WebElement element : elements) {
			String value = element.getText();
			if (value.contains(IOSLocators.peopleCountTextSubstring)) {
				result = Integer.parseInt(value.substring(0,
						value.indexOf(IOSLocators.peopleCountTextSubstring)));
			}
		}
		return result;
	}

	public int numberOfParticipantsAvatars() {
		List<WebElement> elements = driver.findElements(By
				.xpath(IOSLocators.xpathParticipantAvatarCell));
		return elements.size();
	}

	public List<WebElement> getCurrentParticipants() {
		return avatarCollectionView.findElements(By
				.className("UIACollectionCell"));
	}

	public void exitGroupInfoPage() {
		exitGroupInfoPageButton.click();
	}

	public void leaveConversation() {
		leaveChat.click();
	}

	public void confirmLeaveConversation() {
		leaveChatButton.click();
	}

	public OtherUserPersonalInfoPage selectContactByName(String name)
			throws Exception {
		DriverUtils.mobileTapByCoordinates(this.getDriver(),
				driver.findElementByName(name.toUpperCase()));

		return new OtherUserPersonalInfoPage(this.getDriver(), this.getWait());
	}

	public ConnectToPage selectNotConnectedUser(String name) throws Exception {
		driver.findElementByName(name.toUpperCase()).click();

		return new ConnectToPage(this.getDriver(), this.getWait());
	}

	public boolean isLeaveConversationAlertVisible() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.name(IOSLocators.nameLeaveConversationAlert));
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		IOSPage page = null;
		switch (direction) {
		case DOWN: {
			page = new GroupChatPage(this.getDriver(), this.getWait());
			break;
		}
		case UP: {
			break;
		}
		case LEFT: {
			page = new GroupChatInfoPage(this.getDriver(), this.getWait());
			break;
		}
		case RIGHT: {
			page = new ContactListPage(this.getDriver(), this.getWait());
			break;
		}
		}
		return page;
	}

	public String getConversationName() {
		return conversationName;
	}

	public void setConversationName(String newName) {
		conversationName = newName;
	}

	public BufferedImage takeScreenShot() throws IOException {
		return DriverUtils.takeScreenshot(this.getDriver());
	}

	public void clickOnAddButton() {
		addContactButton.click();
	}

	public boolean isAddDialogHeaderVisible() {
		boolean flag = DriverUtils.isElementDisplayed(addDialogHeader);
		return flag;
	}

	public PeoplePickerPage clickOnAddDialogContinueButton() throws Throwable {
		PeoplePickerPage page = null;
		addDialogContinueButton.click();
		page = new PeoplePickerPage(this.getDriver(), this.getWait());
		return page;
	}
}