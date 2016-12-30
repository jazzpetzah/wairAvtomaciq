package com.wearezeta.auto.android_tablet.pages.details_overlay.group;

import com.wearezeta.auto.android.pages.details_overlay.group.GroupInfoPage;
import com.wearezeta.auto.android_tablet.pages.details_overlay.ISupportsPopoverRelatedActions;
import com.wearezeta.auto.android_tablet.pages.details_overlay.TabletBaseDetailsOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletGroupInfoPopover extends TabletBaseDetailsOverlay<GroupInfoPage> implements ISupportsPopoverRelatedActions{
    public TabletGroupInfoPopover(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilParticipantVisible(String name) throws Exception {
        return this.getDedicatePage().waitUntilParticipantVisible(name);
    }

    public boolean waitUntilParticipantInvisible(String name) throws Exception {
        return this.getDedicatePage().waitUntilParticipantInvisible(name);
    }

    public void tapOnParticipantsHeader() throws Exception {
        this.getDedicatePage().tapOnParticipantsHeader();
    }

    public void renameGroupChat(String chatName) throws Exception {
        this.getDedicatePage().renameGroupChat(chatName);
    }

    public void tapOnParticipant(String name) throws Exception {
        this.getDedicatePage().tapOnParticipant(name);
    }

    public boolean waitUntilSubHeaderVisible(String subHeaderText) throws Exception {
        return this.getDedicatePage().waitUntilSubHeaderVisible(subHeaderText);
    }

    public String getConversationName() throws Exception {
        return this.getDedicatePage().getConversationName();
    }

    public boolean waitUntilParticipantAvatarVisible(String name) throws Exception {
        return this.getDedicatePage().waitUntilParticipantAvatarVisible(name);
    }

    public boolean waitUntilVerifiedParticipantAvatarVisible(String name) throws Exception {
        return this.getDedicatePage().waitUntilVerifiedParticipantAvatarVisible(name);
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
    protected GroupInfoPage getDedicatePage() throws Exception {
        return this.getAndroidPageInstance(GroupInfoPage.class);
    }
}
