package com.wearezeta.auto.common;

import io.appium.java_client.AppiumDriver;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
	
	protected static AppiumDriver driver = null;
	protected static WebDriverWait wait;
	
	protected void InitConnection(String URL, DesiredCapabilities capabilities) throws MalformedURLException {
		
		if(null == driver) {
			
			driver = new AppiumDriver(new URL(URL), capabilities);
			try {
				driver.manage().timeouts().implicitlyWait(Integer.parseInt(CommonUtils.getDriverTimeoutFromConfig(getClass())), TimeUnit.SECONDS);
			
				wait = new WebDriverWait(driver, Integer.parseInt(CommonUtils.getDriverTimeoutFromConfig(getClass())));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		PageFactory.initElements(driver, this);
	}

	public void Close() throws IOException {
		
		if(driver != null) {
			driver.quit();
			driver = null;
		}
	}
	
	public BufferedImage takeScreenshot() throws IOException{
		return DriverUtils.takeScreenshot(driver);
	}
	
	public abstract BasePage swipeLeft(int time) throws IOException;
	
	public abstract BasePage swipeRight(int time) throws IOException;
	
	public abstract BasePage swipeUp(int time) throws IOException;
	
	public abstract BasePage swipeDown(int time) throws IOException;

}
