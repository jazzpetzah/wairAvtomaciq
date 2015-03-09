package com.wearezeta.auto.ios.pages;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class PeoplePickerPage extends IOSPage {

	@FindBy(how = How.NAME, using = IOSLocators.namePickerSearch)
	private WebElement peoplePickerSearch;

	@FindBy(how = How.NAME, using = IOSLocators.namePickerClearButton)
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

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSendAnInviteButton)
	private WebElement sendInviteButton;

	private int numberTopSelected = 0;
	private boolean laterClicked = false;

	public PeoplePickerPage(ZetaIOSDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void clickLaterButton() throws Exception {
		if (isLaterClicked()) {
			return;
		}
		if (DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.nameShareButton))) {
			shareButton.click();
			setLaterClicked(true);
		}
	}

	public Boolean isPeoplePickerPageVisible() throws Exception {

		boolean result = DriverUtils.waitUntilElementAppears(driver,
				By.name(IOSLocators.namePickerClearButton));
		clickLaterButton();
		return result;
	}

	public void tapOnPeoplePickerSearch() {
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
		return ImageUtil.getOverlapScore(clockImage, templateImage);
	}

	public BufferedImage getAvatarClockIconScreenShot(String name)
			throws IOException {
		return getElementScreenshot(driver.findElement(By.name(name)));
	}

	public void fillTextInPeoplePickerSearch(String text) {
		peoplePickerSearch.sendKeys(text);
	}

	public boolean waitUserPickerFindUser(String user) throws Exception {
		return DriverUtils.waitUntilElementAppears(driver, By.name(user));
	}

	public ConnectToPage clickOnNotConnectedUser(String name) throws Exception {
		ConnectToPage page;
		driver.findElement(By.name(name)).click();
		page = new ConnectToPage(this.getDriver(), this.getWait());
		return page;
	}

	public ConnectToPage pickUserAndTap(String name) throws Exception {

		PickUser(name).click();
		return new ConnectToPage(this.getDriver(), this.getWait());
	}

	public PendingRequestsPage pickIgnoredUserAndTap(String name)
			throws Exception {
		PickUser(name).click();
		return new PendingRequestsPage(this.getDriver(), this.getWait());
	}

	public ContactListPage dismissPeoplePicker() throws Exception {
		peoplePickerClearBtn.click();
		return new ContactListPage(this.getDriver(), this.getWait());
	}

	public boolean isAddToConversationBtnVisible() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
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
		if (isGroupChat) {
			return new GroupChatPage(this.getDriver(), this.getWait());
		} else {
			return new DialogPage(this.getDriver(), this.getWait());
		}
	}

	public GroupChatInfoPage clickOnUserToAddToExistingGroupChat(String name)
			throws Throwable {
		GroupChatInfoPage page = null;
		driver.findElement(By.name(name)).click();
		page = new GroupChatInfoPage(this.getDriver(), this.getWait());
		return page;
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		IOSPage page = null;
		switch (direction) {
		case DOWN: {
			page = new ContactListPage(this.getDriver(), this.getWait());
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
		user = driver.findElementByName(name);
		return user;
	}

	public void clearInputField() {
		peoplePickerSearch.clear();
	}

	public boolean isContactsLabelVisible() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.namePeoplePickerContactsLabel));
	}

	public void selectUser(String name) throws Exception {
		WebElement el = driver.findElement(By.name(name));
		DriverUtils.waitUntilElementClickable(driver, el);
		el.click();
	}

	public void tapNumberOfTopConnections(int numberToTap) {
		numberTopSelected = 0;
		for (int i = 1; i < numberToTap + 1; i++) {
			numberTopSelected++;
			driver.findElement(
					By.xpath(String.format(
							IOSLocators.xpathPeoplePickerTopConnectionsAvatar,
							i))).click();
		}
	}

	public boolean isCreateConversationButtonVisible() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.nameCreateConversationButton));
	}

	public IOSPage clickCreateConversationButton() throws Throwable {
		createConverstaionButton.click();
		if (numberTopSelected >= 2) {
			return new GroupChatPage(this.getDriver(), this.getWait());
		} else {
			return new DialogPage(this.getDriver(), this.getWait());
		}
	}

	public boolean isTopPeopleLabelVisible() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.NamePeoplePickerTopPeopleLabel));
	}

	public boolean isUserSelected(String name) {
		WebElement el = driver.findElement(By.xpath(String.format(
				IOSLocators.xpathPeoplePickerUserAvatar, name)));
		boolean flag = el.getAttribute("value").equals("1");
		return flag;
	}

	public void clickConnectedUserAvatar(String name) {
		WebElement el = driver.findElement(By.xpath(String.format(
				IOSLocators.xpathPeoplePickerUserAvatar, name)));
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
		return new GroupChatPage(this.getDriver(), this.getWait());
	}

	public OtherUserOnPendingProfilePage clickOnUserOnPending(String contact)
			throws Exception {
		OtherUserOnPendingProfilePage page;
		driver.findElement(By.name(contact)).click();
		page = new OtherUserOnPendingProfilePage(this.getDriver(),
				this.getWait());
		return page;
	}

	public boolean isUploadDialogShown() throws Exception {
		boolean isLaterBtnVisible = DriverUtils.isElementDisplayed(
				this.getDriver(), By.name(IOSLocators.nameShareButton));
		return isLaterBtnVisible;
	}

	public void clickContinueButton() throws Exception {
		if (DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.nameContinueUploadButton))) {
			continueButton.click();
		}
	}

	public boolean isPeopleYouMayKnowLabelVisible() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.namePeopleYouMayKnowLabel));
	}

	public DialogPage unblockUser() throws Exception {
		unblockButton.click();
		return new DialogPage(this.getDriver(), this.getWait());
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

	public boolean isLaterClicked() {
		return laterClicked;
	}

	public void setLaterClicked(boolean laterClicked) {
		this.laterClicked = laterClicked;
	}

}
