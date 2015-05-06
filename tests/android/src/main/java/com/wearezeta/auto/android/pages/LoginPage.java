package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
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
import com.wearezeta.auto.common.log.ZetaLogger;

public class LoginPage extends AndroidPage {

	private static final Logger log = ZetaLogger.getLog(LoginPage.class
			.getSimpleName());

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPeoplePickerClearbtn")
	private WebElement pickerClearBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idSignInButton")
	private WebElement signInButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idWelcomeSlogan")
	private WebElement welcomeSlogan;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idSignUpButton")
	protected WebElement signUpButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idForgotPass")
	private WebElement forgotPasswordButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idLoginButton")
	protected WebElement confirmSignInButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage .CLASS_NAME, locatorKey = "idSelfUserAvatar")
	protected WebElement selfUserAvatar;
	
	@FindBy(xpath = AndroidLocators.LoginPage.xpathLoginInput)
	private WebElement loginInput;

	@FindBy(xpath = AndroidLocators.LoginPage.xpathPasswordInput)
	private WebElement passwordInput;

	@FindBy(className = AndroidLocators.CommonLocators.classEditText)
	private List<WebElement> editText;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idLoginError")
	private WebElement loginError;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.LoginPage.CLASS_NAME, locatorKey = "idWelcomeSlogan")
	private List<WebElement> welcomeSloganContainer;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idContactListNames")
	private WebElement contactListNames;

	@FindBy(how = How.XPATH, using = AndroidLocators.CommonLocators.xpathDismissUpdateButton)
	private WebElement dismissUpdateButton;

	private static final String LOGIN_ERROR_TEXT = "WRONG ADDRESS OR PASSWORD.\nPLEASE TRY AGAIN.";

	public LoginPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
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

	public LoginPage SignIn() throws Exception {
		refreshUITree();
		// this.getWait().until(ExpectedConditions.visibilityOf(signInButton));
		signInButton.click();
		return this;
	}

	public CommonAndroidPage forgotPassword() throws Exception {
		refreshUITree();
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(forgotPasswordButton));
		forgotPasswordButton.click();
		Thread.sleep(2000);
		if (isVisible(forgotPasswordButton)) {
			DriverUtils.androidMultiTap(this.getDriver(), forgotPasswordButton,
					1, 50);
		}
		return new CommonAndroidPage(this.getLazyDriver());
	}

	public ContactListPage LogIn() throws Exception {
		confirmSignInButton.click();
		return new ContactListPage(this.getLazyDriver());
	}

	public void setLogin(String login) throws Exception {
		refreshUITree();
		if (CommonUtils.getAndroidApiLvl(LoginPage.class) > 42) {
			try {
				loginInput.sendKeys(login);
			} catch (Exception e) {
				log.debug(this.getDriver().getPageSource());

			}
		} else {
			for (WebElement editField : editText) {
				if (editField.getText().toLowerCase().equals("email")) {
					editField.sendKeys(login);
				}
			}
		}
		/*
		 * try { hideKeyboard(); Thread.sleep(1000); } catch (Exception ex) { //
		 * }
		 */
	}

	public void setPassword(String password) throws Exception {
		if (CommonUtils.getAndroidApiLvl(LoginPage.class) > 42) {
			passwordInput.click();
			passwordInput.sendKeys(password);
		} else {
			for (WebElement editField : editText) {
				if (editField.getText().toLowerCase().isEmpty()) {
					editField.sendKeys(password);
				}
			}
		}
	}

	public boolean waitForLoginScreenDisappear() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(AndroidLocators.LoginPage.idLoginButton), 40);
	}

	public boolean waitForLogin() throws Exception {
		return DriverUtils
				.waitUntilLocatorDissapears(this.getDriver(),
						AndroidLocators.LoginPage
								.getByForLoginPageRegistrationButton(), 40);
	}

	public Boolean isLoginFinished()
			throws NumberFormatException, Exception {
		refreshUITree();
		// some workarounds for AN-1973
		try {
			this.getWait().until(ExpectedConditions.visibilityOf(pickerClearBtn));
			pickerClearBtn.click();
			
		} catch (Exception ex) {
			refreshUITree();
			/*if (isVisible(pickerClearBtn)) {
				pickerClearBtn.click();
			}*/
			this.getWait().until(ExpectedConditions.visibilityOf(selfUserAvatar));
			refreshUITree();
		}
		return (isVisible(selfUserAvatar));
	}

	public Boolean isWelcomeButtonsExist() throws Exception {
		refreshUITree();
		this.getWait().until(
				ExpectedConditions
						.visibilityOfAllElements(welcomeSloganContainer));
		return welcomeSloganContainer.size() > 0;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) {
		// no need to swipe
		return null;
	}

	public RegistrationPage join() throws Exception {
		signUpButton.click();
		DriverUtils
				.waitUntilLocatorDissapears(this.getDriver(),
						AndroidLocators.LoginPage
								.getByForLoginPageRegistrationButton());
		return new RegistrationPage(this.getLazyDriver());
	}

	public boolean isDismissUpdateVisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorAppears(
						this.getDriver(),
						By.xpath(AndroidLocators.CommonLocators.xpathDismissUpdateButton));
	}
}
