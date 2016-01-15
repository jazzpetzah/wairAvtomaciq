package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ConnectToPage extends IOSPage {

    private static final String xpathConnectCloseButton = "//UIAApplication[1]/UIAWindow[1]/UIAButton[1]";
    @FindBy(xpath = xpathConnectCloseButton)
    private WebElement closeConnectDialoButon;

    private static final String nameSendConnectButton = "SEND";
    @FindBy(name = nameSendConnectButton)
    private WebElement sendConnectButton;

    private static final String xpathConnectOtherUserButton = "//UIAButton[@name='CONNECT' or @name='OtherUserMetaControllerLeftButton']";
    @FindBy(xpath = xpathConnectOtherUserButton)
    private WebElement connectOtherUserButton;

    private static final String nameIgnoreOtherUserButton = "IGNORE";
    @FindBy(name = nameIgnoreOtherUserButton)
    private WebElement ignoreOtherUserButton;

    private static final String nameSendConnectionInputField = "SendConnectionRequestMessageView";
    @FindBy(name = nameSendConnectionInputField)
    private WebElement sendConnectionInput;

    public ConnectToPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public Boolean isConnectToUserDialogVisible() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(),
                connectOtherUserButton, 5);
        return connectOtherUserButton.isDisplayed();
    }

    public boolean isConnectButtonVisible() throws Exception {
        return DriverUtils.waitUntilElementClickable(getDriver(),
                connectOtherUserButton, 10);
    }

    public PeoplePickerPage sendInvitation() throws Exception {
        if (DriverUtils.isElementPresentAndDisplayed(getDriver(),
                connectOtherUserButton)) {
            connectOtherUserButton.click();
        } else if (isKeyboardVisible()) {
            clickKeyboardEnterButton();
            connectOtherUserButton.click();
        }
        return new PeoplePickerPage(this.getLazyDriver());
    }
}
