package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.DummyElement;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.misc.Timedelta;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class FirstTimeUsernameOverlay extends AndroidPage {

    private static final By xpathKeepThisOneButton = By.xpath("//*[@value='Usernames are here.']/../" +
            "*[@id='zb__username_first_assign__keep']");

    private static final By xpathChooseYourOwnButton = By.xpath("//*[@value='Usernames are here.']/../" +
            "*[@id='zb__username_first_assign__choose']");

    private static final By xpathOfferedUsername = By.xpath("//*[@id='ttv__username' and string-length(@value) > 1]");

    public FirstTimeUsernameOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathChooseYourOwnButton);
    }

    public boolean isVisible(int timeoutSeconds) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathChooseYourOwnButton, timeoutSeconds);
    }

    public boolean isInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathChooseYourOwnButton);
    }

    public void tapKeepThisOneButton() throws Exception {
        getElement(xpathKeepThisOneButton).click();
    }

    public void tapKeepThisOneIfVisible(Timedelta timeout) throws Exception {
        getElementIfDisplayed(xpathKeepThisOneButton, timeout).orElseGet(DummyElement::new).click();
    }

    public void tapChooseYourOwnButton() throws Exception {
        getElement(xpathChooseYourOwnButton).click();
    }

    public boolean isOfferedUsernameVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOfferedUsername);
    }

    public boolean isOfferedUsernameInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathOfferedUsername);
    }
}
