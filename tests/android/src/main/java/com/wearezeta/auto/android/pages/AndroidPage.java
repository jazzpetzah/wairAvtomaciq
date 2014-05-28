package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.wearezeta.auto.common.BasePage;

public class AndroidPage extends BasePage {
	
	public AndroidPage(String URL, String path) throws MalformedURLException {
		
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("app", path);
        capabilities.setCapability("app-package", "com.waz.zclient");
        capabilities.setCapability("app-activity", ".StartupScreenActivity");
        super.InitConnection(URL, capabilities);
	}

	@Override
	public void Close() throws IOException {
		Runtime.getRuntime().exec("cmd /C adb shell am force-stop com.waz.zclient");
		super.Close();
	}
}
