package registration;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

/**
 * This is the very first page that we see when we open a fresh
 * installation of the application - containing the phone number
 * 
 * @author deancook
 */

public class WelcomePage extends AndroidPage {
	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(LoginPage.class
		.getSimpleName());

	@FindBy(id = AndroidLocators.WelcomePage.phoneInputField)
	private WebElement phoneInputField;

	@FindBy(id = AndroidLocators.WelcomePage.idHaveAccountButton)
	protected WebElement haveAccountButton;

	@FindBy(id = AndroidLocators.WelcomePage.idWelcomeSlogan)
	private List<WebElement> welcomeSloganContainer;
	
	public WelcomePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
	
	public void inputPhoneNumber(String phoneNumber) {
		//TODO implement
	}
	
	public EmailSignInPage clickIHaveAnAccount() throws Exception {
		if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
			By.id(AndroidLocators.WelcomePage.idHaveAccountButton))) {
			haveAccountButton.click();
		}
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.EmailSignInPage.idLoginInput));
		return new EmailSignInPage(this.getLazyDriver());
	}
	
	public boolean waitForInitialScreen() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(AndroidLocators.WelcomePage.idWelcomeSlogan));
	}
}
