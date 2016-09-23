package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class GroupConversationViewPage extends ConversationViewPage {
    private static final By nameYouLeftMessage = MobileBy.AccessibilityId("YOU LEFT");

    private static final Function<String, String> xpathStrYouAddedToGroupChatMessageByName =
            name -> String.format("//XCUIElementTypeStaticText[starts-with(@name,'YOU ADDED %s')]",
                    name.toUpperCase());

    public GroupConversationViewPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isYouAddedUserMessageShown(String user) throws Exception {
        final By locator = By.xpath(xpathStrYouAddedToGroupChatMessageByName.apply(user));
        return isElementDisplayed(locator);
    }

    public boolean isYouRenamedConversationMessageVisible() throws Exception {
        return isElementDisplayed(nameYouRenamedConversation);
    }

    public boolean isYouLeftMessageShown() throws Exception {
        return isElementDisplayed(nameYouLeftMessage);
    }

}
