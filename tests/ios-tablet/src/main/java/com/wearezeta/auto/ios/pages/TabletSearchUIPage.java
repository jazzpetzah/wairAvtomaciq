package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletSearchUIPage extends SearchUIPage {
    public static final By xpathSearchField =
            By.xpath("(//XCUIElementTypeTextView[@name='textViewSearch'])[last()]");

    public static final By namePeoplePickerAddToConversationButton = MobileBy.AccessibilityId("ADD");

    public static final By xpathPeoplePickerCreateConversationButton =
            By.xpath("(//XCUIElementTypeButton[@name='CREATE GROUP'])[last()]");

    public static final Function<String,String> xpathStrIPADPeoplePickerResultUserName = name ->
            String.format("(//XCUIElementTypeStaticText[@name='%s'])[last()]", name);

    public TabletSearchUIPage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void selectConnectedUser(String name) throws Exception {
        getElement(By.xpath(xpathStrIPADPeoplePickerResultUserName.apply(name))).click();
    }

    public void clickAddToConversationButtonOniPadPopover() throws Exception {
        getElement(namePeoplePickerAddToConversationButton).click();
    }

    public void fillTextInTabletPeoplePickerSearchField(String text) throws Exception {
        final WebElement pickerSearchField = getElement(xpathSearchField);
        try {
            pickerSearchField.sendKeys(text);
        } catch (WebDriverException ex) {
            pickerSearchField.clear();
            pickerSearchField.sendKeys(text);
        }
    }

    public void clickCreateConversationButtonOniPadPopover() throws Exception {
        getElement(xpathPeoplePickerCreateConversationButton).click();
    }
}
