package com.wearezeta.auto.android.pages.details_overlay.common;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class ConversationOptionsMenuOverlayPage extends AndroidPage {
    protected static final Function<String, String> xpathStrOptionsMenuItemByName = name -> String
            .format("//*[@id='fl__participant__settings_box']"
                    + "//*[starts-with(@id, 'ttv__settings_box__item') and @value='%s']"
                    + "/parent::*//*[@id='fl_options_menu_button']", name.toUpperCase());

    public ConversationOptionsMenuOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapOnOptionsMenuItem(String itemName) throws Exception {
        final By locator = By.xpath(xpathStrOptionsMenuItemByName.apply(itemName));
        getElement(locator,
                String.format("Conversation menu item '%s' could not be found on the current screen", itemName)).click();
    }

    public boolean waitUntilOptionMenuItemVisible(String itemName) throws Exception {
        final By locator = By.xpath(xpathStrOptionsMenuItemByName.apply(itemName));
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
