package com.wearezeta.auto.ios.pages;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class GroupInfoPage extends IOSPage {
    private static final By nameRightActionButton = MobileBy.AccessibilityId("metaControllerRightButton");

    private static final By nameConfirmLeaveConversationButton = MobileBy.AccessibilityId("LEAVE");

    private static final String strNameConversationNameTextField = "ParticipantsView_GroupName";
    private static final By fbNameConversationNameTextField =
            FBBy.AccessibilityId(strNameConversationNameTextField);

    private static final By nameConfirmRemoveButton = MobileBy.AccessibilityId("REMOVE");

    private static final By xpathConfirmDeleteButton =
            By.xpath("//XCUIElementTypeButton[@name='CANCEL']/following::XCUIElementTypeButton[@name='DELETE']");

    private static final Function<String, String> xpathStrConversationNameByText = text ->
            String.format("//*[@name='%s' and @value='%s']", strNameConversationNameTextField, text);

    private static final Function<String, String> xpathStrConversationNameByExpr = expr ->
            String.format("//*[@name='%s' and %s]", strNameConversationNameTextField, expr);

    private static final By nameExitGroupInfoPageButton =
            MobileBy.AccessibilityId("metaControllerCancelButton");

    private static final By namLeftActionButton = MobileBy.AccessibilityId("metaControllerLeftButton");

    private static final By nameConversationMenu = MobileBy.AccessibilityId("metaControllerRightButton");

    private static final By nameAlsoLeaveCheckbox = MobileBy.AccessibilityId("ALSO LEAVE THE CONVERSATION");

    private static final Function<Integer, String> nameStrNumberPeopleByCount =
            count -> String.format("%s PEOPLE", count);

    private static final Function<String, String> xpathPeopleViewCollectionCellByName = name ->
            String.format("//XCUIElementTypeButton[@name='metaControllerCancelButton']/following::" +
                            "XCUIElementTypeCell[ ./XCUIElementTypeStaticText[@name='%s'] ]",
                    name.toUpperCase());

    private static final By xpathNameParticipantAvatarCell =
            By.xpath("//XCUIElementTypeCollectionView/XCUIElementTypeCell[ ./XCUIElementTypeStaticText ]");

    public GroupInfoPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isGroupNameEqualTo(String expectedName) throws Exception {
        final By locator = By.xpath(xpathStrConversationNameByText.apply(expectedName));
        return isLocatorDisplayed(locator);
    }

    public void setGroupChatName(String name) throws Exception {
        final FBElement nameInputField = (FBElement) getElement(fbNameConversationNameTextField);
        tapAtTheCenterOfElement(nameInputField);
        this.isKeyboardVisible();
        nameInputField.clear();
        nameInputField.sendKeys(name);
        tapKeyboardCommitButton();
    }

    public boolean isCorrectConversationName(List<String> expectedNames) throws Exception {
        final String xpathExpr = String.join(" and ", expectedNames.stream().
                map(x -> String.format("contains(@value, '%s')", x)).
                collect(Collectors.toList()));
        final By locator = By.xpath(xpathStrConversationNameByExpr.apply(xpathExpr));
        return isLocatorDisplayed(locator);
    }

    public boolean isNumberOfPeopleEquals(int expectedNumber) throws Exception {
        final By locator = MobileBy.AccessibilityId(nameStrNumberPeopleByCount.apply(expectedNumber));
        return isLocatorDisplayed(locator);
    }

    public int getParticipantsAvatarsCount() throws Exception {
        return selectVisibleElements(xpathNameParticipantAvatarCell).size();
    }

    public void tapLeaveConversation() throws Exception {
        getElement(nameRightActionButton).click();
        if (!isLocatorInvisible(nameRightActionButton)) {
            throw new IllegalStateException("Menu button is still shown");
        }
        getElement(nameConfirmLeaveConversationButton).click();
    }

    public void confirmLeaveConversation() throws Exception {
        getElement(nameConfirmLeaveConversationButton).click();
    }

    public void selectParticipant(String name) throws Exception {
        final By locator = FBBy.xpath(xpathPeopleViewCollectionCellByName.apply(name));
        getElement(locator).click();
        // Wait for animation
        Thread.sleep(1000);
    }

    public boolean waitForContactToDisappear(String contact) throws Exception {
        final By locator = By.xpath(xpathPeopleViewCollectionCellByName.apply(contact));
        return isLocatorInvisible(locator);
    }

    public int getGroupNameLength() throws Exception {
        return getElement(fbNameConversationNameTextField).getText().length();
    }

    private static By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "add people":
                return namLeftActionButton;
            case "confirm removal":
                return nameConfirmRemoveButton;
            case "confirm deletion":
                return xpathConfirmDeleteButton;
            case "open menu":
                return nameConversationMenu;
            case "also leave":
                return nameAlsoLeaveCheckbox;
            case "x":
                return nameExitGroupInfoPageButton;
            case "confirm leaving":
                return nameConfirmLeaveConversationButton;
            case "leave":
                return nameRightActionButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        getElement(locator).click();
        // Wait for animation
        Thread.sleep(1500);
    }
}
