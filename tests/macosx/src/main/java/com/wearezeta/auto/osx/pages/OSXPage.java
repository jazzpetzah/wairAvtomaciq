package com.wearezeta.auto.osx.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;

public class OSXPage extends BasePage {
	protected static ZetaOSXDriver driver;
	protected static WebDriverWait wait;

	public OSXPage(String URL, String path) throws Exception {
		this(URL, path, true);
	}

	public OSXPage(String URL, String path, boolean doNavigate)
			throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
		capabilities.setCapability(CapabilityType.PLATFORM, Platform.Mac
				.getName().toUpperCase());
		capabilities.setCapability("platformName", Platform.Mac.getName());
		super.InitConnection(URL, capabilities);

		driver = (ZetaOSXDriver) drivers.get(Platform.Mac);
		wait = waits.get(Platform.Mac);

		if (doNavigate) {
			driver.navigate().to(path);
		}
	}

	@Override
	public void close() throws Exception {
		super.close();
	}

	public BufferedImage takeScreenshot() throws IOException {
		return DriverUtils.takeScreenshot(driver);
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
