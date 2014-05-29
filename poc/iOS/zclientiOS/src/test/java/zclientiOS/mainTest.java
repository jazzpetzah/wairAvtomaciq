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
	 private String password ="asdfer123";
	 private static String myUserName = "Piotr Iazadji";
	 private String loginQA = "mengli.wong+mqa6@wearezeta.com";
	 private String passwordQA ="123456";
	 private static String qaUserName = "mqa6";
	 private String conversation = "Maxim";
	 private String testMessage = UUID.randomUUID().toString();
	 private String bandle = "com.wearezeta.zclient-alpha";
	 
			 
			 
			 
	 
	 @Before
	    public void setUp() throws Exception 
	    {
	        String appPath = "/Project/ZClient.app";
	        DesiredCapabilities capabilities = new DesiredCapabilities();
	        capabilities.setCapability("platformName", "iOS");
	        capabilities.setCapability("app", appPath);

	        //capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
	        //capabilities.setCapability("deviceName","iPhone 4S");
	        //capabilities.setCapability("platformVersion", "4.4");
	        //capabilities.setCapability("app-package", "com.waz.zclient");
	        //capabilities.setCapability("app-activity", ".RegistrationActivity");
	        
	        driver = new AppiumDriver(new URL("http://192.168.153.128:4723/wd/hub"), capabilities);
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    }
	 
	 @After
	 public void tearDown() throws Exception
	 {
		 Thread.sleep(3000);
		 driver.quit();
	 }
	 
	 
	 public void LoginAs(String name, String pass)
	 {
		 WebElement LoginBtn = driver.findElementByName("SignIn");
		 WebDriverWait wait = new WebDriverWait(driver, 30);
		 wait.until(ExpectedConditions.elementToBeClickable(LoginBtn));
		 LoginBtn.click();
		 
		 WebElement LoginInput = driver.findElementByName("SignInEmail");
		 WebElement PassInput = driver.findElementByName("SignInPassword");
		 WebElement SignInBtn = driver.findElementByName("ConfirmSignIn");
		 
		 wait.until(ExpectedConditions.elementToBeClickable(LoginInput));
		 LoginInput.sendKeys(name);
		 PassInput.sendKeys(pass);
		 SignInBtn.click();
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
		 List<WebElement> chat = driver.findElementsByClassName("UIATableCell");
		 int pos = chat.size();
		 String lastMessageLoc = "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[" + pos + "]/UIATextView[1]";
		 WebElement lastMessageEl = driver.findElementByXPath(lastMessageLoc);
		 String MessageBody = lastMessageEl.getAttribute("name");
		 Assert.assertEquals(text, MessageBody);
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
	 
	 public void TapMainScreen()
	 {
		 WebElement tableView = driver.findElementByClassName("UIATableView");
		 tableView.click();
	 }
	 
	 public void WaitElementClickable(WebElement element) {
		 WebDriverWait wait = new WebDriverWait(driver, 30);
		 wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	 
	 public void SendMessage(String message)
	 {
		 WebElement MessageLabel = driver.findElementByName("TAP OR SLIDE  ");
		 WebDriverWait wait = new WebDriverWait(driver, 30);
		 wait.until(ExpectedConditions.elementToBeClickable(MessageLabel));
		 MessageLabel.click();
		 WebElement MessageInput = driver.findElementByName("ComposeControllerTextView");
		 MessageInput.sendKeys(message + "\n");
	 }
	 
	 public void MessageValidation(String message)
	 {
		 WebElement sentMessage = null;
		 try
		 {
			 sentMessage = driver.findElementByName(message);
		 }
		 catch(Exception e){//nothing
		 }
		 Assert.assertNotNull("Element not found", sentMessage);
	 }
	 
	 public void DismissAlert()
	 {
		 
		 WebElement Alert = driver.findElementByClassName("UIAAlert");
		 driver.switchTo().alert().dismiss();
		 //WebDriverWait wait = new WebDriverWait(driver, 30);
		 //wait.until(ExpectedConditions.elementToBeClickable(Alert));
		 //WebElement DismissBtn = driver.findElementByName("Don’t Allow");
		 //DismissBtn.click();
	 }
	 
	 public void AcceptAlert()
	 {
		 
		 WebElement Alert = driver.findElementByClassName("UIAAlert");
		 driver.switchTo().alert().accept();
	 }
	 
	 public void PickUser(String username)
	 {
		 WebElement ZLogoImg = driver.findElementByName("PULL TO START CONVERSATION");
		 iPhoneSwipeDown();
		 WebElement SearchBox = driver.findElementByName("textViewSearch");
		 SearchBox.click();
		 SearchBox.sendKeys(username);
		 WebElement user = driver.findElementByXPath("//UIAApplication[1]/UIAWindow[1]/UIACollectionView[2]/UIACollectionCell[1]/UIAStaticText[1]");
		 user.click();
	 }
	 
	 public void PickUserAndStartConversation(String username)
	 {
		 PickUser(username);
		 WebElement CreateConversationBtn = driver.findElementByName("CREATE CONVERSATION");
		 CreateConversationBtn.click();
	 }
	 
	 
	 @Test
	 public void LoginLogoutTest()
	 {
		 LoginAs(login, password);
		 
		 DismissAlert();
		 
		 WebElement myuserLabel = driver.findElementByName(myUserName);
		 myuserLabel.click();
		 
		 WebElement ContinueBtn = driver.findElementByName("CONTINUE");
		 iPhoneSwipeLeft();
		 
		 
		 iPhoneSwipeUp();
		 
		 WebElement SignOutBtn = driver.findElementByName("Sign out");
		 //wait.until(ExpectedConditions.elementToBeClickable(SignOutBtn));
		 SignOutBtn.click();
		 
		 WebElement LoginBtn = null;
		 LoginBtn = driver.findElementByName("SignIn");
		 Assert.assertNotNull(LoginBtn);
		 
	 }
	 
	 @Test
	 public void SendMessageTest()
	 {
		 LoginAs(login, password);
		 
		 DismissAlert();
		 
		 SrollToElementAndClick(conversation, "");
		 
		 SendMessage(testMessage);
		 
		 ChatLastMessageValidation(testMessage);
		 
		 iPhoneSwipeRight();
		 WebElement myuserLabel = driver.findElementByName(myUserName);
		 myuserLabel.click();
		 iPhoneSwipeLeft();
		 iPhoneSwipeLeft();
		 
		 
		 iPhoneSwipeUp();
		 
		 WebElement SignOutBtn = driver.findElementByName("Sign out");
		 //wait.until(ExpectedConditions.elementToBeClickable(SignOutBtn));
		 SignOutBtn.click();
		
	 }
	 
	 @Test
	 public void SetNewPhotoTest()
	 {
		 LoginAs(login, password);
		 
		 DismissAlert();
		 
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
	 
	 @Test
	 public void UserPickerRealDevice()
	 {
		 LoginAs(login, password);
		 
		 DismissAlert();
		
		 PickUserAndStartConversation(conversation);		 
		 
		 SendMessage(testMessage);
		 
		 TapMainScreen();
		 
		 MessageValidation(testMessage);
		 
		 
	 }
	 
}
