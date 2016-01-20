package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class CallingLockscreenPage extends AndroidPage {

    private static final By idLockScreenLogo = By.id("gtv__notifications__incoming_call__lockscreen__logo");

    private static final Function<String, String> xpathStrCallingUserByName = name ->
            String.format("//*[@id='ttv__notifications__incoming_call__lockscreen__header' and @value='%s']", name);

    public static final By idMainContent = By.id("fl_main_content");

    public CallingLockscreenPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idLockScreenLogo);
    }

    public boolean waitUntilCallerNameExists(String expectedName) throws Exception {
        final By locator = By.xpath(xpathStrCallingUserByName.apply(expectedName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void acceptCall() throws Exception {
        DriverUtils.swipeElementPointToPoint(getDriver(), getElement(idMainContent), 1500, 50, 90, 80, 90);
    }

}
