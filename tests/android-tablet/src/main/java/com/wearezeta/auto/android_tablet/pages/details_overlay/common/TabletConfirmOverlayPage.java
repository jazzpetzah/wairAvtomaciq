package com.wearezeta.auto.android_tablet.pages.details_overlay.common;

import com.wearezeta.auto.android.pages.details_overlay.common.ConfirmOverlayPage;
import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletConfirmOverlayPage extends AndroidTabletPage {

    public TabletConfirmOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapOnButton(String buttonLabel) throws Exception {
        this.getDedicatePage().tapOnButton(buttonLabel);
    }

    public void tapOnCheckbox() throws Exception {
        this.getDedicatePage().tapOnCheckbox();
    }

    public boolean waitUntilCheckboxVisible() throws Exception {
        return this.getDedicatePage().waitUntilCheckboxVisible();
    }

    public boolean waitUntilCheckboxInvisible() throws Exception {
        return this.getDedicatePage().waitUntilCheckboxInvisible();
    }

    //TODO: replace it with function call
    public String getHeaderText() throws Exception {
        return this.getDedicatePage().getHeaderText();
    }

    private ConfirmOverlayPage getDedicatePage() throws Exception {
        return this.getAndroidPageInstance(ConfirmOverlayPage.class);
    }
}
