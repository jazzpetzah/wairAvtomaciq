package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.SearchListPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletSearchListPage extends AbstractTabletSearchPage {

    public TabletSearchListPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapOnUserName(String name) throws Exception {
        getSearchListPage().tapOnUserName(name);
    }

    public void tapOnGroupName(String name) throws Exception {
        getSearchListPage().tapOnGroupName(name);
    }

    private SearchListPage getSearchListPage() throws Exception {
        return this.getAndroidPageInstance(SearchListPage.class);
    }

    @Override
    protected Class getAndroidPageClass() {
        return SearchListPage.class;
    }
}
