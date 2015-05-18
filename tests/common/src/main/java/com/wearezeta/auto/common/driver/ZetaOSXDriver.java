package com.wearezeta.auto.common.driver;

import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class ZetaOSXDriver extends AppiumDriver implements
		ZetaDriver {

	private SessionHelpers wrappedDriver;

	public ZetaOSXDriver(URL remoteAddress, Capabilities desiredCapabilities) {
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

	public boolean isSessionLost() {
		return this.wrappedDriver.isSessionLost();
	}

	@Override
	public MobileElement scrollTo(String text) {
		throw new RuntimeException("Not implemented for OSX");
	}

	@Override
	public MobileElement scrollToExact(String text) {
		throw new RuntimeException("Not implemented for OSX");
	}
}
