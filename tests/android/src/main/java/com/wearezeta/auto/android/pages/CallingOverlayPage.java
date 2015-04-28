package com.wearezeta.auto.android.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class CallingOverlayPage extends AndroidPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlayPage.CLASS_NAME, locatorKey = "idCallingOverlayContainer")
	private WebElement callingOverlayContainer;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlayPage.CLASS_NAME, locatorKey = "idIgnoreButton")
	private WebElement ignoreButton;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlayPage.CLASS_NAME, locatorKey = "idAcceptButton")
	private WebElement acceptButton;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlayPage.CLASS_NAME, locatorKey = "idCallingUsersName")
	private WebElement callingUsersName;

	public CallingOverlayPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		return null;
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(), callingOverlayContainer);
	}

	public void muteConversation() {
		ignoreButton.click();
	}

	public String getCallersName() {
		return callingUsersName.getText();
	}

	public void acceptCall() {
		acceptButton.click();
	}

}
