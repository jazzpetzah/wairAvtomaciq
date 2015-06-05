package com.wearezeta.auto.ios.pages;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class PeoplePickerPage extends IOSPage {

	@FindBy(how = How.NAME, using = IOSLocators.namePickerSearch)
	private WebElement peoplePickerSearch;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPickerClearButton)
	private WebElement peoplePickerClearBtn;

	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameContactListNames)
	private List<WebElement> resultList;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathUnicUserPickerSearchResult)
	private WebElement userPickerSearchResult;

	@FindBy(how = How.NAME, using = IOSLocators.nameKeyboardGoButton)
	private WebElement goButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameCreateConversationButton)
	private WebElement createConverstaionButton;

	@FindBy(how = How.NAME, using = IOSLocators.namePeoplePickerContactsLabel)
	private WebElement contactsLabel;

	@FindBy(how = How.NAME, using = IOSLocators.namePeoplePickerOtheraLabel)
	private WebElement othersLabel;

	@FindBy(how = How.NAME, using = IOSLocators.NamePeoplePickerTopPeopleLabel)
	private WebElement topPeopleLabel;

	@FindBy(how = How.NAME, using = IOSLocators.namePeoplePickerAddToConversationButton)
	private WebElement addToConversationBtn;

	// @FindBy(how = How.NAME, using = IOSLocators.nameLaterButton)
	// private WebElement laterButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameShareButton)
	private WebElement shareButton;

	@FindBy(how = How.NAME, using = IOSLocators.PeoplePickerPage.nameNotNowButton)
	private WebElement notNowButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameContinueUploadButton)
	private WebElement continueButton;

	@FindBy(how = How.NAME, using = IOSLocators.namePeopleYouMayKnowLabel)
	private WebElement peopleYouMayKnowLabel;

	@FindBy(how = How.NAME, using = IOSLocators.nameUnblockButton)
	private WebElement unblockButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPeoplePickerAllTopPeople)
	private List<WebElement> topPeopleList;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathInviteCopyButton)
	private WebElement inviteCopyButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameSendAnInviteButton)
	private WebElement sendInviteButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameInstantConnectButton)
	private WebElement instantConnectButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSearchResultCell)
	private WebElement searchResultCell;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSearchResultContainer)
	private WebElement searchResultContainer;

	@FindBy(how = How.NAME, using = IOSLocators.PeoplePickerPage.nameLaterButton)
	private WebElement maybeLaterButton;

	private int numberTopSelected = 0;

	public PeoplePickerPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void clickMaybeLaterButton() {
		maybeLaterButton.click();
	}

	public void clickNotNowButton() {
		notNowButton.click();
	}

	public void clickLaterButton() throws Exception {
		for (int i = 0; i < 3; i++) {
			if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
					By.name(IOSLocators.PeoplePickerPage.nameNotNowButton), 3)) {
				if (i > 0) {
					this.minimizeApplication(3);
				}
				getWait().until(
						ExpectedConditions.elementToBeClickable(notNowButton));
				notNowButton.click();
				break;
			}
		}
	}

	public Boolean isPeoplePickerPageVisible() throws Exception {
		boolean result = DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(IOSLocators.namePickerSearch));
		Thread.sleep(1000);
		clickLaterButton();
		return result;
	}

	public void tapOnPeoplePickerSearch() throws Exception {
		this.getDriver().tap(1, peoplePickerSearch.getLocation().x + 40,
				peoplePickerSearch.getLocation().y + 30, 1);// workaround for
															// people picker
															// activation
	}

	public void tapOnPeoplePickerClearBtn() {
		peoplePickerClearBtn.click();
	}

	public double checkAvatarClockIcon(String name) throws Exception {
		String path = null;
		BufferedImage clockImage = getAvatarClockIconScreenShot(name);
		path = CommonUtils.getAvatarWithClockIconPathIOS(GroupChatPage.class);
		BufferedImage templateImage = ImageUtil.readImageFromFile(path);
		double score = ImageUtil.getOverlapScore(clockImage, templateImage);
		return score;
	}

	public BufferedImage getAvatarClockIconScreenShot(String name)
			throws Exception {
		return getScreenshotByCoordinates(
				searchResultCell.getLocation().x,
				searchResultCell.getLocation().y * 2,
				searchResultCell.getSize().height * 5 / 2,
				searchResultCell.getLocation().y
						+ searchResultCell.getSize().height * 4 / 5)
				.orElseThrow(IllegalStateException::new);
	}

	public void fillTextInPeoplePickerSearch(String text) {
		try {
			peoplePickerSearch.sendKeys(text);
		} catch (WebDriverException ex) {
			peoplePickerSearch.clear();
			peoplePickerSearch.sendKeys(text);
		}
	}

	public boolean waitUserPickerFindUser(String user) throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(user));
	}

	public ConnectToPage clickOnNotConnectedUser(String name) throws Exception {
		ConnectToPage page;
		getDriver().findElement(By.name(name)).click();
		page = new ConnectToPage(this.getLazyDriver());
		return page;
	}

	public ConnectToPage pickUserAndTap(String name) throws Exception {

		PickUser(name).click();
		return new ConnectToPage(this.getLazyDriver());
	}

	public PendingRequestsPage pickIgnoredUserAndTap(String name)
			throws Exception {
		PickUser(name).click();
		return new PendingRequestsPage(this.getLazyDriver());
	}

	public ContactListPage dismissPeoplePicker() throws Exception {
		peoplePickerClearBtn.click();
		return new ContactListPage(this.getLazyDriver());
	}

	public void hidePeoplePickerKeyboard() throws Exception {
		DriverUtils.swipeUp(this.getDriver(), sendInviteButton, 500, 50, 40);
	}

	public void swipeToRevealHideSuggestedContact(String contact)
			throws Exception {
		WebElement contactToSwipe = this
				.getDriver()
				.findElement(
						By.xpath(String
								.format(IOSLocators.PeoplePickerPage.xpathSuggestedContactToSwipe,
										contact)));
		DriverUtils.swipeRight(this.getDriver(), contactToSwipe, 500, 50, 50);
	}

	public void swipeCompletelyToDismissSuggestedContact(String contact)
			throws Exception {
		WebElement contactToSwipe = this
				.getDriver()
				.findElement(
						By.xpath(String
								.format(IOSLocators.PeoplePickerPage.xpathSuggestedContactToSwipe,
										contact)));
		DriverUtils.swipeRight(this.getDriver(), contactToSwipe, 1000, 100, 50);
	}

	public void tapHideSuggestedContact(String contact) throws Exception {
		WebElement hideButtonforContact = this.getDriver().findElement(
				By.xpath(String.format(
						IOSLocators.PeoplePickerPage.xpathHideButtonForContact,
						contact)));
		hideButtonforContact.click();
	}

	public boolean isSuggestedContactVisible(String contact) throws Exception {

		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(String.format(
										IOSLocators.PeoplePickerPage.xpathSuggestedContact,
										contact)), 2);
	}

	public boolean isAddToConversationBtnVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.namePeoplePickerAddToConversationButton));
	}

	public boolean addToConversationNotVisible() {
		boolean flag;
		try {
			addToConversationBtn.click();
			flag = false;
		} catch (Exception e) {
			flag = true;
		}
		return flag;
	}

	public IOSPage clickOnGoButton(boolean isGroupChat) throws Exception {
		goButton.click();
		if (numberTopSelected >= 2 || isGroupChat) {
			return new GroupChatPage(this.getLazyDriver());
		} else {
			return new DialogPage(this.getLazyDriver());
		}
	}

	public GroupChatInfoPage clickOnUserToAddToExistingGroupChat(String name)
			throws Throwable {
		GroupChatInfoPage page = null;
		getDriver().findElement(By.name(name)).click();
		page = new GroupChatInfoPage(this.getLazyDriver());
		return page;
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		IOSPage page = null;
		switch (direction) {
		case DOWN: {
			page = new ContactListPage(this.getLazyDriver());
			break;
		}
		case UP: {
			page = this;
			break;
		}
		case LEFT: {
			break;
		}
		case RIGHT: {
			break;
		}
		}
		return page;
	}

	private WebElement PickUser(String name) throws Exception {
		WebElement user = null;
		fillTextInPeoplePickerSearch(name);
		waitUserPickerFindUser(name);
		user = getDriver().findElementByName(name);
		return user;
	}

	public void clearInputField() {
		peoplePickerSearch.clear();
	}

	public boolean isContactsLabelVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.namePeoplePickerContactsLabel));
	}

	public void selectUser(String name) throws Exception {
		List<WebElement> el = getDriver().findElements(By.name(name));
		if (el.size() == 0) {
			throw new NoSuchElementException("Element not found");
		}
		for (int i = 0; i < el.size(); i++) {
			if (el.get(i).isDisplayed() && el.get(i).isEnabled()) {
				DriverUtils.mobileTapByCoordinates(getDriver(), el.get(i));
				break;
			}
		}
	}

	public void tapNumberOfTopConnections(int numberToTap) throws Exception {
		numberTopSelected = 0;
		for (int i = 1; i < numberToTap + 1; i++) {
			numberTopSelected++;
			getDriver().findElement(
					By.xpath(String.format(
							IOSLocators.xpathPeoplePickerTopConnectionsAvatar,
							i))).click();
		}
	}

	public boolean isCreateConversationButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.nameCreateConversationButton));
	}

	public IOSPage clickCreateConversationButton() throws Throwable {
		createConverstaionButton.click();
		if (numberTopSelected >= 2) {
			return new GroupChatPage(this.getLazyDriver());
		} else {
			return new DialogPage(this.getLazyDriver());
		}
	}

	public boolean isTopPeopleLabelVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.NamePeoplePickerTopPeopleLabel), 2);
	}

	public boolean isConnectLabelVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.namePeopleYouMayKnowLabel));
	}

	public boolean isUserSelected(String name) throws Exception {
		WebElement el = getDriver().findElement(
				By.xpath(String.format(IOSLocators.xpathPeoplePickerUserAvatar,
						name)));
		boolean flag = el.getAttribute("value").equals("1");
		return flag;
	}

	public void clickConnectedUserAvatar(String name) throws Exception {
		WebElement el = getDriver().findElement(
				By.xpath(String.format(IOSLocators.xpathPeoplePickerUserAvatar,
						name)));
		el.click();
	}

	public void hitDeleteButton() {
		peoplePickerSearch.sendKeys(Keys.DELETE);
	}

	public void goIntoConversation() {
		peoplePickerSearch.sendKeys("\n");
	}

	public GroupChatPage clickAddToCoversationButton() throws Exception {
		addToConversationBtn.click();
		return new GroupChatPage(this.getLazyDriver());
	}

	public OtherUserOnPendingProfilePage clickOnUserOnPending(String contact)
			throws Exception {

		WebElement el = getDriver().findElement(By.name(contact));
		DriverUtils.mobileTapByCoordinates(getDriver(), el);
		return new OtherUserOnPendingProfilePage(this.getLazyDriver());
	}

	public boolean isUploadDialogShown() throws Exception {
		boolean isLaterBtnVisible = DriverUtils.waitUntilLocatorIsDisplayed(
				this.getDriver(), By.name(IOSLocators.nameShareButton), 2);
		return isLaterBtnVisible;
	}

	public void clickContinueButton() throws Exception {
		if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.nameContinueUploadButton))) {
			continueButton.click();
		}
	}

	public boolean isPeopleYouMayKnowLabelVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.namePeopleYouMayKnowLabel));
	}

	public DialogPage unblockUser() throws Exception {
		unblockButton.click();
		return new DialogPage(this.getLazyDriver());
	}

	public int getNumberOfSelectedTopPeople() {
		int selectedPeople = 0;
		for (WebElement people : topPeopleList) {
			if (people.getAttribute("value").equals("1")) {
				selectedPeople++;
			}
		}
		return selectedPeople;
	}

	public void tapSendInviteButton() {
		sendInviteButton.click();
	}

	public void tapSendInviteCopyButton() throws UnsupportedFlavorException,
			Exception {
		inviteCopyButton.click();
	}

	public void pressInstantConnectButton() {
		instantConnectButton.click();
	}

}
