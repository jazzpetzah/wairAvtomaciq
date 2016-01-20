package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PendingRequestsPage extends IOSPage {

    private static final By namePendingRequestIgnoreButton = By.name("IGNORE");

    private static final By namePendingRequestConnectButton = By.name("CONNECT");

    private static final By xpathPendingRequesterName = By.xpath(
            xpathStrMainWindow + "/UIATableView[1]//UIAStaticText[contains(@name, 'Connect to')]");

    private static final By xpathYouBothKnowPeopleIcon = By.xpath(
            xpathStrMainWindow + "/UIATableView[1]/UIATableCell[1]/UIAButton[2]");

    private static final By nameYouBothKnowHeader = By.name("YOU BOTH KNOW");

    public PendingRequestsPage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void clickIgnoreButton() throws Exception {
        getElement(namePendingRequestIgnoreButton, "Ignore button is not visible").click();
    }

    public void clickIgnoreButtonMultiple(int clicks) throws Exception {
        final WebElement ignoreRequestButton = getElement(namePendingRequestIgnoreButton,
                "Ignore button is not visible");
        for (int i = 0; i < clicks; i++) {
            ignoreRequestButton.click();
            Thread.sleep(500);
        }
    }

    public void clickConnectButton() throws Exception {
        getElement(namePendingRequestConnectButton, "Connect button is not visible").click();
    }

    public void clickConnectButtonMultiple(int clicks) throws Exception {
        final WebElement connectRequestButton = getElement(namePendingRequestConnectButton,
                "Connect button is not visible");
        for (int i = 0; i < clicks; i++) {
            connectRequestButton.click();
            Thread.sleep(500);
        }
    }

    public boolean isConnectButtonDisplayed() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), namePendingRequestConnectButton, 5);
    }

    public String getRequesterName() throws Exception {
        final String CONNECT_TO = "Connect to ";
        return getElement(xpathPendingRequesterName).getText().replace(CONNECT_TO, "");
    }

    public boolean isYouBothKnowDisplayed() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameYouBothKnowHeader, 5);
    }

    public void clickYouBothKnowPeopleIcon() throws Exception {
        getElement(xpathYouBothKnowPeopleIcon).click();
    }

}
