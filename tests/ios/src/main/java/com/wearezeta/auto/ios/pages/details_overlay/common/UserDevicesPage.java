package com.wearezeta.auto.ios.pages.details_overlay.common;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.details_overlay.BaseUserDetailsOverlay;
import com.wearezeta.auto.ios.pages.details_overlay.ISupportsTabSwitching;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class UserDevicesPage extends BaseUserDetailsOverlay implements ISupportsTabSwitching {
    private static final Function<String, String> xpathStrDeviceId = id ->
            String.format("//XCUIElementTypeStaticText[contains(@name, '%s')]", id);

    private static final String xpathStrDevicesList = "//XCUIElementTypeButton[@name='DEVICES']/following::" +
            "XCUIElementTypeTable[1]/XCUIElementTypeCell";

    private static final Function<Integer, String> xpathStrDevicesByCount = count ->
            String.format("//XCUIElementTypeTable[count(XCUIElementTypeCell)=%s]", count);

    private static final Function<Integer, String> xpathStrDeviceByIndex =
            idx -> String.format("%s[%s]", xpathStrDevicesList, idx);

    public UserDevicesPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        throw new IllegalStateException("There are no buttons to tap on devices overlay");
    }

    public boolean isParticipantDevicesCountEqualTo(int expectedCount) throws Exception {
        final By locator = By.xpath(xpathStrDevicesByCount.apply(expectedCount));
        return isLocatorDisplayed(locator);
    }

    public void openDeviceDetailsPage(int deviceIndex) throws Exception {
        final By locator = By.xpath(xpathStrDeviceByIndex.apply(deviceIndex));
        getElement(locator).click();
        // Wait for animation
        Thread.sleep(1000);
    }

    @Override
    protected By getLeftActionButtonLocator() {
        throw new IllegalStateException("Left action button is not available for devices overlay");
    }

    @Override
    protected By getRightActionButtonLocator() {
        throw new IllegalStateException("Right action button is not available for devices overlay");
    }

    @Override
    public void switchToTab(String tabName) throws Exception {
        super.switchToTab(tabName);
    }

    private static String convertStringIDtoLocatorTypeID(String id) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < id.length(); i += 2) {
            sb.append(id.substring(i, i + 2)).append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    public boolean isUserDeviceIdVisible(String deviceId) throws Exception {
        String locator = xpathStrDeviceId.apply(convertStringIDtoLocatorTypeID(deviceId));
        return isLocatorExist(By.xpath(locator));
    }
}
