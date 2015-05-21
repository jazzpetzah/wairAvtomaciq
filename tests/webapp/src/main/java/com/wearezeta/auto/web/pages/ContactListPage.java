package com.wearezeta.auto.web.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.common.WebAppConstants.Browser;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ContactListPage extends WebPage {

	private static final Logger log = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathContactListEntries)
	private List<WebElement> contactListEntries;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathArchivedContactListEntries)
	private List<WebElement> archivedContactListEntries;

	@FindBy(how = How.CSS, using = WebAppLocators.ContactListPage.cssSelfProfileEntry)
	private WebElement selfName;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathOpenArchivedConvosButton)
	private WebElement openArchivedConvosButton;

	@FindBy(how = How.CSS, using = WebAppLocators.ContactListPage.cssOpenPeoplePickerButton)
	private WebElement openPeoplePickerButton;

	public ContactListPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
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
					.waitUntilLocatorIsDisplayed(
							this.getDriver(),
							By.xpath(WebAppLocators.ContactListPage.xpathContactListEntries),
							3)) {
				convoNamesToCheck.addAll(contactListEntries);
			}
			if (includeArchived) {
				if (DriverUtils
						.waitUntilLocatorIsDisplayed(
								this.getDriver(),
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

	public boolean waitForContactListVisible() throws Exception {
		// FIXME: Try to refresh the page if convo list is not visible
		// (workaround for Amazon server issue)
		if (!DriverUtils
				.waitUntilLocatorAppears(
						this.getDriver(),
						By.cssSelector(WebAppLocators.ContactListPage.cssOpenPeoplePickerButton))) {
			this.getDriver().navigate().to(this.getDriver().getCurrentUrl());
		}
		return DriverUtils
				.waitUntilLocatorAppears(
						this.getDriver(),
						By.cssSelector(WebAppLocators.ContactListPage.cssOpenPeoplePickerButton));

	}

	public boolean isSelfNameEntryExist() throws Exception {
		final By locator = By
				.cssSelector(WebAppLocators.ContactListPage.cssSelfProfileEntry);
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, 5);
		log.debug(String.format("Looking for self name entry '%s'...", usrMgr
				.getSelfUserOrThrowError().getName()));

		final String selfNameElementText = getSelfName(locator);
		log.debug(String.format("Result self name is '%s'.",
				selfNameElementText));
		return selfNameElementText.equals(usrMgr.getSelfUserOrThrowError()
				.getName());
	}

	private String getSelfName(By locator) throws Exception {
		String name = "";
		for (int i = 1; i < 6; i++) {
			name = getDriver().findElement(locator).getText();
			if (!name.equals("")) {
				break;
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return name;

	}

	public boolean isConvoListEntryWithNameExist(String name) throws Exception {
		log.debug("Looking for contact with name '" + name + "'");
		name = fixDefaultGroupConvoName(name, false, false);
		if (name == null) {
			return false;
		}
		final String locator = WebAppLocators.ContactListPage.cssContactListEntryByName
				.apply(name);
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.cssSelector(locator), 5);
	}

	public boolean isArchiveListEntryWithNameExist(String name)
			throws Exception {
		log.debug("Looking for contact with name '" + name + "'");
		name = fixDefaultGroupConvoName(name, true, false);
		if (name == null) {
			return false;
		}
		final String locator = WebAppLocators.ContactListPage.cssArchiveListEntryByName
				.apply(name);
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.cssSelector(locator), 5);
	}

	public boolean isConvoListEntryNotVisible(String name) throws Exception {
		log.debug("Looking for contact with name '" + name + "'");
		name = fixDefaultGroupConvoName(name, false, false);
		if (name == null) {
			return true;
		}
		final String locator = WebAppLocators.ContactListPage.cssContactListEntryByName
				.apply(name);
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.cssSelector(locator), 5);
	}

	public WebElement getContactWithName(String name, boolean includeArchived)
			throws Exception {
		name = fixDefaultGroupConvoName(name, includeArchived);
		final String locator = WebAppLocators.ContactListPage.cssContactListEntryByName
				.apply(name);
		return getDriver().findElement(By.cssSelector(locator));
	}

	public boolean isMissedCallVisibleForContact(String conversationName)
			throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final String locator = WebAppLocators.ContactListPage.xpathMissedCallNotificationByContactName
				.apply(conversationName);
		List<WebElement> missedCallNotification = getDriver().findElements(
				By.xpath(locator));
		return missedCallNotification.size() > 0;
	}

	public void openArchive() throws Exception {
		this.getWait().until(
				ExpectedConditions
						.elementToBeClickable(openArchivedConvosButton));
		openArchivedConvosButton.click();
	}

	public void clickArchiveConversationForContact(String conversationName)
			throws Exception {
		waitForOptionButtonsToBeClickable(conversationName);

		final By archiveLocator = By
				.xpath(WebAppLocators.ContactListPage.xpathArchiveButtonByContactName
						.apply(conversationName));
		final WebElement archiveButton = this.getDriver().findElement(
				archiveLocator);
		archiveButton.click();
	}

	public void clickMuteConversationForContact(String conversationName)
			throws Exception {
		waitForOptionButtonsToBeClickable(conversationName);

		final By muteLocator = By
				.xpath(WebAppLocators.ContactListPage.xpathMuteButtonByContactName
						.apply(conversationName));
		final WebElement muteButton = this.getDriver().findElement(muteLocator);
		muteButton.click();
	}

	private void waitForOptionButtonsToBeClickable(String conversationName)
			throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final By archiveLocator = By
				.xpath(WebAppLocators.ContactListPage.xpathArchiveButtonByContactName
						.apply(conversationName));
		final By muteLocator = By
				.xpath(WebAppLocators.ContactListPage.xpathMuteButtonByContactName
						.apply(conversationName));
		final By unmuteLocator = By
				.xpath(WebAppLocators.ContactListPage.xpathUnmuteButtonByContactName
						.apply(conversationName));
		final WebElement archiveButton = this.getDriver().findElement(
				archiveLocator);
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				archiveButton);
		assert (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				muteLocator, 3) && DriverUtils.waitUntilElementClickable(
				this.getDriver(), this.getDriver().findElement(muteLocator), 3))
				|| (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
						unmuteLocator, 3) && DriverUtils
						.waitUntilElementClickable(this.getDriver(), this
								.getDriver().findElement(unmuteLocator), 3));
	}

	public boolean isConversationMuted(String conversationName)
			throws Exception {
		// moving focus from contact - to now show ... button
		try {
			DriverUtils.moveMouserOver(this.getDriver(), selfName);
		} catch (WebDriverException e) {
			// do nothing (safari workaround)
		}
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.xpath(WebAppLocators.ContactListPage.xpathMuteIconByContactName
								.apply(conversationName)), 5);
	}

	public void clickOptionsButtonForContact(String conversationName)
			throws Exception {
		try {
			DriverUtils.moveMouserOver(this.getDriver(),
					getContactWithName(conversationName, false));
		} catch (WebDriverException e) {
			// Safari workaround
		}
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final String cssOptionsButtonLocator = WebAppLocators.ContactListPage.cssOptionsButtonByContactName
				.apply(conversationName);
		final By locator = By.cssSelector(cssOptionsButtonLocator);
		if (!DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator,
				5)) {
			// Safari workaround
			final String showOptionsButtonJScript = "$(\""
					+ cssOptionsButtonLocator + "\").css({'opacity': '100'})";
			this.getDriver().executeScript(showOptionsButtonJScript);
			assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
					locator);
		}
		final WebElement optionsButton = getDriver().findElement(locator);
		optionsButton.click();
	}

	private static final int SELECTION_TIMEOUT = 3; // seconds
	private static final String NON_SELECTED_ITEM_COLOR = "rgba(255, 255, 255, 1)";

	private boolean isEntrySelected(WebElement entry) {
		return !entry.getCssValue("color").equals(NON_SELECTED_ITEM_COLOR);
	}

	private void waitUtilEntryIsSelected(final WebElement entry)
			throws Exception {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(this.getDriver())
				.withTimeout(SELECTION_TIMEOUT, TimeUnit.SECONDS).pollingEvery(
						1, TimeUnit.SECONDS);
		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				return isEntrySelected(entry);
			}
		});
	}

	public void clickWithJS(String cssSelector) throws Exception {
		this.getDriver()
				.executeScript(
						String.format("$(document).find(\"%s\").click();",
								cssSelector));
	}

	private void selectEntryWithRetry(By locator, String cssLocator)
			throws Exception {
		final WebElement entry = getDriver().findElement(locator);
		if (!isEntrySelected(entry)) {
			entry.click();
			try {
				waitUtilEntryIsSelected(entry);
			} catch (TimeoutException e) {
				clickWithJS(cssLocator);
				waitUtilEntryIsSelected(entry);
			}
		}
	}

	private static final int OPEN_CONVO_LIST_ENTRY_TIMEOUT = 3; // seconds

	public ConversationPage openConversation(String conversationName)
			throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final By entryLocator = By
				.cssSelector(WebAppLocators.ContactListPage.cssContactListEntryByName
						.apply(conversationName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				entryLocator, OPEN_CONVO_LIST_ENTRY_TIMEOUT) : "Conversation item '"
				+ conversationName
				+ "' has not been found in the conversations list within "
				+ OPEN_CONVO_LIST_ENTRY_TIMEOUT + " second(s) timeout";
		selectEntryWithRetry(entryLocator,
				WebAppLocators.ContactListPage.cssContactListEntryByName
						.apply(conversationName));
		return new ConversationPage(this.getLazyDriver());
	}

	public PendingConnectionsPage openConnectionRequestsList() throws Exception {
		final By entryLocator = By
				.cssSelector(WebAppLocators.ContactListPage.cssIncomingPendingConvoItem);
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				entryLocator, OPEN_CONVO_LIST_ENTRY_TIMEOUT) : "Incoming connection requests entry has not been found within "
				+ OPEN_CONVO_LIST_ENTRY_TIMEOUT + " second(s) timeout";
		selectEntryWithRetry(entryLocator,
				WebAppLocators.ContactListPage.cssIncomingPendingConvoItem);
		return new PendingConnectionsPage(this.getLazyDriver());
	}

	public SelfProfilePage openSelfProfile() throws Exception {
		final By entryLocator = By
				.cssSelector(WebAppLocators.ContactListPage.cssSelfProfileEntry);
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				entryLocator, OPEN_CONVO_LIST_ENTRY_TIMEOUT) : "Self profile entry has not been found within "
				+ OPEN_CONVO_LIST_ENTRY_TIMEOUT + " second(s) timeout";
		selectEntryWithRetry(entryLocator,
				WebAppLocators.ContactListPage.cssSelfProfileEntry);
		return new SelfProfilePage(this.getLazyDriver());
	}

	public PeoplePickerPage openPeoplePicker() throws Exception {
		DriverUtils
				.waitUntilLocatorAppears(
						this.getDriver(),
						By.cssSelector(WebAppLocators.ContactListPage.cssOpenPeoplePickerButton));
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				openPeoplePickerButton);
		if (WebAppExecutionContext.getCurrentBrowser() == Browser.InternetExplorer) {
			clickWithJS(WebAppLocators.ContactListPage.cssOpenPeoplePickerButton);
		} else {
			openPeoplePickerButton.click();
		}
		return new PeoplePickerPage(this.getLazyDriver());
	}

	public String getSelfNameColor() {
		return selfName.getCssValue("color");
	}

	public void clickUnmuteConversationForContact(String conversationName)
			throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final By locator = By
				.xpath(WebAppLocators.ContactListPage.xpathUnmuteButtonByContactName
						.apply(conversationName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, 5);
		final WebElement unmuteButton = this.getDriver().findElement(locator);
		unmuteButton.click();
	}

	public ConversationPage unarchiveConversation(String conversationName)
			throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, true);
		final By archivedEntryLocator = By
				.xpath(WebAppLocators.ContactListPage.xpathArchivedContactListEntryByName
						.apply(conversationName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				archivedEntryLocator, 3);
		final WebElement archivedEntry = this.getDriver().findElement(
				archivedEntryLocator);
		archivedEntry.click();

		final By unarchivedEntryLocator = By
				.cssSelector(WebAppLocators.ContactListPage.cssContactListEntryByName
						.apply(conversationName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				unarchivedEntryLocator, 3);
		final WebElement unarchivedEntry = this.getDriver().findElement(
				unarchivedEntryLocator);
		waitUtilEntryIsSelected(unarchivedEntry);

		return new ConversationPage(this.getLazyDriver());
	}

	public String getIncomingPendingItemText() throws Exception {
		final By entryLocator = By
				.cssSelector(WebAppLocators.ContactListPage.cssIncomingPendingConvoItem);
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				entryLocator, 3) : "There are no visible incoming pending connections in the conversations list";
		return getDriver().findElement(entryLocator).getText();
	}

	public boolean isSelfNameEntrySelected() throws Exception {
		final By locator = By
				.cssSelector(WebAppLocators.ContactListPage.cssSelfProfileEntry);
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, 3);
		final WebElement entry = getDriver().findElement(locator);
		try {
			waitUtilEntryIsSelected(entry);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isConversationSelected(String convoName) throws Exception {
		convoName = fixDefaultGroupConvoName(convoName, false);
		final By locator = By
				.cssSelector(WebAppLocators.ContactListPage.cssContactListEntryByName
						.apply(convoName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, 3);
		final WebElement entryElement = getDriver().findElement(locator);
		try {
			waitUtilEntryIsSelected(entryElement);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public int getItemIndex(String convoName) throws Exception {
		convoName = fixDefaultGroupConvoName(convoName, false);
		final int entriesCount = this
				.getDriver()
				.findElements(
						By.xpath(WebAppLocators.ContactListPage.xpathContactListEntries))
				.size();
		for (int entryIdx = 1; entryIdx <= entriesCount; entryIdx++) {
			final WebElement entryElement = this
					.getDriver()
					.findElement(
							By.xpath(WebAppLocators.ContactListPage.xpathContactListEntryByIndex
									.apply(entryIdx)));
			if (entryElement.getAttribute("data-uie-value").equals(convoName)) {
				return entryIdx;
			}
		}
		throw new AssertionError(String.format(
				"There is no '%s' conversation in the list", convoName));
	}

	public void waitUntilArhiveButtonIsNotVisible(int archiveBtnVisilityTimeout)
			throws Exception {
		assert DriverUtils
				.waitUntilLocatorDissapears(
						this.getDriver(),
						By.xpath(WebAppLocators.ContactListPage.xpathOpenArchivedConvosButton),
						archiveBtnVisilityTimeout) : "Open Archive button is still visible after "
				+ archiveBtnVisilityTimeout + " second(s)";
	}

	public void waitUntilArhiveButtonIsVisible(int archiveBtnVisilityTimeout)
			throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.xpath(WebAppLocators.ContactListPage.xpathOpenArchivedConvosButton),
						archiveBtnVisilityTimeout) : "Open Archive button is not visible after "
				+ archiveBtnVisilityTimeout + " second(s)";
	}
}
