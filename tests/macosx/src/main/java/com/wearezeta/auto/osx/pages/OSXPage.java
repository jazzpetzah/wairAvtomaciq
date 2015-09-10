package com.wearezeta.auto.osx.pages;

import java.io.IOException;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import java.util.concurrent.TimeUnit;

public abstract class OSXPage extends BasePage {

	private String path = null;
	private Future<ZetaWebAppDriver> secondaryDriver;

	@Override
	protected ZetaOSXDriver getDriver() throws Exception {
		return (ZetaOSXDriver) super.getDriver();
	}

	protected ZetaWebAppDriver getSecondaryDriver() throws Exception {
		return (ZetaWebAppDriver) secondaryDriver.get(
				ZetaDriver.INIT_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Future<ZetaOSXDriver> getLazyDriver() {
		return (Future<ZetaOSXDriver>) super.getLazyDriver();
	}

	@SuppressWarnings("unchecked")
	protected Future<ZetaWebAppDriver> getSecondaryLazyDriver() {
		return secondaryDriver;
	}

	public OSXPage(Future<ZetaOSXDriver> lazyDriver,
			Future<ZetaWebAppDriver> secondaryDriver) throws Exception {
		this(lazyDriver, secondaryDriver, null);
	}

	public OSXPage(Future<ZetaOSXDriver> lazyDriver,
			Future<ZetaWebAppDriver> secondaryDriver, String path)
			throws Exception {
		super(lazyDriver);
		this.path = path;
		this.secondaryDriver = secondaryDriver;
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
