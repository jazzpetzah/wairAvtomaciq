package com.wearezeta.auto.web.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.Browser;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class PeoplePickerPage extends WebPage {

	private static final Logger log = ZetaLogger.getLog(PeoplePickerPage.class
			.getSimpleName());

	@FindBy(how = How.CSS, using = WebAppLocators.PeoplePickerPage.cssNameSearchInput)
	private WebElement searchInput;

	@FindBy(how = How.CSS, using = WebAppLocators.PeoplePickerPage.cssOpenOrCreateConversationButton)
	private WebElement openOrCreateConversationButton;

	@FindBy(how = How.CSS, using = WebAppLocators.PeoplePickerPage.cssCallButton)
	private WebElement callButton;

	@FindBy(css = WebAppLocators.PeoplePickerPage.cssCloseSearchButton)
	private WebElement closeSearchButton;

	@FindBy(how = How.CSS, using = WebAppLocators.PeoplePickerPage.cssBringFriendsFromGMailButton)
	private WebElement bringFriendsFromGmailButton;

	@FindBy(how = How.CSS, using = WebAppLocators.PeoplePickerPage.cssInvitePeopleButton)
	private WebElement invitePeopleButton;

	@FindBy(xpath = "//*[contains(@class,'people-picker-list-suggestions')]//div[@data-uie-name='item-user']")
	private List<WebElement> suggestions;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathSelectedTopPeopleList)
	private List<WebElement> selectedTopPeopleItemLocator;

	@FindBy(css = WebAppLocators.PeoplePickerPage.cssMoreButton)
	private WebElement moreButton;

	@FindBy(xpath = "//*[contains(@class,'search-list search-list-sm')]//div[@data-uie-name='item-user']")
	private List<WebElement> topPeople;

	public PeoplePickerPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void createConversation() throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				openOrCreateConversationButton);
		openOrCreateConversationButton.click();
	}

	public void clickNotConnectedUserName(String name) throws Exception {
		String foundUserXpath = WebAppLocators.PeoplePickerPage.xpathSearchResultByName
				.apply(name);
		WebElement foundUserElement = getDriver().findElement(
				By.xpath(foundUserXpath));
		foundUserElement.click();
	}

	public boolean isUserFound(String name) throws Exception {
		String foundUserXpath = WebAppLocators.PeoplePickerPage.xpathSearchResultByName
				.apply(name);
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.xpath(foundUserXpath));
	}

	public boolean isUserNotFound(String name) throws Exception {
		String foundUserXpath = WebAppLocators.PeoplePickerPage.xpathSearchResultByName
				.apply(name);
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.xpath(foundUserXpath));
	}

	public void closeSearch() throws Exception {
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
				locator, 5);
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

	public void waitUntilInvitePeopleButtonIsVisible() throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.cssSelector(WebAppLocators.PeoplePickerPage.cssInvitePeopleButton)) : "Bring Your Friends button is not visible";
	}

	public void clickInvitePeopleButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				invitePeopleButton);
		invitePeopleButton.click();
	}

	public void clickBringFriendsFromGmailButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				bringFriendsFromGmailButton);
		bringFriendsFromGmailButton.click();
	}

	public void switchToGooglePopup() throws Exception {
		WebDriver driver = this.getDriver();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(
				DriverUtils.getDefaultLookupTimeoutSeconds(), TimeUnit.SECONDS)
				.pollingEvery(1, TimeUnit.SECONDS);
		try {
			wait.until(drv -> {
				return (drv.getWindowHandles().size() > 1);
			});
		} catch (TimeoutException e) {
			throw new TimeoutException("No Popup for Google was found", e);
		}
		Set<String> handles = driver.getWindowHandles();
		handles.remove(driver.getWindowHandle());
		driver.switchTo().window(handles.iterator().next());
	}

	public void clickCallButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), callButton);
		callButton.click();
	}

	public int getNumberOfSuggestions() {
		return suggestions.size();
	}

	public void clickRemoveButtonOnSuggestion(String user) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.PeoplePickerPage.xpathSearchResultByName
						.apply(user));
		if (WebAppExecutionContext.getBrowser()
				.isSupportingNativeMouseActions()) {
			DriverUtils.moveMouserOver(this.getDriver(), getDriver()
					.findElement(locator));
		} else {
			// safari
			DriverUtils.addClass(this.getDriver(), this.getDriver()
					.findElement(locator), "hover");
		}
		getDriver()
				.findElement(
						By.cssSelector(WebAppLocators.PeoplePickerPage.cssDismissIconByName
								.apply(user))).click();
	}

	public void clickPlusButtonOnSuggestion(String user) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.PeoplePickerPage.xpathSearchResultByName
						.apply(user));
		if (this.getDriver().getCapabilities().getBrowserName()
				.equals(Browser.Safari.toString())) {
			log.debug("safari workaround");
			DriverUtils.addClass(this.getDriver(), this.getDriver()
					.findElement(locator), "hover");
		} else {
			DriverUtils.moveMouserOver(this.getDriver(), getDriver()
					.findElement(locator));
		}
		getDriver().findElement(
				By.cssSelector(WebAppLocators.PeoplePickerPage.cssAddIconByName
						.apply(user))).click();
		DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
	}

	public void clickPendingUserName(String name) throws Exception {
		clickPendingUser(name);
	}

	private void clickPendingUser(String name) throws Exception {
		String foundPendingUserXpath = WebAppLocators.PeoplePickerPage.xpathSearchPendingResultByName
				.apply(name);
		WebElement foundPendingUserElement = getDriver().findElement(
				By.xpath(foundPendingUserXpath));
		foundPendingUserElement.click();
	}

	public boolean isTopPeopleLabelVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.xpath(WebAppLocators.PeoplePickerPage.xpathTopPeople));
	}

	public void clickNameInTopPeople(String name) throws Exception {
		String topPeopleItemLocator = WebAppLocators.PeoplePickerPage.cssTopPeopleListByName
				.apply(name);
		DriverUtils.isElementPresentAndDisplayed(getDriver(), getDriver()
				.findElement(By.cssSelector(topPeopleItemLocator)));
		getDriver().findElement(By.cssSelector(topPeopleItemLocator)).click();
	}

	public ArrayList<String> getNamesOfSelectedTopPeople() throws Exception {
		ArrayList<String> namesOfSelectedTopPeople = new ArrayList<String>();
		final By selectedTopPeopleItemLocator = By
				.xpath(WebAppLocators.PeoplePickerPage.xpathSelectedTopPeopleList);
		for (WebElement element : getDriver().findElements(
				selectedTopPeopleItemLocator)) {
			namesOfSelectedTopPeople
					.add(element.getAttribute("data-uie-value"));
		}
		return namesOfSelectedTopPeople;
	}

	public boolean isSearchOpened() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.cssSelector(WebAppLocators.PeoplePickerPage.cssSearchField));
	}

	public boolean waitForSearchFieldToBeEmpty() throws Exception {
		By locator = By
				.cssSelector(WebAppLocators.PeoplePickerPage.cssSearchField);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
				.withTimeout(DriverUtils.getDefaultLookupTimeoutSeconds(),
						TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		return wait.until(drv -> {
			try {
				String value = drv.findElement(locator).getAttribute("value");
				return (value.equals(""));
			} catch (WebDriverException e) {
				return true;
			}
		});
	}

	public boolean isGroupConversationFound(String name) throws Exception {
		String foundGroupXpath = WebAppLocators.PeoplePickerPage.xpathSearchResultGroupByName
				.apply(name);
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.xpath(foundGroupXpath));
	}

	public boolean isGroupConversationNotFound(String name) throws Exception {
		String foundGroupXpath = WebAppLocators.PeoplePickerPage.xpathSearchResultGroupByName
				.apply(name);
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.xpath(foundGroupXpath));
	}

	public boolean isMoreButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.cssSelector(WebAppLocators.PeoplePickerPage.cssMoreButton));
	}

	public void clickMoreButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), moreButton);
		moreButton.click();
	}

	public int getNumberOfTopPeople() {
		return topPeople.size();
	}
}
