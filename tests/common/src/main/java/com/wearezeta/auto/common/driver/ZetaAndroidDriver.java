package com.wearezeta.auto.common.driver;

import java.net.URL;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;

public class ZetaAndroidDriver extends AndroidDriver implements ZetaDriver {

	private SessionHelpers wrappedDriver;

	public ZetaAndroidDriver(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);
		wrappedDriver = new SessionHelpers(this);
	}

	@Override
	public List<WebElement> findElements(By by) {
		return this.wrappedDriver.findElements(by);
	}

	@Override
	public WebElement findElement(By by) {
		return this.wrappedDriver.findElement(by);
	}

	@Override
	public void close() {
		this.wrappedDriver.close();
	}

	@Override
	public void quit() {
		this.wrappedDriver.quit();
	}

	@Override
	public boolean isSessionLost() {
		return this.wrappedDriver.isSessionLost();
	}

}
