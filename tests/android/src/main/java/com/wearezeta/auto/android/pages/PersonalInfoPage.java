package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class PersonalInfoPage extends AndroidPage {
    private static final String xpathStrParentSelfProfileOverlay = "//*[@id='fl__profile__main_container']";

    private static final By xpathSettingsBox = By.xpath(xpathStrParentSelfProfileOverlay
            + "//*[@id='ll__settings_box_container']");

//    private static final Function<String, String> xpathEmailFieldByValue = value -> String
//            .format(xpathStrParentSelfProfileOverlay
//                    + "//*[@id='ttv__profile__email' and @value='%s']", value);

    private static final Function<String, String> xpathStrNameFieldByValue = value -> String
            .format(xpathStrParentSelfProfileOverlay + "//*[@id='ttv__profile__name' and @value='%s']", value);

    private static final Function<String, String> xpathStrEditFieldByValue = value -> String
            .format(xpathStrParentSelfProfileOverlay + "//*[@id='tet__profile__guided' and @value='%s']", value);

    private static final By xpathNameEdit =
            By.xpath(xpathStrParentSelfProfileOverlay + "//*[@id='tet__profile__guided']");

    private static final By idProfileOptionsButton = By.id("gtv__profile__settings_button");

    private static final Function<String, String> xpathStrProfileMenuItem = name ->
            String.format("//*[@id='ttv__settings_box__item' and @value='%s']" +
                    "/parent::*//*[@id='fl_options_menu_button']", name.toUpperCase());

    private static final By xpathSelfProfileClose = By.xpath(xpathStrParentSelfProfileOverlay
            + "//*[@id='gtv__profile__close_button']");

    private static final By idLightBulbButton = By.id("gtv__profile__theme_button");

    public PersonalInfoPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void tapOnPage() throws Exception {
        this.getDriver().tap(1, getElement(idPager), 500);
    }

    public void tapEllipsisButton() throws Exception {
        getElement(idProfileOptionsButton, "Ellipsis button is not visible").click();
    }

    public boolean isProfileMenuItemInvisible(String itemName) throws Exception {
        final By locator = By.xpath(xpathStrProfileMenuItem.apply(itemName));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void tapProfileMenuItem(String itemName) throws Exception {
        final By locator = By.xpath(xpathStrProfileMenuItem.apply(itemName));
        getElement(locator, String.format("Menu item '%s' is not present on the page", itemName)).click();
    }

    public void tapOnMyName(String name) throws Exception {
        final By locator = By.xpath(xpathStrNameFieldByValue.apply(name));
        getDriver().findElement(locator).click();
    }

    public boolean waitUntilNameEditIsVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrEditFieldByValue.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                locator);
    }

    public void clearSelfName() throws Exception {
        getElement(xpathNameEdit).clear();
    }

    public void changeSelfNameTo(String newName) throws Exception {
        getElement(xpathNameEdit).sendKeys(newName);
        // Sometimes a phone might fail to apply the new name without this sleep
        Thread.sleep(500);
        this.getDriver().navigate().back();
    }

    public boolean waitUntilNameIsVisible(String expectedName) throws Exception {
        final By locator = By.xpath(xpathStrNameFieldByValue.apply(expectedName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isSettingsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSettingsBox);
    }

    public void pressCloseButton() throws Exception {
        getElement(xpathSelfProfileClose, "Close Self Profile button is not visible").click();
    }

    public void tapLightBulbButton() throws Exception {
        getElement(idLightBulbButton).click();
    }
}
