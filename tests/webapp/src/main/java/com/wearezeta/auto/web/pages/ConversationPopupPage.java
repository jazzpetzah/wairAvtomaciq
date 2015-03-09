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

public abstract class ConversationPopupPage extends WebPage {

	private static final Logger log = ZetaLogger
			.getLog(ConversationPopupPage.class.getSimpleName());

	@FindBy(how = How.ID, using = WebAppLocators.ConversationPopupPage.idConversationPopupPage)
	protected WebElement conversationPopup;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPopupPage.xpathAddPeopleMessage)
	private WebElement addPeopleMessage;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPopupPage.xpathConfirmAddButton)
	private WebElement confirmAddButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPopupPage.xpathProfilePageSearchField)
	private WebElement profilePageSearchField;

	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.PeoplePickerPage.classNameCreateConversationButton)
	private WebElement createConversationButton;

	public ConversationPopupPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isConversationPopupPageVisible() throws Exception {
		return DriverUtils
				.waitUntilElementAppears(
						driver,
						By.id(WebAppLocators.ConversationPopupPage.idConversationPopupPage),
						10);
	}

	public boolean isAddPeopleMessageShown() {
		return addPeopleMessage.isDisplayed();
	}

	public void confirmAddPeople() throws Exception {
		DriverUtils.waitUntilElementClickable(driver, confirmAddButton);
		confirmAddButton.click();
	}

	public abstract void clickAddPeopleButton() throws Exception;

	public void searchForUser(String searchText) throws Exception {
		DriverUtils.waitUntilElementClickable(driver, profilePageSearchField);
		profilePageSearchField.clear();
		profilePageSearchField.sendKeys(searchText);
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

	public void clickCreateConversation() {
		createConversationButton.click();
	}
}
