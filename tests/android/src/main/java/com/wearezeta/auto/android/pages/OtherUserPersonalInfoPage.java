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

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(OtherUserPersonalInfoPage.class.getSimpleName());

	public static final String REMOVE_FROM_CONVERSATION_BUTTON = "Remove";
	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.75;
	public static final String LEAVE_CONVERSATION_BUTTON = "Leave conversation";
	public static final String LEAVE_BUTTON = "LEAVE";

	public static final String idUnblockBtn = "zb__connect_request__unblock_button";
	@FindBy(id = idUnblockBtn)
	private WebElement unblockButton;

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

	public static final String idRightActionButton = "gtv__participants__right__action";
	@FindBy(id = idRightActionButton)
	private WebElement rightConversationButton;

	private static final String idRenameButton = "ttv__conversation_settings__rename";
	@FindBy(id = idRenameButton)
	private WebElement renameButton;

	private static final Function<String, String> xpathConvOptionsMenuItemByName = name -> String
			.format("//*[starts-with(@id, 'ttv__settings_box__item') and @value='%s']",
					name.toUpperCase());

	@FindBy(id = PeoplePickerPage.idParticipantsClose)
	private WebElement closeButton;

	@FindBy(how = How.CLASS_NAME, using = classNameFrameLayout)
	private WebElement frameLayout;

	@FindBy(id = idPager)
	private WebElement backGround;

	@FindBy(xpath = xpathConfirmBtn)
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

	public boolean isUnblockBtnVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				unblockButton);
	}

	private static By[] getOneToOneOptionsMenuLocators() {
		return new By[] {
				By.xpath(xpathConvOptionsMenuItemByName.apply("BLOCK")),
				By.xpath(xpathConvOptionsMenuItemByName.apply("SILENCE")),
				By.xpath(xpathConvOptionsMenuItemByName.apply("ARCHIVE")) };
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

	private static By[] getParticipantPageLocators() {
		return new By[] { By.id(PeoplePickerPage.idParticipantsClose),
				By.id(idParticipantsSubHeader), By.id(idParticipantsHeader) };
	}

	public boolean isParticipatPageUIContentNotVisible() throws Exception {
		for (By locator : getParticipantPageLocators()) {
			if (!DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
					locator, MENU_ITEM_VISIBILITY_TIMEOUT_SECONDS)) {
				return false;
			}
		}
		return true;
	}

	public boolean isParticipatPageUIContentVisible() throws Exception {
		for (By locator : getParticipantPageLocators()) {
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

	public void selectConvoSettingsMenuItem(String itemName) throws Exception {
		final By locator = By.xpath(xpathConvOptionsMenuItemByName
				.apply(itemName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("Conversation menu item '%s' could not be found on the current screen");
		getDriver().findElement(locator).click();
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

	public boolean isConversationAlertVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				confirmMenu);
	}

	public OtherUserPersonalInfoPage pressConfirmBtn() throws Exception {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(confirmBtn));
		confirmBtn.click();
		assert DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(xpathConfirmBtn), 3) : "Confirmation button is still visible after 3 seconds timeout";
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

	public boolean isParticipantNotVisible(String name) throws Exception {
		final By locator = By.xpath(xpathParticipantAvatarByName.apply(name));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
	}

	public void tapOnParticipantsHeader() {
		groupChatName.click();
	}

	public void renameGroupChat(String chatName) throws Exception {
		groupChatNameEditable.clear();
		groupChatNameEditable.sendKeys(chatName);
		this.pressEnter();
		this.pressEsc();
	}

	public AndroidPage tapOnParticipant(String name) throws Exception {
		this.getWait().until(ExpectedConditions.visibilityOf(groupChatName));
		final By nameLocator = By.xpath(xpathParticipantAvatarByName
				.apply(name));
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(getDriver(), nameLocator);
		// Wait for animation
		Thread.sleep(1000);
		this.getDriver().findElement(nameLocator).click();
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

	public DialogPage tapCloseButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), closeButton);
		final int halfHeight = this.getDriver().manage().window().getSize()
				.getHeight() / 2;
		int ntry = 1;
		final int maxRetries = 3;
		do {
			closeButton.click();
			ntry++;
		} while (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(PeoplePickerPage.idParticipantsClose), 1)
				&& closeButton.getLocation().getY() < halfHeight
				&& ntry <= maxRetries);
		if (ntry > maxRetries) {
			throw new AssertionError(
					String.format(
							"The conversations details screen has not been closed after %s retries",
							maxRetries));
		}
		return new DialogPage(this.getLazyDriver());
	}

	public boolean isParticipantAvatarVisible(String name) throws Exception {
		final By locator = By.xpath(xpathParticipantAvatarByName.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}
}
