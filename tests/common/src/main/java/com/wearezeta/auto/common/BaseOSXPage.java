package com.wearezeta.auto.common;

import io.appium.java_client.AppiumDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;

public class BaseOSXPage {
	
	protected static AppiumDriver driver = null;
	
	protected void InitConnection(String URL, DesiredCapabilities capabilities) throws MalformedURLException {
		
		if(null == driver) {
			
			driver = new AppiumDriver(new URL(URL), capabilities);
//			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		PageFactory.initElements(driver, this);
	}

	public void Close() throws IOException {
		
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

}
