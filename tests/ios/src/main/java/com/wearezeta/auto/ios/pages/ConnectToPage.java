package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class ConnectToPage extends IOSPage {

    @FindBy(how = How.XPATH, using = IOSLocators.xpathConnectCloseButton)
    private WebElement closeConnectDialoButon;

    @FindBy(how = How.NAME, using = IOSLocators.nameSendConnectButton)
    private WebElement sendConnectButton;

    @FindBy(how = How.XPATH, using = IOSLocators.xpathConnectOtherUserButton)
    private WebElement connectOtherUserButton;

    @FindBy(how = How.NAME, using = IOSLocators.nameIgnoreOtherUserButton)
    private WebElement ignoreOtherUserButton;

    @FindBy(how = How.NAME, using = IOSLocators.nameSendConnectionInputField)
    private WebElement sendConnectionInput;

    private static final Logger log = ZetaLogger.getLog(ConnectToPage.class
            .getSimpleName());

    private String inviteMessage = CommonSteps.CONNECTION_MESSAGE;

    public ConnectToPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public Boolean isConnectToUserDialogVisible() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(),
                connectOtherUserButton, 5);
        return connectOtherUserButton.isDisplayed();
    }

    public void fillHelloTextInConnectDialog() {
        sendConnectionInput.sendKeys(inviteMessage);
    }

    public void deleteTextInConnectDialog() {
        sendConnectionInput.clear();
        // additional steps required because clear() does not disable the
        // connect button
        sendConnectionInput.sendKeys("a");
        clickKeyboardDeleteButton();
    }

    public ContactListPage sendInvitation(String name) throws Exception {
        ContactListPage page = null;
        fillHelloTextInConnectDialog();
        sendConnectButton.click();
        page = new ContactListPage(this.getLazyDriver());
        return page;
    }

    @Override
    public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean waitForConnectDialog() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.className(IOSLocators.clasNameConnectDialogLabel));
    }

    public boolean isConnectButtonVisible() throws Exception {
        return DriverUtils.waitUntilElementClickable(getDriver(),
                connectOtherUserButton, 10);
    }

    public boolean isConnectButtonVisibleAndDisabled() throws Exception {
        return (DriverUtils.isElementPresentAndDisplayed(getDriver(),
                connectOtherUserButton))
                && !(DriverUtils.waitUntilElementClickable(getDriver(),
                connectOtherUserButton, 5));
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

    public void acceptInvitation() {
        connectOtherUserButton.click();
    }

}
