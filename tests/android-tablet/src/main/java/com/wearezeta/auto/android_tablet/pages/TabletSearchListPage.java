package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.SearchListPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletSearchListPage extends AbstractTabletPeoplePickerPage {

    public TabletSearchListPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected Class getAndroidPageClass() {
        return SearchListPage.class;
    }
}
