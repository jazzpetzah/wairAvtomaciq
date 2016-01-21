package com.wearezeta.auto.ios.pages;

import java.util.List;
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

    private static final By xpathStartConversationAfterDelete = By.xpath(
            "//UIAStaticText[starts-with(@name, 'START A CONVERSATION WITH')]");

    private static final Function<String, String> xpathStrYouAddedToGroupChatMessageByName =
            name -> String.format("//UIAStaticText[contains(@name, 'YOU ADDED %s')]", name.toUpperCase());

    private static final By nameConversationCursorInput = By.name("ConversationTextInputField");

    public GroupChatPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean areRequiredContactsAddedToChat(List<String> names) throws Exception {
        final String lastMsg = getStartedChatMessage();
        return names.stream().anyMatch(x -> lastMsg.toLowerCase().contains(x.toLowerCase()));
    }

    public boolean areContactsAddedAfterDeleteContent(List<String> names) throws Exception {
        final String lastMsg = getStartedChatMessageAfterDeleteContent();
        return names.stream().anyMatch(x -> lastMsg.toLowerCase().contains(x.toLowerCase()));
    }

    private String getStartedChatMessageAfterDeleteContent() throws Exception {
        return getElement(xpathStartConversationAfterDelete,
                "Chat started message is not present in the conversation view after timeout expired").getText();
    }

    public boolean areRequired3ContactsAddedToChat(String name1, String name2, String name3) throws Exception {
        String lastMessage = getAddedToChatMessage();
        return lastMessage.contains(name1.toUpperCase())
                && lastMessage.contains(name2.toUpperCase())
                && lastMessage.contains(name3.toUpperCase());
    }

    public boolean isGroupChatPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameConversationCursorInput);
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
