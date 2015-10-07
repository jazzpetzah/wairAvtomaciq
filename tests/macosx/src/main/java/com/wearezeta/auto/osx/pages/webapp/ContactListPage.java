package com.wearezeta.auto.osx.pages.webapp;

import com.wearezeta.auto.osx.pages.osx.ContactContextMenuPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.google.common.base.Function;
import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.DriverUtils;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.Browser;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;

import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;

import com.wearezeta.auto.web.pages.PendingConnectionsPage;
import com.wearezeta.auto.web.pages.PeoplePickerPage;
import com.wearezeta.auto.web.pages.SelfProfilePage;
import com.wearezeta.auto.web.pages.WebPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class ContactListPage extends WebPage {

	private static final Logger LOG = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	// TODO hide behind driver impl
	private final Robot robot = new Robot();

	private static final String DEFAULT_GROUP_CONVO_NAMES_SEPARATOR = ",";
	private static final int OPEN_CONVO_LIST_ENTRY_TIMEOUT = 8; // seconds
	private static final int SELECTION_TIMEOUT = 3; // seconds
	private static final String NON_SELECTED_ITEM_COLOR = "rgba(255, 255, 255, 1)";

	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	private final OSXPagesCollection osxPagesCollection = OSXPagesCollection
			.getInstance();

	@FindBy(how = How.CSS, using = WebAppLocators.ContactListPage.cssSelfProfileAvatar)
	private WebElement selfProfileAvatar;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathContactListEntries)
	private List<WebElement> contactListEntries;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathArchivedContactListEntries)
	private List<WebElement> archivedContactListEntries;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathOpenArchivedConvosButton)
	private WebElement openArchivedConvosButton;

	@FindBy(how = How.CSS, using = WebAppLocators.ContactListPage.cssOpenPeoplePickerButton)
	private WebElement openPeoplePickerButton;

	@FindBy(how = How.ID, using = WebAppLocators.ConversationPage.idConversationInput)
	private WebElement conversationInput;

	// options popover bubble

	@FindBy(css = WebAppLocators.ContactListPage.cssArchiveButton)
	private WebElement archiveButton;

	@FindBy(css = WebAppLocators.ContactListPage.cssMuteButton)
	private WebElement muteButton;

	@FindBy(css = WebAppLocators.ContactListPage.cssUnmuteButton)
	private WebElement unmuteButton;

	// leave warning

	@FindBy(css = WebAppLocators.ContactListPage.cssLeaveModalCancelButton)
	private WebElement leaveModalCancelButton;

	@FindBy(css = WebAppLocators.ContactListPage.cssLeaveModalActionButton)
	private WebElement leaveModalActionButton;

	// block warning

	@FindBy(css = WebAppLocators.ContactListPage.cssBlockModalCancelButton)
	private WebElement blockModalCancelButton;

	@FindBy(css = WebAppLocators.ContactListPage.cssBlockModalActionButton)
	private WebElement blockModalActionButton;

	// delete warning

	@FindBy(css = WebAppLocators.ContactListPage.cssDeleteModalCancelButtonGroup)
	private WebElement deleteModalCancelButtonGroup;

	@FindBy(css = WebAppLocators.ContactListPage.cssDeleteModalActionButtonGroup)
	private WebElement deleteModalActionButtonGroup;

	@FindBy(css = WebAppLocators.ContactListPage.cssDeleteModalLeaveCheckboxGroup)
	private WebElement deleteModalLeaveCheckboxGroup;

	@FindBy(css = WebAppLocators.ContactListPage.cssDeleteModalActionButtonSingle)
	private WebElement deleteModalActionButtonSingle;

	@FindBy(css = WebAppLocators.ContactListPage.cssDeleteModalCancelButtonSingle)
	private WebElement deleteModalCancelButtonSingle;

	public ContactListPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public ContactContextMenuPage openContextMenuForContact(String name)
			throws Exception {
		LOG.debug("Looking for contact with name '" + name + "'");
		if (name == null) {
			throw new Exception(
					"The name to look for in the conversation list was null");
		}
		name = fixDefaultGroupConvoName(name, false, false);
		final String locator = WebAppLocators.ContactListPage.cssContactListEntryByName
				.apply(name);
		DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.cssSelector(locator));
		WebElement entry = getDriver().findElement(By.cssSelector(locator));
		new Actions(getDriver()).contextClick(entry).perform();
		return osxPagesCollection.getPage(ContactContextMenuPage.class);
	}

	public boolean waitForContactListVisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorAppears(
						this.getDriver(),
						By.cssSelector(WebAppLocators.ContactListPage.cssOpenPeoplePickerButton));

	}

	public void waitForSelfProfileAvatar() throws Exception {
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				selfProfileAvatar);
	}

	public boolean isConvoListEntryWithNameExist(String name) throws Exception {
		LOG.debug("Looking for contact with name '" + name + "'");
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
		LOG.debug("Looking for contact with name '" + name + "'");
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
		LOG.debug("Looking for contact with name '" + name + "'");
		name = fixDefaultGroupConvoName(name, false, false);
		if (name == null) {
			return true;
		}
		final String locator = WebAppLocators.ContactListPage.cssContactListEntryByName
				.apply(name);
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.cssSelector(locator), 5);
	}

	public WebElement getListElementByName(String name, boolean includeArchived)
			throws Exception {
		name = fixDefaultGroupConvoName(name, includeArchived);
		final String locator = WebAppLocators.ContactListPage.cssContactListEntryByName
				.apply(name);
		return getDriver().findElement(By.cssSelector(locator));
	}

	public String getActiveConversationName() throws Exception {
		final String locator = WebAppLocators.ContactListPage.xpathActiveConversationEntry;
		return getDriver().findElement(By.xpath(locator)).getText();
	}

	public int getActiveConversationIndex() throws Exception {
		final String locator = WebAppLocators.ContactListPage.xpathActiveConversationEntry;
		return getItemIndex(getDriver().findElement(By.xpath(locator))
				.getText());
	}

	public boolean isMissedCallVisibleForContact(String conversationName)
			throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final String locator = WebAppLocators.ContactListPage.xpathMissedCallNotificationByContactName
				.apply(conversationName);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(locator));
	}

	public boolean isMissedCallInvisibleForContact(String conversationName)
			throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final String locator = WebAppLocators.ContactListPage.xpathMissedCallNotificationByContactName
				.apply(conversationName);
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(locator));
	}

	public boolean isJoinedGroupCallNotificationVisibleForConversation(
			String conversationName) throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final String locator = WebAppLocators.ContactListPage.xpathJoinedGroupCallNotificationByConversationName
				.apply(conversationName);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(locator));
	}

	public boolean isJoinedGroupCallNotificationInvisibleForConversation(
			String conversationName) throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final String locator = WebAppLocators.ContactListPage.xpathJoinedGroupCallNotificationByConversationName
				.apply(conversationName);
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(locator));
	}

	public boolean isUnjoinedGroupCallNotificationVisibleForConversation(
			String conversationName) throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final String locator = WebAppLocators.ContactListPage.xpathUnjoinedGroupCallNotificationByConversationName
				.apply(conversationName);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(locator));
	}

	public boolean isUnjoinedGroupCallNotificationInvisibleForConversation(
			String conversationName) throws Exception {
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final String locator = WebAppLocators.ContactListPage.xpathUnjoinedGroupCallNotificationByConversationName
				.apply(conversationName);
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(locator));
	}

	public void pressShortCutToMute() throws Exception {
		robot.keyPress(KeyEvent.VK_META);// command key
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_ALT);
		robot.keyRelease(KeyEvent.VK_META);
	}

	public void pressShortCutToArchive() throws Exception {
		robot.keyPress(KeyEvent.VK_META);// command key
		robot.keyPress(KeyEvent.VK_D);
		robot.keyRelease(KeyEvent.VK_D);
		robot.keyRelease(KeyEvent.VK_META);
	}

	public void pressShortCutForNextConv() throws Exception {
		Thread.sleep(100);
		robot.keyPress(KeyEvent.VK_ALT);
		Thread.sleep(100);
		robot.keyPress(KeyEvent.VK_META);// command key
		Thread.sleep(100);
		robot.keyPress(KeyEvent.VK_UP);
		Thread.sleep(100);
		robot.keyRelease(KeyEvent.VK_UP);
		Thread.sleep(100);
		robot.keyRelease(KeyEvent.VK_META);
		Thread.sleep(100);
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	public void pressShortCutForPrevConv() throws Exception {
		Thread.sleep(100);
		robot.keyPress(KeyEvent.VK_ALT);
		Thread.sleep(100);
		robot.keyPress(KeyEvent.VK_META);// command key
		Thread.sleep(100);
		robot.keyPress(KeyEvent.VK_DOWN);
		Thread.sleep(100);
		robot.keyRelease(KeyEvent.VK_DOWN);
		Thread.sleep(100);
		robot.keyRelease(KeyEvent.VK_META);
		Thread.sleep(100);
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	public void openArchive() throws Exception {
		this.getWait().until(
				ExpectedConditions
						.elementToBeClickable(openArchivedConvosButton));
		openArchivedConvosButton.click();
	}

	public boolean isConversationMuted(String conversationName)
			throws Exception {
		// moving focus from contact - to now show ... button
		// do nothing (safari workaround)
		if (WebAppExecutionContext.getBrowser()
				.isSupportingNativeMouseActions()) {
			DriverUtils.moveMouserOver(this.getDriver(), selfProfileAvatar);
		}
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.xpath(WebAppLocators.ContactListPage.xpathMuteIconByContactName
								.apply(conversationName)));
	}

	public boolean isConversationNotMuted(String conversationName)
			throws Exception {
		// moving focus from contact - to now show ... button
		// do nothing (safari workaround)
		if (WebAppExecutionContext.getBrowser()
				.isSupportingNativeMouseActions()) {
			DriverUtils.moveMouserOver(this.getDriver(), selfProfileAvatar);
		}
		return DriverUtils
				.waitUntilLocatorDissapears(
						this.getDriver(),
						By.xpath(WebAppLocators.ContactListPage.xpathMuteIconByContactName
								.apply(conversationName)));
	}

	public void clickOptionsButtonForContact(String conversationName)
			throws Exception {
		if (!WebAppExecutionContext.getBrowser()
				.isSupportingNativeMouseActions()) {
			DriverUtils.addClassToParent(this.getDriver(),
					getListElementByName(conversationName, false), "hover");
		} else {
			DriverUtils.moveMouserOver(this.getDriver(),
					getListElementByName(conversationName, false));
		}
		conversationName = fixDefaultGroupConvoName(conversationName, false);
		final String cssOptionsButtonLocator = WebAppLocators.ContactListPage.cssOptionsButtonByContactName
				.apply(conversationName);
		final By locator = By.cssSelector(cssOptionsButtonLocator);
		final WebElement optionsButton = getDriver().findElement(locator);
		DriverUtils.waitUntilElementClickable(getDriver(), optionsButton);
		optionsButton.click();
		if (!WebAppExecutionContext.getBrowser()
				.isSupportingNativeMouseActions()) {
			DriverUtils.removeClassFromParent(this.getDriver(),
					getListElementByName(conversationName, false), "hover");
		}
	}

	public void clickWithJS(String cssSelector) throws Exception {
		this.getDriver()
				.executeScript(
						String.format("$(document).find(\"%s\").click();",
								cssSelector));
	}

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
		return webappPagesCollection.getPage(ConversationPage.class);
	}

	public PendingConnectionsPage openConnectionRequestsList() throws Exception {
		final By entryLocator = By
				.cssSelector(WebAppLocators.ContactListPage.cssIncomingPendingConvoItem);
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				entryLocator, OPEN_CONVO_LIST_ENTRY_TIMEOUT) : "Incoming connection requests entry has not been found within "
				+ OPEN_CONVO_LIST_ENTRY_TIMEOUT + " second(s) timeout";
		selectEntryWithRetry(entryLocator,
				WebAppLocators.ContactListPage.cssIncomingPendingConvoItem);
		return webappPagesCollection.getPage(PendingConnectionsPage.class);
	}

	public SelfProfilePage openSelfProfile() throws Exception {
		waitForSelfProfileAvatar();
		selfProfileAvatar.click();
		return webappPagesCollection.getPage(SelfProfilePage.class);
	}

	public PeoplePickerPage openPeoplePicker() throws Exception {
		DriverUtils
				.waitUntilLocatorAppears(
						this.getDriver(),
						By.cssSelector(WebAppLocators.ContactListPage.cssOpenPeoplePickerButton));
		if (WebAppExecutionContext.getBrowser() == Browser.InternetExplorer) {
			clickWithJS(WebAppLocators.ContactListPage.cssOpenPeoplePickerButton);
		} else {
			openPeoplePickerButton.click();
		}
		return webappPagesCollection.getPage(PeoplePickerPage.class);
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

		return webappPagesCollection.getPage(ConversationPage.class);
	}

	public String getIncomingPendingItemText() throws Exception {
		final By entryLocator = By
				.cssSelector(WebAppLocators.ContactListPage.cssIncomingPendingConvoItem);
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				entryLocator) : "There are no visible incoming pending connections in the conversations list";
		return getDriver().findElement(entryLocator).getText();
	}

	public boolean isSelfNameEntrySelected() throws Exception {
		final By locator = By
				.cssSelector(WebAppLocators.ContactListPage.cssSelfProfileAvatar);
		waitForSelfProfileAvatar();
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

	public void waitUntilArchiveButtonIsNotVisible(int archiveBtnVisilityTimeout)
			throws Exception {
		assert DriverUtils
				.waitUntilLocatorDissapears(
						this.getDriver(),
						By.xpath(WebAppLocators.ContactListPage.xpathOpenArchivedConvosButton),
						archiveBtnVisilityTimeout) : "Open Archive button is still visible after "
				+ archiveBtnVisilityTimeout + " second(s)";
	}

	public void waitUntilArchiveButtonIsVisible(int archiveBtnVisilityTimeout)
			throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.xpath(WebAppLocators.ContactListPage.xpathOpenArchivedConvosButton),
						archiveBtnVisilityTimeout) : "Open Archive button is not visible after "
				+ archiveBtnVisilityTimeout + " second(s)";
	}

	public AccentColor getCurrentPingIconAccentColor(String name)
			throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ContactListPage.xpathPingIconByContactName
						.apply(name));
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, 3);
		final WebElement entry = getDriver().findElement(locator);
		return AccentColor.getByRgba(entry.getCssValue("color"));
	}

	public AccentColor getCurrentUnreadDotAccentColor(String name)
			throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ContactListPage.xpathUnreadDotByContactName
						.apply(name));
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator);
		final WebElement entry = getDriver().findElement(locator);
		return AccentColor.getByRgba(entry.getCssValue("background-color"));
	}

	public boolean isPingIconVisibleForConversation(String conversationName)
			throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ContactListPage.xpathPingIconByContactName
						.apply(conversationName));
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, 3);
	}

	public String getMuteButtonToolTip() throws Exception {
		return muteButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
	}

	public void pressShortCutToSearch() throws Exception {
		robot.keyPress(KeyEvent.VK_META);// command key
		robot.keyPress(KeyEvent.VK_N);
		robot.keyRelease(KeyEvent.VK_N);
		robot.keyRelease(KeyEvent.VK_META);
	}

	public boolean isLeaveWarningModalVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.cssSelector(WebAppLocators.ContactListPage.cssLeaveModal));
	}

	public void clickCancelOnLeaveWarning() {
		leaveModalCancelButton.click();
	}

	public boolean isBlockWarningModalVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.cssSelector(WebAppLocators.ContactListPage.cssBlockModal));
	}

	public void clickCancelOnBlockWarning() {
		blockModalCancelButton.click();
	}

	public void clickBlockOnBlockWarning() {
		blockModalActionButton.click();
	}

	public boolean isDeleteWarningModalForGroupVisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.cssSelector(WebAppLocators.ContactListPage.cssDeleteModalGroup));
	}

	public void clickDeleteOnDeleteWarning() {
		deleteModalActionButtonGroup.click();
	}

	public void clickLeaveOnLeaveWarning() {
		leaveModalActionButton.click();
	}

	public void clickLeaveCheckboxOnDeleteWarning() {
		deleteModalLeaveCheckboxGroup.click();
	}

	public void clickCancelOnDeleteWarning() {
		deleteModalCancelButtonGroup.click();
	}

	public boolean isDeleteWarningModalSingleVisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.cssSelector(WebAppLocators.ContactListPage.cssDeleteModalSingle));
	}

	public void clickDeleteOnDeleteWarningSingle() {
		deleteModalActionButtonSingle.click();
	}

	public void clickCancelOnDeleteWarningSingle() {
		deleteModalCancelButtonSingle.click();
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

}
