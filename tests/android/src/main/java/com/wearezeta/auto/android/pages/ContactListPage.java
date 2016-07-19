package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class ContactListPage extends AbstractPickUserPage {
    public static final By xpathTopPeopleAvatars = By.xpath("//*[@id='cwtf__startui_top_user']");

    public static final Function<String, String> xpathStrTopPeopleAvatarByName = name -> String
            .format("//*[@id='cwtf__startui_top_user' and .//*[@value='%s']]", name.toUpperCase());

    private static final Function<String, String> xpathStrUserByName = name
            -> String.format("//*[@id='ttv__contactlist__user__name' and @value='%s']", name);

    private static final Function<String, String> xpathStrAvatarByUserName = name
            -> String.format("%s/parent::*/*[@id='cv__contactlist__user__chathead']",
            xpathStrUserByName.apply(name));

    private static final Function<String, String> xpathStrInviteButtonByUserName = name
            -> String.format("%s/parent::*/*[@id='zb__contactlist__user_selected_button']",
            xpathStrUserByName.apply(name));

    public ContactListPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapOnTopPeople(String name) throws Exception {
        final By locator = By.xpath(xpathStrTopPeopleAvatarByName.apply(name));
        getElement(locator, String.format("Top People item '%s' is not visible", name)).click();
    }

    public boolean waitUntilTopPeopleHeaderVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathTopPeopleAvatars);
    }

    public boolean waitUntilTopPeopleHeaderInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathTopPeopleAvatars);
    }

    public void swipeRightOnContactAvatar(String name) throws Exception {
        final By locator = By.xpath(xpathStrUserByName.apply(name));
        swipeRightOnContactAvatar(locator);
    }

    public void tapOnUserName(String name) throws Exception {
        final By locator = By.xpath(xpathStrUserByName.apply(name));
        tapOnUserName(locator, name);
    }

    protected By getUserAvatarLocator(String name) {
        return By.xpath(xpathStrAvatarByUserName.apply(name));
    }

    protected By getInviteButtonLocator(String name) {
        return By.xpath(xpathStrInviteButtonByUserName.apply(name));
    }

    protected By getNameLocator(boolean isGroup, String name) {
        if (isGroup) {
            throw new IllegalStateException("Do not support to find group in contact list");
        }
        return By.xpath(xpathStrUserByName.apply(name));
    }
}