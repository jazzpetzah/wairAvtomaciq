package com.wearezeta.auto.common.driver;

import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;

import io.appium.java_client.ios.IOSDriver;

public class ZetaIOSDriver extends IOSDriver<WebElement> implements ZetaDriver {

	private SessionHelper sessionHelper;

	public ZetaIOSDriver(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);
		sessionHelper = new SessionHelper(this);
	}

	@Override
	public List<WebElement> findElements(By by) {
		return this.sessionHelper.wrappedFindElements(super::findElements, by);
	}

	@Override
	public WebElement findElement(By by) {
		return this.sessionHelper.wrappedFindElement(super::findElement, by);
	}

	@Override
	public void close() {
		this.sessionHelper.wrappedClose(super::close);
	}

	@Override
	public void quit() {
		this.sessionHelper.wrappedQuit(super::quit);
	}

	@Override
	public boolean isSessionLost() {
		return this.sessionHelper.isSessionLost();
	}
}
