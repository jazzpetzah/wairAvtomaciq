package com.wearezeta.auto.android.pages.details_overlay.common;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class ConfirmOverlayPage extends AndroidPage {
    private static final String strIdConfirm = "positive";
    private static final String strIdCancel = "negative";

    private static final By icRootOverlay = By.id("cm__confirm_action_light");
    private static final By idHeader = By.id("header");
    private static final By idCheckbox = By.id("gtv__checkbox_icon");

    private static final Function<String, String> xpathStrConfirmButtonByName = name -> String
            .format("//*[@id='%s' and @value='%s']", strIdConfirm, name);

    public ConfirmOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapOnButton(String buttonLabel) throws Exception {
        By locator = getButtonLocator(buttonLabel);
        getElement(locator).click();
    }

    public void tapOnCheckbox() throws Exception {
        getElement(idCheckbox).click();
    }

    public boolean waitUntilCheckboxVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCheckbox);
    }

    public boolean waitUntilCheckboxInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idCheckbox);
    }

    public boolean waitUntilPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), icRootOverlay);
    }

    public boolean waitUntilPageInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), icRootOverlay);
    }

    //TODO: replace it with function call
    public String getHeaderText() throws Exception {
        return getElement(idHeader, "Header is not present").getText();
    }

    protected By getConfirmButtonLocator(String name) {
        return By.xpath(xpathStrConfirmButtonByName.apply(name));
    }

    protected By getCancelButtonLocator() {
        return By.id(strIdCancel);
    }

    protected By getButtonLocator(String name) {
        switch (name.toLowerCase()) {
            case "cancel":
                return getCancelButtonLocator();
            case "delete":
            case "remove":
            case "leave":
            case "block":
                return getConfirmButtonLocator(name.toUpperCase());
            default:
                throw new IllegalArgumentException(String.format("Cannot find the locator for '%s'", name));
        }
    }
}
