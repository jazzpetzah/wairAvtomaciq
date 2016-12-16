package com.wearezeta.auto.android.pages.details_overlay;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public abstract class BaseUserDetailsOverlay extends BaseDetailsOverlay {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private static final String strIdUniqueName = "ttv__user_details__user_name";
    private static final String strIdUserInfo = "ttv__user_details__user_info";
    private static final String strIdAcceptButton = "zb__connect_request__accept_button";
    private static final String strIdIgnoreButton = "zb__connect_request__ignore_button";

    private static final By idUniqueUserName = By.id(strIdUniqueName);
    private static final By idUserInfo = By.id(strIdUserInfo);

    private static final By idAcceptButton = By.id(strIdAcceptButton);
    private static final By idIgnoreButton = By.id(strIdIgnoreButton);

    private static final Function<String, String> xpathStrTabByText = text -> String.format("//*[@value='%s']", text);
    private static final Function<String, String> xpathStrUniqueUserName = text -> String.format
            ("//*[@id='%s' and @value='@%s']", strIdUniqueName, text);
    private static final Function<String, String> xpathStrUserInfo = text -> String.format
            ("//*[@id='%s' and normalize-space(@value)='%s']", strIdUserInfo, text);
    private final Function<String, String> xpathStrUserName = userName -> String.format
            ("//*[@id='%s' and @value='%s']", getUserNameId(), userName);
    private final Function<String, String> xpathStrAcceptButtonByUserName = userName -> String.format
            ("//*[@id='ll__connect_request__main_container' and .%s]//*[@id='%s']", xpathStrUserName.apply(userName), strIdAcceptButton);
    private final Function<String, String> xpathStrIgnoreButtonByUserName = userName -> String.format
            ("//*[@id='ll__connect_request__main_container' and .%s]//*[@id='%s']", xpathStrUserName.apply(userName), strIdIgnoreButton);

    public BaseUserDetailsOverlay(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    public boolean waitUntilUserDataVisible(String type, String nameAlias) throws Exception {
        By locator = getUserDataLocator(type, nameAlias);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilUserDataInvisible(String type, String nameAlias) throws Exception {
        By locator = getUserDataLocator(type, nameAlias);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean waitUntilUserDataVisible(String type) throws Exception {
        By locator = getUserDataLocator(type);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilUserDataInvisible(String type) throws Exception {
        By locator = getUserDataLocator(type);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean waitUntilAvatarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), getAvatarLocator());
    }

    protected abstract String getUserNameId();

    protected By getUserDataLocator(String type, String text) {
        switch (type.toLowerCase()) {
            case "user name":
                text = usrMgr.replaceAliasesOccurences(text, ClientUsersManager.FindBy.NAME_ALIAS);
                return By.xpath(xpathStrUserName.apply(text));
            case "unique user name":
            case "unique username":
                text = usrMgr.replaceAliasesOccurences(text, ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
                return By.xpath(xpathStrUniqueUserName.apply(text));
            case "user info":
                return By.xpath(xpathStrUserInfo.apply(text));
            default:
                throw new IllegalArgumentException(
                        String.format("Cannot find the locator for '%s'", type));
        }
    }

    protected By getUserDataLocator(String type) {
        switch (type.toLowerCase()) {
            case "user name":
                return By.id(getUserNameId());
            case "unique user name":
            case "unique username":
                return idUniqueUserName;
            case "user info":
                return idUserInfo;
            default:
                throw new IllegalArgumentException(String.format("Cannot find the locator for '%s'", type));
        }
    }

    protected void switchToTab(String tabName) throws Exception {
        final By locator = By.xpath(xpathStrTabByText.apply(tabName));
        getElement(locator).click();
    }

    protected boolean waitUntilCommonUserVisible(String userName) throws Exception {
        throw new NotImplementedException("not implemented");
    }

    protected boolean waitUntilCommonUserInvisible(String userName) throws Exception {
        throw new NotImplementedException("not implemented");
    }

    protected By getButtonLocatorByName(String name) {
        throw new IllegalStateException(String.format("Cannot find button locator of '%s'", name));
    }

    protected By getAcceptButtonLocator() {
        return idAcceptButton;
    }

    protected By getIgnoreButtonLocator() {
        return idIgnoreButton;
    }

    protected By getAcceptButtonLocatorByUsername(String userName) {
        return By.xpath(xpathStrAcceptButtonByUserName.apply(userName));
    }

    protected By getIgnoreButtonLocatorByUserName(String userName) {
        return By.xpath(xpathStrIgnoreButtonByUserName.apply(userName));
    }

    protected abstract By getAvatarLocator();
}
