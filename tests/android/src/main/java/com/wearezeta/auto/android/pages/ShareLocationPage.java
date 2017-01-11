package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.DummyElement;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.misc.Timedelta;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class ShareLocationPage extends AndroidPage {
    private static final By idSendButton = By.id("ttv__location_send_button");
    private static final long WAIT_UNTIL_INTERVAL_MILLISECONDS = 200;

    public ShareLocationPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private By getButtonLocator(String name) {
        switch (name.toLowerCase()) {
            case "send":
                return idSendButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        getElement(getButtonLocator(name)).click();
    }

    public void retryTapButtonUntilInvisible(String btnName, int timeoutSeconds) throws Exception {
        boolean tapButtonComplete = CommonUtils.waitUntilTrue(
                timeoutSeconds,
                WAIT_UNTIL_INTERVAL_MILLISECONDS,
                () -> {
                    getElementIfDisplayed(getButtonLocator(btnName),
                            Timedelta.fromSeconds(1)).orElseGet(DummyElement::new).click();
                    return DriverUtils.waitUntilLocatorDissapears(getDriver(), getButtonLocator(btnName), 2);
                }
        );

        if (!tapButtonComplete) {
            throw new IllegalStateException(
                    String.format("Wait for tapping on button '%s' timeout in %d seconds", btnName, timeoutSeconds));
        }
    }
}
