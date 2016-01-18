package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PendingRequestsPage extends IOSPage {

    private static final String namePendingRequestIgnoreButton = "IGNORE";
    @FindBy(name = namePendingRequestIgnoreButton)
    private WebElement ignoreRequestButton;

    private static final String namePendingRequestConnectButton = "CONNECT";
    @FindBy(name = namePendingRequestConnectButton)
    private WebElement connectRequestButton;

    private static final String xpathPendingRequesterName =
            xpathMainWindow + "/UIATableView[1]//UIAStaticText[contains(@name, 'Connect to')]";
    @FindBy(xpath = xpathPendingRequesterName)
    private WebElement requesterName;

    private static final String xpathPendingRequestMessage =
            xpathMainWindow + "/UIATableView[1]//UIAStaticText[3]";
    @FindBy(xpath = xpathPendingRequestMessage)
    private WebElement pendingMessage;

    private static final String xpathYouBothKnowPeopleIcon =
            xpathMainWindow + "/UIATableView[1]/UIATableCell[1]/UIAButton[2]";
    @FindBy(xpath = xpathYouBothKnowPeopleIcon)
    private WebElement youBothKnowPeopleIcon;

    private static final String nameYouBothKnowHeader = "YOU BOTH KNOW";

    public PendingRequestsPage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void clickIgnoreButton() throws Exception {
        verifyLocatorPresence(By.name(namePendingRequestIgnoreButton), "Ignore button is not visible");
        ignoreRequestButton.click();
    }

    public void clickIgnoreButtonMultiple(int clicks) throws Exception {
        verifyLocatorPresence(By.name(namePendingRequestIgnoreButton), "Ignore button is not visible");
        for (int i = 0; i < clicks; i++) {
            ignoreRequestButton.click();
            Thread.sleep(500);
        }
    }

    public void clickConnectButton() throws Exception {
        verifyLocatorPresence(By.name(namePendingRequestConnectButton), "Connect button is not visible");
        connectRequestButton.click();
    }

    public void clickConnectButtonMultiple(int clicks) throws Exception {
        verifyLocatorPresence(By.name(namePendingRequestConnectButton), "Connect button is not visible");
        for (int i = 0; i < clicks; i++) {
            connectRequestButton.click();
            Thread.sleep(500);
        }
    }

    public boolean isConnectButtonDisplayed() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.name(namePendingRequestConnectButton), 5);
    }

    public String getRequesterName() {
        final String CONNECT_TO = "Connect to ";
        return requesterName.getText().replace(CONNECT_TO, "");
    }

    public boolean isYouBothKnowDisplayed() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(nameYouBothKnowHeader), 5);
    }

    public void clickYouBothKnowPeopleIcon() {
        youBothKnowPeopleIcon.click();
    }

}
