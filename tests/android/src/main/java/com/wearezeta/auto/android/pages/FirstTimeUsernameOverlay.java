package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.DummyElement;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class FirstTimeUsernameOverlay extends AndroidPage {

    public static final By xpathKeepThisOneButton = By.xpath("//*[@value='Usernames are here.']/../" +
            "*[@id='zb__username_first_assign__keep']");

    public static final By xpathChooseYourOwnButton = By.xpath("//*[@value='Usernames are here.']/../" +
            "*[@id='zb__username_first_assign__choose']");

    public static final Function<String, By> xpathOfferedUsername = (username) -> By.xpath(String.format
            ("//*[@id='ttv__username' and value='%s'", username));

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

    public void tapKeepThisOneIfVisible(int timeoutSeconds) throws Exception {
        getElementIfDisplayed(xpathKeepThisOneButton, timeoutSeconds).orElseGet(DummyElement::new).click();
    }

    public void tapChooseYourOwnButton() throws Exception {
        getElement(xpathChooseYourOwnButton).click();
    }

    public boolean isOfferedUsernameVisible(String username) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOfferedUsername.apply(username));
    }

    public boolean isOfferedUsernameInvisible(String username) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathOfferedUsername.apply(username));
    }
}
