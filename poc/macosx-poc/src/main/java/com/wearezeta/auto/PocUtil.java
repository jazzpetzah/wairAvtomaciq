package com.wearezeta.auto;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class PocUtil {
    public static void preventStuckOnZClientFailure(RemoteWebDriver driver) {
    	try {
    		WebElement failCancelButton = driver.findElement(By.id("_NS:9"));
    		failCancelButton.click();
    	} catch (NoSuchElementException e) {
    	}
    }
    
    public static void goToContactWithName(RemoteWebDriver driver, String name) { 
        List<WebElement> cells = driver.findElements(By.xpath("//AXRow")); 
        for (WebElement cell: cells) { 
            try { 
                WebElement contact = cell.findElement(By.id("clListItemNameField")); 
                if (contact.getText().equals(name)) { 
                    System.out.println(name + " is found, trying to click."); 
                    cell.click(); 
                    break; 
                } 
            } catch (Exception e) {  } 
        } 
   } 
     
   public static String generateString(Random rng, String characters, int length) { 
       char[] text = new char[length]; 
       for (int i = 0; i < length; i++) { 
           text[i] = characters.charAt(rng.nextInt(characters.length())); 
       } 
       return new String(text); 
   } 
}
