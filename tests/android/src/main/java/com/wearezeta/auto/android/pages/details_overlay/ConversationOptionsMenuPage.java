package com.wearezeta.auto.android.pages.details_overlay;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import javax.ws.rs.NotSupportedException;
import java.util.concurrent.Future;
import java.util.function.Function;

public class ConversationOptionsMenuPage extends BaseUserDetailsOverlay {
    private static final By idSettingsContainer = By.id("ll__settings_box__container");
    private static final By idCancelButton = By.id("ttv__settings_box__cancel_button");
    private static final Function<String, String> xpathStrConvoSettingsMenuItemByName = name -> String
            .format("//*[@id='ttv__settings_box__item' and @value='%s']" +
                    "/parent::*//*[@id='fl_options_menu_button']", name.toUpperCase());
    private static String idUsername = "ttv__settings_box__title";

    public ConversationOptionsMenuPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected String getUserNameId() {
        return idUsername;
    }

    @Override
    protected By getAvatarLocator() {
        throw new NotSupportedException("No avatar on conversation options menu");
    }

    public void tapConversationOptionsMenuItem(String itemName) throws Exception {
        final By locator = getButtonLocator(itemName);
        getElement(locator, String
                .format("Conversation menu item '%s' could not be found on the current screen", itemName)).click();
    }

    public boolean waitUntilConversationOptionsMenuItemVisible(String itemName) throws Exception {
        final By locator = getButtonLocator(itemName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilConversationOptionsMenuItemInvisible(String itemName) throws Exception {
        final By locator = getButtonLocator(itemName);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    private By getButtonLocator(String itemName) throws Exception {
        switch (itemName.toUpperCase()) {
            case "CANCEL":
                return idCancelButton;
            default:
                return By.xpath(xpathStrConvoSettingsMenuItemByName.apply(itemName));
        }
    }

    public boolean waitUntilOptionMenuVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idSettingsContainer);
    }

    public boolean waitUntilOptionMenuInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idSettingsContainer);
    }
}
