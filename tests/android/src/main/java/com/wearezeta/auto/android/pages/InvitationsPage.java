package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import org.openqa.selenium.By;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

public class InvitationsPage extends AndroidPage {

    private static final By idInviteMorePeopleContactsBtn = By.id("zb__conversationlist__show_contacts");

    private static final By idInviteSearchField = By.id("puet_pickuser__searchbox");

    private static final By idInvitePageCloseBtn = By.id("gtv_pickuser__clearbutton");

    private static final Function<String, String> xpathStrUserToInviteByName = name
            -> String.format("//*[@id='ttv__contactlist__user__name' and @value='%s']", name);

    private static final Function<String, String> xpathStrInviteButtonByUserName = name
            -> String.format("%s/parent::*/*[@id='zb__contactlist__user_selected_button']",
            xpathStrUserToInviteByName.apply(name));

    private static final Function<String, String> xpathStrAvatarByUserName = name
            -> String.format("%s/parent::*/*[@id='cv__contactlist__user__chathead']",
            xpathStrUserToInviteByName.apply(name));

    private static final Function<String, String> xpathStrAlertItemByValue = value
            -> String.format("//*[starts-with(@id,'text') and @value='%s']", value);

    private static final By xpathAlertOK = By.xpath("//*[starts-with(@id,'button') and @value='OK']");

    public InvitationsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilUserNameIsVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrUserToInviteByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public Optional<BufferedImage> getAvatarScreenshot(String name) throws Exception {
        final By locator = By.xpath(xpathStrAvatarByUserName.apply(name));
        assert waitUntilUserNameIsVisible(name) : String.format(
                "User '%s' is not visible in the invites list", name);
        return this.getElementScreenshot(getElement(locator));
    }

    public void tapInviteButtonFor(String name) throws Exception {
        final By locator = By.xpath(xpathStrInviteButtonByUserName.apply(name));
        assert waitUntilUserNameIsVisible(name) : String.format(
                "User '%s' is not visible in the invites list", name);
        getElement(locator).click();
    }

    public void selectEmailOnAlert(String email) throws Exception {
        final By locator = By.xpath(xpathStrAlertItemByValue.apply(email));
        getElement(locator, String.format("Email address '%s' is not visible on the alert", email)).click();
    }

    public void confirmInvitationAlert() throws Exception {
        getElement(xpathAlertOK).click();
    }

    public boolean isInvitationMessageReceivedBy(String email) throws Throwable {
        return BackendAPIWrappers.getInvitationMessage(email).
                orElseThrow(() -> {
                    throw new IllegalStateException("Invitation message has not been received");
                }).
                isValid();
    }

    public boolean waitForInviteMorePeopleContactsButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idInviteMorePeopleContactsBtn);
    }

    public boolean waitForInviteMorePeopleContactsButtonNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idInviteMorePeopleContactsBtn);
    }

    public String getRecentInvitationCode(String email) throws Throwable {
        final String link = BackendAPIWrappers.getInvitationMessage(email).
                orElseThrow(() -> {
                    throw new IllegalStateException("Invitation message has not been received");
                }).
                extractInvitationLink();
        return link.substring(link.indexOf("/i/") + 3, link.length());
    }

    public void tapOnInviteSearchField() throws Exception {
        getElement(idInviteSearchField).click();
    }

    public void tapOnInvitePageCloseBtn() throws Exception {
        getElement(idInvitePageCloseBtn).click();
    }

    public boolean waitUntilUserNameIsInvisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrUserToInviteByName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }
}