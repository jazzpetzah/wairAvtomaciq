package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class PeoplePickerPage extends WebPage {

	private static final Logger log = ZetaLogger.getLog(PeoplePickerPage.class
			.getSimpleName());

	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.PeoplePickerPage.classNameSearchInput)
	private WebElement searchInput;

	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.PeoplePickerPage.classNameCreateConversationButton)
	private WebElement createConversationButton;

	public PeoplePickerPage(String URL, String path) throws Exception {
		super(URL, path);
	}

	public void searchForUser(String searchText) {
		DriverUtils.waitUntilElementClickable(driver, searchInput);
		searchInput.clear();
		searchInput.sendKeys(searchText);
	}

	public void selectUserFromSearchResult(String user) {
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

	public void createConversation() {
		createConversationButton.click();
	}
}
