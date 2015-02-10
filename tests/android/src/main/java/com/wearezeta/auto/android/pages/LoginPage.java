package com.wearezeta.auto.android.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class LoginPage extends AndroidPage {
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPeoplePickerClearbtn")
	private WebElement pickerClearBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idSignInButton")
	private WebElement signInButton;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idWelcomeSlogan")
	private WebElement welcomeSlogan;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idYourName")
	private WebElement yourUser;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idSignUpButton")
	private WebElement signUpButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idLoginButton")
	private WebElement confirmSignInButton;

	@AndroidFindBy(xpath =  AndroidLocators.LoginPage.xpathLoginInput)
	private WebElement loginInput;
	
	@AndroidFindBy(xpath =  AndroidLocators.LoginPage.xpathPasswordInput)
	private WebElement passwordInput;
	
	@FindBy(className  = AndroidLocators.CommonLocators.classEditText)
	private List<WebElement> editText;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idLoginError")
	private WebElement loginError;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idWelcomeSlogan")
	private List<WebElement> welcomeSloganContainer;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idContactListNames")
	private WebElement contactListNames;

	@FindBy(how = How.ID, using = AndroidLocators.CommonLocators.idDismissUpdateButton)
	private WebElement dismissUpdateButton;
	
	private String login;
	private String password;
	private String url;
	private String path;
	private static final String LOGIN_ERROR_TEXT = "WRONG ADDRESS OR PASSWORD.\nPLEASE TRY AGAIN.";

	public LoginPage(String URL, String path) throws Exception {

		super(URL, path);
		this.url = URL;
		this.path = path;
	}
	
	public LoginPage(String URL, String path, boolean isUnicode) throws Exception {

		super(URL, path, isUnicode);
		this.url = URL;
		this.path = path;
	}
	
	
	public ZetaAndroidDriver getDriver() {
		return driver;
	}

	public Boolean isVisible() {

		return welcomeSlogan != null;
	}

	public Boolean isLoginError() {
		return loginError.isDisplayed();
	}
	
	public Boolean isLoginErrorTextOk() {	
		return loginError.getText().equals(LOGIN_ERROR_TEXT);
	}
	
	public LoginPage SignIn() throws IOException {
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOf(signInButton));
		signInButton.click();
		return this;
	}

	public ContactListPage LogIn() throws Exception {
		confirmSignInButton.click();
		return new ContactListPage(url, path);
	}
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) throws Exception {
		refreshUITree();
		if(CommonUtils.getAndroidApiLvl(LoginPage.class) > 42){
			loginInput.sendKeys(login);
		}
		else{
			for(WebElement editField : editText){
				if (editField.getText().toLowerCase().equals("email")){
					editField.sendKeys(login);
				}
			}
		}
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws Exception {
		if(CommonUtils.getAndroidApiLvl(LoginPage.class) > 42){
			passwordInput.click();
			passwordInput.sendKeys(password);
		}
		else{
			for(WebElement editField : editText){
				if (editField.getText().toLowerCase().isEmpty()){
					editField.sendKeys(password);
				}
			}
		}
	}

	public boolean waitForLoginScreenDisappear() {
		return DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.LoginPage.idLoginButton), 40);
	}
	public boolean waitForLogin() {

		return DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.LoginPage.idLoginProgressBar), 40);
	}

	public Boolean isLoginFinished(String contact) throws InterruptedException {
		Boolean flag = false;
		refreshUITree();
		try{
			wait.until(ExpectedConditions.visibilityOf(yourUser));
		}
		catch (Exception ex){
			pickerClearBtn.click();
			refreshUITree();
		}
		
		if(yourUser.getText().toLowerCase().equals(contact.toLowerCase())){
			flag = true;
		}
		//return DriverUtils.waitForElementWithTextByXPath(AndroidLocators.ContactListPage.xpathContacts,contact,driver);
		return flag;
	}

	public Boolean isWelcomeButtonsExist(){
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOfAllElements(welcomeSloganContainer));
		return welcomeSloganContainer.size() > 0;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) {
		// no need to swipe
		return null;
	}

	public RegistrationPage join() throws Exception {
		signUpButton.click();
		DriverUtils.waitUntilElementDissapear(driver, AndroidLocators.LoginPage.getByForLoginPageRegistrationButton());
		return new RegistrationPage(url, path);
	}
	
	public void dismissUpdate() {
		try {
			dismissUpdateButton.click();
		} catch (NoSuchElementException e) {
			
		}
	}
	
	public boolean isDismissUpdateVisible() {
		return DriverUtils.waitUntilElementAppears(driver, By.id(AndroidLocators.CommonLocators.idDismissUpdateButton));
	}
}
