package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class GroupChatPage extends DialogPage {
    private static final By nameYouLeftMessage = By.name("YOU LEFT");

    private static final Function<String, String> xpathStrYouAddedToGroupChatMessageByName =
            name -> String.format("//UIAStaticText[contains(@name, 'YOU ADDED %s')]", name.toUpperCase());

    public GroupChatPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isYouAddedUserMessageShown(String user) throws Exception {
        final By locator = By.xpath(xpathStrYouAddedToGroupChatMessageByName.apply(user));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator);
    }

    public boolean isYouRenamedConversationMessageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),nameYouRenamedConversation);
    }

    @Override
    public void swipeUp(int time) throws Exception {
        final WebElement element = getElement(nameMainWindow);
        final Point coords = element.getLocation();
        final Dimension elementSize = element.getSize();
        this.getDriver().swipe(coords.x + elementSize.width / 2,
                coords.y + elementSize.height - 170,
                coords.x + elementSize.width / 2, coords.y + 40, time);
    }

    public boolean isYouLeftMessageShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameYouLeftMessage);
    }

}
