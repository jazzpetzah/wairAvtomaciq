package com.wearezeta.auto.ios.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class GroupChatPage extends DialogPage {
	@FindBy(how = How.NAME, using = IOSLocators.nameMainWindow)
	private WebElement groupChatWindow;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathNewGroupConversationNameChangeTextField)
	private WebElement newGroupConversationNameChangeTextField;

	@FindBy(how = How.NAME, using = IOSLocators.nameYouHaveLeft)
	private WebElement youLeft;

	@FindBy(how = How.NAME, using = IOSLocators.nameYouLeftMessage)
	private WebElement youLeftMessage;

	@FindBy(how = How.NAME, using = IOSLocators.nameYouRenamedConversationMessage)
	private WebElement yourRenamedMessage;

	public GroupChatPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean areRequiredContactsAddedToChat(List<String> names) {
		final String lastMsg = getStartedtChatMessage();
		for (String name : names) {
			if (!lastMsg.toLowerCase().contains(name.toLowerCase())) {
				return false;
			}
		}
		return true;
	}

	public boolean areRequired3ContactsAddedToChat(String name1, String name2,
			String name3) {
		String lastMessage = getStartedtChatMessage();
		boolean flag = lastMessage.contains(name1.toUpperCase())
				&& lastMessage.contains(name2.toUpperCase())
				&& lastMessage.contains(name3.toUpperCase());
		return flag;
	}

	public boolean isGroupChatPageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(IOSLocators.nameConversationCursorInput));
	}

	public boolean isConversationChangedInChat() {

		System.out.println(newGroupConversationNameChangeTextField.getText());
		System.out.println(PagesCollection.groupChatInfoPage
				.getConversationName());

		String groupChatName = PagesCollection.groupChatInfoPage
				.getConversationName();
		return newGroupConversationNameChangeTextField.getText().contains(
				groupChatName);
	}

	public boolean isYouHaveLeftVisible() {
		return youLeft.isDisplayed();
	}

	public boolean isUserAddedContactVisible(String user, String contact)
			throws Exception {
		return this
				.getDriver()
				.findElement(
						By.name(user.toUpperCase() + " ADDED "
								+ contact.toUpperCase())).isDisplayed();
	}

	public boolean isYouAddedUserMessageShown(String user) throws Exception {
		return isMessageShownInGroupChat(String.format(
				IOSLocators.nameYouAddetToGroupChatMessage, user.toUpperCase()));
	}

	public boolean isYouRenamedConversationMessageVisible(String name) {
		return getRenamedMessage().equals(
				String.format(IOSLocators.nameYouRenamedConversationMessage,
						name));
	}

	public boolean isMessageShownInGroupChat(String message) throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(message));
	}

	public boolean isContactAvailableInChat(String contact) throws Exception {
		WebElement el = null;
		boolean result = false;

		try {
			el = getDriver().findElementByName(contact);
		} catch (NoSuchElementException ex) {
			el = null;
		} finally {
			result = el != null;
		}

		return result;
	}

	public boolean waitForContactToDisappear(String contact) throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.name(contact));
	}

	@Override
	public IOSPage openConversationDetailsClick() throws Exception {
		for (int i = 0; i < 3; i++) {
			if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
					By.name(IOSLocators.nameOpenConversationDetails))) {
				openConversationDetails.click();
				DriverUtils.waitUntilLocatorAppears(this.getDriver(),
						By.name(IOSLocators.nameAddContactToChatButton), 5);
			}
			if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
					By.name(IOSLocators.nameAddContactToChatButton))) {
				break;
			} else {
				swipeUp(1000);
			}
		}

		return new GroupChatInfoPage(this.getLazyDriver());
	}

	@Override
	public IOSPage swipeUp(int time) throws Exception {
		WebElement element = getDriver().findElement(
				By.name(IOSLocators.nameMainWindow));

		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();
		this.getDriver().swipe(coords.x + elementSize.width / 2,
				coords.y + elementSize.height - 170,
				coords.x + elementSize.width / 2, coords.y + 40, time);
		return returnBySwipe(SwipeDirection.UP);
	}

	@Override
	public IOSPage swipeRight(int time) throws Exception {
		WebElement element = getDriver().findElement(
				By.name(IOSLocators.nameMainWindow));

		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();
		this.getDriver().swipe(coords.x + 10, coords.y + 30,
				coords.x + elementSize.width / 2 + 20, coords.y + 30, time);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		IOSPage page = null;
		switch (direction) {
		case DOWN: {
			page = this;
			break;
		}
		case UP: {
			page = new GroupChatInfoPage(this.getLazyDriver());
			break;
		}
		case LEFT: {
			break;
		}
		case RIGHT: {
			page = new ContactListPage(this.getLazyDriver());
			break;
		}
		}
		return page;
	}

	public boolean isYouLeftMessageShown() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.nameYouLeftMessage));
	}

}
