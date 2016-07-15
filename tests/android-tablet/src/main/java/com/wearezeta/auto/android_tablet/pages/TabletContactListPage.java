package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletContactListPage extends AbstractTabletPeoplePickerPage {

    public TabletContactListPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    private ContactListPage getContactListPage() throws Exception {
        return this.getAndroidPageInstance(ContactListPage.class);
    }

    public void tapOnTopPeople(String name) throws Exception {
        getContactListPage().tapOnTopPeople(name);
    }

    @Override
    protected Class getAndroidPageClass() {
        return ContactListPage.class;
    }
}
