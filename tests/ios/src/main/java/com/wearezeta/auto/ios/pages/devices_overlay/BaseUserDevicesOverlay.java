package com.wearezeta.auto.ios.pages.devices_overlay;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.details_overlay.BaseDetailsOverlay;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public abstract class BaseUserDevicesOverlay extends BaseDetailsOverlay {
    private static final Function<String, String> xpathStrDeviceId = id ->
            String.format("//XCUIElementTypeStaticText[contains(@name, '%s')]", id);

    private final Function<Integer, String> xpathStrDevicesByCount = count ->
            String.format("//XCUIElementTypeTable[count(XCUIElementTypeCell)=%s]", count);

    private final Function<Integer, String> xpathStrDeviceByIndex =
            idx -> String.format("%s/XCUIElementTypeCell[%s]", getDevicesListRootXPath(), idx);

    public BaseUserDevicesOverlay(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        throw new IllegalStateException("There are no buttons to tap on devices overlay");
    }

    protected abstract String getDevicesListRootXPath();

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
