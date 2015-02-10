package com.wearezeta.auto.web.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class UserProfilePopup extends WebPage {

	@FindBy(how = How.ID, using = WebAppLocators.UserProfilePopupPage.idUserProfilePage)
	private WebElement userProfilePopup;

	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathUserName)
	private WebElement userName;

	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathNameAddPeopleButton)
	private WebElement addPeopleButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathNameBlockButton)
	private WebElement blockButton;
	
	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathLeaveGroupChat)
	private WebElement leaveButton;
	
	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathConfirmLeaveButton)
	private WebElement confirmLeaveButton;

	public UserProfilePopup(String URL, String path) throws Exception {
		super(URL, path);

	}
	
	public void leaveGroupChat() {
		wait.until(ExpectedConditions.elementToBeClickable(leaveButton));
		leaveButton.click();
	}
	
	public void confirmLeaveGroupChat() {
		wait.until(ExpectedConditions.elementToBeClickable(confirmLeaveButton));
		confirmLeaveButton.click();
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
