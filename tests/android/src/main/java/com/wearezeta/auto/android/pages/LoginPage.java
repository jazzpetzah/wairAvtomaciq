package com.wearezeta.auto.android.pages;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;
import com.wearezeta.auto.common.DriverUtils;

public class LoginPage extends AndroidPage {
	
	@FindBy(how = How.CLASS_NAME, using = "android.support.v4.view.ViewPager")
	private WebElement viewPager;
	
	@FindBy(how = How.CLASS_NAME, using = "android.widget.Button")
	private List<WebElement> loginScreensButtons;
	
	@FindBy(how = How.CLASS_NAME, using = "android.widget.EditText")
	private List<WebElement> inputFields;
	
	private String login;
	
	private String password;
	
	public LoginPage(String URL, String path) throws MalformedURLException {
		
		super(URL, path);
	}
	
	public Boolean isVisible() {
		
		return viewPager != null;
	}
	
	public void SignIn() {
		
		 for(WebElement button : loginScreensButtons)
		 {
			 if(button.getText().equals("SIGN IN"))
			 {
				 button.click();
				 break;
			 }
		 }
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {

		 for(WebElement field : inputFields)
		 {
			 try {
				 if(field.getText().equals("Email"))
				 {
					 field.sendKeys(login);
				 }
			 }
			 catch(Exception ex){
			 	continue;
			 }
		 }
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		 
		 for(WebElement field : inputFields)
		 {
			 try {
				 if(DriverUtils.isNullOrEmpty(field.getText()))
				 {
					 field.sendKeys(password);
				 }
			 }
			 catch(Exception ex) {
			 	continue;
			 }
		 }
	}
	
	public boolean waitForLogin() {
		 Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			       .withTimeout(30, TimeUnit.SECONDS)
			       .pollingEvery(2, TimeUnit.SECONDS)
			       .ignoring(NoSuchElementException.class);
		 
		 
		 Boolean bool = wait.until(new Function<WebDriver, Boolean>() {
			 
			 public Boolean apply(WebDriver driver) {
			       return (driver.findElements(By.className("android.widget.ProgressBar")).size() == 0);
			     }
		 });
		 
		 return bool;
	}
	
	public Boolean isLoginFinished(String contact) {
		
		HashMap<String,Integer> usersMap = DriverUtils.waitForElementWithTextByClassName("android.widget.TextView", driver);
		
		return usersMap.containsKey(contact);
	}

}
