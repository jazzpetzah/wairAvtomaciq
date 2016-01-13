package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletPeoplePickerPage extends PeoplePickerPage {
    public static final String nameSearchField = "SEARCH BY NAME OR EMAIL";
    @FindBy(name = nameSearchField)
    private WebElement searchField;

    public static final String xpathSearchField =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIATextView[@name='textViewSearch']";
    @FindBy(xpath = xpathSearchField)
    private WebElement pickerSearchField;

    public static final String namePeoplePickerAddToConversationButton = "ADD TO CONVERSATION";
    @FindBy(name = namePeoplePickerAddToConversationButton)
    private WebElement addToConversationButtoniPad;

    public static final String xpathIPADPeoplePickerResultUserName = "//UIAPopover//UIAStaticText[@name='%s']";

    public TabletPeoplePickerPage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
        // TODO Auto-generated constructor stub
    }

    public void pressIntoSearchField() {
        pickerSearchField.click();
    }

    public void selectConnectedUser(String name) throws Exception {
        WebElement el = getDriver().findElement(
                By.xpath(String.format(xpathIPADPeoplePickerResultUserName, name)));
        el.click();
    }

    public GroupChatPage clickAddToConversationButtonOniPadPopover() throws Throwable {
        addToConversationButtoniPad.click();
        return new GroupChatPage(this.getLazyDriver());
    }

    public void fillTextInTabletPeoplePickerSearchField(String text) {
        try {
            pickerSearchField.sendKeys(text);
        } catch (WebDriverException ex) {
            pickerSearchField.clear();
            pickerSearchField.sendKeys(text);
        }
    }

}
