package com.wearezeta.auto.web.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.popovers.AbstractPopoverContainer;
import com.wearezeta.auto.web.pages.popovers.ConnectToPopoverContainer;
import com.wearezeta.auto.web.pages.popovers.SendInvitationPopoverContainer;

public class PeoplePickerPage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(PeoplePickerPage.class
			.getSimpleName());

	@FindBy(how = How.CSS, using = WebAppLocators.PeoplePickerPage.cssNameSearchInput)
	private WebElement searchInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathNameCreateConversationButton)
	private WebElement openOrCreateConversationButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathCloseSearchButton)
	private WebElement closeSearchButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathSendInvitationButton)
	private WebElement sendInvitationButton;

	@FindBy(xpath = "//*[contains(@class,'people-picker-list-suggestions')]//div[@data-uie-name='item-user']")
	private List<WebElement> suggestions;

	public PeoplePickerPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void createConversation() throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				openOrCreateConversationButton);
		openOrCreateConversationButton.click();
	}

	private void clickNotConnectedUser(String name) throws Exception {
		String foundUserXpath = WebAppLocators.PeoplePickerPage.xpathSearchResultByName
				.apply(name);
		WebElement foundUserElement = getDriver().findElement(
				By.xpath(foundUserXpath));
		foundUserElement.click();
	}

	public AbstractPopoverContainer clickNotConnectedUserName(String name)
			throws Exception {
		clickNotConnectedUser(name);
		PagesCollection.popoverPage = new ConnectToPopoverContainer(
				this.getLazyDriver());
		return (ConnectToPopoverContainer) PagesCollection.popoverPage;
	}

	public boolean isUserFound(String name) throws Exception {
		String foundUserXpath = WebAppLocators.PeoplePickerPage.xpathSearchResultByName
				.apply(name);
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.xpath(foundUserXpath), 3);
	}

	public void closeSearch() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By
				.xpath(WebAppLocators.PeoplePickerPage.xpathCloseSearchButton),
				5);
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				closeSearchButton);
		closeSearchButton.click();
	}

	public boolean isParticipantVisible(String name) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.PeoplePickerPage.xpathSearchResultByName
						.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, 5);
	}

	public void clickOnParticipant(String name) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.PeoplePickerPage.xpathSearchResultByName
						.apply(name));
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, 3);
		WebElement participant = getDriver().findElement(locator);
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				participant);
		participant.click();
	}

	public void selectUserFromSearchResult(String user) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.PeoplePickerPage.xpathSearchResultByName
						.apply(user));
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator);
		WebElement userEl = getDriver().findElement(locator);
		assert DriverUtils.waitUntilElementClickable(this.getDriver(), userEl);
		userEl.click();
	}

	public void clickCreateConversation() {
		openOrCreateConversationButton.click();
	}

	public void searchForUser(String name) throws Exception {
		if (!WebCommonUtils.isElementFocused(this.getDriver(),
				WebAppLocators.PeoplePickerPage.cssNameSearchInput)) {
			WebCommonUtils.setFocusToElement(this.getDriver(),
					WebAppLocators.PeoplePickerPage.cssNameSearchInput);
		}
		searchInput.sendKeys(name);
	}

	public boolean isVisibleAfterTimeout(int secondsTimeout) throws Exception {
		Thread.sleep(secondsTimeout * 1000);
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.className(WebAppLocators.PeoplePickerPage.classNamePeoplePickerVisible));
	}

	private static final int BUTTON_VISIBILITY_TIMEOUT_SECONDS = 5;

	public void waitUntilSendInvitationButtonIsVisible() throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.xpath(WebAppLocators.PeoplePickerPage.xpathSendInvitationButton),
						BUTTON_VISIBILITY_TIMEOUT_SECONDS) : "Send Invitation button is not visible after "
				+ BUTTON_VISIBILITY_TIMEOUT_SECONDS + " seconds timeout";
	}

	public SendInvitationPopoverContainer clickSendInvitationButton()
			throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				sendInvitationButton);
		sendInvitationButton.click();

		return new SendInvitationPopoverContainer(this.getLazyDriver());
	}

	public int getNumberOfSuggestions() {
		return suggestions.size();
	}

	public void clickRemoveButtonOnSuggestion(String user) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.PeoplePickerPage.xpathSearchResultByName
						.apply(user));
		try {
			DriverUtils.moveMouserOver(this.getDriver(), getDriver()
					.findElement(locator));
		} catch (WebDriverException e) {
			// Safari workaround
		}
		getDriver()
				.findElement(
						By.cssSelector(WebAppLocators.PeoplePickerPage.cssRemoveIconByName
								.apply(user))).click();
	}
	
	public void clickPlusButtonOnSuggestion(String user) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.PeoplePickerPage.xpathSearchResultByName
						.apply(user));
		try {
			DriverUtils.moveMouserOver(this.getDriver(), getDriver()
					.findElement(locator));
		} catch (WebDriverException e) {
			// Safari workaround
		}
		getDriver()
				.findElement(
						By.cssSelector(WebAppLocators.PeoplePickerPage.cssAddIconByName
								.apply(user))).click();
	}

	public ConnectToPopoverContainer clickPendingUserName(String name)
			throws Exception {
		clickPendingUser(name);
		PagesCollection.popoverPage = new ConnectToPopoverContainer(
				this.getLazyDriver());
		return (ConnectToPopoverContainer) PagesCollection.popoverPage;
		}
	
	private void clickPendingUser(String name) throws Exception {
		String foundPendingUserXpath = WebAppLocators.PeoplePickerPage.xpathSearchPendingResultByName
				.apply(name);
		WebElement foundPendingUserElement = getDriver().findElement(
				By.xpath(foundPendingUserXpath));
		foundPendingUserElement.click();
	}
}
