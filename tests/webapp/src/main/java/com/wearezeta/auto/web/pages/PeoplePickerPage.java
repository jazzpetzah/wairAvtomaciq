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
import com.wearezeta.auto.web.pages.popovers.ConnectToPopoverContainer;

public class PeoplePickerPage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(PeoplePickerPage.class
			.getSimpleName());

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathNameSearchInput)
	private WebElement searchInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathNameCreateConversationButton)
	private WebElement openOrCreateConversationButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathCloseSearchButton)
	private WebElement closeSearchButton;

	public PeoplePickerPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void createConversation() throws Exception {
		DriverUtils.waitUntilElementClickable(driver,
				openOrCreateConversationButton);
		openOrCreateConversationButton.click();
	}

	private void clickNotConnectedUser(String name) {
		String foundUserXpath = WebAppLocators.PeoplePickerPage.xpathSearchResultByName
				.apply(name);
		WebElement foundUserElement = driver.findElement(By
				.xpath(foundUserXpath));
		foundUserElement.click();
	}

	public ConnectToPopoverContainer clickNotConnectedUserName(String name)
			throws Exception {
		clickNotConnectedUser(name);
		PagesCollection.popoverPage = new ConnectToPopoverContainer(
				this.getDriver(), this.getWait());
		return (ConnectToPopoverContainer) PagesCollection.popoverPage;
	}

	public boolean isUserFound(String name) throws Exception {
		String foundUserXpath = WebAppLocators.PeoplePickerPage.xpathSearchResultByName
				.apply(name);
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.xpath(foundUserXpath), 3);
	}

	public void closeSearch() throws Exception {
		assert DriverUtils.isElementDisplayed(driver, By
				.xpath(WebAppLocators.PeoplePickerPage.xpathCloseSearchButton),
				5);
		assert DriverUtils.waitUntilElementClickable(driver, closeSearchButton);
		closeSearchButton.click();
	}

	public boolean isParticipantVisible(String name) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.PeoplePickerPage.xpathSearchResultByName
						.apply(name));
		return DriverUtils.isElementDisplayed(driver, locator, 5);
	}

	public void clickOnParticipant(String name) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.PeoplePickerPage.xpathSearchResultByName
						.apply(name));
		assert DriverUtils.isElementDisplayed(driver, locator, 3);
		WebElement participant = driver.findElement(locator);
		assert DriverUtils.waitUntilElementClickable(driver, participant);
		participant.click();
	}

	public void selectUserFromSearchResult(String user) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.PeoplePickerPage.xpathSearchResultByName
						.apply(user));
		assert DriverUtils.isElementDisplayed(driver, locator,
				DriverUtils.DEFAULT_VISIBILITY_TIMEOUT);
		WebElement userEl = driver.findElement(locator);
		assert DriverUtils.waitUntilElementClickable(driver, userEl);
		userEl.click();
	}

	public void clickCreateConversation() {
		openOrCreateConversationButton.click();
	}

	public void searchForUser(String name) {
		searchInput.sendKeys(name);
	}
}
