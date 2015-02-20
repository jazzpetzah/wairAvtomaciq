package com.wearezeta.auto.web.pages;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
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
	

	@SuppressWarnings("unused")
	private String url;
	@SuppressWarnings("unused")
	private String path;
	
	public UserProfilePopupPage(String URL, String path) throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	public boolean isUserProfilePopupPageVisible() {
		return DriverUtils.waitUntilElementAppears(driver, userProfilePopup, 10);
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
