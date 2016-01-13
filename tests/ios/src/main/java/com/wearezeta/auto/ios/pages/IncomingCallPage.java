package com.wearezeta.auto.ios.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class IncomingCallPage extends CallPage {
	public static final String nameAcceptCallButton = "AcceptButton";
	@FindBy(name = nameAcceptCallButton)
	private WebElement acceptCallButton;

    public static final String nameEndCallButton = "LeaveCallButton";
    @FindBy(name = nameEndCallButton)
	private WebElement endCallButton;

    public static final String xpathCallingMessage =
            "//UIAStaticText[contains(@value, '%s') and contains(@value, ' IS CALLING')]";
    @FindBy(xpath = xpathCallingMessage)
	private WebElement callingMessage;

    public static final String nameIgnoreCallButton = "IgnoreButton";
    @FindBy(name = nameIgnoreCallButton)
	private WebElement ignoreCallButton;

    public static final String nameCallingMessageUser = "CallStatusLabel";
    @FindBy(name = nameCallingMessageUser)
	private WebElement callingMessageUser;

    public static final String nameJoinCallButton = "JOIN CALL";
    @FindBy(name = nameJoinCallButton)
	private WebElement joinCallButton;

    public static final String nameSecondCallAlert = "Answer call?";
    @FindBy(name = nameSecondCallAlert)
	private WebElement secondCallAlert;

    public static final String nameAnswerCallAlertButton = "Answer";
    @FindBy(name = nameAnswerCallAlertButton)
	private WebElement answerCallAlertButton;

    public static final String xpathGroupCallAvatars =
            "//UIAWindow[@name='ZClientNotificationWindow']//UIACollectionCell";
    @FindBy(xpath = xpathGroupCallAvatars)
	private List<WebElement> numberOfGroupCallAvatars;

    public static final String xpathGroupCallFullMessage =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAAlert[@name='The call is full']";
    @FindBy(xpath = xpathGroupCallFullMessage)
	private WebElement groupCallFullMessage;

    public static final String xpathUserInCallContactListCell =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAStaticText";
    @FindBy(xpath = xpathUserInCallContactListCell)
	private List<WebElement> contactListNamesInACall;

	public IncomingCallPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isCallingMessageVisible(String contact) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(String.format(xpathCallingMessage, contact)));
	}

	public StartedCallPage acceptIncomingCallClick() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), acceptCallButton);
		acceptCallButton.click();
		return new StartedCallPage(getLazyDriver());
	}

	public void ignoreIncomingCallClick() {
		ignoreCallButton.click();
	}

	public boolean isCallingMessageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathCallingMessage), 15);
	}

	public boolean isGroupCallingMessageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(nameCallingMessageUser), 15);
	}

	public boolean isJoinCallBarVisible() {
		return joinCallButton.isDisplayed();
	}

	public boolean isSecondCallAlertVisible() {
		return secondCallAlert.isDisplayed();
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

	public boolean isGroupCallFullMessageShown() {
		return groupCallFullMessage.isDisplayed();
	}

}



