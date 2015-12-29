package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

public class InvitationsPage extends AndroidPage {

    private static final String idInviteMorePeopleContactsBtn = "zb__conversationlist__show_contacts";
    @FindBy(id = idInviteMorePeopleContactsBtn)
    private By inviteContactsBtnLocator = By.id(idInviteMorePeopleContactsBtn);

    private static final String idInviteSearchField = "puet_pickuser__searchbox";
    @FindBy(id = idInviteSearchField)
    private WebElement inviteSearchField;

    private static final String idInvitePageCloseBtn = "gtv_pickuser__clearbutton";
    @FindBy(id = idInvitePageCloseBtn)
    private WebElement invitePageCloseBtn;

    private static final Function<String, String> xpathUserToInviteByName = name
            -> String.format("//*[@id='ttv__contactlist__user__name' and @value='%s']", name);

    private static final Function<String, String> xpathInviteButtonByUserName = name
            -> String.format("%s/parent::*/*[@id='zb__contactlist__user_selected_button']",
            xpathUserToInviteByName.apply(name));

    private static final Function<String, String> xpathAvatarByUserName = name
            -> String.format("%s/parent::*/*[@id='cv__contactlist__user__chathead']",
            xpathUserToInviteByName.apply(name));

    private static final Function<String, String> xpathAlertItemByValue = value
            -> String.format("//*[starts-with(@id,'text') and @value='%s']", value);

    private static final String xpathAlertOK = "//*[starts-with(@id,'button') and @value='OK']";
    @FindBy(xpath = xpathAlertOK)
    private WebElement alertOKButton;

    public InvitationsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilUserNameIsVisible(String name) throws Exception {
        final By locator = By.xpath(xpathUserToInviteByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public Optional<BufferedImage> getAvatarScreenshot(String name) throws Exception {
        final By locator = By.xpath(xpathAvatarByUserName.apply(name));
        assert waitUntilUserNameIsVisible(name) : String.format(
                "User '%s' is not visible in the invites list", name);
        return this.getElementScreenshot(getDriver().findElement(locator));
    }

    public void tapInviteButtonFor(String name) throws Exception {
        final By locator = By.xpath(xpathInviteButtonByUserName.apply(name));
        assert waitUntilUserNameIsVisible(name) : String.format(
                "User '%s' is not visible in the invites list", name);
        getDriver().findElement(locator).click();
    }

    public void selectEmailOnAlert(String email) throws Exception {
        final By locator = By.xpath(xpathAlertItemByValue.apply(email));
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String.format(
                "Email address '%s' is not visible on the alert", email);
        getDriver().findElement(locator).click();
    }

    public void confirmInvitationAlert() {
        alertOKButton.click();
    }

    public boolean isInvitationMessageReceivedBy(String email) throws Throwable {
        return BackendAPIWrappers.getInvitationMessage(email).
                orElseThrow(() -> {
                    throw new IllegalStateException("Invitation message has not been received");
                }).
                isValid();
    }

    public boolean waitForInviteMorePeopleContactsButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), inviteContactsBtnLocator);
    }

    public boolean waitForInviteMorePeopleContactsButtonNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                inviteContactsBtnLocator);
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
        inviteSearchField.click();
    }

    public void tapOnInvitePageCloseBtn() throws Exception {
        invitePageCloseBtn.click();
    }

    public boolean waitUntilUserNameIsInvisible(String name) throws Exception {
        final By locator = By.xpath(xpathUserToInviteByName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }
}