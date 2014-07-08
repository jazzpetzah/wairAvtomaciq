package com.wearezeta.auto.osx.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.DriverUtils;

public class OSXPage extends BasePage {

	public static String imagesPath = System.getProperty("user.home") + "/Desktop/";
	
	public OSXPage(String URL, String path) throws MalformedURLException {
		DesiredCapabilities capabilities = new DesiredCapabilities(); 
        capabilities.setCapability(CapabilityType.BROWSER_NAME, ""); 
        capabilities.setCapability(CapabilityType.PLATFORM, "Mac"); 
        super.InitConnection(URL, capabilities);
        
        driver.navigate().to(path);
	}

	@Override
	public void Close() throws IOException {
		super.Close();
	}

	public BufferedImage takeScreenshot() throws IOException {
		return DriverUtils.takeScreenshot(driver);
	}
	
	//not used in OS X
	@Override public BasePage swipeLeft(int time) throws IOException { return null; }
	@Override public BasePage swipeRight(int time) throws IOException { return null; }
	@Override public BasePage swipeUp(int time) throws IOException { return null; }
	@Override public BasePage swipeDown(int time) throws IOException { return null; }
}
