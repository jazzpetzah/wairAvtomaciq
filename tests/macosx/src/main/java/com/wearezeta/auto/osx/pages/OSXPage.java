package com.wearezeta.auto.osx.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.wearezeta.auto.common.BaseOSXPage;

public class OSXPage extends BaseOSXPage {
	
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
}
