package com.wearezeta.auto.android_tablet.pages.details_overlay;

public interface ISupportsPopoverRelatedActions {
    boolean waitUntilPopoverVisible() throws Exception;

    boolean waitUntilPopoverInvisible() throws Exception;

    void tapInTheCenterOfPopover() throws Exception;

    void tapOutsideOfPopover() throws Exception;

    void doShortSwipeDownOnPopover() throws Exception;
}
