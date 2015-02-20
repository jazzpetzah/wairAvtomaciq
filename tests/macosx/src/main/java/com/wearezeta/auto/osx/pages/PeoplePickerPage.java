package com.wearezeta.auto.osx.pages;

import java.awt.HeadlessException;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.App;
import org.sikuli.script.Env;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.util.NSPoint;

@SuppressWarnings("deprecation")
public class PeoplePickerPage extends OSXPage {
	private static final Logger log = ZetaLogger.getLog(PeoplePickerPage.class
			.getSimpleName());

	@FindBy(how = How.XPATH, using = OSXLocators.xpathMainWindow)
	private WebElement mainWindow;

	@FindBy(how = How.NAME, using = OSXLocators.namePeoplePickerAddToConversationButton)
	private WebElement addToConversationButton;

	@FindBy(how = How.NAME, using = OSXLocators.namePeoplePickerCreateConversationButton)
	private WebElement createConversationButton;

	private WebElement searchField;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPeoplePickerSearchResultTable)
	private WebElement peoplePickerSearchResultTable;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPeoplePickerTopContactsSectionHeader)
	private WebElement peoplePickerTopContactsSectionHeader;

	@FindBy(how = How.ID, using = OSXLocators.idPeoplePickerSearchResultEntry)
	private List<WebElement> searchResults;

	@FindBy(how = How.ID, using = OSXLocators.idUnblockUserButton)
	private WebElement unblockUserButton;

	@FindBy(how = How.ID, using = OSXLocators.idPeoplePickerTopContactsGrid)
	private WebElement peoplePickerTopContactsList;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPeoplePickerTopContacts)
	private WebElement peoplePickerTopContacts;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPeoplePickerTopContactAvatar)
	private WebElement peoplePickerTopContactAvatar;

	public PeoplePickerPage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public WebElement findSearchField() {
		List<WebElement> textAreaCandidates = driver.findElements(By
				.className("AXTextArea"));
		for (WebElement textArea : textAreaCandidates) {
			if (textArea.getAttribute("AXIdentifier").equals(
					OSXLocators.idPeoplePickerSearchField)) {
				return textArea;
			}
		}
		return null;
	}

	public WebElement findCancelButton() {
		for (int i = 0; i < 3; i++) {
			log.debug("Looking for CancelPeoplePicker button. Instance #" + i);
			List<WebElement> buttonCandidates = driver.findElements(By
					.className("AXButton"));
			for (WebElement button : buttonCandidates) {
				String attribute = button.getAttribute("AXIdentifier");
				log.debug("Looking for button with attribute " + attribute);
				if (attribute.equals(OSXLocators.idPeoplePickerDismissButton)) {
					log.debug("Found people picker cancel button. Location "
							+ NSPoint.fromString(button
									.getAttribute("AXPosition")));
					return button;
				}
			}
		}
		return null;
	}

	public void searchForText(String text) {
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

	public boolean sendInvitationToUserIfRequested() {
		try {
			WebElement sendButton = driver.findElement(By
					.id(OSXLocators.idSendInvitationButton));
			sendButton.click();
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	public void unblockUser() {
		unblockUserButton.click();
	}

	public boolean areSearchResultsContainUser(String username)
			throws Exception {
		String xpath = String.format(
				OSXLocators.xpathFormatPeoplePickerSearchResultUser, username);

		DriverUtils.setImplicitWaitValue(driver, 60);
		boolean result = DriverUtils.waitUntilElementAppears(driver,
				By.xpath(xpath));
		DriverUtils.setDefaultImplicitWait(driver);
		return result;
	}

	public void scrollToUserInSearchResults(String user) {
		NSPoint mainPosition = NSPoint.fromString(mainWindow
				.getAttribute("AXPosition"));
		NSPoint mainSize = NSPoint
				.fromString(mainWindow.getAttribute("AXSize"));

		NSPoint latestPoint = new NSPoint(mainPosition.x() + mainSize.x(),
				mainPosition.y() + mainSize.y());

		// get scrollbar for contact list
		WebElement peopleDecrementSB = null;
		WebElement peopleIncrementSB = null;

		WebElement scrollArea = driver.findElement(By
				.xpath(OSXLocators.xpathSearchResultsScrollArea));

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
				.getAttribute("AXPosition"));
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
						.getAttribute("AXPosition"));
			}
			while (userPosition.y() < mainPosition.y()) {
				peopleDecrementSB.click();
				userPosition = NSPoint.fromString(userContact
						.getAttribute("AXPosition"));
			}
		}

	}

	public boolean isPeoplePickerPageVisible() throws Exception {
		boolean isFound = false;
		isFound = DriverUtils.waitUntilElementAppears(driver,
				peoplePickerSearchResultTable, 5);
		if (!isFound) {
			isFound = DriverUtils.waitUntilElementAppears(driver,
					peoplePickerTopContactsSectionHeader, 5);
		}
		return isFound;
	}

	public void closePeoplePicker() {
		findCancelButton().click();
	}

	public void selectUserInSearchResults(String user) {
		for (WebElement userEntry : searchResults) {
			if (userEntry.getText().equals(user)) {
				userEntry.click();
				break;
			}
		}
	}

	public void chooseUserInSearchResults(String user) throws Exception {
		selectUserInSearchResults(user);
		addSelectedUsersToConversation();
	}

	public ConversationPage addSelectedUsersToConversation() throws Exception {
		if (isCreateConversationButtonVisible())
			createConversationButton.click();
		else
			addToConversationButton.click();
		return new ConversationPage(this.getDriver(), this.getWait());
	}

	public boolean isTopPeopleVisible() throws Exception {
		return DriverUtils
				.waitUntilElementAppears(
						driver,
						By.xpath(OSXLocators.xpathPeoplePickerTopContactsSectionHeader));
	}

	public boolean isCreateConversationButtonVisible() throws Exception {
		if (DriverUtils.waitUntilElementAppears(driver,
				createConversationButton, 5)) {
			return NSPoint.fromString(
					createConversationButton.getAttribute("AXSize")).y() > 0;
		} else {
			return false;
		}
	}

	public void selectUserFromTopPeople() throws Exception {
		peoplePickerTopContactAvatar.click();
		Screen s = new Screen();
		try {
			App.focus(CommonUtils
					.getOsxApplicationPathFromConfig(PeoplePickerPage.class));
			if (!isCreateConversationButtonVisible())
				s.click(Env.getMouseLocation());
			if (!isCreateConversationButtonVisible())
				s.click(Env.getMouseLocation());
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (FindFailed e) {
			e.printStackTrace();
		}
	}
}
