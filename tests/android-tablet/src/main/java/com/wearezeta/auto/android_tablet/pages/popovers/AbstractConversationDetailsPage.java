package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public abstract class AbstractConversationDetailsPage extends AbstractPopoverPage {
    public final static By idOptionsButton = By.id("ll__participants__right__action");

    public final static String idStrOptionsContainer = "fl__participant__settings_box";
    public final static Function<String, String> xpathStrOptionMenuItemByName = itemName -> String
            .format("//*[@id='%s']//*[@value='%s']/parent::*//*[@id='fl_options_menu_button']",
                    idStrOptionsContainer, itemName.toUpperCase());

    public final static By xpathAddPeopleButton =
            By.xpath("//*[@id='ttv__participants__left_label' and @value='ADD PEOPLE']");

    public final static By idCloseButton = By.id("gtv__participants__close");

    public AbstractConversationDetailsPage(
            Future<ZetaAndroidDriver> lazyDriver,
            AbstractPopoverContainer container) throws Exception {
        super(lazyDriver, container);
    }

    public void tapOptionsButton() throws Exception {
        getElement(idOptionsButton).click();
    }

    public void selectMenuItem(String itemName) throws Exception {
        final By locator = By.xpath(xpathStrOptionMenuItemByName.apply(itemName));
        getElement(locator, String.format("Options menu item '%s' is not displayed", itemName)).click();
    }

    public boolean isMenuItemVisible(String itemName) throws Exception {
        final By locator = By.xpath(xpathStrOptionMenuItemByName.apply(itemName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isMenuItemInvisible(String itemName) throws Exception {
        final By locator = By.xpath(xpathStrOptionMenuItemByName.apply(itemName));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void tapAddPeopleButton() throws Exception {
        getElement(xpathAddPeopleButton).click();
    }

    private WebElement getCloseButton() throws Exception {
        return getElement(this.getContainer().getLocator()).findElement(idCloseButton);
    }

    public void tapCloseButton() throws Exception {
        getCloseButton().click();
    }
}
