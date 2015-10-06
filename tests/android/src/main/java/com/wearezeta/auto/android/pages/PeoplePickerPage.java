package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class PeoplePickerPage extends AndroidPage {

	private static final Function<String, String> xpathTopConversationContactByName = name -> String
			.format("//*[@value='%s']", name.toUpperCase());

	public static final String idParticipantsClose = "gtv__participants__close";

	// numbering starts from 1
	private static final Function<Integer, String> xpathPYMKItemByIdx = idx -> String
			.format("(//*[@id='ll__pickuser__sliding_row'])[%d]", idx);
	private static final Function<Integer, String> xpathPYMKItemByIdxLabel = idx -> String
			.format("%s//*[@id='ttv_pickuser__recommended_name']",
					xpathPYMKItemByIdx.apply(idx));
	private static final Function<Integer, String> xpathPYMKItemByIdxPlusButton = idx -> String
			.format("%s//*[@id='gtv__pickuser__recommended__quick_add']",
					xpathPYMKItemByIdx.apply(idx));
	private static final Function<Integer, String> xpathPYMKItemByIdxHideButton = idx -> String
			.format("%s/parent::*//*[@value='HIDE']",
					xpathPYMKItemByIdx.apply(idx));

	private static final Function<String, String> xpathPYMKItemByName = name -> String
			.format("//*[@id='ll__pickuser__sliding_row' and .//*[@value='%s']]",
					name);

	public static final String idPickerSearch = "puet_pickuser__searchbox";

	public static final String idPeoplePickerClearbtn = "gtv_pickuser__clearbutton";

	public static final String idSendConnectionRequestButton = "zb__send_connect_request__connect_button";

	private static final Function<String, String> xpathPeoplePickerGroupByName = name -> String
			.format("//*[@id='ttv_pickuser_searchconversation_name' and @value='%s']",
					name);

	public static final Function<String, String> xpathPeoplePickerContactByName = name -> String
			.format("//*[@id='ttv_pickuser__searchuser_name' and @value='%s']",
					name);

	private static final String idPickerSearchUsers = "ttv_pickuser__searchuser_name";
	@FindBy(id = idPickerSearchUsers)
	private WebElement pickerSearchUser;
	private static final Function<String, String> xpathPickerUserByName = name -> String
			.format("//*[@id='%s' and @value='%s']", idPickerSearchUsers, name);

	private static final String idTopPeopleRoot = "rv_top_users";

	private static final String idPeoplePickerSerchConversations = "ttv_pickuser_searchconversation_name";
	@FindBy(id = idPeoplePickerSerchConversations)
	private List<WebElement> pickerSearchConversations;

	@FindBy(id = idPeoplePickerClearbtn)
	private WebElement pickerClearBtn;

	private static final String idPickerRows = "ll_pickuser__rowview_searchuser";
	@FindBy(id = idPickerRows)
	private List<WebElement> pickerSearchRows;

	private static final String idPickerUsersUnselected = "pick_user_chathead_unselected";
	@FindBy(id = idPickerUsersUnselected)
	private List<WebElement> pickerUsersUnselected;

	private static final String idPickerGrid = "gv_pickuser__topresult__gridview";
	@FindBy(id = idPickerGrid)
	private WebElement pickerGrid;

	private static final String idPickerBtnDone = "zb__pickuser__confirmation_button";
	@FindBy(id = idPickerBtnDone)
	private WebElement addToConversationsButton;

	private static final String idCreateOrOpenConversationButton = "zb__conversation_quick_menu__conversation_button";
	@FindBy(id = idCreateOrOpenConversationButton)
	private WebElement createOrOpenConversation;

	private static final String idNoResultsFound = "ttv_pickuser__error_header";
	@FindBy(id = idNoResultsFound)
	private WebElement noResults;

	private static final String idPickerListContainer = "pfac__pickuser__header_list_view";
	@FindBy(id = idPickerListContainer)
	private WebElement content;

	@FindBy(id = ConnectToPage.idConnectToHeader)
	private List<WebElement> connectToHeader;

	private static final String idPickerRecomendedName = "ttv_pickuser__recommended_name";
	@FindBy(id = idPickerRecomendedName)
	private WebElement recommendedName;

	public PeoplePickerPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public WebElement findVisiblePickerSearch() throws Exception {
		DriverUtils.waitUntilLocatorAppears(getDriver(), By.id(idPickerSearch));
		List<WebElement> pickerSearches = getDriver().findElements(
				By.id(idPickerSearch));
		for (WebElement candidate : pickerSearches) {
			if (DriverUtils
					.isElementPresentAndDisplayed(getDriver(), candidate)
					&& candidate.getLocation().getX() >= 0
					&& candidate.getLocation().getY() >= 0) {
				return candidate;
			}
		}
		throw new ElementNotVisibleException(
				"People Picker input is not displayed");
	}

	public void tapPeopleSearch() throws Exception {
		findVisiblePickerSearch().click();
	}

	public void tapOnContactInTopPeoples(String name) throws Exception {
		final By locator = By.xpath(xpathTopConversationContactByName
				.apply(name));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		this.getDriver().findElement(locator).click();
	}

	public void typeTextInPeopleSearch(String text) throws Exception {
		final WebElement pickerSearch = findVisiblePickerSearch();
		pickerSearch.clear();
		pickerSearch.sendKeys(text);
	}

	public void addTextToPeopleSearch(String text) throws Exception {
		findVisiblePickerSearch().sendKeys(text);
	}

	public boolean isNoResultsFoundVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(), noResults);
	}

	public boolean isTopPeopleHeaderVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idTopPeopleRoot));
	}

	public boolean waitUntilTopPeopleHeaderInvisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(idTopPeopleRoot));
	}

	public void selectContact(String contactName) throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				pickerSearchUser) : String.format(
				"The user '%s' has not been found in People Picker",
				contactName);
		pickerSearchUser.click();
	}

	public void selectGroup(String contactName) throws Exception {
		final By locator = By.xpath(xpathPeoplePickerGroupByName
				.apply(contactName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		this.getDriver().findElement(locator).click();
	}

	public boolean isPeoplePickerPageVisible() throws Exception {
		try {
			findVisiblePickerSearch();
			return true;
		} catch (ElementNotVisibleException e) {
			return false;
		}
	}

	public void waitUserPickerFindUser(String contactName) throws Exception {
		final By locator = By.xpath(xpathPickerUserByName.apply(contactName));
		assert DriverUtils.waitUntilLocatorAppears(getDriver(), locator) : String
				.format("User '%s' does not exist in the People Picker list",
						contactName);
	}

	public void navigateBack() throws Exception {
		pickerClearBtn.click();
	}

	public boolean isAddToConversationBtnVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idPickerBtnDone));
	}

	public void clickOnAddToCoversationButton() throws Exception {
		addToConversationsButton.click();
	}

	public void tapCreateConversation() throws Exception {
		// this.hideKeyboard();
		assert waitUntilOpenConversationButtonIsVisible() : "Create/Open Conversation button is not visible in People Picker";
		createOrOpenConversation.click();
	}

	public void tapClearButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				pickerClearBtn);
		pickerClearBtn.click();
	}

	public boolean userIsVisible(String contact) throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.xpath(xpathPeoplePickerContactByName.apply(contact)));
	}

	public boolean groupIsVisible(String contact) throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.xpath(xpathPeoplePickerGroupByName.apply(contact)));
	}

	public PeoplePickerPage selectContactByLongTap(String contact)
			throws Exception {
		final By locator = By.xpath(xpathPeoplePickerContactByName
				.apply(contact));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		DriverUtils.androidLongClick(this.getDriver(),
				getDriver().findElement(locator));
		return this;
	}

	// !!! Indexing starts from 1

	public String getPYMKItemName(int index) throws Exception {
		final By locator = By.xpath(xpathPYMKItemByIdxLabel.apply(index));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		return getDriver().findElement(locator).getText();
	}

	public void clickPlusOnPYMKItem(int index) throws Exception {
		final By locator = By.xpath(xpathPYMKItemByIdxPlusButton.apply(index));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		this.getDriver().findElement(locator).click();
	}

	public void longSwipeRigthOnPYMKItem(int index) throws Exception {
		final By locator = By.xpath(xpathPYMKItemByIdx.apply(index));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		DriverUtils.swipeRight(getDriver(), getDriver().findElement(locator),
				1000, 10, 50, 75, 50);
	}

	public void shortSwipeRigthOnPYMKItem(int index) throws Exception {
		final By locator = By.xpath(xpathPYMKItemByIdx.apply(index));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		DriverUtils.swipeRight(getDriver(), getDriver().findElement(locator),
				1000, 10, 50, 50, 50);
	}

	public void clickHideButtonOnPYMKItem(int index) throws Exception {
		final By locator = By.xpath(xpathPYMKItemByIdxHideButton.apply(index));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		this.getDriver().findElement(locator).click();
	}

	public boolean isUserInPYMKList(String name) throws Exception {
		final By locator = By.xpath(xpathPYMKItemByName.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitUntilPYMKItemIsInvisible(String name) throws Exception {
		final By locator = By.xpath(xpathPYMKItemByName.apply(name));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
	}

	public boolean waitUntilPYMKItemIsVisible(int index) throws Exception {
		final By locator = By.xpath(xpathPYMKItemByIdx.apply(index));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void tapPYMKItem(int index) throws Exception {
		final By locator = By.xpath(xpathPYMKItemByIdx.apply(index));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : "The first PYMK item is not visible";
		getDriver().findElement(locator).click();
	}

	public void doShortSwipeDown() throws Exception {
		final Point coords = content.getLocation();
		final Dimension elementSize = content.getSize();
		this.getDriver().swipe(coords.x + elementSize.width / 2, coords.y,
				coords.x + elementSize.width / 2,
				coords.y + elementSize.height / 10, 500);
	}

	public void doLongSwipeDown() throws Exception {
		final Point coords = content.getLocation();
		final Dimension elementSize = content.getSize();
		this.getDriver().swipe(coords.x + elementSize.width / 2, coords.y,
				coords.x + elementSize.width / 2,
				coords.y + elementSize.height / 4 * 3, 2000);
	}

	public void tapOpenConversationButton() {
		createOrOpenConversation.click();
	}

	public boolean waitUntilOpenConversationButtonIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idCreateOrOpenConversationButton));
	}

	public boolean waitUntilOpenConversationButtonIsInvisible()
			throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(idCreateOrOpenConversationButton));
	}

	public AndroidPage swipeDown(int durationMilliseconds) throws Exception {
		DriverUtils.swipeByCoordinates(getDriver(), durationMilliseconds, 50,
				20, 50, 90);
		return new ContactListPage(getLazyDriver());
	}
}
