package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class OtherUserPersonalInfoPage extends AndroidPage {

	private static final Logger log = ZetaLogger
			.getLog(OtherUserPersonalInfoPage.class.getSimpleName());

	public static final String REMOVE_FROM_CONVERSATION_BUTTON = "Remove";
	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.75;
	public static final String LEAVE_CONVERSATION_BUTTON = "Leave conversation";
	public static final String LEAVE_BUTTON = "LEAVE";

	private static final Function<String, String> xpathPartcipantNameByText = text -> String
			.format("//*[@id='ttv__participants__header' and @value='%s']",
					text);

	private static final Function<String, String> xpathPartcipantEmailByText = text -> String
			.format("//*[@id='ttv__participants__sub_header' and @value='%s']",
					text);

	private static final Function<String, String> xpathSingleParticipantNameByText = text -> String
			.format("//*[@id='ttv__single_participants__header' and @value='%s']",
					text);

	private static final Function<String, String> xpathSingleParticipantEmailByText = text -> String
			.format("//*[@id='ttv__single_participants__sub_header' and @value='%s']",
					text);

	// Names on avatars are in uppercase and only the first part is visible
	private static final Function<String, String> xpathParticipantAvatarByName = name -> String
			.format("//*[@value='%s']/parent::*/parent::*", name.toUpperCase()
					.split("\\s+")[0]);

	private static final String idParticipantsHeader = "ttv__participants__header";
	@FindBy(id = idParticipantsHeader)
	private WebElement groupChatName;

	private static final String idParticipantsHeaderEditable = "taet__participants__header__editable";
	@FindBy(id = idParticipantsHeaderEditable)
	private WebElement groupChatNameEditable;

	private static final String idUserProfileConfirmationMenu = "user_profile_confirmation_menu";
	@FindBy(id = idUserProfileConfirmationMenu)
	private WebElement confirmMenu;

	private static final String idBlockButton = "ttv__conversation_settings__block";
	@FindBy(id = idBlockButton)
	private WebElement blockButton;

	public static final String idRightActionButton = "gtv__participants__right__action";
	@FindBy(id = idRightActionButton)
	private WebElement rightConversationButton;

	private static final String idRenameButton = "ttv__conversation_settings__rename";
	@FindBy(id = idRenameButton)
	private WebElement renameButton;

	private static final String idArchiveButton = "ttv__conversation_settings__archive";
	@FindBy(id = idArchiveButton)
	private WebElement archiveButton;

	private static final String idLeaveButton = "ttv__conversation_settings__leave";
	@FindBy(id = idLeaveButton)
	private WebElement leaveButton;

	private static final String idSilenceButton = "ttv__conversation_settings__silence";
	@FindBy(id = idSilenceButton)
	private WebElement silenceButton;

	public static final String idUnblockBtn = "zb__connect_request__unblock_button";
	@FindBy(id = idUnblockBtn)
	private WebElement unblockButton;

	@FindBy(id = PeoplePickerPage.idParticipantsClose)
	private WebElement closeButton;

	@FindBy(how = How.CLASS_NAME, using = classNameFrameLayout)
	private WebElement frameLayout;

	@FindBy(id = idPager)
	private WebElement backGround;

	@FindBy(id = idConfirmBtn)
	private WebElement confirmBtn;

	private static final String idLeftActionButton = "gtv__participants__left__action";
	@FindBy(id = idLeftActionButton)
	private WebElement addContactBtn;

	private static final String idLeftActionLabel = "ttv__participants__left_label";
	@FindBy(id = idLeftActionLabel)
	private WebElement addContactLabel;

	private static final String idParticipantsSubHeader = "ttv__participants__sub_header";
	@FindBy(id = idParticipantsSubHeader)
	private WebElement participantsSubHeader;

	@FindBy(id = ConnectToPage.idConnectToHeader)
	private List<WebElement> connectToHeader;

	public OtherUserPersonalInfoPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void pressOptionsMenuButton() throws Exception {
		this.getWait().until(
				ExpectedConditions
						.elementToBeClickable(rightConversationButton));
		rightConversationButton.click();
	}

	public ContactListPage pressLeaveButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), leaveButton);
		leaveButton.click();
		return new ContactListPage(this.getLazyDriver());
	}

	public void pressSilenceButton() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), silenceButton);
		silenceButton.click();
	}

	public void clickBlockBtn() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), blockButton);
		blockButton.click();
	}

	public AndroidPage clickUnblockBtn() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), unblockButton);
		unblockButton.click();
		return new DialogPage(this.getLazyDriver());
	}

	public boolean isUnblockBtnVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(unblockButton);
	}

	private static By[] getOneToOneOptionsMenuLocators() {
		return new By[] { By.id(idBlockButton), By.id(idSilenceButton),
				By.id(idArchiveButton) };
	}

	private static final int MENU_ITEM_VISIBILITY_TIMEOUT_SECONDS = 5;

	public boolean areOneToOneMenuOptionsVisible() throws Exception {
		for (By locator : getOneToOneOptionsMenuLocators()) {
			if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator,
					MENU_ITEM_VISIBILITY_TIMEOUT_SECONDS)) {
				return false;
			}
		}
		return true;
	}

	public boolean areOneToOneMenuOptionsNotVisible() throws Exception {
		for (By locator : getOneToOneOptionsMenuLocators()) {
			if (!DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
					locator, MENU_ITEM_VISIBILITY_TIMEOUT_SECONDS)) {
				return false;
			}
		}
		return true;
	}

	private static By[] getUserProfileLocators() {
		return new By[] { By.id(PeoplePickerPage.idParticipantsClose),
				By.id(idParticipantsSubHeader), By.id(idParticipantsHeader) };
	}

	public boolean isOneToOneUserProfileUIContentNotVisible() throws Exception {
		for (By locator : getUserProfileLocators()) {
			if (!DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
					locator, MENU_ITEM_VISIBILITY_TIMEOUT_SECONDS)) {
				return false;
			}
		}
		return true;
	}

	public boolean isOneToOneUserProfileUIContentVisible() throws Exception {
		for (By locator : getUserProfileLocators()) {
			if (!DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
					locator, MENU_ITEM_VISIBILITY_TIMEOUT_SECONDS)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		switch (direction) {
		case DOWN: {
			return new PeoplePickerPage(this.getLazyDriver());
		}
		default:
			return null;
		}
	}

	public boolean isOtherUserNameVisible(String expectedName) throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathPartcipantNameByText.apply(expectedName)), 1)
				|| DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By
						.xpath(xpathSingleParticipantNameByText
								.apply(expectedName)), 1);
	}

	public boolean isOtherUserMailVisible(String expectedEmail)
			throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathPartcipantEmailByText.apply(expectedEmail)), 1)
				|| DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By
						.xpath(xpathSingleParticipantEmailByText
								.apply(expectedEmail)), 1);
	}

	public boolean isConversationAlertVisible() {
		return DriverUtils.isElementPresentAndDisplayed(confirmMenu);
	}

	public OtherUserPersonalInfoPage pressConfirmBtn() throws Exception {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(confirmBtn));
		confirmBtn.click();
		return new OtherUserPersonalInfoPage(this.getLazyDriver());
	}

	public PeoplePickerPage tapAddContactBtn() throws Exception {
		addContactBtn.click();
		return new PeoplePickerPage(this.getLazyDriver());
	}

	public boolean isBackGroundImageCorrect(String imageName) throws Exception {
		final BufferedImage bgImage = getElementScreenshot(backGround)
				.orElseThrow(IllegalStateException::new);
		String path = CommonUtils.getImagesPath(CommonUtils.class);
		BufferedImage realImage = ImageUtil.readImageFromFile(path + imageName);
		double score = ImageUtil.getOverlapScore(realImage, bgImage);
		System.out.println(score);
		return (score >= MIN_ACCEPTABLE_IMAGE_VALUE);
	}

	public boolean isParticipantExists(String name) throws Exception {
		final By locator = By.xpath(xpathParticipantAvatarByName.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void tapOnParticipantsHeader() {
		groupChatName.click();
	}

	public void renameGroupChat(String chatName) throws Exception {
		groupChatNameEditable.clear();
		groupChatNameEditable.sendKeys(chatName);
		// FIXME: The app crashes if we apply name changes too fast :-@
		groupChatNameEditable.sendKeys("\n");
	}

	public AndroidPage tapOnParticipant(String name) throws Exception {
		this.getWait().until(ExpectedConditions.visibilityOf(groupChatName));
		try {
			final By nameLocator = By.xpath(xpathParticipantAvatarByName
					.apply(name));
			assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
					nameLocator);
			this.getDriver().findElement(nameLocator).click();
		} catch (Exception e) {
			log.debug("Failed to find contact with name " + name
					+ "\n. Page source: " + this.getDriver().getPageSource());
			throw e;
		}
		if (connectToHeader.size() > 0) {
			return new ConnectToPage(this.getLazyDriver());
		} else {
			return new OtherUserPersonalInfoPage(this.getLazyDriver());
		}
	}

	public String getSubHeader() {
		return participantsSubHeader.getText();
	}

	public String getConversationName() throws Exception {
		return groupChatName.getText();
	}

	public DialogPage tabBackButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), closeButton);
		closeButton.click();
		return new DialogPage(this.getLazyDriver());
	}

	public boolean isParticipantAvatarVisible(String name) throws Exception {
		final By locator = By.xpath(xpathParticipantAvatarByName.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}
}
