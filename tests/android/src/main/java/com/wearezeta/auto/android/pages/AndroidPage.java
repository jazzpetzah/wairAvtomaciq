package com.wearezeta.auto.android.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public abstract class AndroidPage extends BasePage {
	protected static ZetaAndroidDriver driver;
	protected static WebDriverWait wait;
	
	private DesiredCapabilities capabilities = new DesiredCapabilities();
	
	@AndroidFindBy(className = AndroidLocators.CommonLocators.classNameLoginPage)
	private WebElement content;
	
	@AndroidFindBy(className = AndroidLocators.CommonLocators.classListView)
	private WebElement container;
	
	@FindBy(xpath = AndroidLocators.CommonLocators.xpathImagesFrameLayout)
	private List<WebElement> frameLayouts;
	
	@FindBy(xpath = AndroidLocators.CommonLocators.xpathImage)
	private List<WebElement> image;
	
	private String url;
	
	public AndroidPage(String URL, String path) throws Exception {
		this(URL,path,false);
	}
	
	
	public AndroidPage(String URL, String path, boolean isUnicode) throws Exception {
		
        url = URL;
        
        capabilities.setCapability("platformName", CommonUtils.PLATFORM_NAME_ANDROID);
        capabilities.setCapability("deviceName", CommonUtils.getAndroidDeviceNameFromConfig(AndroidPage.class));
        capabilities.setCapability("app", path);
        capabilities.setCapability("appPackage", CommonUtils.getAndroidPackageFromConfig(AndroidPage.class));
        capabilities.setCapability("appActivity", CommonUtils.getAndroidActivityFromConfig(AndroidPage.class));
        capabilities.setCapability("appWaitActivity", CommonUtils.getAndroidActivityFromConfig(AndroidPage.class));
        
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
	
	public void selectPhoto(){
		refreshUITree();
		try{
			frameLayouts.get(0).click();
			return;
		}
		catch(Exception ex)
		{

		}
		try{
			image.get(0).click();
			return;
		}
		catch(Exception ex){
		}
	}
	
	public void hideKeyboard(){
		driver.hideKeyboard();
	}
	public AndroidPage navigateBack() throws Exception{
		driver.navigate().back();
		return null;
	}
	
	public void minimizeApplication () throws InterruptedException {

		driver.sendKeyEvent(3);
		Thread.sleep(1000);
	}
	
	public void restoreApplication() {
		
		try {
			driver.runAppInBackground(10);
			Thread.sleep(1000);
		}
		catch (WebDriverException ex) {
			//do nothing, sometimes after restoring the app we have this exception, Appium bug
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		Point coords = content.getLocation();
		Dimension elementSize = content.getSize();
		try{
			driver.swipe(coords.x+elementSize.width / 2, coords.y + elementSize.height - 300, coords.x + elementSize.width / 2, coords.y, time);
		}
		catch(Exception ex){

		}

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
	
	public static void clearPagesCollection() throws IllegalArgumentException, IllegalAccessException {
		clearPagesCollection(PagesCollection.class, AndroidPage.class);
	}
}
