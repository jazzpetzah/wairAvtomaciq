package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.*;


public abstract class AndroidPage extends BasePage {
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameLoginPage)
	private WebElement content;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classListView)
	private WebElement container;
	
	public AndroidPage(String URL, String path) throws IOException {
		
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", CommonUtils.getAndroidDeviceNameFromConfig(AndroidPage.class));
        capabilities.setCapability("app", path);
        capabilities.setCapability("appPackage", CommonUtils.getAndroidPackageFromConfig(AndroidPage.class));
        capabilities.setCapability("appActivity", CommonUtils.getAndroidActivityFromConfig(AndroidPage.class));
        capabilities.setCapability("appWaitActivity", CommonUtils.getAndroidActivityFromConfig(AndroidPage.class));
        //capabilities.setCapability("unicodeKeyboard", true);
        capabilities.setCapability("resetKeyboard", true);
        super.InitConnection(URL, capabilities);
	}
	
	public void refreshUITree() {
		driver.getPageSource();
	}
	
	public void navigateBack(){
		driver.navigate().back();
	}
	@Override
	public void Close() throws IOException {
		try {
			CommonUtils.killAndroidClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public void dialogsPagesSwipeUp(int time){
		Point coords = container.getLocation();
		 Dimension elementSize = container.getSize();
		 driver.swipe(coords.x+elementSize.width / 2, coords.y + elementSize.height - 300, coords.x + elementSize.width / 2, coords.y, time);
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
		PagesCollection.personalInfoPage = null;
		PagesCollection.peoplePickerPage = null;
		PagesCollection.connectToPage = null;
		PagesCollection.otherUserPersonalInfoPage = null;
		PagesCollection.groupChatPage = null;
		PagesCollection.registrationPage = null;
		PagesCollection.groupChatInfoPage = null;
	}
}
