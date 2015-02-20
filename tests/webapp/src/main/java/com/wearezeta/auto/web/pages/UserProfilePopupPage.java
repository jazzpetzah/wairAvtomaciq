package com.wearezeta.auto.web.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class UserProfilePopupPage extends WebPage {

	@FindBy(how = How.ID, using = WebAppLocators.UserProfilePopupPage.idUserProfilePage)
	private WebElement userProfilePopup;

	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathUserName)
	private WebElement userName;

	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathNameAddPeopleButton)
	private WebElement addPeopleButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathNameBlockButton)
	private WebElement blockButton;

	public UserProfilePopupPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isUserProfilePopupPageVisible() {
		return DriverUtils.isElementDisplayed(userProfilePopup);
	}

	public String getUserName() {
		return userName.getText();
	}

	public boolean isAddPeopleButtonVisible() {
		return DriverUtils.isElementDisplayed(addPeopleButton);
	}

	public boolean isBlockButtonVisible() {
		return DriverUtils.isElementDisplayed(blockButton);
	}
}
