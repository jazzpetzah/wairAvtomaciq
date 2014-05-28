package com.wearezeta.auto.common;

import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverUtils {
	
	 public static boolean isNullOrEmpty(String s) {
		    return s == null || s.length() == 0;
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
