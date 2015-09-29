package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public abstract class AbstractPopoverContainer extends AndroidTabletPage {

	public AbstractPopoverContainer(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	protected abstract By getLocator();

	public boolean waitUntilVisible() throws Exception {
		if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), getLocator())) {
			final WebElement self = getDriver().findElement(getLocator());
			return self.getLocation().getX() >= 0;
		}
		return false;
	}

	public boolean waitUntilInvisible() throws Exception {
		if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), getLocator(),
				2)) {
			final WebElement self = getDriver().findElement(getLocator());
			return self.getLocation().getX() < 0;
		}
		return true;
	}

	public void tapInTheCenter() throws Exception {
		final WebElement self = this.getDriver().findElement(getLocator());
		DriverUtils.tapInTheCenterOfTheElement(getDriver(), self);
	}

	public void tapOutside() throws Exception {
		final WebElement self = this.getDriver().findElement(getLocator());
		DriverUtils.tapOutsideOfTheElement(getDriver(), self, 30, -30);
	}

	public void doShortSwipeDown() throws Exception {
		final WebElement self = this.getDriver().findElement(getLocator());
		final Point coords = self.getLocation();
		final Dimension elementSize = self.getSize();
		final int xOffset = elementSize.width / 2;
		final int yOffset = (int) Math.round(elementSize.height * 0.15);
		this.getDriver().swipe(coords.x + xOffset,
				coords.y + elementSize.height / 20, coords.x + xOffset,
				coords.y + elementSize.height / 20 + yOffset, 500);
	}
}
