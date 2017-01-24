package com.wearezeta.auto.android_tablet.pages.collections;


import com.wearezeta.auto.android.pages.collections.CollectionImagePreviewPage;
import com.wearezeta.auto.android.pages.collections.ISupportsCollectionTopToolbar;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletCollectionImagePreviewPage extends TabletCollectionsPage implements ISupportsCollectionTopToolbar {
    public TabletCollectionImagePreviewPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return getCollectionImagePreviewPage().waitUntilVisible();
    }

    public boolean waitUntilInvisible() throws Exception {
        return getCollectionImagePreviewPage().waitUntilInvisible();
    }

    public void tapOnButton(String buttonName) throws Exception {
        getCollectionImagePreviewPage().tapOnButton(buttonName);
    }

    private CollectionImagePreviewPage getCollectionImagePreviewPage() throws Exception {
        return this.getAndroidPageInstance(CollectionImagePreviewPage.class);
    }
}
