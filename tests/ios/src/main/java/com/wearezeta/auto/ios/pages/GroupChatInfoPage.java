package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Future;

import io.appium.java_client.ios.IOSElement;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

public class GroupChatInfoPage extends IOSPage {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.80;

    private final String AQA_PICTURE_CONTACT = "AQAPICTURECONTACT";
    private final String AQA_AVATAR_CONTACT = "AQAAVATAR";

    private String conversationName = null;

    public static final String nameConversationMenu = "metaControllerRightButton";
    @FindBy(name = nameConversationMenu)
    private WebElement leaveChat;

    public static final String nameLeaveConversationButton = "LEAVE";
    @FindBy(name = nameLeaveConversationButton)
    private WebElement leaveChatButton;

    public static final String nameConversationNameTextField = "ParticipantsView_GroupName";
    @FindBy(name = nameConversationNameTextField)
    private WebElement conversationNameTextField;

    public static final String nameExitGroupInfoPageButton = "metaControllerCancelButton";
    @FindBy(name = nameExitGroupInfoPageButton)
    private WebElement exitGroupInfoPageButton;

    public static final String xpathNumberOfParticipantsText = xpathMainWindow + "/UIAStaticText[3]";
    @FindBy(xpath = xpathNumberOfParticipantsText)
    private WebElement numberOfParticipantsText;

    public static final String xpathAvatarCollectionView = xpathMainWindow + "/UIACollectionView[1]";
    @FindBy(xpath = xpathAvatarCollectionView)
    private WebElement avatarCollectionView;

    public static final String nameAddContactToChatButton = "metaControllerLeftButton";
    @FindBy(name = nameAddContactToChatButton)
    private WebElement addContactButton;

    public static final String nameAddPeopleDialogHeader = "Add people and share history?";
    @FindBy(name = nameAddPeopleDialogHeader)
    private WebElement addDialogHeader;

    public static final String nameAddPeopleCancelButton = "CANCEL";
    @FindBy(name = nameAddPeopleCancelButton)
    private WebElement addDialogCancelButton;

    public static final String nameAddPeopleContinueButton = "CONTINUE";
    @FindBy(name = nameAddPeopleContinueButton)
    private WebElement addDialogContinueButton;

    public static final String nameOtherUserProfilePageCloseButton = "OtherUserProfileCloseButton";
    @FindBy(name = nameOtherUserProfilePageCloseButton)
    private WebElement closeButton;

    public static final String nameLeaveConversationAlert = "Leave conversation?";

    public static final String xpathUserNameLabel =
            "//UIACollectionView[preceding-sibling::UIATextView[@name='ParticipantsView_GroupName']]" +
                    "/UIACollectionCell/UIAStaticText[last() and @name='%s']";

    public static String xpathNumberPeopleText = xpathMainWindow + "/UIAStaticText[contains(@name, 'PEOPLE')]";

    public static final String xpathPeopleViewCollectionCell =
            "//UIAButton[@name='metaControllerCancelButton']/following-sibling::" +
                    "UIACollectionView/UIACollectionCell/UIAStaticText[@name='%s']";

    public static final String peopleCountTextSubstring = " PEOPLE";

    public static final String xpathParticipantAvatarCell = xpathAvatarCollectionView + "/UIACollectionCell";

    public GroupChatInfoPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public String getGroupChatName() {
        return conversationNameTextField.getText();
    }

    public void changeConversationName(String name) {
        conversationNameTextField.clear();
        int maxRetrys = 3;
        int retryCounter = 0;
        while (retryCounter < maxRetrys) {
            try {
                conversationNameTextField.sendKeys(name + "\n");
                retryCounter = maxRetrys;
            } catch (WebDriverException ex) {
                conversationNameTextField.clear();
                retryCounter++;
            }
        }

    }

    public void setGroupChatName(String name) throws Exception {
        ((IOSElement) getDriver().findElementByName(nameConversationNameTextField)).setValue(name);
        clickKeyboardEnterButton();
    }

