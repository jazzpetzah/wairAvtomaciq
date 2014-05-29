package zclientiOS;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.xpath.operations.Bool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;

import org.openqa.selenium.*;



public class mainTest
{
	 private static AppiumDriver driver;
	 private String login = "piotr.iazadji@wearezeta.com";
	 private String password ="asdfer123qwertywz";
	 private static String myUserName = "Piotr Iazadji";
	 private String loginQA = "mengli.wong+mqa6@wearezeta.com";
	 private String passwordQA ="123456";
	 private static String qaUserName = "mqa6";
	 private String conversation = "Sergey";
//	 private String testMessage = "Test test\n";
	 private String testMessage = UUID.randomUUID().toString() + "\n";
	 private String bandle = "com.wearezeta.zclient-alpha";
			 
			 
			 
	 
	 @Before
	    public void setUp() throws Exception 
	    {
	        String appPath = "/Users/admin/Desktop/ZClient_Debug_3f609be.ipa";
	        DesiredCapabilities capabilities = new DesiredCapabilities();
	        //capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
	        capabilities.setCapability("platformName", "iOS");
	        //capabilities.setCapability("deviceName","Android Emulator");
	        //capabilities.setCapability("platformVersion", "4.4");
	        capabilities.setCapability("app", appPath);
	        //capabilities.setCapability("app-package", "com.waz.zclient");
	        //capabilities.setCapability("app-activity", ".RegistrationActivity");
	        driver = new AppiumDriver(new URL("http://192.168.232.128:4723/wd/hub"), capabilities);
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    }
	 
	 @After
	 public void tearDown() throws Exception
	 {
		 Thread.sleep(3000);
		 driver.quit();
	 }
	 
	 public static void ScrollToName(String name)
	 {
		 JavascriptExecutor js = (JavascriptExecutor) driver;
		 WebElement element = driver.findElement(By.name(name));
		 HashMap<String, String> scrollToObject = new HashMap<String, String>();
		 scrollToObject.put("element",((RemoteWebElement) element).getId());
		 js.executeScript("mobile: scrollTo", scrollToObject);
	 }
	 
	 
	 public static void SrollToElementAndClick(String name, String last)
	 {
		 Boolean flag = false;
		 List<WebElement> chatList = driver.findElementsByClassName("UIACollectionCell");
		 String lastName = chatList.get(chatList.size()-1).getAttribute("name");
		 if(!last.equals(lastName))
		 {
			 for (WebElement item : chatList)
			 {
				 if (item.getAttribute("name").toString().equals(name))
					 {
					 flag = true;
					 item.click();
					 break;
					 }
			 }
			 if(!flag)
			 {
				 ScrollToName(lastName);
				 SrollToElementAndClick(name,lastName);
			 }
		 }
		 
	 }
	 
	 public void ChatLastMessageValidation(String text)
	 {
		 List<WebElement> chat = driver.findElementsByClassName("UIATextView");
		 String lastMessage = chat.get(chat.size()-1).getAttribute("name");
		 Assert.assertEquals(lastMessage, text);
	 }
	 
	 public void iPhoneSwipeRight() {
		 driver.swipe(90, 170, 230, 170, 500);
	}
	 
	 public void iPhoneSwipeLeft() {
		 driver.swipe(300, 300, 130, 300, 500);
	}
	 
	 public void iPhoneSwipeUp() {
		 driver.swipe(170, 400, 170, 200, 500);
	}
	 
	 public void iPhoneSwipeDown() {
		 driver.swipe(170, 200, 170, 400, 500);
	}
	 
