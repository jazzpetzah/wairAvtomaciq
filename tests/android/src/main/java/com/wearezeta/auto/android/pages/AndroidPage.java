package com.wearezeta.auto.android.pages;

import java.io.IOException;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;

public class AndroidPage extends BasePage {
	
	public AndroidPage(String URL, String path) throws IOException {
		
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("app", path);
        capabilities.setCapability("app-package", CommonUtils.getAndroidPackageFromConfig(AndroidPage.class));
        capabilities.setCapability("app-activity", CommonUtils.getAndroidActivityFromConfig(AndroidPage.class));
        super.InitConnection(URL, capabilities);
	}

	@Override
	public void Close() throws IOException {
		Runtime.getRuntime().exec("cmd /C adb shell am force-stop com.waz.zclient");
		super.Close();
	}
}
