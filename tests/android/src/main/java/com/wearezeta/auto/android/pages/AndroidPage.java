package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.AndroidLocators;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.SwipeDirection;


public abstract class AndroidPage extends BasePage {
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameLoginPage)
	private WebElement content;
	
	public AndroidPage(String URL, String path) throws IOException {
		
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("app", path);
        capabilities.setCapability("appPackage", CommonUtils.getAndroidPackageFromConfig(AndroidPage.class));
        capabilities.setCapability("appActivity", CommonUtils.getAndroidActivityFromConfig(AndroidPage.class));
        capabilities.setCapability("appWaitActivity", CommonUtils.getAndroidActivityFromConfig(AndroidPage.class));
        super.InitConnection(URL, capabilities);
	}

	@Override
	public void Close() throws IOException {
		//Runtime.getRuntime().exec("cmd /C adb shell am force-stop com.waz.zclient");
		super.Close();
	}
	
	public abstract AndroidPage returnBySwipe (SwipeDirection direction) throws IOException;
	
	@Override
	public AndroidPage swipeLeft(int time) throws IOException
	{
		DriverUtils.swipeLeft(driver, content, time);
		return returnBySwipe(SwipeDirection.LEFT);
	}
	
	@Override
	public AndroidPage swipeRight(int time) throws IOException
	{
		DriverUtils.swipeRight(driver, content, time);
		return returnBySwipe(SwipeDirection.RIGHT);
	}
	
	@Override
	public AndroidPage swipeUp(int time) throws IOException
	{
		DriverUtils.swipeUp(driver, content, time);
		return returnBySwipe(SwipeDirection.UP);
	}
	
	@Override
	public AndroidPage swipeDown(int time) throws IOException
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
		PagesCollection.personalInfoPaga = null;
		PagesCollection.peoplePickerPage = null;
		PagesCollection.connectToPage = null;
		PagesCollection.otherUserPersonalInfoPage = null;
		PagesCollection.groupChatPage = null;
		PagesCollection.registrationPage = null;
		PagesCollection.groupChatInfoPage = null;
	}
}
