package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class LikersPage extends IOSPage {

    private static final Function<String, String> xpathStrContactAvatarByName = name ->
            String.format("//XCUIElementTypeCell[./XCUIElementTypeStaticText[@name='%s']]", name);

    private static final By nameCloseButton = MobileBy.AccessibilityId("BackButton");

    public LikersPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isLikerVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrContactAvatarByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void tapCloseButton() throws Exception {
        getElement(nameCloseButton).click();
    }
}
