package com.wearezeta.auto.android_tablet.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletSelfProfilePage extends AndroidTabletPage {
    public static final By idSelfNameInput = By.id("tet__profile__guided");

    public static final String idStrSelfProfileView = "ll_self_form";
    public static final By idSelfProfileView = By.id(idStrSelfProfileView);

    public static final Function<String, String> xpathStrSelfNameByContent = content -> String
            .format("//*[@id='ttv__profile__name' and @value='%s']", content);

    public static final Function<String, String> xpathStrOptionsMenuItemByName = name -> String
            .format("//*[@id='fl__profile__settings_box']//*[@id='ttv__settings_box__item' and @value='%s']" +
                    "/parent::*//*[@id='fl_options_menu_button']", name.toUpperCase());

    public static final By idOptionsButton = By.id("gtv__profile__settings_button");

    public TabletSelfProfilePage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public boolean isNameVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrSelfNameByContent.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void tapOptionsButton() throws Exception {
        getElement(idOptionsButton).click();
    }

    public void selectOptionsMenuItem(String itemName) throws Exception {
        final By locator = By.xpath(xpathStrOptionsMenuItemByName.apply(itemName));
        getElement(locator, String.format("The item '%s' is not present in Options menu", itemName)).click();
    }

    public void tapSelfNameField() throws Exception {
        getElement(idSelfNameInput, "Self name input is not visible").click();
    }

    public void changeSelfNameTo(String newName) throws Exception {
        final WebElement selfNameInput = getElement(idSelfNameInput);
        selfNameInput.clear();
        selfNameInput.sendKeys(newName);
        this.getDriver().tapSendButton();
    }

    public BufferedImage getScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idSelfProfileView)).orElseThrow(IllegalStateException::new);
    }

    public void tapInTheCenter() throws Exception {
        DriverUtils.tapInTheCenterOfTheElement(this.getDriver(), getElement(idSelfProfileView));
    }

    public boolean waitUntilOptionsMenuItemVisible(String itemName) throws Exception {
        final By locator = By.xpath(xpathStrOptionsMenuItemByName.apply(itemName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }
}
