package com.wearezeta.auto.osx.pages.osx;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import com.wearezeta.auto.web.pages.WebPage;

public class GroupPeoplePopoverPage extends WebPage {

    @FindBy(xpath = PopoverLocators.GroupPopover.xpathRootLocator)
    private WebElement rootElement;

    @FindBy(css = PopoverLocators.Shared.cssCreateGroupConversationButton)
    private WebElement createGroupConversationButton;

    @FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantsListPage.xpathConversationTitle)
    private WebElement conversationTitle;

    @FindBy(how = How.CSS, using = ".participants [data-uie-name='enter-users']")
    private WebElement searchInputField;

    public GroupPeoplePopoverPage(Future<ZetaWebAppDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void waitUntilVisibleOrThrowException() throws Exception {
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(PopoverLocators.SingleUserPopover.xpathRootLocator)) : "Popover "
                + PopoverLocators.SingleUserPopover.xpathRootLocator
                + " has not been shown";
    }

    public void waitUntilNotVisibleOrThrowException() throws Exception {
        assert DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(PopoverLocators.SingleUserPopover.xpathRootLocator)) : "Popover "
                + PopoverLocators.SingleUserPopover.xpathRootLocator
                + " has not been shown";
    }

    public boolean isAddPeopleMessageShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(PopoverLocators.Shared.xpathContinueButton));
    }

    public boolean isAddToConversationButtonShown() throws Exception {
        return DriverUtils
                .waitUntilLocatorIsDisplayed(
                        getDriver(),
                        By.cssSelector(PopoverLocators.Shared.cssCreateGroupConversationButton));
    }

    public String getConversationTitle() {
        return conversationTitle.getText();
    }

    public void searchForUser(String searchText) throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), searchInputField);
        searchInputField.clear();
        searchInputField.sendKeys(searchText);
    }

    public void selectUserFromSearchResult(String name) throws Exception {
        By locator = By.xpath(PopoverLocators.Shared.xpathSearchResultByName.apply(name));
        DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
        getDriver().findElement(locator).click();
    }
}
