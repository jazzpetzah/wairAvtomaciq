package com.wearezeta.auto.android.pages.details_overlay.group;

import com.wearezeta.auto.android.pages.details_overlay.BaseDetailsOverlay;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Future;
import java.util.function.Function;

public class GroupInfoPage extends BaseDetailsOverlay {
    private static final String strIdSubHeader = "ttv__participants__sub_header";

    private static final By idParticipantsHeader = By.id("ttv__participants__header");
    private static final By idParticipantsSubHeader = By.id(strIdSubHeader);
    private static final By idParticipantsHeaderEditable = By.id("taet__participants__header__editable");

    private static final Function<String, String> xpathParticipantAvatarByName = name -> String
            .format("//*[@id='cv__chathead' and ./parent::*/*[@value='%s']]", name.toUpperCase());
    private static final Function<String, String> xpathStrSubHeaderByText = text -> String
            .format("//*[@id='%s' and @value='%s']", strIdSubHeader, text.toUpperCase());
    private static final Function<String, String> xpathVerifiedParticipantAvatarByName = name -> String
            .format("//*[@id='pgv__participants']//*[@value='Verified']/following::"
                    + "*[@id='cv__chathead' and ./parent::*/*[@value='%s']]", name.toUpperCase());

    public GroupInfoPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    public boolean waitUntilPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idParticipantsHeader)
                & DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idParticipantsSubHeader);
    }

    public boolean waitUntilParticipantVisible(String name) throws Exception {
        final By locator = By.xpath(xpathParticipantAvatarByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilParticipantInvisible(String name) throws Exception {
        final By locator = By.xpath(xpathParticipantAvatarByName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void tapOnParticipantsHeader() throws Exception {
        getElement(idParticipantsHeader).click();
    }

    public void renameGroupChat(String chatName) throws Exception {
        final WebElement groupChatNameEditable = getElement(idParticipantsHeaderEditable);
        groupChatNameEditable.clear();
        groupChatNameEditable.sendKeys(chatName);
        this.pressKeyboardSendButton();
    }

    public void tapOnParticipant(String name) throws Exception {
        // Wait for animation
        Thread.sleep(1000);
        final By nameLocator = By.xpath(xpathParticipantAvatarByName.apply(name));
        getElement(nameLocator, String.format("The avatar of '%s' is not visible", name)).click();
    }

    public boolean waitUntilSubHeaderVisible(String subHeaderText) throws Exception {
        By locator = By.xpath(xpathStrSubHeaderByText.apply(subHeaderText));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    //TODO: Should change to waitUntilConversationNameVisible(String expectedConversationName)
    public String getConversationName() throws Exception {
        return getElement(idParticipantsHeader).getText();
    }

    public boolean waitUntilParticipantAvatarVisible(String name) throws Exception {
        final By locator = By.xpath(xpathParticipantAvatarByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilVerifiedParticipantAvatarVisible(String name) throws Exception {
        final By locator = By.xpath(xpathVerifiedParticipantAvatarByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "add people":
                return super.getLeftActionButtonLocator();
            case "open menu":
                return super.getRightActionButtonLocator();
            default:
                throw new IllegalArgumentException(String.format("Cannot find the locator for button '%s'", name));
        }
    }
}
