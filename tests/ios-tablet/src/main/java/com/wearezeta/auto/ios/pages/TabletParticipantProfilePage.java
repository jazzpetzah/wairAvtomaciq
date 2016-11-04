package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletParticipantProfilePage extends ParticipantProfilePage {
    private static final Function<String, String> xpathStrOtherUserEmailField = email ->
            String.format("//XCUIElementTypeTextView[@value='%s']", email.toUpperCase());

    private static final By xpathOtherUserConnectButton =
            By.xpath("(//XCUIElementTypeButton[@label='CONNECT'])[last()]");

    // idx starts from 1
    private static final Function<Integer, String> xpathStrDeviceByIndex = idx ->
            String.format("//XCUIElementTypeButton[@name='DEVICES']/following::" +
                    "XCUIElementTypeTable[1]/XCUIElementTypeCell[%d]", idx);

    public TabletParticipantProfilePage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isConnectButtonVisible() throws Exception {
        return isLocatorDisplayed(xpathOtherUserConnectButton);
    }

    public void tapConnectButton() throws Exception {
        getElement(xpathOtherUserConnectButton).click();
    }

    public boolean isNameVisible(String user) throws Exception {
        final By locator = MobileBy.AccessibilityId(user);
        return selectVisibleElements(locator).size() > 0;
    }

    public boolean isEmailVisible(String email) throws Exception {
        final By locator = By.xpath(xpathStrOtherUserEmailField.apply(email));
        return isLocatorDisplayed(locator);
    }

    @Override
    public void openDeviceDetailsPage(int deviceIndex) throws Exception {
        final By locator = By.xpath(xpathStrDeviceByIndex.apply(deviceIndex));
        getElement(locator).click();
    }
}
