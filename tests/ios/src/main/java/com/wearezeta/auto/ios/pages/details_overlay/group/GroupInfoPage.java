package com.wearezeta.auto.ios.pages.details_overlay.group;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.ios.pages.details_overlay.ICanContainVerificationShield;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class GroupInfoPage extends GroupDetailsOverlay implements ICanContainVerificationShield {
    private static final By nameExitGroupInfoPageButton =
            MobileBy.AccessibilityId("metaControllerCancelButton");

    private static final String strNameConversationNameTextField = "ParticipantsView_GroupName";
    private static final By fbNameConversationNameTextField =
            FBBy.AccessibilityId(strNameConversationNameTextField);

    private static final Function<String, String> xpathStrConversationNameByText = text ->
            String.format("//*[@name='%s' and @value='%s']", strNameConversationNameTextField, text);

    private static final Function<String, String> xpathStrConversationNameByExpr = expr ->
            String.format("//*[@name='%s' and %s]", strNameConversationNameTextField, expr);

    private static final By nameAlsoLeaveCheckbox =
            MobileBy.AccessibilityId("ALSO LEAVE THE CONVERSATION");

    private static final Function<Integer, String> nameStrNumberPeopleByCount =
            count -> String.format("%s PEOPLE", count);

    private static final Function<String, String> xpathPeopleViewCollectionCellByName = name ->
            String.format("//XCUIElementTypeTextView[@name='%s']/following::" +
                            "XCUIElementTypeCell[ ./XCUIElementTypeStaticText[@name='%s'] ]",
                    strNameConversationNameTextField, name.toUpperCase());

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

    public void selectParticipant(String name) throws Exception {
        final By locator = FBBy.xpath(xpathPeopleViewCollectionCellByName.apply(name));
        getElement(locator).click();
        // Wait for animation
        Thread.sleep(1000);
    }

    public boolean isParticipantVisible(String name) throws Exception {
        final By locator = By.xpath(xpathPeopleViewCollectionCellByName.apply(name));
        return isLocatorDisplayed(locator);
    }

    public boolean isParticipantInvisible(String name) throws Exception {
        final By locator = By.xpath(xpathPeopleViewCollectionCellByName.apply(name));
        return isLocatorInvisible(locator);
    }

    public int getGroupNameLength() throws Exception {
        return getElement(fbNameConversationNameTextField).getText().length();
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "add people":
                return getLeftActionButtonLocator();
            case "also leave":
                return nameAlsoLeaveCheckbox;
            case "x":
                return nameExitGroupInfoPageButton;
            case "open menu":
                return getRightActionButtonLocator();
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    @Override
    public void tapButton(String name) throws Exception {
        super.tapButton(name);
        // Wait for animation
        Thread.sleep(1500);
    }

    @Override
    public boolean isShieldIconVisible() throws Exception {
        return super.isShieldIconVisible();
    }

    @Override
    public boolean isShieldIconNotVisible() throws Exception {
        return super.isShieldIconNotVisible();
    }
}
