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

public class EmailSignInPage extends AndroidPage {
	private static final Logger log = ZetaLogger.getLog(LoginPage.class
		.getSimpleName());
	
	@FindBy(id = AndroidLocators.EmailSignInPage.idLoginInput)
	private WebElement loginInput;
	
	@FindBy(id = AndroidLocators.EmailSignInPage.idPasswordInput)
	private WebElement passwordInput;
	
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
}
