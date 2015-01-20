package com.wearezeta.auto.osx.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;

public class OSXPage extends BasePage {
	protected static ZetaOSXDriver driver;
	protected static WebDriverWait wait;
	
	public static String imagesPath = System.getProperty("user.home") + "/Documents/";
	
	public OSXPage(String URL, String path) throws IOException {
		this(URL, path, true);
	}
	
	public OSXPage(String URL, String path, boolean doNavigate) throws IOException {
		DesiredCapabilities capabilities = new DesiredCapabilities(); 
        capabilities.setCapability(CapabilityType.BROWSER_NAME, ""); 
        capabilities.setCapability(CapabilityType.PLATFORM, CommonUtils.PLATFORM_NAME_OSX); 
        capabilities.setCapability("platformName", CommonUtils.PLATFORM_NAME_OSX); 
        super.InitConnection(URL, capabilities);

        driver = (ZetaOSXDriver) drivers.get(CommonUtils.PLATFORM_NAME_OSX);
        wait = waits.get(CommonUtils.PLATFORM_NAME_OSX);
        
        if (doNavigate) {
        	driver.navigate().to(path);
        }
	}

	@Override
	public void Close() throws IOException {
		super.Close();
	}

	public BufferedImage takeScreenshot() throws IOException {
		return DriverUtils.takeScreenshot(driver);
	}
	
	public void startApp() throws IOException {
		driver.navigate().to(CommonUtils.getOsxApplicationPathFromConfig(OSXPage.class));
	}
	
	//not used in OS X
	@Override public BasePage swipeLeft(int time) throws IOException { return null; }
	@Override public BasePage swipeRight(int time) throws IOException { return null; }
	@Override public BasePage swipeUp(int time) throws IOException { return null; }
	@Override public BasePage swipeDown(int time) throws IOException { return null; }
}
