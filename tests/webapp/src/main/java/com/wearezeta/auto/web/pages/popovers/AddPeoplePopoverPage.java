package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

class AddPeoplePopoverPage extends AbstractPopoverPage {
	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.PeoplePickerPage.classNameCreateConversationButton)
	private WebElement createConversationButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathProfilePageSearchField)
	private WebElement profilePageSearchField;

	public AddPeoplePopoverPage(ZetaWebAppDriver driver, WebDriverWait wait,
			PeoplePopoverContainer container) throws Exception {
		super(driver, wait, container);
	}

	public void searchForUser(String searchText) throws Exception {
		DriverUtils.waitUntilElementClickable(driver, profilePageSearchField);
		profilePageSearchField.clear();
		profilePageSearchField.sendKeys(searchText);
	}

	public void createConversation() throws Exception {
		DriverUtils.waitUntilElementClickable(driver, createConversationButton);
		createConversationButton.click();
	}
}
