package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

/**
 * This page represents the email sign in screen, which you can reach
 * from the welcome screen. From here, you can choose to go to the phone
 * signin page, go back to the welcome screen, or log in with an existing
 * email and password
 * 
 * @author deancook
 *
 */
public class EmailSignInPage extends AndroidPage {
	private static final Logger log = ZetaLogger.getLog(LoginPage.class
		.getSimpleName());
	
	public static final String idLoginInput = "get__sign_in__email";
	@FindBy(id = idLoginInput)
	private WebElement loginInput;
	
	public static final String idPasswordInput = "get__sign_in__password";
	@FindBy(id = idPasswordInput)
	private WebElement passwordInput;
	
	public static final String idLoginButton = "pcb__signin__email";
	@FindBy(id = idLoginButton)
	protected WebElement confirmSignInButton;
	
	public EmailSignInPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
	
	public void setLogin(String login) throws Exception {
		try {
			loginInput.sendKeys(login);
		} catch (Exception e) {
			log.debug(this.getDriver().getPageSource());
			throw e;
		}
	}

	public void setPassword(String password) throws Exception {
		passwordInput.click();
		passwordInput.sendKeys(password);
	}
	
	/**
	 * If the user already has a phone number attached to their account,
	 * this page will go directly to the start UI, or else it will return
	 * a page in which the user is asked to add a phone number
	 * @return either a {@link AddPhoneNumberPage} or {@link ContactListPage}
	 * @throws Exception
	 */
	public AndroidPage logIn() throws Exception {
		confirmSignInButton.click();
		
		if (waitForAddPhoneNumberAppear()) {
			return new AddPhoneNumberPage(this.getLazyDriver());
		} else {
			return new ContactListPage(this.getLazyDriver());
		}
	}
	
	private boolean waitForAddPhoneNumberAppear() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(AddPhoneNumberPage.idNotNowButton), 2);
	}
}
