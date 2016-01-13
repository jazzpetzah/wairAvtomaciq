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
