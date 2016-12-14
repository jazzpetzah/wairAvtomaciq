package com.wearezeta.auto.android_tablet.pages;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.CommonUtils;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.WebElement;

public class TabletWelcomePage extends AndroidTabletPage {
    private final static int SIGN_IN_TIMEOUT_IN_SECONDS = 10;
    private final static long SIGN_IN_INTERVAL_IN_MILLISECONDS = 1000L;

    private static final By idRegisterButton = By.id("zb__welcome__create_account");
    private static final By idHaveAccountButton = By.id("zb__welcome__sign_in");

    private final static Function<String, String> xpathLinkByText = text -> String.format("//*[@value='%s']", text);

    public TabletWelcomePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitForInitialScreen() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idHaveAccountButton, 30);
    }

    public void tapSignInButton() throws Exception {
        CommonUtils.waitUntilTrue(
                SIGN_IN_TIMEOUT_IN_SECONDS,
                SIGN_IN_INTERVAL_IN_MILLISECONDS,
                () -> {
                    Optional<WebElement> el = DriverUtils.getElementIfDisplayed(getDriver(), idHaveAccountButton);
                    if (el.isPresent()) {
                        el.get().click();
                        return false;
                    }
                    return true;
                }
        );
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
