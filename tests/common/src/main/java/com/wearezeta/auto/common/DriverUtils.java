package com.wearezeta.auto.common;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
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

}
