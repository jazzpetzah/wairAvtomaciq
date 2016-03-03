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
    
    public boolean isIncomingCallVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathIncomingCallByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }
    
    public boolean isIncomingCallNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathIncomingCallByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(locator));
    }
    
    public boolean isOutgoingCallVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathOutgoingCallByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }
    
    public boolean isOutgoingCallNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathOutgoingCallByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(locator));
    }
    
    public boolean isOngoingCallVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathOngoingCallByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }
    
    public boolean isOngoingCallNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathOngoingCallByConversationName
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
        final String locator = WebAppLocators.CallPage.xpathAcceptCallButtonByConversationName
                .apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(locator));
    }

    public boolean isAcceptVideoCallButtonNotVisibleForConversation(
            String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathAcceptCallButtonByConversationName
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
        WebElement declineCallButton = getDriver().findElementByXPath(locator);
        declineCallButton.click();
    }

    public void clickAcceptCallButton(String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathAcceptCallButtonByConversationName.apply(conversationName);
        WebElement acceptCallButton = getDriver().findElementByXPath(locator);
        acceptCallButton.click();
    }

    public void clickMuteCallButton(String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathMuteCallButtonByConversationName.apply(conversationName);
        WebElement muteCallButton = getDriver().findElementByXPath(locator);
        muteCallButton.click();
    }

    public boolean isMuteCallButtonPressed(String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathMuteCallButtonPressed.apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(locator));
    }

    public boolean isMuteCallButtonNotPressed(String conversationName) throws Exception {
        conversationName = fixDefaultGroupConvoName(conversationName, false);
        final String locator = WebAppLocators.CallPage.xpathMuteCallButtonNotPressed.apply(conversationName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(locator));
    }
}
