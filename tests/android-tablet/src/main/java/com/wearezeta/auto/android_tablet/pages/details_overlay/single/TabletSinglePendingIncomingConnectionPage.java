package com.wearezeta.auto.android_tablet.pages.details_overlay.single;


import com.wearezeta.auto.android.pages.details_overlay.single.SinglePendingIncomingConnectionPage;
import com.wearezeta.auto.android_tablet.pages.details_overlay.ISupportsPopoverRelatedActions;
import com.wearezeta.auto.android_tablet.pages.details_overlay.TabletBasePendingIncomingConnectionOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletSinglePendingIncomingConnectionPage extends TabletBasePendingIncomingConnectionOverlay implements ISupportsPopoverRelatedActions {

    public TabletSinglePendingIncomingConnectionPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    public void scrollToContact(String contactName) throws Exception {
        this.getDedicatePage().scrollToContact(contactName);
    }

    public void scrollToContact(String userName, final int maxUsersToScroll) throws Exception {
        this.getDedicatePage().scrollToContact(userName, maxUsersToScroll);
    }

    public void tapButton(String userName, String buttonName) throws Exception {
        this.getDedicatePage().tapButton(userName, buttonName);
    }

    @Override
    public boolean waitUntilPopoverVisible() throws Exception {
        return super.waitUntilPopoverVisible();
    }

    @Override
    public boolean waitUntilPopoverInvisible() throws Exception {
        return super.waitUntilPopoverInvisible();
    }

    @Override
    public void tapInTheCenterOfPopover() throws Exception {
        super.tapInTheCenterOfPopover();
    }

    @Override
    public void tapOutsideOfPopover() throws Exception {
        super.tapOutsideOfPopover();
    }

    @Override
    public void doShortSwipeDownOnPopover() throws Exception {
        super.doShortSwipeDownOnPopover();
    }

    @Override
    protected SinglePendingIncomingConnectionPage getDedicatePage() throws Exception {
        return this.getAndroidPageInstance(SinglePendingIncomingConnectionPage.class);
    }
}
