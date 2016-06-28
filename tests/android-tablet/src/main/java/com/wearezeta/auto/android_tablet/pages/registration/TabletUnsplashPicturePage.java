package com.wearezeta.auto.android_tablet.pages.registration;

import com.wearezeta.auto.android.pages.RegistrationPage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.ScreenOrientation;

import java.util.concurrent.Future;

public class TabletUnsplashPicturePage extends AndroidTabletPage {
    public TabletUnsplashPicturePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private RegistrationPage getAndroidRegistrationPage() throws Exception {
        return this.getAndroidPageInstance(RegistrationPage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), RegistrationPage.idChooseMyOwnButton, 20);
    }

    public void tapChooseMyOwnButton() throws Exception {
        getAndroidRegistrationPage().tapOwnPictureButton();
    }

    public void selectPictureSource(String src) throws Exception {
        getDriver().rotate(ScreenOrientation.LANDSCAPE);
        Thread.sleep(ScreenOrientationHelper.ROTATION_DELAY_MS);
        getAndroidRegistrationPage().selectPictureSource(src);
    }
}
