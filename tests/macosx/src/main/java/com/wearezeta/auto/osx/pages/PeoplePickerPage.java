package com.wearezeta.auto.osx.pages;

import java.awt.HeadlessException;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.sikuli.script.App;
import org.sikuli.script.Env;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.common.SearchResultTypeEnum;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.popovers.ConnectToPopover;
import com.wearezeta.auto.osx.pages.popovers.UnblockPopover;
import com.wearezeta.auto.osx.util.NSPoint;

@SuppressWarnings("deprecation")
public class PeoplePickerPage extends MainWirePage {
	private static final Logger log = ZetaLogger.getLog(PeoplePickerPage.class
			.getSimpleName());

	@FindBy(how = How.NAME, using = OSXLocators.PeoplePickerPage.nameAddToConversationButton)
	private WebElement addToConversationButton;

	@FindBy(how = How.NAME, using = OSXLocators.PeoplePickerPage.nameOpenConversationButton)
	private WebElement openConversationButton;

	@FindBy(how = How.NAME, using = OSXLocators.PeoplePickerPage.nameCreateConversationButton)
	private WebElement createConversationButton;

	private WebElement searchField;

	@FindBy(how = How.ID, using = OSXLocators.PeoplePickerPage.idPeoplePickerSearchResultTable)
	private WebElement peoplePickerSearchResultTable;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPeoplePickerTopContactsSectionHeader)
	private WebElement peoplePickerTopContactsSectionHeader;

	@FindBy(how = How.ID, using = OSXLocators.PeoplePickerPage.idPeoplePickerSearchResultEntry)
	private List<WebElement> searchResults;

	@FindBy(how = How.ID, using = OSXLocators.idPeoplePickerTopContactsGrid)
	private WebElement peoplePickerTopContactsList;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPeoplePickerTopContacts)
	private WebElement peoplePickerTopContacts;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPeoplePickerTopContactAvatar)
	private WebElement peoplePickerTopContactAvatar;

	public PeoplePickerPage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public WebElement findSearchField() throws Exception {
		List<WebElement> textAreaCandidates = getDriver().findElements(
				By.className("AXTextArea"));
		for (WebElement textArea : textAreaCandidates) {
			if (textArea.getAttribute("AXIdentifier").equals(
					OSXLocators.idPeoplePickerSearchField)) {
				return textArea;
			}
		}
		throw new NoSuchElementException(
				String.format(
						"Failed to find Search Field element with Class '%s' and ID '%s'",
						"AXTextArea", OSXLocators.idPeoplePickerSearchField));
	}

	public WebElement findCancelButton() throws Exception {
		for (int i = 0; i < 3; i++) {
			log.debug("Looking for CancelPeoplePicker button. Instance #" + i);
			List<WebElement> buttonCandidates = getDriver().findElements(
					By.className("AXButton"));
			for (WebElement button : buttonCandidates) {
				String attribute = button.getAttribute("AXIdentifier");
				log.debug("Looking for button with attribute " + attribute);
				if (attribute.equals(OSXLocators.idPeoplePickerDismissButton)) {
					log.debug("Found people picker cancel button. Location "
							+ NSPoint.fromString(button
									.getAttribute(OSXConstants.Attributes.AXPOSITION)));
					return button;
				}
			}
		}
		return null;
	}

	public void searchForText(String text) throws Exception {
		int i = 0;
		searchField = null;
		while (searchField == null) {
			searchField = findSearchField();
			if (++i == 10)
				break;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		searchField.sendKeys(text);
	}

	public boolean areSearchResultsContainUser(String username)
			throws Exception {
		String xpath = String.format(
				OSXLocators.xpathFormatPeoplePickerSearchResultUser, username);

		boolean result = DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(xpath));
		return result;
	}

	public void scrollToUserInSearchResults(String user) throws Exception {
		NSPoint mainPosition = NSPoint.fromString(window
				.getAttribute(OSXConstants.Attributes.AXPOSITION));
		NSPoint mainSize = NSPoint.fromString(window
				.getAttribute(OSXConstants.Attributes.AXSIZE));

		NSPoint latestPoint = new NSPoint(mainPosition.x() + mainSize.x(),
				mainPosition.y() + mainSize.y());

		// get scrollbar for contact list
		WebElement peopleDecrementSB = null;
		WebElement peopleIncrementSB = null;

		WebElement scrollArea = getDriver().findElement(
				By.xpath(OSXLocators.xpathSearchResultsScrollArea));

		WebElement userContact = null;
		boolean isFoundPeople = false;
		try {
			for (WebElement contact : searchResults) {
				if (contact.getText().equals(user)) {
					isFoundPeople = true;
					userContact = contact;
				}
			}
		} catch (NoSuchElementException e) {
			isFoundPeople = false;
		}

		NSPoint userPosition = NSPoint.fromString(userContact
				.getAttribute(OSXConstants.Attributes.AXPOSITION));
		if (userPosition.y() > latestPoint.y()
				|| userPosition.y() < mainPosition.y()) {
			if (isFoundPeople) {
				WebElement scrollBar = scrollArea.findElement(By
						.xpath("//AXScrollBar"));
				List<WebElement> scrollButtons = scrollBar.findElements(By
						.xpath("//AXButton"));
				for (WebElement scrollButton : scrollButtons) {
					String subrole = scrollButton.getAttribute("AXSubrole");
					if (subrole.equals("AXDecrementPage")) {
						peopleDecrementSB = scrollButton;
					}
					if (subrole.equals("AXIncrementPage")) {
						peopleIncrementSB = scrollButton;
					}
				}
			}

			while (userPosition.y() > latestPoint.y()) {
				peopleIncrementSB.click();
				userPosition = NSPoint.fromString(userContact
						.getAttribute(OSXConstants.Attributes.AXPOSITION));
			}
			while (userPosition.y() < mainPosition.y()) {
				peopleDecrementSB.click();
				userPosition = NSPoint.fromString(userContact
						.getAttribute(OSXConstants.Attributes.AXPOSITION));
			}
		}

	}

	public boolean isPeoplePickerPageVisible() throws Exception {
		boolean isFound = false;
		isFound = DriverUtils
				.waitUntilLocatorAppears(
						this.getDriver(),
						By.xpath(OSXLocators.PeoplePickerPage.idPeoplePickerSearchResultTable),
						5);
		if (!isFound) {
			isFound = DriverUtils
					.waitUntilLocatorAppears(
							this.getDriver(),
							By.xpath(OSXLocators.xpathPeoplePickerTopContactsSectionHeader),
							5);
		}
		return isFound;
	}

	public void closePeoplePicker() throws Exception {
		findCancelButton().click();
	}

	public OSXPage selectContactInSearchResults(String user,
			SearchResultTypeEnum type) throws Exception {
		String xpath = String
				.format(OSXLocators.PeoplePickerPage.xpathFormatSearchResultEntry,
						user);
		WebElement userEntry = getDriver().findElement(By.xpath(xpath));
		userEntry.click();

		switch (type) {
		case NOT_CONNECTED:
			return new ConnectToPopover(this.getLazyDriver());
		case BLOCKED:
			return new UnblockPopover(this.getLazyDriver());
		default:
			return null;
		}
	}

	public boolean isRequiredScrollToUser(String user) throws Exception {
		String xpath = String
				.format(OSXLocators.PeoplePickerPage.xpathFormatSearchResultEntry,
						user);
		return DriverUtils
				.waitUntilLocatorIsDisplayed(this.getDriver(), By.xpath(xpath));
	}

	public void chooseUserInSearchResults(String user, SearchResultTypeEnum type)
			throws Exception {
		selectContactInSearchResults(user, type);
		addSelectedUsersToConversation();
	}

	public ConversationPage addSelectedUsersToConversation() throws Exception {
		if (isCreateConversationButtonVisible()) {
			createConversationButton.click();
		} else if (isOpenConversationButtonVisible())
			openConversationButton.click();
		else
			addToConversationButton.click();
		return new ConversationPage(this.getLazyDriver());
	}

	public boolean isTopPeopleVisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorAppears(
						this.getDriver(),
						By.xpath(OSXLocators.xpathPeoplePickerTopContactsSectionHeader),
						3);
	}

	public boolean isCreateConversationButtonVisible() throws Exception {
		if (DriverUtils
				.waitUntilLocatorAppears(
						this.getDriver(),
						By.name(OSXLocators.PeoplePickerPage.nameCreateConversationButton),
						5)) {
			return NSPoint.fromString(
					createConversationButton
							.getAttribute(OSXConstants.Attributes.AXSIZE)).y() > 0;
		} else {
			return false;
		}
	}

	public boolean isOpenConversationButtonVisible() throws Exception {
		if (DriverUtils.waitUntilLocatorAppears(this.getDriver(), By
				.name(OSXLocators.PeoplePickerPage.nameOpenConversationButton),
				5)) {
			return NSPoint.fromString(
					openConversationButton
							.getAttribute(OSXConstants.Attributes.AXSIZE)).y() > 0;
		} else {
			return false;
		}
	}

	public void selectUserFromTopPeople() throws Exception {
		peoplePickerTopContactAvatar.click();
		Screen s = new Screen();
		try {
			App.focus(OSXExecutionContext.wirePath);
			if (!isOpenConversationButtonVisible())
				s.click(Env.getMouseLocation());
			if (!isOpenConversationButtonVisible())
				s.click(Env.getMouseLocation());
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (FindFailed e) {
			e.printStackTrace();
		}
	}
}
