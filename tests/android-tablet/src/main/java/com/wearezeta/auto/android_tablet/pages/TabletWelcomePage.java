package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletWelcomePage extends AndroidTabletPage {
    public final static By idRegisterButton = By.id("zb__welcome__create_account");

    public static final By idHaveAccountButton = By.id("zb__welcome__sign_in");

    private final static Function<String, String> xpathLinkByText = text -> String
            .format("//*[@value='%s']", text);

    public TabletWelcomePage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public boolean waitForInitialScreen() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                idHaveAccountButton, 30);
    }

    public void tapSignInButton() throws Exception {
        getElement(idHaveAccountButton, "LOG IN button is not clickable after timeout").click();
    }

    public void tapRegisterButton() throws Exception {
        getElement(idRegisterButton).click();
    }

    public boolean waitUntilLinkVisible(String linkText) throws Exception {
        final By locator = By.xpath(xpathLinkByText.apply(linkText));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void tapLink(String linkText) throws Exception {
        getElement(By.xpath(xpathLinkByText.apply(linkText))).click();
    }
}
