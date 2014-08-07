package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;


public abstract class AndroidPage extends BasePage {
	
	private DesiredCapabilities capabilities = new DesiredCapabilities();
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameLoginPage)
	private WebElement content;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classListView)
	private WebElement container;
	
	private String url;
	
	public AndroidPage(String URL, String path) throws Exception {
		this(URL,path,false);
	}
	
	
	public AndroidPage(String URL, String path, boolean isUnicode) throws Exception {
		
        url = URL;
        
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", CommonUtils.getAndroidDeviceNameFromConfig(AndroidPage.class));
        capabilities.setCapability("app", path);
        capabilities.setCapability("appPackage", CommonUtils.getAndroidPackageFromConfig(AndroidPage.class));
        capabilities.setCapability("appActivity", CommonUtils.getAndroidActivityFromConfig(AndroidPage.class));
        capabilities.setCapability("appWaitActivity", CommonUtils.getAndroidActivityFromConfig(AndroidPage.class));
        
        if(isUnicode){
        	initUnicodeDriver();
        }
        else{
        	initNoneUnicodeDriver();
        }
	}
	
	private void initUnicodeDriver() throws Exception
	{
		capabilities.setCapability("unicodeKeyboard", true);
        capabilities.setCapability("resetKeyboard", true);
        super.InitConnection(url, capabilities);
	}
	
	private void initNoneUnicodeDriver() throws MalformedURLException
	{
        super.InitConnection(url, capabilities);
	}
	
	public void navigateBack(){
		driver.navigate().back();
	}
	@Override
	public void Close() throws IOException {
		try {
			AndroidCommonUtils.killAndroidClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.Close();
	}
	
	public abstract AndroidPage returnBySwipe (SwipeDirection direction) throws Exception;
	
	@Override
	public AndroidPage swipeLeft(int time) throws Exception
	{
		DriverUtils.swipeLeft(driver, content, time);
		return returnBySwipe(SwipeDirection.LEFT);
	}
	
	@Override
	public AndroidPage swipeRight(int time) throws Exception
	{
		DriverUtils.swipeRight(driver, content, time);
		return returnBySwipe(SwipeDirection.RIGHT);
	}
	
	@Override
	public AndroidPage swipeUp(int time) throws Exception
	{
		DriverUtils.swipeUp(driver, content, time);
		return returnBySwipe(SwipeDirection.UP);
	}
	
	public void dialogsPagesSwipeUp(int time){
		Point coords = container.getLocation();
		 Dimension elementSize = container.getSize();
		 driver.swipe(coords.x+elementSize.width / 2, coords.y + elementSize.height - 300, coords.x + elementSize.width / 2, coords.y, time);
	}
	
	@Override
	public AndroidPage swipeDown(int time) throws Exception
	{
		
		DriverUtils.swipeDown(driver, content, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}
	
	public void tapButtonByClassNameAndIndex(WebElement element, String className, int index)
	{
		List<WebElement> buttonsList = element.findElements(By.className(className));
		buttonsList.get(index).click();
	}
	
	public static void clearPagesCollection()
	{
		PagesCollection.androidPage = null;
		PagesCollection.contactListPage = null;
		PagesCollection.dialogPage = null;
		PagesCollection.instructionsPage = null;
		PagesCollection.loginPage = null;
		PagesCollection.personalInfoPage = null;
		PagesCollection.peoplePickerPage = null;
		PagesCollection.connectToPage = null;
		PagesCollection.otherUserPersonalInfoPage = null;
		PagesCollection.groupChatPage = null;
		PagesCollection.registrationPage = null;
		PagesCollection.groupChatInfoPage = null;
		PagesCollection.aboutPage = null;
	}
}
