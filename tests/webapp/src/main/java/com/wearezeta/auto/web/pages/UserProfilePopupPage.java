package com.wearezeta.auto.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class UserProfilePopupPage extends ConversationPopupPage {

	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathUserName)
	private WebElement userName;

	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathAddPeopleButton)
	private WebElement addPeopleButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathNameBlockButton)
	private WebElement blockButton;

	public UserProfilePopupPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isUserProfilePopupPageVisible() throws Exception {
		return this.isConversationPopupPageVisible();
	}

	public String getUserName() {
		return userName.getText();
	}

	public boolean isAddPeopleButtonVisible() throws Exception {
		return DriverUtils
				.isElementDisplayed(
						this.getDriver(),
						By.xpath(WebAppLocators.UserProfilePopupPage.xpathAddPeopleButton),
						5);
	}

	@Override
	public void clickAddPeopleButton() throws Exception {
		DriverUtils.waitUntilElementClickable(driver, addPeopleButton);
		addPeopleButton.click();
	}

	public boolean isBlockButtonVisible() throws Exception {
		return DriverUtils
				.isElementDisplayed(
						this.getDriver(),
						By.xpath(WebAppLocators.UserProfilePopupPage.xpathNameBlockButton));
	}
}
