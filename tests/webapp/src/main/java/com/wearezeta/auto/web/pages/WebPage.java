package com.wearezeta.auto.web.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

public class WebPage extends BasePage {
	@Override
	public ZetaWebAppDriver getDriver() {
		return (ZetaWebAppDriver) this.driver;
	}

	private String url = null;

	public String getUrl() {
		return this.url;
	}

	public WebPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		this(driver, wait, null);
	}

	public WebPage(ZetaWebAppDriver driver, WebDriverWait wait, String url)
			throws Exception {
		super(driver, wait);

		this.url = url;
		if (url != null) {
			this.getDriver().navigate().to(url);
		}
	}

	@Override
	public void close() throws Exception {
		super.close();
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
