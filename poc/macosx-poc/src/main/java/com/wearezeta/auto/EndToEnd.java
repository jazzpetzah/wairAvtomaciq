package com.wearezeta.auto;

import java.net.MalformedURLException; 
import java.net.URL;
import java.util.List;

import org.junit.After; 
import org.junit.Before; 
import org.junit.BeforeClass;
import org.junit.Test; 
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement; 
import org.openqa.selenium.remote.CapabilityType; 
import org.openqa.selenium.remote.DesiredCapabilities; 
import org.openqa.selenium.remote.RemoteWebDriver; 
import org.sikuli.basics.ImageLocator;
import org.sikuli.basics.Settings;
  
public class EndToEnd { 
      
    RemoteWebDriver webDriver; 
      
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
//      WebElement quitMenuItem = webDriver.findElement(By.name("Quit ZClient")); 
//      quitMenuItem.click(); 
      webDriver.quit(); 
    } 
      
	private WebElement findNewMessageTextArea() {
		List<WebElement> rows = webDriver.findElements(By.xpath("//AXTextArea")); 
        for (WebElement row: rows) { 
            if (row.getText().equals("")) { 
                return row;
            } 
        } 
        return null;
	}
    @Test
    public void E2ETest() {
        //start application 
         webDriver.navigate().to("ZClient");
         WebElement element = findNewMessageTextArea();
         element.sendKeys("");
         WebElement addImage = webDriver.findElement(By.id("AddImageButton"));
         addImage.click();

         try { Thread.sleep(30000); } catch (InterruptedException e) {} 
         return;
         /*
//         String xp = "//AXRow/[AXCell/AXStaticText/@AXIdentifier = 'clListItemNameField']"
         String xp = "//AXRow[descendant::AXStaticText[@AXIdentifier='clListItemNameField']]";
         
         WebElement el = webDriver.findElement(By.id("people_picker_searchfield"));
         System.out.println(el);
         System.out.println(webDriver.getPageSource());
         webDriver.findElement(By.xpath(xp));
         
         
         try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); } 
         long sDate = new Date().getTime();
         WebElement emailForm = webDriver.findElement(By.xpath("//AXTextField[1]")); 
         emailForm.sendKeys("Kyrylo.Aleksandrov@wearezeta.com"); 
         long eDate = new Date().getTime();
         System.out.println("" + (eDate - sDate) + "(ms)");
         
         WebElement passwordForm = webDriver.findElement(By.xpath("//AXTextField[2]")); 
         passwordForm.sendKeys("k1r1k111987"); 
           
         WebElement signInSecondButtonsPot = webDriver.findElement(By.name("Sign In")); 
         signInSecondButtonsPot.click(); 
           
         try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); } 
         // go to my page 
         PocUtil.goToContactWithName(webDriver, "Kirill Aleksandrov"); 
           
         //go to Sergey page 
         PocUtil.goToContactWithName(webDriver, "Sergey"); 
      
         //insert some text and send it 
         String testString = PocUtil.generateString(new Random(), "abcedfghijklmnopqrstuvwxyz", 10); 
         List<WebElement> rows = webDriver.findElements(By.xpath("//AXTextArea")); 
         for (WebElement row: rows) { 
             if (row.getText().equals("")) { 
                 row.sendKeys(testString); 
                 row.submit(); 
             } 
         } 
           
         //check if message added 
         WebElement conversationScroll = webDriver.findElement(By.id("ConversationScrollArea")); 
         List<WebElement> textArea = conversationScroll.findElements(By.xpath("//AXStaticText")); 
        
         boolean isFound = false;
         for (WebElement texts: textArea) {
        	 String textAreaContent = texts.getText();
        	 if (textAreaContent.equals(testString)) {
        		 isFound = true;
        		 break;
        	 }
         }
    	 Assert.assertTrue(isFound);
    	 
         //send image 
         List<WebElement> insertMessageForm = webDriver.findElements(By.xpath("//AXTextArea")); 
         for (WebElement row: insertMessageForm) { 
             if (row.getText().equals("")) { 
                 row.sendKeys(""); 
             } 
         } 
  
         WebElement addImageButton = webDriver.findElement(By.id("AddImageButton")); 
         addImageButton.click(); 
           
//       try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); } 
         WebElement scrollArea = webDriver.findElement(By.xpath("//AXScrollArea")); 
         scrollArea.sendKeys("test.jpg"); 
           
         WebElement openButton = webDriver.findElement(By.id("_NS:55")); 
         openButton.click(); 
           
         //sign in/out test 
//       try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); } 
         WebElement signOut = webDriver.findElement(By.name("Sign Out")); 
         signOut.click(); 
           
//       try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); } 
//       WebElement signInButton = webDriver.findElement(By.id("_NS:9")); 
//       signInButton.click(); 
           
  
//       try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); } 
//       WebElement emailForm = webDriver.findElement(By.id("_NS:43")); 
//       emailForm.sendKeys("Kyrylo.Aleksandrov@wearezeta.com"); 
           
//       WebElement passwordForm = webDriver.findElement(By.id("_NS:76")); 
//       passwordForm.sendKeys("k1r1k111987"); 
  
//       WebElement signInSecondButton = webDriver.findElement(By.id("_NS:133")); 
//       System.out.println(signInSecondButton); 
//       signInSecondButton.click(); 
           
         long endDate = new Date().getTime(); 
         System.out.println("Execution time: " + (endDate - startDate)); */
    } 
} 

