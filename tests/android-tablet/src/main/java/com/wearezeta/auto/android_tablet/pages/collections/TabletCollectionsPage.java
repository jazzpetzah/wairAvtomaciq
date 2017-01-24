package com.wearezeta.auto.android_tablet.pages.collections;


import com.wearezeta.auto.android.pages.collections.CollectionsPage;
import com.wearezeta.auto.android.pages.collections.ISupportsCollectionTopToolbar;
import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.misc.Timedelta;

import java.util.concurrent.Future;

public class TabletCollectionsPage extends AndroidTabletPage implements ISupportsCollectionTopToolbar {

    public TabletCollectionsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilItemIsVisible(String category) throws Exception {
        return getCollectionsPage().waitUntilItemIsVisible(category);
    }

    public boolean waitUntilItemIsInvisible(String category) throws Exception {
        return getCollectionsPage().waitUntilItemIsInvisible(category);
    }

    public void tapOnItemByNumber(String tapType, int number, String itemType) throws Exception {
        getCollectionsPage().tapOnItemByNumber(tapType, number, itemType);
    }

    public void tapShowAll(String categoryName) throws Exception {
        getCollectionsPage().tapShowAll(categoryName);
    }

    public boolean waitUntilCountOfItemsByCategory(String category, int expectCount, Timedelta timeout) throws Exception {
        return getCollectionsPage().waitUntilCountOfItemsByCategory(category, expectCount, timeout);
    }

    public void tapOnButton(String buttonName) throws Exception {
        getCollectionsPage().tapOnButton(buttonName);
    }

    @Override
    public boolean waitUntilTopToolbarItemVisible(String itemType, String text) throws Exception {
        return getCollectionsPage().waitUntilTopToolbarItemVisible(itemType, text);
    }


    private CollectionsPage getCollectionsPage() throws Exception {
        return this.getAndroidPageInstance(CollectionsPage.class);
    }
}
