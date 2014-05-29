package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.wearezeta.auto.common.BasePage;

public class IOSPage extends BasePage {
	
	public IOSPage(String URL, String path) throws MalformedURLException {
		
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("app", path);
        super.InitConnection(URL, capabilities);
	}

	@Override
	public void Close() throws IOException {
		super.Close();
	}
}
