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
    
    public boolean isVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathUserNameByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }
    
    public boolean isNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathUserNameByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(locator));
    }
    
    public boolean isMuteCallButtonVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathMuteCallButtonByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }

    public boolean isVideoButtonVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathVideoButtonByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }

    public boolean isEndCallButtonVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathEndCallButtonByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }

    public void clickEndCallButton(String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathEndCallButtonByConversationName
                .apply(conversationName);
        WebElement endCallButton = getDriver().findElementByXPath(locator);
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
        String locator = WebAppLocators.CallPage.xpathUserNameByConversationName
                .apply(user);
        return DriverUtils
                .waitUntilLocatorIsDisplayed(this.getDriver(), By.xpath(locator));
    }

    public boolean isVideoButtonNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathVideoButtonByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(locator));
    }

    public boolean isEndCallButtonNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathEndCallButtonByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(locator));
    }

    public boolean isMuteCallButtonNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathMuteCallButtonByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(locator));
    }

    public boolean isAcceptVideoCallButtonVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathAcceptVideoCallButtonByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }

    public boolean isAcceptVideoCallButtonNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathAcceptVideoCallButtonByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(locator));
    }

    public boolean isDeclineCallButtonVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathDeclineCallButtonByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }

    public boolean isDeclineCallButtonNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathDeclineCallButtonByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(locator));
    }

    public void clickDeclineCallButton(String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathDeclineCallButtonByConversationName.apply(conversationName);
        WebElement declineCallButton = getDriver().findElementByCssSelector(locator);
        declineCallButton.click();
    }

    public void clickAcceptVideoCallButton(String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathAcceptVideoCallButtonByConversationName.apply(conversationName);
        WebElement acceptVideoCallButton = getDriver().findElementByXPath(locator);
        acceptVideoCallButton.click();
    }

    public void clickMuteCallButton(String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathMuteCallButtonByConversationName.apply(conversationName);
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
