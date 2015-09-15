package com.wearezeta.auto.osx.pages;

import java.io.IOException;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import java.util.concurrent.Future;

public abstract class OSXPage extends BasePage {

	private String path = null;
	private Future<ZetaWebAppDriver> secondaryDriver;

	@Override
	protected ZetaOSXDriver getDriver() throws Exception {
		return (ZetaOSXDriver) super.getDriver();
	}

	protected ZetaWebAppDriver getSecondaryDriver() throws Exception {
		return (ZetaWebAppDriver) secondaryDriver;
	}

	public OSXPage(Future<ZetaOSXDriver> osxDriver,
			Future<ZetaWebAppDriver> webDriver) throws Exception {
		this(osxDriver, webDriver, null);
	}

	public OSXPage(Future<ZetaOSXDriver> osxDriver) throws Exception {
		super(osxDriver);
	}

	public OSXPage(Future<ZetaOSXDriver> osxDriver,
			Future<ZetaWebAppDriver> webDriver, String path) throws Exception {
		super(osxDriver);
		this.path = path;
		this.secondaryDriver = webDriver;
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
		clearPagesCollection(OSXPagesCollection.class, OSXPage.class);
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
