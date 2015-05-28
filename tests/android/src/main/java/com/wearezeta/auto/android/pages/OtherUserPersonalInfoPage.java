package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
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

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idParticipantsHeader)
	private WebElement groupChatName;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idParticipantsHeaderEditable)
	private WebElement groupChatNameEditable;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idUserProfileConfirmationMenu)
	private WebElement confirmMenu;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idBlockButton)
	private WebElement blockButton;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idRightActionButton)
	private WebElement rightConversationButton;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idRenameButton)
	private WebElement renameButton;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idArchiveButton)
	private WebElement archiveButton;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idLeaveButton)
	private WebElement leaveButton;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idSilenceButton)
	private WebElement silenceButton;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idUnblockBtn)
	private WebElement unblockButton;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idParticipantsClose)
	private WebElement closeButton;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameFrameLayout)
	private WebElement frameLayout;

	@FindBy(id = AndroidLocators.CommonLocators.idPager)
	private WebElement backGround;

	@FindBy(id = AndroidLocators.CommonLocators.idConfirmBtn)
	private WebElement confirmBtn;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idLeftActionButton)
	private WebElement addContactBtn;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idLeftActionLabel)
	private WebElement addContactLabel;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idParticipantsSubHeader)
	private WebElement participantsSubHeader;

	@FindBy(id = AndroidLocators.ConnectToPage.idConnectToHeader)
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
		return new By[] {
				By.id(AndroidLocators.OtherUserPersonalInfoPage.idBlockButton),
				By.id(AndroidLocators.OtherUserPersonalInfoPage.idSilenceButton),
				By.id(AndroidLocators.OtherUserPersonalInfoPage.idArchiveButton) };
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
		return new By[] {
				By.id(AndroidLocators.PeoplePickerPage.idParticipantsClose),
				By.id(AndroidLocators.OtherUserPersonalInfoPage.idRightActionButton),
				By.id(AndroidLocators.OtherUserPersonalInfoPage.idLeftActionButton),
				By.id(AndroidLocators.OtherUserPersonalInfoPage.idLeftActionLabel),
				By.id(AndroidLocators.OtherUserPersonalInfoPage.idParticipantsSubHeader),
				By.id(AndroidLocators.OtherUserPersonalInfoPage.idParticipantsHeader) };
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
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.xpath(AndroidLocators.OtherUserPersonalInfoPage.xpathPartcipantNameByText
								.apply(expectedName)), 1)
				|| DriverUtils
						.waitUntilLocatorIsDisplayed(
								getDriver(),
								By.xpath(AndroidLocators.OtherUserPersonalInfoPage.xpathSingleParticipantNameByText
										.apply(expectedName)), 1);
	}

	public boolean isOtherUserMailVisible(String expectedEmail)
			throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.xpath(AndroidLocators.OtherUserPersonalInfoPage.xpathPartcipantEmailByText
								.apply(expectedEmail)), 1)
				|| DriverUtils
						.waitUntilLocatorIsDisplayed(
								getDriver(),
								By.xpath(AndroidLocators.OtherUserPersonalInfoPage.xpathSingleParticipantEmailByText
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
		final By locator = By
				.xpath(AndroidLocators.OtherUserPersonalInfoPage.xpathParticipantAvatarByName
						.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void tapOnParticipantsHeader() {
		groupChatName.click();
	}

	public void renameGroupChat(String chatName) {
		groupChatNameEditable.sendKeys(chatName + "\n");
	}

	public AndroidPage tapOnParticipant(String name) throws Exception {
		this.getWait().until(ExpectedConditions.visibilityOf(groupChatName));
		try {
			final By nameLocator = By
					.xpath(AndroidLocators.OtherUserPersonalInfoPage.xpathParticipantAvatarByName
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
		closeButton.click();
		return new DialogPage(this.getLazyDriver());
	}

	public boolean isParticipantAvatarVisible(String name) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.OtherUserPersonalInfoPage.xpathParticipantAvatarByName
						.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}
}
