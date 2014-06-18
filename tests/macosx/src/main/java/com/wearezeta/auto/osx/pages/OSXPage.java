package com.wearezeta.auto.osx.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.wearezeta.auto.common.BasePage;

public class OSXPage extends BasePage {
	
	public OSXPage(String URL, String path) throws MalformedURLException {
		DesiredCapabilities capabilities = new DesiredCapabilities(); 
        capabilities.setCapability(CapabilityType.BROWSER_NAME, ""); 
        capabilities.setCapability(CapabilityType.PLATFORM, "Mac"); 
        super.InitConnection(URL, capabilities);
        
        driver.navigate().to(path);
	}

	public void takeScreenshot() {
		 byte[] scrImage = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
		 //TODO: implement screenshot taking
	}
	
	@Override
	public void Close() throws IOException {
		super.Close();
	}

	//not used in OS X
	@Override public BasePage swipeLeft(int time) throws IOException { return null; }
	@Override public BasePage swipeRight(int time) throws IOException { return null; }
	@Override public BasePage swipeUp(int time) throws IOException { return null; }
	@Override public BasePage swipeDown(int time) throws IOException { return null; }
}
