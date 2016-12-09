package com.wearezeta.auto.android.pages.details_overlay.common;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class E2EEConfirmOverlayPage extends ConfirmOverlayPage {
    private static final By xpathTakeoverScreenText = By
            .xpath("//*[@id='text' and @value='Do you still want to send your message?']");

    public E2EEConfirmOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilE2EETextVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathTakeoverScreenText);
    }

    @Override
    protected By getButtonLocator(String name) {
        switch (name.toLowerCase()) {
            case "show people":
            case "show device":
                return super.getCancelButtonLocator();
            case "send anyway":
                return super.getConfirmButtonLocator("Send anyway");
        }
        return super.getButtonLocator(name);
    }
}
