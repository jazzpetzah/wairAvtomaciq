package com.wearezeta.auto.common.driver;

import java.net.URL;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;

public class ZetaAndroidDriver extends AndroidDriver implements ZetaDriver {

	private SessionHelper sessionHelper;

	public ZetaAndroidDriver(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);
		sessionHelper = new SessionHelper();
	}

	@Override
	public List<WebElement> findElements(By by) {
		return this.sessionHelper.wrappedFindElements(super::findElements, by);
	}

	@Override
	public WebElement findElement(By by) {
		return this.sessionHelper.wrappedFindElement(super::findElement, by);
	}

	private Void closeDriver() {
		super.close();
		return null;
	}
	
	@Override
	public void close() {
		this.sessionHelper.wrappedClose(this::closeDriver);
	}

	private Void quitDriver() {
		super.quit();
		return null;
	}
	
	@Override
	public void quit() {
		this.sessionHelper.wrappedQuit(this::quitDriver);
	}

	@Override
	public boolean isSessionLost() {
		return this.sessionHelper.isSessionLost();
	}
}
