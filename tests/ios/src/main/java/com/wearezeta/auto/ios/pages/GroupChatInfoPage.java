package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class GroupChatInfoPage extends IOSPage {
	private String url;
	private String path;
	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.95;

	private String conversationName = null;

	@FindBy(how = How.NAME, using = IOSLocators.nameLeaveConversation)
	private WebElement leaveChat;

	@FindBy(how = How.NAME, using = IOSLocators.nameLeaveConversationButton)
	private WebElement leaveChatButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathConversationNameTextField)
	private WebElement conversationNameTextField;

	@FindBy(how = How.NAME, using = IOSLocators.nameExitGroupInfoPageButton)
	private WebElement exitGroupInfoPageButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathNumberOfParticipantsText)
	private WebElement numberOfParticipantsText;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathAvatarCollectionView)
	private WebElement avatarCollectionView;

	public GroupChatInfoPage(String URL, String path)
			throws MalformedURLException {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	public void changeConversationNameToRandom() {
		conversationName = CommonUtils.generateGUID().substring(0, 15);
		conversationNameTextField.sendKeys(conversationName + "\n");
	}
	
	public void changeConversationName(String name){
		conversationNameTextField.sendKeys(name + "\n");
	}

	public boolean isNumberOfParticipants(int correctNumber) {
		int givenNumberOfParticipants = Integer
				.parseInt(numberOfParticipantsText.getText().replaceAll("\\D+",
						""));
		return givenNumberOfParticipants == correctNumber;
	}

	public boolean areParticipantAvatarsCorrect() throws IOException {
		List<WebElement> participantAvatars = getCurrentParticipants();
		BufferedImage avatarIcon = null;
		for (WebElement avatar : participantAvatars) {
			avatarIcon = CommonUtils.getElementScreenshot(avatar, driver);
			String avatarName = avatar.getAttribute("name");
			if (avatarName.equalsIgnoreCase("AQAPICTURECONTACT")) {
				BufferedImage realImage = ImageUtil.readImageFromFile(IOSPage
						.getImagesPath() + "avatarPictureTest.png");
				double score = ImageUtil.getOverlapScore(realImage, avatarIcon);
				if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
					return false;
				}
			}
			if (avatarName.equalsIgnoreCase("AT")) {
				// must be a yellow user with initials AT
				BufferedImage realImage = ImageUtil.readImageFromFile(IOSPage
						.getImagesPath() + "avatarTest.png");
				double score = ImageUtil.getOverlapScore(realImage, avatarIcon);
				if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
					return false;
				}
			}
		}
		return true;
	}

	public void tapAndCheckAllParticipants() throws IOException {
		List<WebElement> participants = getCurrentParticipants();
		String participantNameTextFieldValue = null;
		String participantName = null;
		String participantEmailTextFieldValue = null;
		String unconnectedUsername = CommonUtils
				.retrieveRealUserContactPasswordValue(CommonUtils.YOUR_UNCONNECTED_USER);
		for (WebElement participant : participants) {
			ClientUser participantUser = getParticipantUser(participant);
			participantName = participantUser.getName();
			PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) tapOnParticipant(getParticipantName(participant));
			participantNameTextFieldValue = PagesCollection.otherUserPersonalInfoPage
					.getNameFieldValue();
			participantEmailTextFieldValue = PagesCollection.otherUserPersonalInfoPage
					.getEmailFieldValue();
			Assert.assertTrue(
					"Participant Name is incorrect and/or not displayed",
					participantNameTextFieldValue
							.equalsIgnoreCase(participantName));
			if (participantName.equalsIgnoreCase(unconnectedUsername)) {
				Assert.assertTrue("Unconnected user's email is displayed",
						participantEmailTextFieldValue.equals(""));
			} else {
				Assert.assertTrue(
						"Participant Email is incorrect and/or not displayed",
						participantEmailTextFieldValue
								.equalsIgnoreCase(participantUser.getEmail()));
			}
			PagesCollection.groupChatInfoPage = (GroupChatInfoPage) PagesCollection.otherUserPersonalInfoPage
					.leavePageToGroupInfoPage();
		}
	}

	public String getParticipantName(WebElement participant) {
		String firstElementName = participant.findElements(By.className("UIAStaticText")).get(0).getAttribute("name"); 
		try{
			return participant.findElements(By.className("UIAStaticText")).get(1).getAttribute("name");
		}catch(IndexOutOfBoundsException e){
			return firstElementName;
		}
	}

	public ClientUser getParticipantUser(WebElement participant) {
		return CommonUtils.findUserNamed(getParticipantName(participant));
	}

	public IOSPage tapOnParticipant(String participantName) throws IOException {
		IOSPage page = null;
		participantName = CommonUtils
				.retrieveRealUserContactPasswordValue(participantName);
		List<WebElement> participants = getCurrentParticipants();
		for (WebElement participant : participants) {
			if (getParticipantName(participant).equalsIgnoreCase(
					participantName)) {
				participant.click();
				page = new OtherUserPersonalInfoPage(url, path);
				return page;
			}
		}
		throw new NoSuchElementException("No participant was found with the name: "+participantName);
	}

	public boolean isCorrectConversationName(String contact1, String contact2) {
		if (conversationNameTextField.getText().equals(conversationName)) {
			return true;
		} else {
			contact1 = CommonUtils
					.retrieveRealUserContactPasswordValue(contact1);
			contact2 = CommonUtils
					.retrieveRealUserContactPasswordValue(contact2);
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

	public OtherUserPersonalInfoPage selectContactByName(String name) throws IOException {
		driver.findElementByName(name.toUpperCase()).click();

		return new OtherUserPersonalInfoPage(url, path);
	}
	
	public ConnectToPage selectNotConnectedUser(String name) throws IOException {
		driver.findElementByName(name.toUpperCase()).click();
		
		return new ConnectToPage(url, path);
	}

	public boolean isLeaveConversationAlertVisible() {

		return DriverUtils.waitUntilElementAppears(driver,
				By.name(IOSLocators.nameLeaveConversationAlert));
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		IOSPage page = null;
		switch (direction) {
		case DOWN: {
			page = new GroupChatPage(url, path);
			break;
		}
		case UP: {
			break;
		}
		case LEFT: {
			page = new GroupChatInfoPage(url, path);
			break;
		}
		case RIGHT: {
			page = new ContactListPage(url, path);
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
		return DriverUtils.takeScreenshot(driver);
	}
}
