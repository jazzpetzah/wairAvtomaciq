package com.wearezeta.auto.android.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.locators.TabletAndroidLocators;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class TabletDialogPage extends DialogPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = TabletAndroidLocators.TabletDialogPage.CLASS_NAME, locatorKey = "idProfileIcon")
	private WebElement profileButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = TabletAndroidLocators.TabletDialogPage.CLASS_NAME, locatorKey = "idParticipantsClose")
	private WebElement participantsClose;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idParticipantsHeader")
	private WebElement otherUserName;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idLeftActionButton")
	private WebElement addContactBtn;

	public TabletDialogPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isProfileButtonDisplayed() throws Exception {
		try {
		return profileButton.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isOtherUserNameDisplayed() throws Exception {
		try {
			return otherUserName.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public void tapOnProfileButton() {
		getWait().until(ExpectedConditions.visibilityOf(profileButton));
		profileButton.click();
	}

	public void tapOnParticipantsClose() {
		getWait().until(ExpectedConditions.visibilityOf(participantsClose));
		participantsClose.click();
	}

	public OtherUserPersonalInfoPage initOtherUserPersonalInfoPage()
			throws Exception {
		return new OtherUserPersonalInfoPage(getDriver(), getWait());
	}

	public boolean isPopOverDisplayed() throws Exception {
		if (isProfileButtonDisplayed() && isOtherUserNameDisplayed()
				&& addContactBtn.isDisplayed()) {
			return true;
		} else {
			return false;
		}
	}
}
