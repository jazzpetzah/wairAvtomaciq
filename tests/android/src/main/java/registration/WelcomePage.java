package registration;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

/**
 * This is the very first page that we see when we open a fresh
 * installation of the application - containing the phone number
 * 
 * @author deancook
 */

public class WelcomePage extends AndroidPage {
	private static final Logger log = ZetaLogger.getLog(LoginPage.class
		.getSimpleName());

	@FindBy(id = AndroidLocators.WelcomePage.phoneInputField)
	private WebElement phoneInputField;

	@FindBy(id = AndroidLocators.LoginPage.idHaveAccountButton)
	protected WebElement haveAccountButton;
	
	public WelcomePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
	
	public void inputPhoneNumber(String phoneNumber) {
		
	}
	
	public EmailSignInPage clickIHaveAnAccount() throws Exception {
		if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
			By.id(AndroidLocators.LoginPage.idHaveAccountButton))) {
			haveAccountButton.click();
		}
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.LoginPage.idLoginInput));
		return new EmailSignInPage(this.getLazyDriver());
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
