package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ContactsUiPage extends IOSPage {

    private static final By xpathSearchInput = By.xpath("//UIATextView[UIAStaticText[@name='SEARCH BY NAME']]");

    private static final By nameInviteOthersButton = By.name("INVITE OTHERS");

    private static final Function<String, String> xpathStrConvoCellByName = name ->
            String.format("//UIATableCell[@name='%s'][preceding::UIAButton[@name='ContactsViewCloseButton']]", name);

    private static final Function<String, String> xpathStrOpenButtonByConvoName = name ->
            String.format("//UIATableCell[@name='%s']" +
                    "[preceding::UIAButton[@name='ContactsViewCloseButton']]/UIAButton[@name='OPEN']", name);

    public ContactsUiPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
        // TODO Auto-generated constructor stub
    }

    public boolean isSearchInputVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSearchInput);
    }

    public void inputTextToSearch(String text) throws Exception {
        final WebElement input = getElement(xpathSearchInput);
        input.click();
        input.sendKeys(text);
    }

    public boolean isContactPresentedInContactsList(String contact)
            throws Exception {
        final By locator = By.xpath(xpathStrConvoCellByName.apply(contact));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 5);
    }

    public void tapInviteOthersButton() throws Exception {
        getElement(nameInviteOthersButton).click();
    }

    public boolean isInviteOthersButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameInviteOthersButton);
    }

    public void clickOpenButtonNextToUser(String contact) throws Exception {
        final By locator = By.xpath(xpathStrOpenButtonByConvoName.apply(contact));
        getElement(locator).click();
    }
}
