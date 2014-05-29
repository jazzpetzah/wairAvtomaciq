package com.wearezeta.auto.android.pages;

import java.net.MalformedURLException;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import com.wearezeta.auto.common.DriverUtils;

public class LoginPage extends AndroidPage {
	
	@FindBy(how = How.CLASS_NAME, using = "android.support.v4.view.ViewPager")
	private WebElement viewPager;
	
	@FindBy(how = How.ID, using = "com.waz.zclient:id/button_sign_in")
	private WebElement signInButton;
	
	@FindBy(how = How.ID, using = "com.waz.zclient:id/button_login")
	private WebElement confirmSignInButton;
	
	@FindBy(how = How.ID, using = "com.waz.zclient:id/username_or_email")
	private WebElement loginField;
	
	@FindBy(how = How.ID, using = "com.waz.zclient:id/password")
	private WebElement passwordField;
	
	private String login;
	
	private String password;
	
	public LoginPage(String URL, String path) throws MalformedURLException {
		
		super(URL, path);
	}
	
	public Boolean isVisible() {
		
		return viewPager != null;
	}
	
	public void SignIn() {
		
		try {
			signInButton.click();
		}
		catch(NoSuchElementException ex) {
			confirmSignInButton.click();
		}
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {

		DriverUtils.setTextForChildByClassName(loginField, "android.widget.EditText", login);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		 
		DriverUtils.setTextForChildByClassName(passwordField, "android.widget.EditText", password);
	}
	
	public boolean waitForLogin() {
		
		return DriverUtils.waitUntilElementDissapear(driver, By.id("com.waz.zclient:id/progressBar1"));
	}
	
	public Boolean isLoginFinished(String contact) {
		
		HashMap<String,Integer> usersMap = DriverUtils.waitForElementWithTextByClassName("android.widget.TextView", driver);
		
		return usersMap.containsKey(contact);
	}

}
