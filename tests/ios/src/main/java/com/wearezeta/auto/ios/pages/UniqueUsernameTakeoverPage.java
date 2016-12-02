package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class UniqueUsernameTakeoverPage extends IOSPage {
    private static final By nameChooseYoursButton = MobileBy.AccessibilityId("CHOOSE YOURS");
    private static final By nameKeepThisOneButton = MobileBy.AccessibilityId("KEEP THIS ONE");

    private static final String nameStrTitleLabel = "Usernames are here.";
    private static final By nameTitleLabel = MobileBy.AccessibilityId(nameStrTitleLabel);

    private static final Function<String, String> xpathStrUniqueUsernameByName = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s']/preceding::XCUIElementTypeStaticText[1][@name='%s']",
                nameStrTitleLabel, name);
    private static final Function<String, String> xpathStrUsernameByName = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s']/preceding::XCUIElementTypeStaticText[2][@name='%s']",
                    nameStrTitleLabel, name);

    public UniqueUsernameTakeoverPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        return isLocatorDisplayed(nameTitleLabel);
    }

    private static By getButtonByName(String name) {
        switch (name.toLowerCase()) {
            case "choose yours":
                return nameChooseYoursButton;
            case "keep this one":
                return nameKeepThisOneButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonByName(name);
        getElement(locator).click();
    }

    public boolean isUniqueUsernameVisible(String expectedUsername) throws Exception {
        final By locator = By.xpath(xpathStrUniqueUsernameByName.apply(expectedUsername));
        return isLocatorDisplayed(locator);
    }

    public boolean isUsernameVisible(String expectedUsername) throws Exception {
        final By locator = By.xpath(xpathStrUsernameByName.apply(expectedUsername));
        return isLocatorDisplayed(locator);
    }
}
