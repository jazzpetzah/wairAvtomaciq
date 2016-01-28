package com.wearezeta.auto.ios.pages;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;


public class GroupChatInfoPage extends IOSPage {
    private String conversationName = null;

    private static final By nameConversationMenu = By.name("metaControllerRightButton");

    private static final By nameLeaveConversationButton = By.name("LEAVE");

    private static final By nameConversationNameTextField = By.name("ParticipantsView_GroupName");

    private static final By nameExitGroupInfoPageButton = By.name("metaControllerCancelButton");

    private static final By xpathAvatarCollectionView = By.xpath(xpathStrMainWindow + "/UIACollectionView[1]");

    private static final By nameAddContactToChatButton = By.name("metaControllerLeftButton");

    private static final By nameAddPeopleContinueButton = By.name("CONTINUE");

    private static final By nameLeaveConversationAlert = By.name("Leave conversation?");

    private static final Function<String,String> xpathStrUserNameLabelByText = text ->
            String.format("//UIACollectionView[preceding-sibling::UIATextView[@name='ParticipantsView_GroupName']]" +
                    "/UIACollectionCell/UIAStaticText[last() and @name='%s']", text);

    private static final By xpathNumberPeopleText =
            By.xpath(xpathStrMainWindow + "/UIAStaticText[contains(@name, 'PEOPLE')]");

    private static final Function<String,String> xpathPeopleViewCollectionCellByName = name ->
            String.format("//UIAButton[@name='metaControllerCancelButton']/following-sibling::" +
                    "UIACollectionView/UIACollectionCell/UIAStaticText[@name='%s']", name.toUpperCase());

    private static final By xpathParticipantAvatarCell = By.xpath(xpathAvatarCollectionView + "/UIACollectionCell");

    private static final String PEOPLE_COUNT_TEXT_SUBSTRING = " PEOPLE";

    private static final Function<String, String> xpathStrParticipantElementByName = name ->
            String.format("//UIAStaticText[@name='%s']", name);

    public GroupChatInfoPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public String getGroupChatName() throws Exception {
        return getElement(nameConversationNameTextField).getText();
    }

    public void setGroupChatName(String name) throws Exception {
        ((IOSElement) getElement(nameConversationNameTextField)).setValue(name);
        clickKeyboardReturnButton();
    }

    public Optional<WebElement> getParticipantElement(String name) throws Exception {
        final By locator = By.xpath(xpathStrParticipantElementByName.apply(name));
        return getElementIfDisplayed(locator);
    }

    public boolean isCorrectConversationName(String contact1, String contact2) throws Exception {
        final WebElement conversationNameTextField = getElement(nameConversationNameTextField);
        if (conversationNameTextField.getText().equals(conversationName)) {
            return true;
        } else {
            if (contact1.contains(" ")) {
                contact1 = contact1.substring(0, contact1.indexOf(" "));
            }
            if (contact2.contains(" ")) {
                contact2 = contact2.substring(0, contact2.indexOf(" "));
            }
            String currentConversationName = conversationNameTextField.getText();
            return currentConversationName.contains(contact1)
                    && currentConversationName.contains(contact2)
                    && currentConversationName.contains(", ");
        }
    }

    public int numberOfPeopleInConversation() throws Exception {
        // FIXME: Optimize locator
        int result = -1;
        List<WebElement> elements = getElements(xpathNumberPeopleText);
        for (WebElement element : elements) {
            String value = element.getText();
            if (value.contains(PEOPLE_COUNT_TEXT_SUBSTRING)) {
                return Integer.parseInt(value.substring(0, value.indexOf(PEOPLE_COUNT_TEXT_SUBSTRING)));
            }
        }
        return result;
    }

    public int numberOfParticipantsAvatars() throws Exception {
        return getDriver().findElements(xpathParticipantAvatarCell).size();
    }

    public void exitGroupInfoPage() throws Exception {
        getElement(nameExitGroupInfoPageButton, "Close group info button is not visible").click();
    }

    public void leaveConversation() throws Exception {
        getElement(nameConversationMenu).click();
        getElement(nameLeaveConversationButton).click();
    }

    public void confirmLeaveConversation() throws Exception {
        getElement(nameLeaveConversationButton).click();
    }

    public void selectContactByName(String name)
            throws Exception {
        final By locator = By.xpath(xpathPeopleViewCollectionCellByName.apply(name));
        DriverUtils.tapByCoordinates(this.getDriver(), getElement(locator));
    }

    public void selectNotConnectedUser(String name) throws Exception {
        getDriver().findElementByName(name.toUpperCase()).click();
    }

    public boolean isLeaveConversationAlertVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameLeaveConversationAlert);
    }

    public void clickOnAddButton() throws Exception {
        getElement(nameAddContactToChatButton).click();
    }

    public void clickOnAddDialogContinueButton() throws Throwable {
        getElement(nameAddPeopleContinueButton).click();
    }

    public boolean waitForContactToDisappear(String contact) throws Exception {
        final By locator = By.xpath(xpathStrUserNameLabelByText.apply(contact));
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }
}
