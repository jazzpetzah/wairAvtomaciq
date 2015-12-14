package com.wearezeta.auto.android.pages.registration;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

/**
 * This is the very first page that we see when we open a fresh installation of
 * the application - containing the phone number
 * 
 * @author deancook
 */

public class WelcomePage extends AndroidPage {
	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(WelcomePage.class
			.getSimpleName());

	public static final String idphoneInputField = "et__reg__phone";
	@FindBy(id = idphoneInputField)
	private WebElement phoneInputField;

	public static final String xpathSignInTab = "//*[@id='til__app_entry']//*[*][1]";
	@FindBy(xpath = xpathSignInTab)
	protected WebElement signInTab;

	public static final String idWelcomeSlogan = "tv__welcome__terms_of_service";
	@FindBy(id = idWelcomeSlogan)
	private List<WebElement> welcomeSloganContainer;

	public static final String idAreaCodeSelector = "tv__country_code";
	@FindBy(id = idAreaCodeSelector)
	protected WebElement areaCodeSelectorButton;

	public static final String idPhoneConfirmationButton = "pcb__signup";
	@FindBy(id = idPhoneConfirmationButton)
	protected WebElement phoneConfirmationButton;

	public WelcomePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void inputPhoneNumber(String phoneNumber) {
		phoneInputField.clear();
		phoneInputField.sendKeys(phoneNumber);
	}

	public PhoneNumberVerificationPage clickConfirm() throws Exception {
		phoneConfirmationButton.click();
		return new PhoneNumberVerificationPage(this.getLazyDriver());
	}

	public void tapSignInTab() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathSignInTab), 30);
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				signInTab);
		signInTab.click();
	}

	public boolean waitForInitialScreen() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(idWelcomeSlogan), 30);
	}

	public AreaCodePage clickAreaCodeSelector() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idAreaCodeSelector));
		areaCodeSelectorButton.click();
		return new AreaCodePage(this.getLazyDriver());
	}
}
