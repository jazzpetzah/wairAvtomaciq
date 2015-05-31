package com.wearezeta.auto.web.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Future;

import org.openqa.selenium.Alert;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

public class WebPage extends BasePage {
	@Override
	protected ZetaWebAppDriver getDriver() throws Exception {
		return (ZetaWebAppDriver) super.getDriver();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Future<ZetaWebAppDriver> getLazyDriver() {
		return (Future<ZetaWebAppDriver>) super.getLazyDriver();
	}

	private String url = null;

	public String getUrl() {
		return this.url;
	}

	protected void setUrl(String url) {
		this.url = url;
	}

	public WebPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
		this(lazyDriver, null);
	}

	public WebPage(Future<ZetaWebAppDriver> lazyDriver, String url)
			throws Exception {
		super(lazyDriver);

		this.url = url;
	}

	public void navigateTo() throws Exception {
		if (this.url != null) {
			this.getDriver().navigate().to(this.url);
		} else {
			throw new RuntimeException(
					String.format(
							"'%s' page does not support direct navigation and can be loaded from other pages only",
							this.getClass().getSimpleName()));
		}
	}

	public Optional<BufferedImage> takeScreenshot() throws Exception {
		return DriverUtils.takeFullScreenShot(this.getDriver());
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

	public void acceptAlert() throws Exception {
		Alert popup = getDriver().switchTo().alert();
		popup.accept();
	}
}
