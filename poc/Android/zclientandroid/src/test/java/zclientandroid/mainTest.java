package zclientandroid;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;

import io.appium.java_client.AppiumDriver;

public class mainTest
{

	 private static AppiumDriver driver;
	 private static String login = "maksym.kuvshynov@wearezeta.com";
	 private static String userName = "Maksym Kuvshynov";
	 private static String contactName = "Kirill";
	 private static String password ="25ef24ss";
	 @Before
	    public void setUp() throws Exception 
	    {
	        File app = new File("C:\\zclient-release-2006.apk");
	        DesiredCapabilities capabilities = new DesiredCapabilities();
	        capabilities.setCapability("automationName", "Selendroid");
	        capabilities.setCapability("platformName", "Android");
	        capabilities.setCapability("app", app.getAbsolutePath());
	        capabilities.setCapability("app-package", "com.waz.zclient");
	        capabilities.setCapability("app-activity", ".StartupScreenActivity");
	        driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    }
	 
	 @After
	 public void tearDown() throws Exception
	 {
		 Runtime.getRuntime().exec("cmd /C adb shell am force-stop com.waz.zclient");
		 Thread.sleep(10000);
		 driver.quit();
	 }
	 @Test
	 public void SendPhotoTest() throws Exception
	 {

		 List<WebElement> loginScreensButtons = driver.findElementsByClassName("android.widget.Button");
		 for(WebElement button : loginScreensButtons)
		 {
			 if(button.getText().equals("SIGN IN"))
			 {
				 button.click();
				 loginScreensButtons.clear();
				 break;
			 }
		 }
		 List<WebElement> inputFields = driver.findElementsByClassName("android.widget.EditText");
		 
		 for(WebElement field : inputFields)
		 {
			 try{
				 if(field.getText().equals("Email"))
				 {
					 field.sendKeys(login);
				 }
	
				 if(isNullOrEmpty(field.getText()))
				 {
					 field.sendKeys(password);
				 }
			 }
			 catch(Exception ex){
			 	continue;
			 }
		 }
		 loginScreensButtons = driver.findElementsByClassName("android.widget.Button");
		 for(WebElement button : loginScreensButtons)
		 {
			 if(button.getText().equals("SIGN IN"))
			 {
				 button.click();
				 break;
			 }
		 }
		 Thread.sleep(5000);

		 HashMap<String,Integer> usersMap = waitForElementWithTextByClassName("android.widget.TextView",userName,false);
		 ArrayList<WebElement> textFields =  (ArrayList<WebElement>) driver.findElementsByClassName("android.widget.TextView");
		 usersMap = waitForElementWithTextByClassName("android.widget.TextView",contactName,false);
		 textFields.get(usersMap.get(contactName)).click();
		 Thread.sleep(1000);
		 WebElement tapOrSwipeField = driver.findElementByClassName("android.widget.EditText");
		 Point coords = tapOrSwipeField.getLocation();
		 Dimension size = tapOrSwipeField.getSize();
		 driver.swipe(coords.x+10,coords.y+size.height/2,coords.x+size.width-20,coords.y+size.height/2,3000);
		 Thread.sleep(5000);
		 ArrayList<WebElement> buttons = (ArrayList<WebElement>) driver.findElementsByXPath("//android.view.View/android.view.View/android.widget.TextView");
		 buttons.get(0).click();
		 buttons.clear();
		 Thread.sleep(2000);
		 buttons =  (ArrayList<WebElement>) driver.findElementsByClassName("android.widget.Button");
		 buttons.get(3).click();
		 Thread.sleep(2000);
		 buttons.clear();
		 buttons =  (ArrayList<WebElement>) driver.findElementsByClassName("android.widget.Button");
		 buttons.get(1).click();
		 Thread.sleep(2000);
		 SignOut();
	 }
	 @Test
	 public void SendMessageTest() throws Exception
	 {
		 List<WebElement> loginScreensButtons = driver.findElementsByClassName("android.widget.Button");
		 for(WebElement button : loginScreensButtons)
		 {
			 if(button.getText().equals("SIGN IN"))
			 {
				 button.click();
				 loginScreensButtons.clear();
				 break;
			 }
		 }
		 List<WebElement> inputFields = driver.findElementsByClassName("android.widget.EditText");
		 
		 for(WebElement field : inputFields)
		 {
			 try{
				 if(field.getText().equals("Email"))
				 {
					 field.sendKeys(login);
				 }
	
				 if(isNullOrEmpty(field.getText()))
				 {
					 field.sendKeys(password);
				 }
			 }
			 catch(Exception ex){
			 	continue;
			 }
		 }
		 loginScreensButtons = driver.findElementsByClassName("android.widget.Button");
		 for(WebElement button : loginScreensButtons)
		 {
			 if(button.getText().equals("SIGN IN"))
			 {
				 button.click();
				 break;
			 }
		 }
		 Thread.sleep(7000);
		 WebElement mainForm = driver.findElementByClassName("android.support.v4.view.ViewPager");
		 Dimension size = mainForm.getSize();
		 HashMap<String,Integer> usersMap = waitForElementWithTextByClassName("android.widget.TextView",userName,false);
		 driver.swipe(size.width/2, size.height-100, size.width/2, 20, 2000);
		 Thread.sleep(2000);
		 ArrayList<WebElement> textFields =  (ArrayList<WebElement>) driver.findElementsByClassName("android.widget.TextView");
		 usersMap = waitForElementWithTextByClassName("android.widget.TextView",contactName,false);
		 textFields.get(usersMap.get(contactName)).click();
		 Thread.sleep(1000);
		 WebElement messageField  = driver.findElement(By.className("android.widget.EditText"));
		 messageField.click();
		 messageField.sendKeys("Test Message longer message\\n");
		 Thread.sleep(1000);
		 Scroll(10,size.height/4,size.width-50,size.height/4,3);	 
		 SignOut();
	 }
	 
