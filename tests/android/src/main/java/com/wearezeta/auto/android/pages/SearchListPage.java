package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class SearchListPage extends AbstractPickUserPage {

    public static final By idSuggestionUserName = By.xpath("//*[@id='ttv_pickuser__searchuser_name']");

    private static final Function<String, String> xpathStrGroupByName = name -> String
            .format("//*[@id='ttv_pickuser_searchconversation_name' and @value='%s']", name);

    public static final Function<String, String> xpathStrUserNameByName = name -> String
            .format("//*[@id='ttv_pickuser__searchuser_name' and @value='%s']/parent::*/parent::*", name);

    public static final Function<String, String> xpathStrUserAvatarByName = name -> String
            .format("//*[@id='ttv_pickuser__searchuser_name' and @value='%s']"
                    + "/preceding-sibling::*[@id='cv_pickuser__searchuser_chathead']", name);

    public SearchListPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isSuggestionVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idSuggestionUserName);
    }

    public boolean isSuggestionInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idSuggestionUserName);
    }

    public void swipeRightOnContactAvatar(String name) throws Exception {
        final By locator = By.xpath(xpathStrUserNameByName.apply(name));
        swipeRightOnContactAvatar(locator);
    }

    public void tapOnUserName(String name) throws Exception {
        final By locator = By.xpath(xpathStrUserNameByName.apply(name));
        tapOnUserName(locator, name);
    }

    public void tapOnGroupName(String name) throws Exception {
        final By locator = By.xpath(xpathStrGroupByName.apply(name));
        tapOnGroupName(locator, name);
    }

    protected By getNameLocator(boolean isGroup, String name) {
        return isGroup ? By.xpath(xpathStrGroupByName.apply(name)) : By.xpath(xpathStrUserNameByName.apply(name));
    }

    protected By getUserAvatarLocator(String name) {
        return By.xpath(xpathStrUserAvatarByName.apply(name));
    }

    protected By getInviteButtonLocator(String name) {
        // TODO: Fix the the avatar locator
        throw new NotImplementedException("Do not support invite button on search list");
    }
}
