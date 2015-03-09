package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class PeoplePickerPage extends WebPage {

	private static final Logger log = ZetaLogger.getLog(PeoplePickerPage.class
			.getSimpleName());

	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.PeoplePickerPage.classNameSearchInput)
	private WebElement searchInput;

	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.PeoplePickerPage.classNameCreateConversationButton)
	private WebElement createConversationButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathCloseSearchButton)
	private WebElement closeSearchButton;

	public PeoplePickerPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void searchForUser(String searchText) throws Exception {
		DriverUtils.waitUntilElementClickable(driver, searchInput);
		searchInput.clear();
		searchInput.sendKeys(searchText);
	}

	public void selectUserFromSearchResult(String user) throws Exception {
		String xpath = String
				.format(WebAppLocators.PeoplePickerPage.xpathFormatSearchListItemWithName,
						user);
		WebElement userEl = driver.findElement(By.xpath(xpath));
		boolean isClickable = DriverUtils.waitUntilElementClickable(driver,
				userEl);
		boolean isVisible = DriverUtils.waitUntilElementVisible(driver, userEl);
		log.debug("Found user element is clickable: " + isClickable
				+ ", isVisible: " + isVisible);
		userEl.click();
	}

	public void createConversation() throws Exception {
		DriverUtils.waitUntilElementClickable(driver, createConversationButton);
		createConversationButton.click();
	}

	private void clickNotConnectedUser(String name) {
		String foundUserXpath = WebAppLocators.PeoplePickerPage.xpathSearchResultByName
				.apply(name);
		WebElement foundUserElement = driver.findElement(By
				.xpath(foundUserXpath));
		foundUserElement.click();
	}

	public ConnectToPopupPage clickNotConnectedUserName(String name)
			throws Exception {
		clickNotConnectedUser(name);
		return new ConnectToPopupPage(this.getDriver(), this.getWait());
	}

	public boolean isUserFound(String name) throws Exception {
		String foundUserXpath = WebAppLocators.PeoplePickerPage.xpathSearchResultByName
				.apply(name);
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.xpath(foundUserXpath));
	}

	public void closeSearch() throws Exception {
		DriverUtils.waitUntilElementAppears(driver, By
				.xpath(WebAppLocators.PeoplePickerPage.xpathCloseSearchButton));
		DriverUtils.waitUntilElementClickable(driver, closeSearchButton);
		closeSearchButton.click();
	}
}
