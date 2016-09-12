package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class WarningPage extends WebPage {

    @FindBy(how = How.CSS, using = WebAppLocators.WarningPage.cssMissingWebRTCSupportWarningBarClose)
    private WebElement missingWebRTCSupportWarningBarCloseButton;

    @FindBy(how = How.CSS, using = WebAppLocators.WarningPage.cssAnotherCallWarningModalClose)
    private WebElement anotherCallWarningModalCloseButton;

    @FindBy(how = How.CSS, using = WebAppLocators.WarningPage.cssFullCallWarningModalClose)
    private WebElement fullCallWarningModalCloseButton;

    @FindBy(how = How.CSS, using = WebAppLocators.WarningPage.cssFullConversationWarningModalClose)
    private WebElement fullConversationWarningModalCloseButton;

    @FindBy(how = How.CSS, using = WebAppLocators.WarningPage.cssFileTransferLimitWarningModalButton)
    private WebElement fileTransferLimitWarningModalButton;

    @FindBy(how = How.CSS, using = WebAppLocators.WarningPage.cssFullHouseWarningModalClose)
    private WebElement fullHouseWarningModalCloseButton;

    public WarningPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isMissingWebRTCSupportWarningBarVisible() throws Exception {
        final String css = WebAppLocators.WarningPage.cssMissingWebRTCSupportWarningBar;
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.cssSelector(css));
    }

    public boolean isMissingWebRTCSupportWarningBarInvisible() throws Exception {
        final String css = WebAppLocators.WarningPage.cssMissingWebRTCSupportWarningBar;
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), By.cssSelector(css));
    }

    public boolean isLinkWithCaptionInMissingWebRTCSupportWarningBarVisible(String caption) throws Exception {
        final By locator = By.xpath(WebAppLocators.WarningPage.xpathMissingWebRTCSupportWarningBarLinkByCaption
                .apply(caption));
        return DriverUtils.waitUntilElementClickable(this.getDriver(), getDriver().findElement(locator));
    }

    public void clickCloseMissingWebRTCSupportWarningBar() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), missingWebRTCSupportWarningBarCloseButton);
        missingWebRTCSupportWarningBarCloseButton.click();
    }

    public boolean isAnotherCallWarningModalVisible() throws Exception {
        final By locator = By.cssSelector(WebAppLocators.WarningPage.cssAnotherCallWarningModal);
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator);
    }

    public boolean isAnotherCallWarningModalInvisible() throws Exception {
        final By locator = By.cssSelector(WebAppLocators.WarningPage.cssAnotherCallWarningModal);
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }

    public void clickCloseAnotherCallWarningModal() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), anotherCallWarningModalCloseButton);
        final By locator = By.cssSelector(WebAppLocators.WarningPage.cssAnotherCallWarningModal);
        anotherCallWarningModalCloseButton.click();
        DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }

    public boolean clickButtonWithCaptionInAnotherCallWarningModal(String caption) throws Exception {
        final By buttonLocator = By.xpath(WebAppLocators.WarningPage.xpathAnotherCallWarningModalButtonByCaption
                .apply(caption));
        WebElement button = getDriver().findElement(buttonLocator);
        final By modalLocator = By.cssSelector(WebAppLocators.WarningPage.cssAnotherCallWarningModal);
        boolean clickable = DriverUtils.waitUntilElementClickable(this.getDriver(), button);
        button.click();
        DriverUtils.waitUntilLocatorDissapears(this.getDriver(), modalLocator);
        return clickable;
    }

    public boolean isFullCallWarningModalVisible() throws Exception {
        final By locator = By.cssSelector(WebAppLocators.WarningPage.cssFullCallWarningModal);
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator);
    }

    public boolean isFullCallWarningModalInvisible() throws Exception {
        final By locator = By.cssSelector(WebAppLocators.WarningPage.cssFullCallWarningModal);
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }

    public void clickCloseFullCallWarningModal() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), fullCallWarningModalCloseButton);
        final By locator = By.cssSelector(WebAppLocators.WarningPage.cssFullCallWarningModalClose);
        fullCallWarningModalCloseButton.click();
        DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }

    public boolean clickButtonWithCaptionInFullCallWarningModal(String caption) throws Exception {
        final By buttonLocator = By.xpath(WebAppLocators.WarningPage.xpathFullCallWarningModalButtonByCaption
                .apply(caption));
        WebElement button = getDriver().findElement(buttonLocator);
        final By modalLocator = By.cssSelector(WebAppLocators.WarningPage.cssFullCallWarningModal);
        boolean clickable = DriverUtils.waitUntilElementClickable(this.getDriver(), button);
        button.click();
        DriverUtils.waitUntilLocatorDissapears(this.getDriver(), modalLocator);
        return clickable;
    }

    public boolean isFullConversationWarningModalVisible() throws Exception {
        final By locator = By.cssSelector(WebAppLocators.WarningPage.cssFullConversationWarningModal);
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator);
    }

    public boolean isFullConversationWarningModalInvisible() throws Exception {
        final By locator = By.cssSelector(WebAppLocators.WarningPage.cssFullConversationWarningModal);
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }

    public void clickCloseFullConversationWarningModal() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), fullConversationWarningModalCloseButton);
        final By locator = By.cssSelector(WebAppLocators.WarningPage.cssFullConversationWarningModalClose);
        fullConversationWarningModalCloseButton.click();
        DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }

    public boolean clickButtonWithCaptionInFullConversationWarningModal(String caption) throws Exception {
        final By buttonLocator = By.xpath(WebAppLocators.WarningPage.xpathFullConversationWarningModalButtonByCaption
                .apply(caption));
        WebElement button = getDriver().findElement(buttonLocator);
        final By modalLocator = By.cssSelector(WebAppLocators.WarningPage.cssFullConversationWarningModal);
        boolean clickable = DriverUtils.waitUntilElementClickable(this.getDriver(), button);
        button.click();
        DriverUtils.waitUntilLocatorDissapears(this.getDriver(), modalLocator);
        return clickable;
    }

    public boolean isFileTransferLimitWarningModalVisible() throws Exception {
        final By locator = By.cssSelector(WebAppLocators.WarningPage.cssFileTransferLimitWarningModal);
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator);
    }

    public boolean isFileTransferLimitWarningModalInvisible() throws Exception {
        final By locator = By.cssSelector(WebAppLocators.WarningPage.cssFileTransferLimitWarningModal);
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }

    public void clickOKInFileTransferLimitWarningModal() throws Exception {
        fileTransferLimitWarningModalButton.click();
    }

    public boolean isFullHouseWarningModalVisible() throws Exception {
        final By locator = By.cssSelector(WebAppLocators.WarningPage.cssFullHouseWarningModal);
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator);
    }

    public boolean isFullHouseWarningModalInvisible() throws Exception {
        final By locator = By.cssSelector(WebAppLocators.WarningPage.cssFullHouseWarningModal);
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }

    public void clickCloseOnFullHouseWarningModal() throws Exception {
        fullHouseWarningModalCloseButton.click();
    }
}
