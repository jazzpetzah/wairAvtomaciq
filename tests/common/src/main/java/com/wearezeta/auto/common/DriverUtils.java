package com.wearezeta.auto.common;


import io.appium.java_client.AppiumDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;

public class DriverUtils {
	
	 public static boolean isNullOrEmpty(String s) {
		    return s == null || s.length() == 0;
	 }
	 
	 public static boolean waitUntilElementDissapear(RemoteWebDriver driver, final By by) {
	 
		 Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			       .withTimeout(30, TimeUnit.SECONDS)
			       .pollingEvery(2, TimeUnit.SECONDS)
			       .ignoring(NoSuchElementException.class);
		 
		 Boolean bool = wait.until(new Function<WebDriver, Boolean>() {
			 
			 public Boolean apply(WebDriver driver) {
			       return (driver.findElements(by).size() == 0);
			     }
		 });
		 
		 return bool;
	 }
	 
	 public static boolean waitUntilElementAppears(RemoteWebDriver driver, final By by) {
		 
		 Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			       .withTimeout(30, TimeUnit.SECONDS)
			       .pollingEvery(2, TimeUnit.SECONDS)
			       .ignoring(NoSuchElementException.class);
		 
		 Boolean bool = wait.until(new Function<WebDriver, Boolean>() {
			 
			 public Boolean apply(WebDriver driver) {
			       return (driver.findElements(by).size() > 0);
			     }
		 });
		 
		 return bool;
	 }
	 
	 public static void setTextForChildByClassName(WebElement parent, String childClassName, String value)
	 {
		 parent.findElement(By.className(childClassName)).sendKeys(value);
	 }
	
	 public static HashMap<String,Integer> waitForElementWithTextByClassName(String className, RemoteWebDriver driver)
	 {
		 Boolean flag = true;
		 int counter = 0;
		 HashMap<String,Integer> usersList = new HashMap<String,Integer>();
		 try {
		        while (flag) {		
		        	counter ++;
		        	ArrayList<WebElement> textFields =  (ArrayList<WebElement>) driver.findElementsByClassName(className);
		        	if(!textFields.isEmpty())
		        	{
		        		for (int i = 0; i < textFields.size(); i++)
		        		{
		        			String text = textFields.get(i).getText(); 
		        			
		        			if (!text.isEmpty())
		        			{
		        				usersList.put(text, i);
		        			}
		        		}
		        	}
		            Thread.sleep(500);
		            if(counter == 10)
		            {
		            	flag = false;
		            }
		        }
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		 return usersList;
	 }

	 public static void scrollToElement(RemoteWebDriver driver, WebElement element) {
		 JavascriptExecutor js = (JavascriptExecutor) driver;
		 HashMap<String, String> scrollToObject = new HashMap<String, String>();
		 scrollToObject.put("element",((RemoteWebElement) element).getId());
		 js.executeScript("mobile: scrollTo", scrollToObject);
	 }
	 
	 public static void swipeLeft(AppiumDriver driver, WebElement element, int time) {
		 Point coords = element.getLocation();
		 Dimension elementSize = element.getSize();
		 driver.swipe(coords.x+elementSize.width - 20, coords.y+elementSize.height / 2, coords.x + 20, coords.y + elementSize.height / 2, time);
	 }
	 
	 public static void swipeRight(AppiumDriver driver, WebElement element, int time) {
		 Point coords = element.getLocation();
		 Dimension elementSize = element.getSize();
		 driver.swipe(coords.x, coords.y + elementSize.height / 2, coords.x + elementSize.width-20, coords.y + elementSize.height / 2, time);
	 }
	 
	 public static void swipeUp(AppiumDriver driver,WebElement element, int time) {
		 Point coords = element.getLocation();
		 Dimension elementSize = element.getSize();
		 driver.swipe(coords.x+elementSize.width / 2, coords.y + elementSize.height - 200, coords.x + elementSize.width / 2, coords.y + 150, time);
	 }
	 
	 public static void swipeDown(AppiumDriver driver,WebElement element, int time) {
		 Point coords = element.getLocation();
		 Dimension elementSize = element.getSize();
		 driver.swipe(coords.x+elementSize.width / 2, coords.y + 150, coords.x + elementSize.width / 2, coords.y + elementSize.height - 200, time);
	 }
	 
	 public static void androidMultiTap(AppiumDriver driver,WebElement element, int tapNumber) throws InterruptedException
	 {
		 Point coords = element.getLocation();
			Dimension elementSize = element.getSize();
			
			JavascriptExecutor js = (JavascriptExecutor) driver;
			HashMap<String, Double> tapObject = new HashMap<String, Double>();
			tapObject.put("tapCount", (double) 1);
			tapObject.put("touchCount", (double) 1);
			tapObject.put("duration", 0.2);
			tapObject.put("x", (double) (coords.x + elementSize.width/2));
			tapObject.put("y", (double) (coords.y + elementSize.height/2));
	        
	        for(int i=0;i<tapNumber;i++)
	        { 
	        	js.executeScript("mobile: tap", tapObject);
	        	Thread.sleep(100);
	        }
	 }
	 
	 public static void iOSMultiTap(AppiumDriver driver,WebElement element, int tapNumber) throws InterruptedException
	 {
		 Point coords = element.getLocation();
			Dimension elementSize = element.getSize();
			
			JavascriptExecutor js = (JavascriptExecutor) driver;
			HashMap<String, Double> tapObject = new HashMap<String, Double>();
			tapObject.put("tapCount", (double) tapNumber);
			tapObject.put("touchCount", (double) 1);
			tapObject.put("duration", 0.2);
			tapObject.put("x", (double) (coords.x + elementSize.width/2));
			tapObject.put("y", (double) (coords.y + elementSize.height/2));
	        
			js.executeScript("mobile: tap", tapObject);
	 }
}
