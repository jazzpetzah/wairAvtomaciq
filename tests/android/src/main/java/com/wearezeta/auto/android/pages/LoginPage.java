package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.AndroidLocators;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.SwipeDirection;

public class LoginPage extends AndroidPage {
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameLoginPage)
	private WebElement viewPager;
	
	@FindBy(how = How.ID, using = AndroidLocators.idSignInButton)
	private WebElement signInButton;
	
	@FindBy(how = How.ID, using = AndroidLocators.idLoginButton)
	private WebElement confirmSignInButton;
	
	@FindBy(how = How.ID, using = AndroidLocators.idLoginField)
	private WebElement loginField;
	
	@FindBy(how = How.ID, using = AndroidLocators.idPasswodField)
	private WebElement passwordField;
	
	@FindBy(how = How.ID, using = AndroidLocators.idWelcomeButtonsContainer)
	private List<WebElement> welcomeButtonsContainer;
	
	private String login;
	private String password;
	private String url;
	private String path;
	
	public LoginPage(String URL, String path) throws IOException {
		
		super(URL, path);
		this.url = URL;
		this.path = path;
	}
	
	public Boolean isVisible() {
		
		return viewPager != null;
	}
	
	public AndroidPage SignIn() throws IOException {
		
		AndroidPage page = null;
		
		try {
			signInButton.click();
			page = this;
		}
		catch(NoSuchElementException ex) {
			confirmSignInButton.click();
			page = new ContactListPage(url, path);
		}
		return page;
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
		
		return DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.idLoginProgressBar));
	}
	
	public Boolean isLoginFinished(String contact) {
		
		HashMap<String,Integer> usersMap = DriverUtils.waitForElementWithTextByClassName("android.widget.TextView", driver);
		
		return usersMap.containsKey(contact);
	}
	
	public Boolean isWelcomeButtonsExist(){
		return welcomeButtonsContainer.size() > 0;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) {
		// no need to swipe
		return null;
	}

}
