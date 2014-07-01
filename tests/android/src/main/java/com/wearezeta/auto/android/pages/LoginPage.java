package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.*;

public class LoginPage extends AndroidPage {

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameLoginPage)
	private WebElement viewPager;

	@FindBy(how = How.ID, using = AndroidLocators.idSignInButton)
	private WebElement signInButton;

	@FindBy(how = How.ID, using = AndroidLocators.idSignUpButton)
	private WebElement signUpButton;

	@FindBy(how = How.ID, using = AndroidLocators.idLoginButton)
	private WebElement confirmSignInButton;

	@FindBy(how = How.ID, using = AndroidLocators.idLoginField)
	private WebElement loginField;

	@FindBy(how = How.ID, using = AndroidLocators.idPasswordField)
	private WebElement passwordField;

	@FindBy(how = How.ID, using = AndroidLocators.idWelcomeButtonsContainer)
	private List<WebElement> welcomeButtonsContainer;

	@FindBy(how = How.ID, using = AndroidLocators.idContactListNames)
	private WebElement contactListNames;

	private String login;
	private String password;
	private String url;
	private String path;

	public LoginPage(String URL, String path) throws IOException {

		super(URL, path);
		this.url = URL;
		this.path = path;
	}
	
	public RemoteWebDriver getDriver() {
		return driver;
	}

	public Boolean isVisible() {

		return viewPager != null;
	}

	public LoginPage SignIn() throws IOException {

		signInButton.click();
		return this;
	}

	public ContactListPage LogIn() throws IOException {
		confirmSignInButton.click();
		return new ContactListPage(url, path);
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

	public void setPassword(String password) throws InterruptedException {

		DriverUtils.setTextForChildByClassName(passwordField, "android.widget.EditText", password + "\n" );
		Thread.sleep(500);
	}

	public boolean waitForLogin() {

		return DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.idLoginProgressBar));
	}

	public Boolean isLoginFinished(String contact) {

		HashMap<String,Integer> usersMap = DriverUtils.waitForElementWithTextById(AndroidLocators.idContactListNames, driver);

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

	public RegistrationPage join() throws IOException {
		signUpButton.click();

		return new RegistrationPage(url, path);
	}

}