	 private static boolean isNullOrEmpty(String s) {
		    return s == null || s.length() == 0;
		}
	 
	 private static HashMap<String,Integer> waitForElementWithTextByClassName(String className, String elementName, Boolean click)
	 {
		 Boolean flag = true;
		 Boolean swipeFlag = false;
		 int counter = 0;
		 HashMap<String,Integer> usersList = new HashMap<String,Integer>();
		 try {
		        while (flag) {		
		        	counter++;
		        	ArrayList<WebElement> textFields =  (ArrayList<WebElement>) driver.findElementsByClassName(className);
		        	if(!textFields.isEmpty())
		        	{
		        		for (int i = 0; i < textFields.size(); i++)
		        		{
		        			String text = textFields.get(i).getText(); 
		        			usersList.put(text,i);
		        			if (text.equals(elementName))
		        			{
		        				if(click)
		        				{
		        					driver.tap(1,textFields.get(i),50);
		        				}
		        				flag = false;
		        			}
		        			if(i == (textFields.size()-1) && swipeFlag && flag)
		        			{
		        				driver.swipe(500, 100, 500, 800, 500);
		        				swipeFlag = false;
		        			}
		        			if(i == (textFields.size()-1) && !swipeFlag && flag)
		        			{
		        				driver.swipe(500, 700, 500, 150, 500);
		        				swipeFlag = true;
		        			}
		        		}
		        	}
		            Thread.sleep(500);
		            if(counter==10)
		            {
		            flag = false;
		            }
		        }
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		 return usersList;
	 }

	 private static void SignOut() throws Exception 
	 {
		 WebElement mainForm = driver.findElementByClassName("android.support.v4.view.ViewPager");
		 Dimension size = mainForm.getSize();	 
		 Thread.sleep(2000); 
		 WebElement tapOrSwipeField = driver.findElementByClassName("android.widget.EditText");
		 Point coords = tapOrSwipeField.getLocation();
		 Dimension tapSize = tapOrSwipeField.getSize();
		 driver.swipe(coords.x,coords.y+tapSize.height/2,coords.x+tapSize.width-20,coords.y+tapSize.height/2,10000);
		 Thread.sleep(1000);
		 Scroll(10,size.height/4,size.width-50,size.height/4,3);	 
		 HashMap<String,Integer> usersMap = waitForElementWithTextByClassName("android.widget.TextView",userName,false);
		 ArrayList<WebElement> textFields =  (ArrayList<WebElement>) driver.findElementsByClassName("android.widget.TextView");
		 textFields.get(usersMap.get(userName)).click();
		 Thread.sleep(2000);
		 driver.swipe(size.width-20, size.height/2, 20, size.height/2, 1000);
		 Thread.sleep(2000);
		 driver.swipe(size.width/2, size.height-100, size.width/2, 20, 2000);
		 Thread.sleep(1000);
		 ArrayList<WebElement> buttons = (ArrayList<WebElement>) driver.findElementsByClassName("android.widget.TextView");

		 for(WebElement button : buttons)
		 {
			 if (button.getText().equals("Sign out"))
			 {
				 button.click();
				 break;
			 }
		 }
		 Thread.sleep(1000);
	 }
	 
	 private static void Scroll(int startx,int starty,int endx,int endy, double duration)
	 {
		 JavascriptExecutor js = (JavascriptExecutor) driver;
		 HashMap<String, Double> swipeObject = new HashMap<String, Double>();
		 swipeObject.put("startX",(double) startx);
		 swipeObject.put("startY",(double) starty);
		 swipeObject.put("endX", (double)endx);
		 swipeObject.put("endY",(double) endy);
		 swipeObject.put("duration", duration);
		 js.executeScript("mobile: swipe", swipeObject);
	 }
	
}