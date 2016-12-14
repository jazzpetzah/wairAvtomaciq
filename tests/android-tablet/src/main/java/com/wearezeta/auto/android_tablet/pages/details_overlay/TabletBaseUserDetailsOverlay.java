package com.wearezeta.auto.android_tablet.pages.details_overlay;

import com.wearezeta.auto.android.pages.details_overlay.BaseUserDetailsOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public abstract class TabletBaseUserDetailsOverlay<T extends BaseUserDetailsOverlay> extends TabletBaseDetailsOverlay<T> {
    public TabletBaseUserDetailsOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilUserDataVisible(String type, String nameAlias) throws Exception {
        return getDedicatePage().waitUntilUserDataVisible(type, nameAlias);
    }

    public boolean waitUntilUserDataInvisible(String type, String nameAlias) throws Exception {
        return getDedicatePage().waitUntilUserDataInvisible(type, nameAlias);
    }

    public boolean waitUntilUserDataVisible(String type) throws Exception {
        return getDedicatePage().waitUntilUserDataVisible(type);
    }

    public boolean waitUntilUserDataInvisible(String type) throws Exception {
        return getDedicatePage().waitUntilUserDataInvisible(type);
    }

    public boolean waitUntilAvatarVisible() throws Exception {
        return getDedicatePage().waitUntilAvatarVisible();
    }
}
