package registration;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.common.log.ZetaLogger;

/**
 * This is the very first page that we see when we open a fresh
 * installation of the application - containing the phone number
 * 
 * @author deancook
 */

public class WelcomePage {
	private static final Logger log = ZetaLogger.getLog(LoginPage.class
		.getSimpleName());

	@FindBy(id = AndroidLocators.WelcomePage.phoneInputField)
	private WebElement phoneInputField;
	
	public void inputPhoneNumber(String phoneNumber) {
		
	}
}
