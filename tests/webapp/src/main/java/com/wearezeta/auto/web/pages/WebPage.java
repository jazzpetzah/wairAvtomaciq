package com.wearezeta.auto.web.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Future;

import org.openqa.selenium.Alert;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

public class WebPage extends BasePage {
	@Override
	protected ZetaWebAppDriver getDriver() {
		return (ZetaWebAppDriver) this.getDriver();
	}

	private String url = null;

	public String getUrl() {
		return this.url;
	}

	public WebPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
		this(lazyDriver, null);
	}

	public WebPage(Future<ZetaWebAppDriver> lazyDriver, String url)
			throws Exception {
		super(lazyDriver);

		this.url = url;
	}

	public void navigateTo() {
		if (this.url != null) {
			this.getDriver().navigate().to(this.url);
		} else {
			throw new RuntimeException(
					String.format(
							"'%s' page does not support direct navigation and can be loaded from other pages only",
							this.getClass().getSimpleName()));
		}
	}

	public BufferedImage takeScreenshot() throws IOException {
		return DriverUtils.takeScreenshot(this.getDriver());
	}

	public static void clearPagesCollection() throws IllegalArgumentException,
			IllegalAccessException {
		clearPagesCollection(PagesCollection.class, WebPage.class);
	}

	// not used in WebApp
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

	public void acceptAlert() {
		Alert popup = getDriver().switchTo().alert();
		popup.accept();
	}
}
