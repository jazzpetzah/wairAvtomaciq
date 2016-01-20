package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class FirstTimeOverlay extends AndroidPage {

    public static final By idGotItButton = By.id("zb__first_launch__confirm");

    public FirstTimeOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idGotItButton);
    }

    public boolean isVisible(int timeoutSeconds) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idGotItButton, timeoutSeconds);
    }


    public boolean isInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idGotItButton);
    }

    public void tapGotItButton() throws Exception {
        getElement(idGotItButton).click();
    }
}
