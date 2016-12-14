package com.wearezeta.auto.android_tablet.pages.details_overlay;


import com.wearezeta.auto.android.pages.details_overlay.BaseDetailsOverlay;
import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Future;

public abstract class TabletBaseDetailsOverlay<T extends BaseDetailsOverlay> extends AndroidTabletPage {
    private static final By idPopoverRootLocator = By.id("fl__participant_dialog__main__container");

    public TabletBaseDetailsOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapButton(String name) throws Exception {
        getDedicatePage().tapButton(name);
    }

    public boolean waitUntilButtonVisible(String name) throws Exception {
        return getDedicatePage().waitUntilButtonVisible(name);
    }

    public boolean waitUntilButtonInvisible(String name) throws Exception {
        return getDedicatePage().waitUntilButtonInvisible(name);
    }

    protected boolean waitUntilPopoverVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), getPopoverRootLocator());
    }

    protected boolean waitUntilPopoverInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), getPopoverRootLocator());
    }

    protected void tapInTheCenterOfPopover() throws Exception {
        final WebElement element = getDriver().findElement(getPopoverRootLocator());
        DriverUtils.tapInTheCenterOfTheElement(getDriver(), element);
    }

    protected void tapOutsideOfPopover() throws Exception {
        final WebElement element = this.getDriver().findElement(getPopoverRootLocator());
        DriverUtils.tapOutsideOfTheElement(getDriver(), element, 30, -30);
    }

    protected void doShortSwipeDownOnPopover() throws Exception {
        final WebElement self = this.getDriver().findElement(getPopoverRootLocator());
        final Point coords = self.getLocation();
        final Dimension elementSize = self.getSize();
        final int xOffset = elementSize.width / 2;
        final int yOffset = (int) Math.round(elementSize.height * 0.15);
        this.getDriver().swipe(coords.x + xOffset,
                coords.y + elementSize.height / 20, coords.x + xOffset,
                coords.y + elementSize.height / 20 + yOffset, 500);
    }

    protected By getPopoverRootLocator() {
        return idPopoverRootLocator;
    }
    //endregion

    protected abstract T getDedicatePage() throws Exception;
}
