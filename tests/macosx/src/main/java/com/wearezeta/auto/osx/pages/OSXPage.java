package com.wearezeta.auto.osx.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.common.OSXExecutionContext;

public abstract class OSXPage extends BasePage {
	private String path = null;

	@Override
	protected ZetaOSXDriver getDriver() throws Exception {
		return (ZetaOSXDriver) super.getDriver();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Future<ZetaOSXDriver> getLazyDriver() {
		return (Future<ZetaOSXDriver>) super.getLazyDriver();
	}

	public OSXPage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
		this(lazyDriver, null);
	}

	public OSXPage(Future<ZetaOSXDriver> lazyDriver, String path)
			throws Exception {
		super(lazyDriver);
		this.path = path;
	}

	public void navigateTo() throws Exception {
		if (this.path == null) {
			throw new RuntimeException(String.format(
					"The page %s does not support direct navigation", this
							.getClass().getName()));
		}
		this.getDriver().navigate().to(this.path);
	}

	@Override
	public void close() throws Exception {
		super.close();
	}

	public BufferedImage takeScreenshot() throws Exception {
		return DriverUtils.takeScreenshot(this.getDriver());
	}

	public void startApp() throws Exception {
		this.getDriver().navigate().to(OSXExecutionContext.wirePath);
	}

	public static void clearPagesCollection() throws IllegalArgumentException,
			IllegalAccessException {
		clearPagesCollection(PagesCollection.class, OSXPage.class);
	}

	// not used in OS X
	@Override
	public BasePage swipeLeft(int time) throws IOException {
		return null;
	}

	@Override
	public BasePage swipeRight(int time) throws IOException {
		return null;
	}

	@Override
	public BasePage swipeUp(int time) throws IOException {
		return null;
	}

	@Override
	public BasePage swipeDown(int time) throws IOException {
		return null;
	}
}
