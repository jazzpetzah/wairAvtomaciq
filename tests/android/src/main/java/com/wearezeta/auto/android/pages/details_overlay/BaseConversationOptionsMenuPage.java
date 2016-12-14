package com.wearezeta.auto.android.pages.details_overlay;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import javax.ws.rs.NotSupportedException;
import java.util.concurrent.Future;
import java.util.function.Function;

public abstract class BaseConversationOptionsMenuPage extends BaseUserDetailsOverlay {
    private static final Function<String, String> xpathStrConvoSettingsMenuItemByName = name -> String
            .format("//*[@id='ttv__settings_box__item' and @value='%s']" +
                    "/parent::*//*[@id='fl_options_menu_button']", name.toUpperCase());
    private static String idUsername = "ttv__settings_box__title";

    public BaseConversationOptionsMenuPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected String getUserNameId() {
        return idUsername;
    }

    @Override
    protected By getAvatarLocator() {
        throw new NotSupportedException("No avatar locator on single conversation list option menu");
    }

    public void tapConversationSettingsMenuItem(String itemName) throws Exception {
        final By locator = By.xpath(xpathStrConvoSettingsMenuItemByName.apply(itemName));
        getElement(locator, String
                .format("Conversation menu item '%s' could not be found on the current screen", itemName)).click();
    }

    public boolean isConversationSettingsMenuItemVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrConvoSettingsMenuItemByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    //TODO: Add real test
    public boolean waitUntilOptionMenuVisible() throws Exception {
        return true;
    }

    //TODO: Add real test
    public boolean waitUntilOptionMenuInvisible() throws Exception {
        return true;
    }
}
