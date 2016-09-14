package com.wearezeta.auto.android_tablet.pages;


import com.wearezeta.auto.android.pages.EditMessageOverlayPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletEditMessageOverlayPage extends AndroidTabletPage  {
    public TabletEditMessageOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private EditMessageOverlayPage getEditMessageOverlayPage() throws Exception {
        return this.getAndroidPageInstance(EditMessageOverlayPage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        return getEditMessageOverlayPage().waitUntilVisible();
    }

    public boolean waitUntilInvisible() throws Exception {
        return getEditMessageOverlayPage().waitUntilInvisible();
    }

    public void tapOnButton(String btnName) throws Exception {
        getEditMessageOverlayPage().tapOnButton(btnName);
    }
}
