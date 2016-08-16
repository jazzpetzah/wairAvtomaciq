package com.wearezeta.auto.android.pages;


import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class EditMessageOverlayPage extends AndroidPage {

    private static By idEditMessageCursorToolbar = By.id("emct__edit_message__toolbar");

    private static By idResetButton = By.id("gtv__edit_message__reset");

    private static By idApproveButton = By.id("gtv__edit_message__approve");

    private static By idCloseButton = By.id("gtv__edit_message__close");

    public EditMessageOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idEditMessageCursorToolbar);
    }

    public boolean waitUntilInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idEditMessageCursorToolbar);
    }

    public void tapOnButton(String btnName) throws Exception {
        By locator = getButtonLocatorByName(btnName);
        getElement(locator).click();
    }

    private By getButtonLocatorByName(String btnName) {
        switch (btnName.toLowerCase()) {
            case "reset":
                return idResetButton;
            case "approve":
                return idApproveButton;
            case "close":
                return idCloseButton;
            default:
                throw new IllegalStateException(
                        String.format("Do not support button '%s' in Edit message toolbar", btnName));
        }
    }


}
