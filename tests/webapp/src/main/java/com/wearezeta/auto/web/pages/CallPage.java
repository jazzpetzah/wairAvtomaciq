package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import java.util.concurrent.Future;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CallPage extends ContactListPage {
    
    public CallPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }
    
    
    public boolean isMuteCallButtonVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathMuteCallButtonByContactName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }

    public boolean isVideoButtonVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathVideoButtonByContactName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }

    public boolean isEndCallButtonVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathEndCallButtonByContactName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }

    public void clickEndCallButton() throws Exception {
        final String locator = WebAppLocators.CallPage.cssEndCallButton;
        WebElement endCallButton = getDriver().findElementByCssSelector(locator);
        endCallButton.click();
    }

    public boolean isSelfVideoVisible() throws Exception {
        final String locator = WebAppLocators.CallPage.idSelfVideoPreview;
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(locator));
    }

    public boolean isSelfVideoNotVisible() throws Exception {
        final String locator = WebAppLocators.CallPage.idSelfVideoPreview;
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.id(locator));
    }

    public boolean isUserNameVisibleInCallingBanner(String user) throws Exception {
        String locator = WebAppLocators.CallPage.xpathUserNameByUserNameInCallingBanner
                .apply(user);
        return DriverUtils
                .waitUntilLocatorIsDisplayed(this.getDriver(), By.xpath(locator));
    }

    public boolean isVideoButtonNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathVideoButtonByContactName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(locator));
    }

    public boolean isEndCallButtonNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathEndCallButtonByContactName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(locator));
    }

    public boolean isMuteCallButtonNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathMuteCallButtonByContactName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(locator));
    }

    public boolean isAcceptVideoCallButtonVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathAcceptVideoCallButtonByContactName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }

    public boolean isAcceptVideoCallButtonNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathAcceptVideoCallButtonByContactName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(locator));
    }

    public boolean isDeclineCallButtonVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathDeclineCallButtonByContactName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }

    public boolean isDeclineCallButtonNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathDeclineCallButtonByContactName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(locator));
    }

    public void clickDeclineCallButton() throws Exception {
        final String locator = WebAppLocators.CallPage.cssDeclineCallButton;
        WebElement declineCallButton = getDriver().findElementByCssSelector(locator);
        declineCallButton.click();
    }

    public void clickAcceptVideoCallButton() throws Exception {
        final String locator = WebAppLocators.CallPage.cssAcceptVideoCallButton;
        WebElement acceptVideoCallButton = getDriver().findElementByCssSelector(locator);
        acceptVideoCallButton.click();
    }

    public void clickMuteCallButton() throws Exception {
        final String locator = WebAppLocators.CallPage.cssMuteCallButton;
        WebElement muteCallButton = getDriver().findElementByCssSelector(locator);
        muteCallButton.click();
    }

    public boolean isMuteCallButtonPressed() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(WebAppLocators.CallPage.xpathMuteCallButtonPressed));
    }

    public boolean isMuteCallButtonNotPressed() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(WebAppLocators.CallPage.xpathMuteCallButtonNotPressed));
    }
}
