package registration;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

/**
 * The page that appears directly after a user logs in using an email
 * and password, IF that user has not already attached a phone number
 * to their account.
 * @author deancook
 *
 */

public class AddPhoneNumberPage extends AndroidPage {

	@FindBy(id = AndroidLocators.AddPhoneNumberPage.idNotNowButton)
	private WebElement notNowButton;

	public AddPhoneNumberPage(Future<ZetaAndroidDriver> lazyDriver)
		throws Exception {
		super(lazyDriver);
	}
	
	public boolean waitForAddPhoneNumberAppear() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(AndroidLocators.AddPhoneNumberPage.idNotNowButton), 20);
	}
	
	public void notNowButtonClick() throws Exception {
		notNowButton.click();
	}

}
