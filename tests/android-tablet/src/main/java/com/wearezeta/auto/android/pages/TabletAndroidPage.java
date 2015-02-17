package com.wearezeta.auto.android.pages;

import java.io.IOException;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public abstract class TabletAndroidPage extends AndroidPage {
	protected static ZetaAndroidDriver driver;
	protected static WebDriverWait wait;
	private DesiredCapabilities capabilities = new DesiredCapabilities();
	private String url;
	
	public TabletAndroidPage(String URL, String path) throws Exception {
		super(URL, path, false);
		this.url = URL;
	}
	
	public TabletAndroidPage(String URL, String path, boolean isUnicode) throws Exception {
		super(URL, path, isUnicode);
        this.url = URL;
        
        capabilities.setCapability("platformName", CommonUtils.PLATFORM_NAME_ANDROID);
        capabilities.setCapability("deviceName", CommonUtils.getAndroidDeviceNameFromConfig(TabletAndroidPage.class));
        capabilities.setCapability("app", path);
        capabilities.setCapability("appPackage", CommonUtils.getAndroidPackageFromConfig(TabletAndroidPage.class));
        capabilities.setCapability("appActivity", CommonUtils.getAndroidActivityFromConfig(TabletAndroidPage.class));
        capabilities.setCapability("appWaitActivity", CommonUtils.getAndroidActivityFromConfig(TabletAndroidPage.class));
        
        if (isUnicode) {
        	initUnicodeDriver();
        }
        else {
        	initNoneUnicodeDriver();
        }
	}
	
	private void initUnicodeDriver() throws Exception
	{
		capabilities.setCapability("unicodeKeyboard", true);
        capabilities.setCapability("resetKeyboard", true);
        super.InitConnection(url, capabilities);

        storeDriverAndWait();
	}
	
	private void initNoneUnicodeDriver() throws IOException
	{
        super.InitConnection(url, capabilities);
        
        storeDriverAndWait();
	}
	
	private void storeDriverAndWait() {
        driver = (ZetaAndroidDriver) drivers.get(CommonUtils.PLATFORM_NAME_ANDROID);
        wait = waits.get(CommonUtils.PLATFORM_NAME_ANDROID);
	}
	
	public static void clearTabletPagesCollection() throws IllegalArgumentException, IllegalAccessException {
		clearPagesCollection(TabletPagesCollection.class, AndroidPage.class);
	}
}
