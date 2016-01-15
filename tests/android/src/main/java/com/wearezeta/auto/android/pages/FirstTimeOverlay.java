package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.Future;

public class FirstTimeOverlay extends AndroidPage {

    public static final String idGotItButton = "zb__first_launch__confirm";
    @FindBy(id = idGotItButton)
    private WebElement gotItButton;

    public FirstTimeOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idGotItButton));
    }

    public boolean isVisible(int timeoutSeconds) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idGotItButton), timeoutSeconds);
    }


    public boolean isInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.id(idGotItButton));
    }

    public void tapGotItButton() {
        gotItButton.click();
    }
}
