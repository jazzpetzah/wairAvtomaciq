package com.wearezeta.auto.ios.pages;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class IncomingCallPage extends CallPage {
    private static final String nameAcceptCallButton = "AcceptButton";
    @FindBy(name = nameAcceptCallButton)
    private WebElement acceptCallButton;

    private static final String nameEndCallButton = "LeaveCallButton";
    @FindBy(name = nameEndCallButton)
    private WebElement endCallButton;

    private static final Function<String, String> xpathCallingMessageByName = name ->
            String.format("//*[contains(@value, '%s') and contains(@value, ' IS CALLING')]",
                    name.toUpperCase());

    private static final String xpathCallingMessage = "//*[contains(@value, ' IS CALLING')]";

    private static final String nameIgnoreCallButton = "IgnoreButton";
    @FindBy(name = nameIgnoreCallButton)
    private WebElement ignoreCallButton;

    private static final String nameCallingMessageUser = "CallStatusLabel";
    @FindBy(name = nameCallingMessageUser)
    private WebElement callingMessageUser;

    private static final String nameJoinCallButton = "JOIN CALL";
    @FindBy(name = nameJoinCallButton)
    private WebElement joinCallButton;

    private static final String nameSecondCallAlert = "Answer call?";
    @FindBy(name = nameSecondCallAlert)
    private WebElement secondCallAlert;

    private static final String nameAnswerCallAlertButton = "Answer";
    @FindBy(name = nameAnswerCallAlertButton)
    private WebElement answerCallAlertButton;

    private static final String xpathGroupCallAvatars =
            "//UIAWindow[@name='ZClientNotificationWindow']//UIACollectionCell";
    @FindBy(xpath = xpathGroupCallAvatars)
    private List<WebElement> numberOfGroupCallAvatars;

    private static final String xpathGroupCallFullMessage = xpathMainWindow + "/UIAAlert[@name='The call is full']";
    @FindBy(xpath = xpathGroupCallFullMessage)
    private WebElement groupCallFullMessage;

    private static final String xpathUserInCallContactListCell = xpathMainWindow + "/UIAStaticText";
    @FindBy(xpath = xpathUserInCallContactListCell)
    private List<WebElement> contactListNamesInACall;

    public IncomingCallPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isCallingMessageVisible(String contact) throws Exception {
        final By locator = By.xpath(xpathCallingMessageByName.apply(contact));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void acceptIncomingCallClick() throws Exception {
        verifyLocatorPresence(By.name(nameAcceptCallButton), "Accept call button is not visible").click();
    }

    public void ignoreIncomingCallClick() {
        ignoreCallButton.click();
    }

    public boolean isCallingMessageInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.xpath(xpathCallingMessage));
    }

    public boolean isGroupCallingMessageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.name(nameCallingMessageUser), 15);
    }

    public boolean isJoinCallBarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameJoinCallButton));
    }

    public boolean isSecondCallAlertVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameSecondCallAlert));
    }

    public void pressAnswerCallAlertButton() {
        answerCallAlertButton.click();

    }

    public int getNumberOfGroupCallAvatar() throws Exception {
        return numberOfGroupCallAvatars.size();
    }

    public void clickJoinCallButton() {
        joinCallButton.click();
    }

    public boolean isGroupCallFullMessageShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(xpathGroupCallFullMessage));
    }

}



