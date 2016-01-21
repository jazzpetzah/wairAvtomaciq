package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PendingRequestsPage extends IOSPage {

    private static final By namePendingRequestIgnoreButton = By.name("IGNORE");

    private static final By namePendingRequestConnectButton = By.name("CONNECT");

    private static final Function<String, String> xpathStrPendingRequesterByName = name ->
            String.format("//UIAStaticText[contains(@name, 'Connect to') and contains(@name, '%s)]", name);

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

    public boolean isConnectToNameExist(String expectedName) throws Exception {
        final By locator = By.xpath(xpathStrPendingRequesterByName.apply(expectedName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isYouBothKnowDisplayed() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameYouBothKnowHeader, 5);
    }

    public void clickYouBothKnowPeopleIcon() throws Exception {
        getElement(xpathYouBothKnowPeopleIcon).click();
    }

}
