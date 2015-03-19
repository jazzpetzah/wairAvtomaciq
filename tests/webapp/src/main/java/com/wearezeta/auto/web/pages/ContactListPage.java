package com.wearezeta.auto.web.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ContactListPage extends WebPage {

	private static final Logger log = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathContactListEntries)
	private List<WebElement> contactListEntries;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathArchivedContactListEntries)
	private List<WebElement> archivedContactListEntries;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathSelfProfileEntry)
	private WebElement selfName;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathOpenArchivedConvosButton)
	private WebElement openArchivedConvosButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathOpenPeoplePickerButton)
	private WebElement openPeoplePickerButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathIncomingPendingConvoItem)
	private WebElement incomingPendingEntry;

	public ContactListPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	private static final String DEFAULT_GROUP_CONVO_NAMES_SEPARATOR = ",";

	/**
	 * Fixes default group conversation name, because we never know the original
	 * order of participant names in group convo name
	 * 
	 * @param conversationName
	 *            the initial name
	 * @return fixed group convo name, as it is displayed in conversation list.
	 *         'conversationName' is not going to be changed if there are no
	 *         comma character(s)
	 * @throws Exception
	 */
	private String fixDefaultGroupConvoName(String conversationName,
			boolean includeArchived, boolean throwOnError) throws Exception {
		if (conversationName.contains(DEFAULT_GROUP_CONVO_NAMES_SEPARATOR)) {
			final Set<String> initialNamesSet = new HashSet<String>(
					Arrays.asList(conversationName.split(","))).stream()
					.map(x -> x.trim()).collect(Collectors.toSet());
			List<WebElement> convoNamesToCheck = new ArrayList<WebElement>();
			if (DriverUtils
					.isElementDisplayed(
							driver,
							By.xpath(WebAppLocators.ContactListPage.xpathContactListEntries),
							3)) {
				convoNamesToCheck.addAll(contactListEntries);
			}
			if (includeArchived) {
				if (DriverUtils
						.isElementDisplayed(
								driver,
								By.xpath(WebAppLocators.ContactListPage.xpathArchivedContactListEntries))) {
					convoNamesToCheck.addAll(archivedContactListEntries);
				}
			}
			for (WebElement convoItem : convoNamesToCheck) {
				final String convoName = convoItem.getText();
				final Set<String> convoItemNamesSet = new HashSet<String>(
						Arrays.asList(convoName.split(","))).stream()
						.map(x -> x.trim()).collect(Collectors.toSet());
				if (convoItemNamesSet.equals(initialNamesSet)) {
					return convoName;
				}
			}
			if (throwOnError) {
				throw new RuntimeException(String.format(
						"Group conversation '%s' does not exists in the list",
						conversationName));
			} else {
				return null;
			}
		} else {
			return conversationName;
		}
	}

	private String fixDefaultGroupConvoName(String conversationName,
			boolean includeArchived) throws Exception {
		return fixDefaultGroupConvoName(conversationName, includeArchived, true);
	}

	public PeoplePickerPage isHiddenByPeoplePicker() throws Exception {
		if (DriverUtils
				.waitUntilElementAppears(
						driver,
						By.className(WebAppLocators.ContactListPage.classNamePeoplePickerVisible),
						3)) {
			return new PeoplePickerPage(this.getDriver(), this.getWait());
		} else {
			return null;
		}
	}

	public boolean waitForContactListVisible() throws Exception {
		return DriverUtils
				.waitUntilElementAppears(
						driver,
						By.xpath(WebAppLocators.ContactListPage.xpathOpenPeoplePickerButton));
	}

	public boolean isSelfNameEntryExist(String name) throws Exception {
		log.debug("Looking for self name entry '" + name + "'");
		return DriverUtils.isElementDisplayed(driver,
				By.xpath(WebAppLocators.ContactListPage.xpathSelfProfileEntry),
				5);
	}

	public boolean isConvoListEntryWithNameExist(String name) throws Exception {
		log.debug("Looking for contact with name '" + name + "'");
		name = fixDefaultGroupConvoName(name, false, false);
		if (name == null) {
			return false;
		}
		final String xpath = WebAppLocators.ContactListPage.xpathContactListEntryByName
				.apply(name);
		return DriverUtils.isElementDisplayed(driver, By.xpath(xpath), 5);
	}

	public boolean isConvoListEntryNotVisible(String name) throws Exception {
		log.debug("Looking for contact with name '" + name + "'");
		name = fixDefaultGroupConvoName(name, false, false);
		if (name == null) {
			return true;
		}
		final String xpath = WebAppLocators.ContactListPage.xpathContactListEntryByName
				.apply(name);
		return DriverUtils.waitUntilElementDissapear(driver, By.xpath(xpath),
				10);
	}

	public WebElement getContactWithName(String name, boolean includeArchived)
			throws Exception {
		name = fixDefaultGroupConvoName(name, includeArchived);
		final String xpath = WebAppLocators.ContactListPage.xpathContactListEntryByName
				.apply(name);
		return driver.findElement(By.xpath(xpath));
	}

	public void openArchive() {
		this.getWait().until(
				ExpectedConditions
						.elementToBeClickable(openArchivedConvosButton));
		openArchivedConvosButton.click();
	}

	public void clickArchiveConversationForContact(String conversationName)
			throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final By locator = By
				.xpath(WebAppLocators.ContactListPage.xpathArchiveButtonByContactName
						.apply(conversationName));
		assert DriverUtils.isElementDisplayed(driver, locator, 5);
		final WebElement archiveButton = this.getDriver().findElement(locator);
		archiveButton.click();
	}

	public void clickMuteConversationForContact(String conversationName)
			throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final By locator = By
				.xpath(WebAppLocators.ContactListPage.xpathMuteButtonByContactName
						.apply(conversationName));
		assert DriverUtils.isElementDisplayed(driver, locator, 5);
		final WebElement muteButton = this.getDriver().findElement(locator);
		muteButton.click();
	}

	// FIXME: check muted state exactly for 'conversationName'
	public boolean isConversationMuted(String conversationName)
			throws Exception {
		// moving focus from contact - to now show ... button
		try {
			DriverUtils.moveMouserOver(driver, selfName);
		} catch (WebDriverException e) {
			// do nothing (safari workaround)
		}
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.className(WebAppLocators.ContactListPage.classMuteIcon), 5);
	}

	public void clickOptionsButtonForContact(String conversationName)
			throws Exception {
		try {
			DriverUtils.moveMouserOver(driver,
					getContactWithName(conversationName, false));
		} catch (WebDriverException e) {
			// Safari workaround
		}
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final By locator = By
				.xpath(WebAppLocators.ContactListPage.xpathOptionsButtonByContactName
						.apply(conversationName));
		if (!DriverUtils.isElementDisplayed(driver, locator, 5)) {
			// Safari workaround
			final String showOptionsButtonJScript = "$('"
					+ WebAppLocators.ContactListPage.classOptionsButton
					+ "').css({'opacity': '100'})";
			driver.executeScript(showOptionsButtonJScript);
			assert DriverUtils.isElementDisplayed(driver, locator);
		}
		final WebElement optionsButton = driver.findElement(locator);
		optionsButton.click();
	}

	private static final int SELECTION_TIMEOUT = 5; // seconds
	private static final String NON_SELECTED_ITEM_COLOR = "rgba(255, 255, 255, 1)";

	private void waitUtilEntryIsSelected(WebElement entry) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(
				SELECTION_TIMEOUT, TimeUnit.SECONDS).pollingEvery(1,
				TimeUnit.SECONDS);
		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				return !entry.getCssValue("color").equals(
						NON_SELECTED_ITEM_COLOR);
			}
		});
	}

	private static final int OPEN_CONVO_LIST_ENTRY_TIMEOUT = 3; // seconds

	public ConversationPage openConversation(String conversationName)
			throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final By entryLocator = By
				.xpath(WebAppLocators.ContactListPage.xpathContactListEntryByName
						.apply(conversationName));
		assert DriverUtils.isElementDisplayed(driver, entryLocator,
				OPEN_CONVO_LIST_ENTRY_TIMEOUT) : "Conversation item '"
				+ conversationName
				+ "' has not been found in the conversations list within "
				+ OPEN_CONVO_LIST_ENTRY_TIMEOUT + " second(s) timeoutÏ";
		final WebElement entry = driver.findElement(entryLocator);
		entry.click();
		waitUtilEntryIsSelected(entry);
		return new ConversationPage(this.getDriver(), this.getWait());
	}

	public PendingConnectionsPage openConnectionRequestsList() throws Exception {
		assert DriverUtils.waitUntilElementClickable(driver,
				incomingPendingEntry);
		incomingPendingEntry.click();
		return new PendingConnectionsPage(this.getDriver(), this.getWait());
	}

	public SelfProfilePage openSelfProfile() throws Exception {
		final By entryLocator = By
				.xpath(WebAppLocators.ContactListPage.xpathSelfProfileEntry);
		assert DriverUtils.isElementDisplayed(driver, entryLocator,
				OPEN_CONVO_LIST_ENTRY_TIMEOUT) : "Self profile entry has not been found within "
				+ OPEN_CONVO_LIST_ENTRY_TIMEOUT + " second(s) timeout";
		final WebElement entry = driver.findElement(entryLocator);
		entry.click();
		waitUtilEntryIsSelected(entry);
		return new SelfProfilePage(this.getDriver(), this.getWait());
	}

	public PeoplePickerPage openPeoplePicker() throws Exception {
		DriverUtils
				.waitUntilElementAppears(
						this.getDriver(),
						By.xpath(WebAppLocators.ContactListPage.xpathOpenPeoplePickerButton));
		DriverUtils.waitUntilElementClickable(driver, openPeoplePickerButton);
		if (WebAppExecutionContext.browserName
				.equals(WebAppConstants.Browser.INTERNET_EXPLORER)) {
			driver.executeScript(String.format(
					"$(document).find(\"%s\").click();",
					WebAppLocators.ContactListPage.cssOpenPeoplePickerButton));
		} else {
			openPeoplePickerButton.click();
		}
		return new PeoplePickerPage(this.getDriver(), this.getWait());
	}

	public void clickUnmuteConversationForContact(String conversationName)
			throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final By locator = By
				.xpath(WebAppLocators.ContactListPage.xpathUnmuteButtonByContactName
						.apply(conversationName));
		assert DriverUtils.isElementDisplayed(driver, locator, 5);
		final WebElement unmuteButton = this.getDriver().findElement(locator);
		unmuteButton.click();
	}

	public ConversationPage unarchiveConversation(String conversationName)
			throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final By archivedEntryLocator = By
				.xpath(WebAppLocators.ContactListPage.xpathArchivedContactListEntryByName
						.apply(conversationName));
		assert DriverUtils.isElementDisplayed(driver, archivedEntryLocator, 3);
		final WebElement archivedEntry = driver
				.findElement(archivedEntryLocator);
		archivedEntry.click();

		final By unarchivedEntryLocator = By
				.xpath(WebAppLocators.ContactListPage.xpathContactListEntryByName
						.apply(conversationName));
		assert DriverUtils
				.isElementDisplayed(driver, unarchivedEntryLocator, 3);
		final WebElement unarchivedEntry = driver
				.findElement(unarchivedEntryLocator);
		waitUtilEntryIsSelected(unarchivedEntry);

		return new ConversationPage(this.getDriver(), this.getWait());
	}

	public String getIncomingPendingItemText() throws Exception {
		final By entryLocator = By
				.xpath(WebAppLocators.ContactListPage.xpathIncomingPendingConvoItem);
		assert DriverUtils.isElementDisplayed(driver, entryLocator, 3) : "There are no visible incoming pending connections in the conversations list";
		return incomingPendingEntry.getText();
	}
}
