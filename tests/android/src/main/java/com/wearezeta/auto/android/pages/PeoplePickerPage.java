package com.wearezeta.auto.android.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

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
	@FindBy(id = idPickerSearch)
	private WebElement pickerSearch;

	public static final String idPeoplePickerClearbtn = "gtv_pickuser__clearbutton";

	public static final String idConnectionRequiesMessage = "cet__send_connect_request__first_message";

	public static final String idSendConnectionRequestButton = "zb__send_connect_request__connect_button";

	private static final Function<String, String> xpathPeoplePickerGroupByName = name -> String
			.format("//*[@id='ttv_pickuser_searchconversation_name' and @value='%s']",
					name);

	public static final Function<String, String> xpathPeoplePickerContactByName = name -> String
			.format("//*[@id='ttv_pickuser__searchuser_name' and @value='%s']",
					name);

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(PeoplePickerPage.class
			.getSimpleName());

	private static final String idPickerSearchUsers = "ttv_pickuser__searchuser_name";
	@FindBy(id = idPickerSearchUsers)
	private List<WebElement> pickerSearchUsers;
	@FindBy(id = idPickerSearchUsers)
	private WebElement pickerSearchUser;

	private static final String idPickerTopPeopleHeader = "ttv_pickuser__list_header_title";
	@FindBy(id = idPickerTopPeopleHeader)
	private WebElement pickerTopPeopleHeader;

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

	private static final String idPickerBtnDone = "ttv_pickuser_confirmbutton__title";
	@FindBy(id = idPickerBtnDone)
	private WebElement addToConversationsButton;

	private static final String idCreateConversationIcon = "gtv_pickuser_confirmbutton__icon";
	@FindBy(id = idCreateConversationIcon)
	private WebElement createConversation;

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

	public void tapPeopleSearch() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idPickerSearch)) : "The People Picker search input is not visible";
		pickerSearch.click();
	}

	public void tapOnContactInTopPeoples(String name) throws Exception {
		final By locator = By.xpath(xpathTopConversationContactByName
				.apply(name));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		this.getDriver().findElement(locator).click();
	}

	public void typeTextInPeopleSearch(String text) throws Exception {
		pickerSearch.clear();
		pickerSearch.sendKeys(text);
	}

	public void addTextToPeopleSearch(String text) throws Exception {
		pickerSearch.sendKeys(text);
	}

	public boolean isNoResultsFoundVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(), noResults);
	}

	public boolean isTopPeopleHeaderVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				pickerTopPeopleHeader);
	}

	public boolean waitUntilTopPeopleHeaderInvisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(idPickerTopPeopleHeader));
	}

	public AndroidPage selectContact(String contactName) throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				pickerSearchUser);
		pickerSearchUser.click();
		final Map<By, AndroidPage> pagesMapping = new HashMap<By, AndroidPage>();
		pagesMapping.put(By.id(OtherUserPersonalInfoPage.idUnblockBtn),
				new OtherUserPersonalInfoPage(this.getLazyDriver()));
		pagesMapping.put(By.id(ConnectToPage.idConnectToHeader),
				new ConnectToPage(this.getLazyDriver()));
		pagesMapping.put(By.id(idPickerBtnDone), this);
		final int maxScanTries = 3;
		int scanTry = 1;
		while (scanTry <= maxScanTries) {
			for (Map.Entry<By, AndroidPage> entry : pagesMapping.entrySet()) {
				if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
						entry.getKey(), 1)) {
					return entry.getValue();
				}
			}
			scanTry++;
		}
		return new DialogPage(this.getLazyDriver());
	}

	public AndroidPage selectGroup(String contactName) throws Exception {
		final By locator = By.xpath(xpathPeoplePickerGroupByName
				.apply(contactName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		this.getDriver().findElement(locator).click();

		if (DriverUtils.isElementPresentAndDisplayed(getDriver(),
				addToConversationsButton)) {
			return this;
		} else {
			return new DialogPage(this.getLazyDriver());
		}
	}

	@Override
	public AndroidPage swipeDown(int time) throws Exception {
		elementSwipeDown(content, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		switch (direction) {
		case DOWN: {
			return new ContactListPage(this.getLazyDriver());
		}
		default:
			return null;
		}
	}

	public boolean isPeoplePickerPageVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				pickerSearch);
	}

	// FIXME: find better locator
	public void waitUserPickerFindUser(String contactName) throws Exception {
		for (int i = 0; i < 50; i++) {
			List<WebElement> elements = pickerSearchUsers;
			for (WebElement element : elements) {
				try {
					if (element.getText().toLowerCase()
							.equals(contactName.toLowerCase())) {
						return;
					}
				} catch (Exception ex) {
					continue;
				}
			}
			Thread.sleep(100);
		}
	}

	public ContactListPage navigateBack() throws Exception {
		pickerClearBtn.click();
		return new ContactListPage(this.getLazyDriver());
	}

	public boolean isAddToConversationBtnVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				addToConversationsButton);
	}

	public DialogPage clickOnAddToCoversationButton() throws Exception {
		this.getDriver().navigate().back();
		addToConversationsButton.click();
		return new DialogPage(this.getLazyDriver());
	}

	// TODO: move this to some base page

	public AndroidPage tapCreateConversation() throws Exception {
		final By locator = By.id(idCreateConversationIcon);
		this.hideKeyboard();
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		createConversation.click();
		return new DialogPage(this.getLazyDriver());
	}

	public ContactListPage tapClearButton() throws Exception {
		pickerClearBtn.click();
		return new ContactListPage(this.getLazyDriver());
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
}
