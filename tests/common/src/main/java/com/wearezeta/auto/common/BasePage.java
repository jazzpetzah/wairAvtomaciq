package com.wearezeta.auto.common;

import io.appium.java_client.AppiumDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
	
	protected static AppiumDriver driver = null;
	protected static WebDriverWait wait;
	
	protected void InitConnection(String URL, DesiredCapabilities capabilities) throws MalformedURLException {
		
		if(null == driver) {
			
			driver = new AppiumDriver(new URL(URL), capabilities);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			wait = new WebDriverWait(driver, 10);
		}
		PageFactory.initElements(driver, this);
	}

	public void Close() throws IOException {
		
		if(driver != null) {
			driver.quit();
			driver = null;
		}
	}

}