    public boolean areParticipantAvatarCorrect(String contact) throws Exception {
        String name, picture;
        if (contact.toLowerCase().contains(AQA_PICTURE_CONTACT.toLowerCase())) {
            name = AQA_PICTURE_CONTACT;
            picture = "avatarPictureTest.png";
        } else {
            name = AQA_AVATAR_CONTACT;
            picture = "avatarTest.png";
        }
        List<WebElement> participantAvatars = getCurrentParticipants();
        BufferedImage avatarIcon;
        boolean flag = false;
        for (WebElement avatar : participantAvatars) {
            avatarIcon = CommonUtils.getElementScreenshot(avatar,
                    this.getDriver()).orElseThrow(IllegalStateException::new);
            List<WebElement> avatarText = avatar.findElements(By
                    .className("UIAStaticText"));

            for (WebElement text : avatarText) {
                String avatarName = text.getAttribute("name");
                if (avatarName.equalsIgnoreCase(name)) {
                    BufferedImage realImage = ImageUtil
                            .readImageFromFile(IOSPage.getImagesPath()
                                    + picture);

                    double score = ImageUtil.getOverlapScore(realImage,
                            avatarIcon, ImageUtil.RESIZE_NORESIZE);
                    if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
                        return false;
                    } else {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    public void tapAndCheckAllParticipants(String user, boolean checkEmail) throws Exception {
        List<WebElement> participants = getCurrentParticipants();
        String participantNameTextFieldValue;
        String participantName;
        String participantEmailTextFieldValue;

        user = usrMgr.findUserByNameOrNameAlias(user).getName();
        String email = usrMgr.findUserByNameOrNameAlias(user).getEmail();

        for (WebElement participant : participants) {
            ClientUser participantUser = getParticipantUser(participant);
            participantName = participantUser.getName();
            if (!participantName.equalsIgnoreCase(user)) {
                continue;
            }
            tapOnParticipant(getParticipantName(participant));
            final OtherUserPersonalInfoPage otherUserPersonalInfoPage =
                    new OtherUserPersonalInfoPage(this.getLazyDriver());
            participantNameTextFieldValue = otherUserPersonalInfoPage
                    .getNameFieldValue(user);
            participantEmailTextFieldValue = otherUserPersonalInfoPage
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
            break;
        }
        new OtherUserPersonalInfoPage(this.getLazyDriver())
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

    public void tapOnParticipant(String participantName) throws Exception {
        participantName = usrMgr.findUserByNameOrNameAlias(participantName)
                .getName();
        List<WebElement> participants = getCurrentParticipants();
        for (WebElement participant : participants) {
            if (getParticipantName(participant).equalsIgnoreCase(
                    participantName)) {
                participant.click();
                return;
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

    public int numberOfPeopleInConversation() throws Exception {
        int result = -1;
        List<WebElement> elements = getDriver().findElements(
                By.xpath(xpathNumberPeopleText));
        for (WebElement element : elements) {
            String value = element.getText();
            if (value.contains(peopleCountTextSubstring)) {
                result = Integer.parseInt(value.substring(0,
                        value.indexOf(peopleCountTextSubstring)));
            }
        }
        return result;
    }

    public int numberOfParticipantsAvatars() throws Exception {
        List<WebElement> elements = getDriver().findElements(
                By.xpath(xpathParticipantAvatarCell));
        return elements.size();
    }

    public List<WebElement> getCurrentParticipants() {
        return avatarCollectionView.findElements(By
                .className("UIACollectionCell"));
    }

    public void exitGroupInfoPage() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(),
                exitGroupInfoPageButton);
        exitGroupInfoPageButton.click();
    }

    public void leaveConversation() throws InterruptedException {
        leaveChat.click();
        Thread.sleep(2000);
        leaveChatButton.click();
    }

    public void confirmLeaveConversation() {
        leaveChatButton.click();
    }

    public OtherUserPersonalInfoPage selectContactByName(String name)
            throws Exception {
        DriverUtils.tapByCoordinates(
                this.getDriver(),
                getDriver().findElementByXPath(
                        String.format(xpathPeopleViewCollectionCell, name.toUpperCase())));

        return new OtherUserPersonalInfoPage(this.getLazyDriver());
    }

    public ConnectToPage selectNotConnectedUser(String name) throws Exception {
        getDriver().findElementByName(name.toUpperCase()).click();

        return new ConnectToPage(this.getLazyDriver());
    }

    public boolean isLeaveConversationAlertVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.name(nameLeaveConversationAlert));
    }

    public void clickOnAddButton() {
        addContactButton.click();
    }

    public void clickOnAddDialogContinueButton() throws Throwable {
        addDialogContinueButton.click();
    }

    public boolean waitForContactToDisappear(String contact) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), By
                .xpath(String.format(xpathUserNameLabel, contact)));
    }
}
