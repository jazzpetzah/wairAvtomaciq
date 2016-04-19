package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class GroupConvoCreationErrorOverlay extends AndroidPage {

    private static final String xpathStrHeader = "//*[@id='header' and @value='Unable to create']";

    public static final By xpathContinueButton = By.xpath(xpathStrHeader + "/following::*[@id='positive']");

    public GroupConvoCreationErrorOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathContinueButton);
    }

    public boolean isInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathContinueButton);
    }

    public void tapContinueButton() throws Exception {
        getElement(xpathContinueButton).click();
    }
}
