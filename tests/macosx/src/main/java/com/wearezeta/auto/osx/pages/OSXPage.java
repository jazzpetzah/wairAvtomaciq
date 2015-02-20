package com.wearezeta.auto.osx.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;

public abstract class OSXPage extends BasePage {
	@Override
	public ZetaOSXDriver getDriver() {
		return (ZetaOSXDriver) this.driver;
	}

	public OSXPage(ZetaOSXDriver driver, WebDriverWait wait) throws Exception {
		this(driver, wait, null);
	}

	public OSXPage(ZetaOSXDriver driver, WebDriverWait wait, String path)
			throws Exception {
		super(driver, wait);
		if (path != null) {
			driver.navigate().to(path);
		}
	}

	@Override
	public void close() throws Exception {
		super.close();
	}

	public BufferedImage takeScreenshot() throws IOException {
		return DriverUtils.takeScreenshot(this.getDriver());
	}

	public void startApp() throws Exception {
		driver.navigate().to(
				CommonUtils.getOsxApplicationPathFromConfig(OSXPage.class));
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
