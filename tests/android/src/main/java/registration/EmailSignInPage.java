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
	
	public EmailSignInPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
