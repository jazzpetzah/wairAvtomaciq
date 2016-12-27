package com.wearezeta.auto.android_tablet.pages.details_overlay;

import com.wearezeta.auto.android.pages.details_overlay.ConversationOptionsMenuPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletConversationOptionsMenuPage extends TabletBaseUserDetailsOverlay<ConversationOptionsMenuPage>
        implements ISupportsPopoverRelatedActions {

    public TabletConversationOptionsMenuPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    public void tapConversationOptionsMenuItem(String itemName) throws Exception {
        this.getDedicatePage().tapConversationOptionsMenuItem(itemName);
    }

    public boolean waitUntilConversationOptionsMenuItemVisible(String itemName) throws Exception {
        return this.getDedicatePage().waitUntilConversationOptionsMenuItemVisible(itemName);
    }

    public boolean waitUntilConversationOptionsMenuItemInvisible(String itemName) throws Exception {
        return this.getDedicatePage().waitUntilConversationOptionsMenuItemInvisible(itemName);
    }

    public boolean waitUntilOptionMenuVisible() throws Exception {
        return this.getDedicatePage().waitUntilOptionMenuVisible();
    }

    public boolean waitUntilOptionMenuInvisible() throws Exception {
        return this.getDedicatePage().waitUntilOptionMenuInvisible();
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
    protected ConversationOptionsMenuPage getDedicatePage() throws Exception {
        return this.getAndroidPageInstance(ConversationOptionsMenuPage.class);
    }
}
