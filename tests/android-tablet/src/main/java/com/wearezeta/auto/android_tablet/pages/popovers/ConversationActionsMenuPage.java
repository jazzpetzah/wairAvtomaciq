package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ConversationActionsMenuPage extends AbstractPopoverPage {
    private final static Function<String, String> xpathActionMenuItemByName = itemName -> String
            .format("%s//*[@value='%s']/parent::*//*[@id='fl_options_menu_button']",
                    ConversationActionsOverlay.xpathRootLocator, itemName.toUpperCase());

    public ConversationActionsMenuPage(Future<ZetaAndroidDriver> lazyDriver,
                                       ConversationActionsOverlay container) throws Exception {
        super(lazyDriver, container);
    }

    public void selectMenuItem(String itemName) throws Exception {
        final By locator = By.xpath(xpathActionMenuItemByName.apply(itemName));
        getElement(locator, String.format("Actions menu item '%s' is not displayed", itemName)).click();
    }
}
