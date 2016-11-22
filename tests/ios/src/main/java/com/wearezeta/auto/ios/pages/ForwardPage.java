package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class ForwardPage extends IOSPage {

    private static final String nameStrButtonSend = "send";

    private static final Function<String, String> xpathStrConversationByName = name ->
            String.format("//XCUIElementTypeButton[@name='%s']/preceding-sibling::XCUIElementTypeTable" +
                    "/XCUIElementTypeCell[.//XCUIElementTypeStaticText[@name='%s']]", nameStrButtonSend, name);

    private static final By nameButtonSend = MobileBy.AccessibilityId(nameStrButtonSend);

    private static final By nameButtonClose = MobileBy.AccessibilityId("close");

    public ForwardPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void selectConversation(String name) throws Exception {
        final By locator = By.xpath(xpathStrConversationByName.apply(name));
        getElement(locator).click();
    }

    private static By getButtonByName(String name) {
        switch (name.toLowerCase()) {
            case "send":
                return nameButtonSend;
            case "close":
                return nameButtonClose;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonByName(name);
        getElement(locator).click();
    }
}
