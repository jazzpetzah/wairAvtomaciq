package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class ExtendedCursorOverlayPage extends AndroidPage {

    public ExtendedCursorOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    protected static final By idExtendedCursorContainer = By.id("ecc__conversation");
}
