package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class CallingLockscreenPage extends AndroidPage {

    private static final String idLockScreenLogo = "gtv__notifications__incoming_call__lockscreen__logo";
    @FindBy(id = idLockScreenLogo)
    private WebElement lockScreenLogo;

    private static final Function<String, String> xpathCallingUserByName = name ->
            String.format("//*[@id='ttv__notifications__incoming_call__lockscreen__header' and @value='%s']", name);

    private static final String idMainContent = "fl_main_content";
    @FindBy(id = idMainContent)
    private WebElement mainContentArea;

    public CallingLockscreenPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        final By locator = By.id(idLockScreenLogo);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilCallerNameExists(String expectedName) throws Exception {
        final By locator = By.xpath(xpathCallingUserByName.apply(expectedName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void acceptCall() throws Exception {
        DriverUtils.swipeElementPointToPoint(getDriver(), mainContentArea, 1500, 50, 90, 80, 90);
    }

}
