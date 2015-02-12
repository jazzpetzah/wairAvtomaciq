package com.wearezeta.auto.android.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import android.view.KeyEvent;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.*;
import com.wearezeta.auto.common.locators.*;

public class SettingsPage extends AndroidPage {

	@FindBy(how = How.XPATH, using = AndroidLocators.SettingsPage.xpathSettingPageTitle)
	private WebElement settingsTitle;
	
	@FindBy(xpath=AndroidLocators.Chrome.ForgotPasswordPage.xpathEditField)
	private WebElement editField;
	
	@FindBy(xpath=AndroidLocators.Chrome.ForgotPasswordPage.xpathChangePasswordButton)
	private WebElement changePassswordButton;
	
	@FindBy(xpath=AndroidLocators.Chrome.xpathChrome)
	private WebElement chromeBrowser;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.Chrome.CLASS_NAME, locatorKey = "idUrlBar")
	private WebElement urlBar;
	
	private static final String SERVER_URL = "https://staging-website.wire.com/forgot/";
	
	public SettingsPage(String URL, String path) throws Exception {
		super(URL, path);
		// TODO Auto-generated constructor stub
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isSettingsPageVisible() {
		
		return settingsTitle.isDisplayed();
	}
	
	public void requestResetPassword(String email) throws Exception{
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOf(editField));
		if(CommonUtils.getAndroidApiLvl(RegistrationPage.class) < 43){
			int ln = urlBar.getText().length();
			urlBar.click();
			for(int i = 0; i < ln; i++){
				driver.sendKeyEvent(KeyEvent.KEYCODE_DEL);
			}
		}
		else{
			urlBar.clear();
		}
		urlBar.sendKeys(SERVER_URL);
		driver.sendKeyEvent(KeyEvent.KEYCODE_ENTER);
		wait.until(ExpectedConditions.visibilityOf(editField));
		editField.click();
		editField.sendKeys(email);
		driver.sendKeyEvent(KeyEvent.KEYCODE_ENTER);
	}

	public PeoplePickerPage resetByLink(String link, String newPass) throws Exception {
		refreshUITree();
		if(CommonUtils.getAndroidApiLvl(RegistrationPage.class) < 43){
			int ln = urlBar.getText().length();
			urlBar.click();
			for(int i = 0; i < ln; i++){
				driver.sendKeyEvent(KeyEvent.KEYCODE_DEL);
			}
		}
		else{
			urlBar.clear();
		}
		urlBar.sendKeys(link);
		driver.sendKeyEvent(KeyEvent.KEYCODE_ENTER);
		editField.click();
		editField.sendKeys(newPass);
		driver.sendKeyEvent(KeyEvent.KEYCODE_ENTER);
		return null;
	}

}
