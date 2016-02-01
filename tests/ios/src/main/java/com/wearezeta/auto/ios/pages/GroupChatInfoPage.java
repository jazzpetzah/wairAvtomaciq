package com.wearezeta.auto.ios.pages;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class GroupChatInfoPage extends IOSPage {
    private static final By nameRightActionButton = By.name("metaControllerRightButton");

    private static final By nameLeaveConversationButton = By.name("LEAVE");

    private static final By nameConversationNameTextField = By.name("ParticipantsView_GroupName");

    private static final Function<String, String> xpathStrConversationNameByExpr = expr ->
            String.format("//*[@name='ParticipantsView_GroupName' and %s]", expr);

    private static final By nameExitParticipantInfoPageButton = By.name("OtherUserProfileCloseButton");

    private static final By nameExitGroupInfoPageButton = By.name("metaControllerCancelButton");


    private static final By namLeftActionButton = By.name("metaControllerLeftButton");

    private static final By nameAddPeopleContinueButton = By.name("CONTINUE");

    private static final By nameLeaveConversationAlert = By.name("Leave conversation?");

    private static final Function<String,String> xpathStrUserNameLabelByText = text ->
            String.format("//UIACollectionView[preceding-sibling::UIATextView[@name='ParticipantsView_GroupName']]" +
                    "/UIACollectionCell/UIAStaticText[last() and @name='%s']", text);

    private static final Function<Integer, String> nameStrNumberPeopleByCount =
            count -> String.format("%s PEOPLE", count);

    private static final Function<String,String> xpathPeopleViewCollectionCellByName = name ->
            String.format("//UIAButton[@name='metaControllerCancelButton']/following-sibling::" +
                    "UIACollectionView/UIACollectionCell/UIAStaticText[@name='%s']", name.toUpperCase());

    private static final By classNameParticipantAvatarCell = By.className("UIACollectionCell");

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

    public boolean isCorrectConversationName(List<String> expectedNames) throws Exception {
        final String xpathExpr =  String.join(" and ", expectedNames.stream().
                map(x -> String.format("contains(@value, '%s')", x)).
                collect(Collectors.toList()));
        final By locator = By.xpath(xpathStrConversationNameByExpr.apply(xpathExpr));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isNumberOfPeopleEquals(int expectedNumber) throws Exception {
        final By locator = By.name(nameStrNumberPeopleByCount.apply(expectedNumber));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public int getParticipantsAvatarsCount() throws Exception {
        return selectVisibleElements(classNameParticipantAvatarCell).size();
    }

    public void exitParticipantInfoPage() throws Exception {
        getElement(nameExitParticipantInfoPageButton).click();
    }

    public void exitGroupInfoPage() throws Exception {
        getElement(nameExitGroupInfoPageButton).click();
    }

    public void leaveConversation() throws Exception {
        getElement(nameRightActionButton).click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameRightActionButton)){
            throw new IllegalStateException("Menu button is still shown");
        }
        getElement(nameLeaveConversationButton).click();
    }

    public void confirmLeaveConversation() throws Exception {
        getElement(nameLeaveConversationButton).click();
    }

    public void selectParticipant(String name)
            throws Exception {
        final By locator = By.xpath(xpathPeopleViewCollectionCellByName.apply(name));
        DriverUtils.tapByCoordinates(this.getDriver(), getElement(locator));
    }

    public boolean isLeaveConversationAlertVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameLeaveConversationAlert);
    }

    public void clickOnAddButton() throws Exception {
        getElement(namLeftActionButton).click();
    }

    public void clickOnAddDialogContinueButton() throws Throwable {
        getElement(nameAddPeopleContinueButton).click();
    }

    public boolean waitForContactToDisappear(String contact) throws Exception {
        final By locator = By.xpath(xpathStrUserNameLabelByText.apply(contact));
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }
}