	 public void WaitElementClickable(WebElement element) {
		 WebDriverWait wait = new WebDriverWait(driver, 30);
		 wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	 
	 
	 @Test
	 public void LoginLogoutTest()
	 {
		 WebElement LoginBtn = driver.findElementByName("SignIn");
		 
		 WebDriverWait wait = new WebDriverWait(driver, 30);
		 wait.until(ExpectedConditions.elementToBeClickable(LoginBtn));
		 LoginBtn.click();
		 
		 WebElement LoginInput = driver.findElementByName("SignInEmail");
		 WebElement PassInput = driver.findElementByName("SignInPassword");
		 WebElement SignInBtn = driver.findElementByName("ConfirmSignIn");
		 
		 wait.until(ExpectedConditions.elementToBeClickable(LoginInput));
		 LoginInput.sendKeys(login);
		 PassInput.sendKeys(password);
		 SignInBtn.click();
		 
		 try
		 {
			 WebElement Alert = driver.findElementByXPath("//UIAApplication[1]/UIAWindow[4]/UIAAlert[1]/UIATableView[1]/UIATableCell[1]");
			 wait.until(ExpectedConditions.elementToBeClickable(Alert));
			 WebElement DismissBtn = driver.findElementByName("Don’t Allow");
			 DismissBtn.click();
		 }
		 catch(Exception ex)
		 {
			 //do nothing
		 }
		 
		 WebElement myuserLabel = driver.findElementByName(myUserName);
		 myuserLabel.click();
		 
		 WebElement ContinueBtn = driver.findElementByName("CONTINUE");
		 iPhoneSwipeLeft();
		 
		 iPhoneSwipeUp();
		 WebElement SignOutBtn = driver.findElementByName("Sign out");
		 wait.until(ExpectedConditions.elementToBeClickable(SignOutBtn));
		 SignOutBtn.click();
		 
		 LoginBtn = null;
		 LoginBtn = driver.findElementByName("SignIn");
		 Assert.assertNotNull(LoginBtn);
		 
	 }
	 
	 @Test
	 public void SendMessageTest()
	 {
		 WebElement LoginBtn = driver.findElementByName("SignIn");
		 
		 WebDriverWait wait = new WebDriverWait(driver, 30);
		 wait.until(ExpectedConditions.elementToBeClickable(LoginBtn));
		 LoginBtn.click();
		 
		 WebElement LoginInput = driver.findElementByName("SignInEmail");
		 WebElement PassInput = driver.findElementByName("SignInPassword");
		 WebElement SignInBtn = driver.findElementByName("ConfirmSignIn");
		 
		 wait.until(ExpectedConditions.elementToBeClickable(LoginInput));
		 LoginInput.sendKeys(login);
		 PassInput.sendKeys(password);
		 SignInBtn.click();
		 
		 try
		 {
			 WebElement Alert = driver.findElementByXPath("//UIAApplication[1]/UIAWindow[4]/UIAAlert[1]/UIATableView[1]/UIATableCell[1]");
			 wait.until(ExpectedConditions.elementToBeClickable(Alert));
			 WebElement DismissBtn = driver.findElementByName("Don’t Allow");
			 DismissBtn.click();
		 }
		 catch(Exception ex)
		 {
			 //do nothing
		 }
		 try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 SrollToElementAndClick(conversation, "");
		 
		 WebElement MessageLabel = driver.findElementByName("TAP OR SLIDE  ");
		 wait.until(ExpectedConditions.elementToBeClickable(MessageLabel));
		 MessageLabel.click();
		 WebElement MessageInput = driver.findElementByName("ComposeControllerTextView");
		 MessageInput.sendKeys(testMessage);
		 try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 //ChatLastMessageValidation(testMessage);
		 
		 iPhoneSwipeRight();
		 WebElement myuserLabel = driver.findElementByName(myUserName);
		 myuserLabel.click();
		 iPhoneSwipeLeft();
		 iPhoneSwipeLeft();
		 
		 
		 iPhoneSwipeUp();
		 
		 WebElement SignOutBtn = driver.findElementByName("Sign out");
		 wait.until(ExpectedConditions.elementToBeClickable(SignOutBtn));
		 SignOutBtn.click();
		
	 }
	 
	 @Test
	 public void SetNewPhotoTest()
	 {
		 WebElement LoginBtn = driver.findElementByName("SignIn");
		 
		 WebDriverWait wait = new WebDriverWait(driver, 30);
		 wait.until(ExpectedConditions.elementToBeClickable(LoginBtn));
		 LoginBtn.click();
		 
		 WebElement LoginInput = driver.findElementByName("SignInEmail");
		 WebElement PassInput = driver.findElementByName("SignInPassword");
		 WebElement SignInBtn = driver.findElementByName("ConfirmSignIn");
		 
		 wait.until(ExpectedConditions.elementToBeClickable(LoginInput));
		 LoginInput.sendKeys(login);
		 PassInput.sendKeys(password);
		 SignInBtn.click();
		 
		 try
		 {
			 WebElement Alert = driver.findElementByXPath("//UIAApplication[1]/UIAWindow[4]/UIAAlert[1]/UIATableView[1]/UIATableCell[1]");
			 wait.until(ExpectedConditions.elementToBeClickable(Alert));
			 WebElement DismissBtn = driver.findElementByName("Don’t Allow");
			 DismissBtn.click();
		 }
		 catch(Exception ex)
		 {
			 //do nothing
		 }
		 
		 WebElement myuserLabel = driver.findElementByName(myUserName);
		 myuserLabel.click();
		 
		 WebElement ContinueBtn = driver.findElementByName("CONTINUE");
		 iPhoneSwipeLeft();
		 
		 WebElement emailField = driver.findElementByXPath("//UIAApplication[1]/UIAWindow[1]/UIATextField[1]");
		 emailField.click();
		 //driver.tap(0, emailField, 0);
		 
		 WebElement CameraBtn = driver.findElementByName("cameraButton");
		 WaitElementClickable(CameraBtn);
		 CameraBtn.click();
		 
	 }
	 
}
