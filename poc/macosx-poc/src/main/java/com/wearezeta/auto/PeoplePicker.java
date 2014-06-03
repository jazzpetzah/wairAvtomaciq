package com.wearezeta.auto;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.sikuli.basics.ImageLocator;
import org.sikuli.basics.Settings;
import org.sikuli.script.App;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

public class PeoplePicker {
	
    RemoteWebDriver webDriver; 

    @Test
    public void PeoplePickerTopConversations() {
    	webDriver.navigate().to("ZClient");

    	PocUtil.preventStuckOnZClientFailure(webDriver);
    	
    	try { Thread.sleep(2000); } catch (InterruptedException e) { }
    	PocUtil.goToContactWithName(webDriver, "Kirill Aleksandrov"); 
    	
    	WebElement plusButton = webDriver.findElement(By.id("addConversationButton"));
    	plusButton.click();
  
    	try {
    		App.focus("ZClient");
    		Screen s = new Screen();
    
    		Pattern contactPattern = new Pattern("sergeyContact.png");
    		contactPattern.similar(0.6f);
    		Region contactRegion = s.wait(contactPattern, 10);
    		contactRegion.click();
    	} catch (FindFailed exc) {
    		Assert.assertTrue(false);
    	}
    }
    
    @Test
    public void PeoplePickerDismiss() {
    	webDriver.navigate().to("ZClient");

    	PocUtil.preventStuckOnZClientFailure(webDriver);
    	
    	try { Thread.sleep(1000); } catch (InterruptedException e) { }
    	PocUtil.goToContactWithName(webDriver, "Kirill Aleksandrov"); 
    	
    	WebElement plusButton = webDriver.findElement(By.id("addConversationButton"));
    	plusButton.click();
    	
    	List<WebElement> dismissButtonCandidates = webDriver.findElements(By.xpath("//AXButton"));
    	for (WebElement dismissButtonCandidate: dismissButtonCandidates) {
    		String axHelp = dismissButtonCandidate.getAttribute("AXHelp");
    		if (axHelp.equals("Dismiss (Esc)")) {
    			dismissButtonCandidate.click();
    			break;
    		}
    	}
    }
    
    @Test
    public void PeoplePickerScrollable() {
    	webDriver.navigate().to("ZClient");
    	
    	PocUtil.preventStuckOnZClientFailure(webDriver);
    	
    	try { Thread.sleep(1000); } catch (InterruptedException e) { }
    	PocUtil.goToContactWithName(webDriver, "Kirill Aleksandrov"); 
    	
    	WebElement plusButton = webDriver.findElement(By.id("addConversationButton"));
    	plusButton.click();
    	
    	List<WebElement> textAreaCandidates = webDriver.findElements(By.xpath("//AXTextArea"));
    	for (WebElement textArea: textAreaCandidates) {
    		if (textArea.getText().equals("")) {
    			textArea.sendKeys("mqa");
    		}
    	}

    	//get window
    	WebElement mainWindow = webDriver.findElement(By.id("Main"));
    	NSPoint mainPosition = NSPoint.fromString(mainWindow.getAttribute("AXPosition"));
    	NSPoint mainSize = NSPoint.fromString(mainWindow.getAttribute("AXSize"));
    	
    	NSPoint latestPoint = new NSPoint(mainPosition.x + mainSize.x, mainPosition.y + mainSize.y);

    	//get scrollbar for contact list
    	WebElement peopleDecrementSB = null;
    	WebElement peopleIncrementSB = null;
    	List<WebElement> scrollAreas = webDriver.findElements(By.xpath("//AXScrollArea"));
    	for (WebElement scrollArea: scrollAreas) {
    		boolean isFoundPeople = false;
    		try {
    			List<WebElement> people = scrollArea.findElements(By.xpath("//AXStaticText"));
    			for (WebElement contact: people) {
    				if (contact.getText().equals("mqa9")) {
    					isFoundPeople = true;
    				}
    			}
    		} catch (NoSuchElementException e) {
    			isFoundPeople = false;
    		}
    		if (isFoundPeople) {
    			WebElement scrollBar = scrollArea.findElement(By.xpath("//AXScrollBar"));
    			List<WebElement> scrollButtons = scrollBar.findElements(By.xpath("//AXButton"));
    			for (WebElement scrollButton: scrollButtons) {
    				String subrole = scrollButton.getAttribute("AXSubrole");
    				if (subrole.equals("AXDecrementPage")) {
    					peopleDecrementSB = scrollButton;
    				}
    				if (subrole.equals("AXIncrementPage")) {
    					peopleIncrementSB = scrollButton;
    				}
    			}
    		}
    	}
    	
    	WebElement mqa9Contact = null;
        List<WebElement> contactList = webDriver.findElements(By.xpath("//AXRow")); 
        for (WebElement cell: contactList) { 
            try { 
                WebElement contact = cell.findElement(By.xpath("//AXStaticText")); 
                if (contact.getText().equals("mqa9")) { 
                    mqa9Contact = contact;
                    break; 
                } 
            } catch (Exception e) { System.out.println(cell); } 
        } 
        
        NSPoint mqa9Position = NSPoint.fromString(mqa9Contact.getAttribute("AXPosition"));
        while (mqa9Position.y > latestPoint.y) {
        	peopleIncrementSB.click();
        	mqa9Position = NSPoint.fromString(mqa9Contact.getAttribute("AXPosition"));
        }
        while (mqa9Position.y < mainPosition.y) {
        	peopleDecrementSB.click();
        	mqa9Position = NSPoint.fromString(mqa9Contact.getAttribute("AXPosition"));
        }
        try { Thread.sleep(500); } catch (InterruptedException e1) { }
        mqa9Contact.click();

        try {
        	WebElement sendButton = webDriver.findElement(By.name("Send"));
        	sendButton.click();
       	} catch (NoSuchElementException e) { }
    }
    
    
    @BeforeClass
    public static void SetupImgPath() {
    	Settings.OcrTextRead = true;
    	Settings.OcrTextSearch = true;
    	String[] path = ImageLocator.getImagePath();
    	ImageLocator.removeImagePath(path);
    	ImageLocator.addImagePath("data");
    	path = ImageLocator.getImagePath();
    }
    
    @Before
    public void SetUp() { 
        DesiredCapabilities capabilities = new DesiredCapabilities(); 
        capabilities.setCapability(CapabilityType.BROWSER_NAME, ""); 
        capabilities.setCapability(CapabilityType.PLATFORM, "Mac"); 
        try { 
            webDriver = new RemoteWebDriver(new URL("http://127.0.0.1:4622/wd/hub"), capabilities); 
              
        } catch (MalformedURLException e) { 
            e.printStackTrace(); 
        } 
    } 
  
    @After
    public void TearDown() { 
      WebElement quitMenuItem = webDriver.findElement(By.name("Quit ZClient")); 
      quitMenuItem.click(); 
      webDriver.quit(); 
    } 
}
