package com.wearezeta.auto.android_tablet.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public abstract class AndroidTabletPage extends AndroidPage {

	public AndroidTabletPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	protected AndroidPage getAndroidPageInstance(
			Class<? extends AndroidPage> pageClass) throws Exception {
		return (AndroidPage) this.instantiatePage(pageClass);
	}

	@Override
	public AndroidTabletPage swipeLeft(int time) throws Exception {
		return null;
	}

	@Override
	public AndroidTabletPage swipeRight(int time) throws Exception {
		return null;
	}

	@Override
	public AndroidTabletPage swipeUp(int time) throws Exception {
		return null;
	}

	@Override
	public AndroidTabletPage swipeDown(int time) throws Exception {
		return null;
	}

	@Override
	public AndroidTabletPage returnBySwipe(SwipeDirection direction)
			throws Exception {
		return null;
	};

	@Override
	public AndroidTabletPage swipeRightCoordinates(int durationMilliseconds)
			throws Exception {
		return (AndroidTabletPage) super
				.swipeRightCoordinates(durationMilliseconds);
	}

	@Override
	public AndroidTabletPage swipeRightCoordinates(int durationMilliseconds,
			int horizontalPercent) throws Exception {
		return (AndroidTabletPage) super.swipeRightCoordinates(
				durationMilliseconds, horizontalPercent);
	}

	@Override
	public AndroidTabletPage swipeLeftCoordinates(int durationMilliseconds)
			throws Exception {
		return (AndroidTabletPage) super
				.swipeLeftCoordinates(durationMilliseconds);
	}

	@Override
	public AndroidTabletPage swipeLeftCoordinates(int durationMilliseconds,
			int horizontalPercent) throws Exception {
		return (AndroidTabletPage) super.swipeLeftCoordinates(
				durationMilliseconds, horizontalPercent);
	}

	@Override
	public AndroidTabletPage swipeUpCoordinates(int durationMilliseconds)
			throws Exception {
		return (AndroidTabletPage) super
				.swipeUpCoordinates(durationMilliseconds);
	}

	@Override
	public AndroidTabletPage swipeUpCoordinates(int durationMilliseconds,
			int verticalPercent) throws Exception {
		return (AndroidTabletPage) super.swipeUpCoordinates(
				durationMilliseconds, verticalPercent);
	}

	@Override
	public AndroidTabletPage swipeByCoordinates(int durationMilliseconds,
			int widthStartPercent, int hightStartPercent, int widthEndPercent,
			int hightEndPercent) throws Exception {
		return (AndroidTabletPage) super.swipeByCoordinates(
				durationMilliseconds, widthStartPercent, hightStartPercent,
				widthEndPercent, hightEndPercent);
	}

	@Override
	public AndroidTabletPage swipeDownCoordinates(int durationMilliseconds)
			throws Exception {
		return (AndroidTabletPage) super
				.swipeDownCoordinates(durationMilliseconds);
	}

	@Override
	public AndroidTabletPage swipeDownCoordinates(int durationMilliseconds,
			int verticalPercent) throws Exception {
		return (AndroidTabletPage) super.swipeDownCoordinates(
				durationMilliseconds, verticalPercent);
	}

	@Override
	public Optional<BufferedImage> getElementScreenshot(WebElement element)
			throws Exception {
		Point elementLocation = element.getLocation();
		Dimension elementSize = element.getSize();
		int x = elementLocation.x;
		int y = elementLocation.y;
		int w = elementSize.width;
		int h = elementSize.height;
		final Optional<BufferedImage> screenshot = takeScreenshot();
		if (screenshot.isPresent()) {
			if (this.getDriver().getOrientation() == ScreenOrientation.LANDSCAPE) {
				return Optional.of(screenshot.get().getSubimage(y, x, h, w));
			} else {
				return Optional.of(screenshot.get().getSubimage(x, y, w, h));
			}
		} else {
			return Optional.empty();
		}
	}
}
