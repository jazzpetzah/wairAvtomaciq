package com.wearezeta.auto.ios.pages;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class GroupChatPage extends DialogPage {
    private static final String xpathNewGroupConversationNameChangeTextField =
            xpathMainWindow + "/UIATableView[1]/UIATableCell[2]/UIATextView[1]";
    @FindBy(xpath = xpathNewGroupConversationNameChangeTextField)
    private WebElement newGroupConversationNameChangeTextField;

    private static final String nameYouHaveLeft = "YOU HAVE LEFT";
    @FindBy(name = nameYouHaveLeft)
    private WebElement youLeft;

    private static final String nameYouLeftMessage = "YOU LEFT";
    @FindBy(name = nameYouLeftMessage)
    private WebElement youLeftMessage;

    private static final String nameYouRenamedConversationMessage = "YOU RENAMED THE CONVERSATION";
    @FindBy(name = nameYouRenamedConversationMessage)
    private WebElement yourRenamedMessage;

    private static final String xpathStartConversationAfterDelete =
            "//UIAStaticText[starts-with(@name, 'START A CONVERSATION WITH')]";
    @FindBy(xpath = xpathStartConversationAfterDelete)
    private WebElement startConvAfterDeleteMessage;

    private static final Function<String, String> xpathYouAddedToGroupChatMessageByName =
            name -> String.format("//UIAStaticText[contains(@name, 'YOU ADDED %s')]", name.toUpperCase());

    private static final String nameConversationCursorInput = "ConversationTextInputField";

    private static final String namePlusButton = "plusButton";

    public GroupChatPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean areRequiredContactsAddedToChat(List<String> names) {
        final String lastMsg = getStartedtChatMessage();
        for (String name : names) {
            if (!lastMsg.toLowerCase().contains(name.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public boolean areContactsAddedAfterDeleteContent(List<String> names) {
        final String lastMsg = getStartedtChatMessageAfterDeleteContent();
        for (String name : names) {
            if (!lastMsg.toLowerCase().contains(name.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    private String getStartedtChatMessageAfterDeleteContent() {
        return startConvAfterDeleteMessage.getText();
    }

    public boolean areRequired3ContactsAddedToChat(String name1, String name2,
                                                   String name3) {
        String lastMessage = getAddedtoChatMessage();
        return lastMessage.contains(name1.toUpperCase())
                && lastMessage.contains(name2.toUpperCase())
                && lastMessage.contains(name3.toUpperCase());
    }

    public boolean isGroupChatPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.name(nameConversationCursorInput));
    }

    public boolean isYouAddedUserMessageShown(String user) throws Exception {
        final By locator = By.xpath(xpathYouAddedToGroupChatMessageByName.apply(user));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator);
    }

    public boolean isYouRenamedConversationMessageVisible(String name) {
        return getRenamedMessage().equals(nameYouRenamedConversationMessage);
    }

    @Override
    public IOSPage openConversationDetailsClick() throws Exception {
        for (int i = 0; i < 3; i++) {
            if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(namePlusButton))) {
                plusButton.click();
                openConversationDetails.click();
                DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.name(nameAddContactToChatButton), 5);
            }
            if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(nameAddContactToChatButton))) {
                break;
            } else {
                swipeUp(1000);
            }
        }

        return new GroupChatInfoPage(this.getLazyDriver());
    }

    @Override
    public void swipeUp(int time) throws Exception {
        WebElement element = getDriver().findElement(
                By.name(nameMainWindow));

        Point coords = element.getLocation();
        Dimension elementSize = element.getSize();
        this.getDriver().swipe(coords.x + elementSize.width / 2,
                coords.y + elementSize.height - 170,
                coords.x + elementSize.width / 2, coords.y + 40, time);
    }

    public boolean isYouLeftMessageShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(nameYouLeftMessage));
    }

}
