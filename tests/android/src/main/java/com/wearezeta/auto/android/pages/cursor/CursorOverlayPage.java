package com.wearezeta.auto.android.pages.cursor;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class CursorOverlayPage extends AndroidPage {

    public CursorOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    protected static final By idExtendedCursorContainer = By.id("ecc__conversation");
}
