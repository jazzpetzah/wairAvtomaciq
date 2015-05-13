package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import android.view.KeyEvent;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class CommonAndroidPage extends AndroidPage {

	public CommonAndroidPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@FindBy(id = AndroidLocators.Gmail.idSubject)
	private WebElement gmailSubject;

	@FindBy(id = AndroidLocators.Gmail.idBoby)
	private WebElement gmailContent;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.Browsers.CLASS_NAME, locatorKey = "idFirefoxUrlBar")
	private WebElement urlBar;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.Browsers.CLASS_NAME, locatorKey = "idFirefoxUrlBarEditText")
	private WebElement urlBarEditText;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.Browsers.CLASS_NAME, locatorKey = "idUrlBar")
	private WebElement chromeUrlBar;

	@FindBy(xpath = AndroidLocators.Browsers.ForgotPasswordPage.xpathEmailEditField)
	private WebElement emailEditField;

	@FindBy(xpath = AndroidLocators.Browsers.ForgotPasswordPage.xpathEnterNewPasswordEditField)
	private WebElement enterNewPasswordEditField;
	
	@FindBy(xpath = AndroidLocators.Browsers.ForgotPasswordPage.xpathChangePasswordButton)
	private WebElement changePassswordButton;

	@FindBy(xpath = AndroidLocators.Browsers.xpathChrome)
	private WebElement chromeBrowser;

	private static final String SERVER_URL = "https://staging-website.zinfra.io/forgot/";

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public PeoplePickerPage activateByLink(String link) throws Exception {
		openFirefoxBrowser();
		Thread.sleep(5000);
		setFirefoxBrowserURL(link);
		return new PeoplePickerPage(this.getLazyDriver());
	}

	public void ConnectByInvitationLink(String link) throws Exception {
		openFirefoxBrowser();
		setFirefoxBrowserURL("wire://connect?code=" + link);
	}

	public void requestResetPassword(String email) throws Exception {
		refreshUITree();
		this.getWait()
				.until(ExpectedConditions.elementToBeClickable(emailEditField));
		setChromeBrowserURL(SERVER_URL);
		this.getWait().until(ExpectedConditions.visibilityOf(emailEditField));
		emailEditField.click();
		emailEditField.sendKeys(email);
		this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_ENTER);
	}

	public PeoplePickerPage resetByLink(String link, String newPass)
			throws Exception {
		setChromeBrowserURL(link);
		enterNewPasswordEditField.click();
		enterNewPasswordEditField.sendKeys(newPass);
		this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_ENTER);
		return null;
	}

	private void openFirefoxBrowser() throws Exception {

		this.getDriver().startActivity("org.mozilla.firefox",
				"org.mozilla.firefox.App", "org.mozilla.firefox",
				"org.mozilla.firefox.App");

	}

	private void setChromeBrowserURL(String link) throws Exception {
		refreshUITree();
		if (CommonUtils.getAndroidApiLvl(RegistrationPage.class) < 43) {
			int ln = chromeUrlBar.getText().length();
			chromeUrlBar.click();
			for (int i = 0; i < ln; i++) {
				this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_DEL);
			}
		} else {
			chromeUrlBar.clear();
		}
		chromeUrlBar.sendKeys(link);
		this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_ENTER);
	}

	private void setFirefoxBrowserURL(String link) throws Exception {
		refreshUITree();
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(AndroidLocators.Browsers.idFirefoxUrlBar));
		urlBar.click();
		for (int i = 0; i < 10; i++) {
			this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_DEL);
		}
		urlBarEditText.sendKeys(link);
		this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_ENTER);
	}

	public String getGmailSubject() throws Exception {
		getWait().until(ExpectedConditions.visibilityOf(gmailSubject));
		return gmailSubject.getText();
	}

	public boolean mailContains(String email) {
		return gmailContent.getText().contains(email);
	}

}
