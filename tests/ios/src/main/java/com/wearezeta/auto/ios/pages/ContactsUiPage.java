package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ContactsUiPage extends IOSPage {

    private static final By xpathSearchInput =
            By.xpath("//XCUIElementTypeTextView[ .//XCUIElementTypeStaticText[@name='SEARCH BY NAME'] ]");

    private static final By nameInviteOthersButton = MobileBy.AccessibilityId("INVITE OTHERS");

    private static final Function<String, String> xpathStrConvoCellByName = name ->
            String.format("//XCUIElementTypeCell[ ./XCUIElementTypeStaticText[@name='%s'] ]", name);

    private static final Function<String, String> xpathStrOpenButtonByConvoName = name ->
            String.format("%s/XCUIElementTypeButton[@name='OPEN']", xpathStrConvoCellByName.apply(name));

    public ContactsUiPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isSearchInputVisible() throws Exception {
        return isElementDisplayed(xpathSearchInput);
    }

    public void inputTextToSearch(String text) throws Exception {
        final WebElement input = getElement(xpathSearchInput);
        input.click();
        input.sendKeys(text);
    }

    public boolean isContactPresentedInContactsList(String contact) throws Exception {
        final By locator = By.xpath(xpathStrConvoCellByName.apply(contact));
        return isElementDisplayed(locator, 5);
    }

    public void tapInviteOthersButton() throws Exception {
        getElement(nameInviteOthersButton).click();
    }

    public boolean isInviteOthersButtonVisible() throws Exception {
        return isElementDisplayed(nameInviteOthersButton);
    }

    public void tapOpenButtonNextToUser(String contact) throws Exception {
        final By locator = By.xpath(xpathStrOpenButtonByConvoName.apply(contact));
        getElement(locator).click();
    }
}
