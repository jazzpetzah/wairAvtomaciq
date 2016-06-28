package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class ShareLocationPage extends AndroidPage {
    private static final By idSendButton = By.id("ttv__location_send_button");

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
}
