package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletPeoplePickerPage extends PeoplePickerPage {
    public static final By xpathSearchField = By.xpath(xpathStrMainWindow +
            "/UIAPopover[1]/UIATextView[@name='textViewSearch']");

    public static final By namePeoplePickerAddToConversationButton = By.name("ADD TO CONVERSATION");

    public static final Function<String,String> xpathStrIPADPeoplePickerResultUserName = name ->
            String.format("//UIAPopover//UIAStaticText[@name='%s']", name);

    public TabletPeoplePickerPage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void pressIntoSearchField() throws Exception {
        getElement(xpathSearchField).click();
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

}
