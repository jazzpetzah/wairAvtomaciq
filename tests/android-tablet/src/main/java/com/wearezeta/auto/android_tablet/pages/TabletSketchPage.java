package com.wearezeta.auto.android_tablet.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.SketchPage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletSketchPage extends AndroidTabletPage {
	public TabletSketchPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private SketchPage getAndroidSketchPage() throws Exception {
		return this.getAndroidPageInstance(SketchPage.class);
	}

	public void setColor(int colorIdx) throws Exception {
		getAndroidSketchPage().setColor(colorIdx);
	}

	public void drawRandomLines(int count) throws Exception {
		getAndroidSketchPage().drawRandomLines(count);
	}

	public Optional<BufferedImage> getCanvasScreenshot() throws Exception {
		return getAndroidSketchPage().getCanvasScreenshot();
	}

	public void tapSendButton() throws Exception {
		getAndroidSketchPage().tapSendButton();
		ScreenOrientationHelper.getInstance().fixOrientation(getDriver());
	}
}
