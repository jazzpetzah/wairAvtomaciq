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
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;
import com.wearezeta.auto.common.log.ZetaLogger;

public class OtherUserPersonalInfoPage extends AndroidPage {

	private static final Logger log = ZetaLogger
			.getLog(OtherUserPersonalInfoPage.class.getSimpleName());

	public static final String REMOVE_FROM_CONVERSATION_BUTTON = "Remove";
	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.75;
	public static final String LEAVE_CONVERSATION_BUTTON = "Leave conversation";
	public static final String LEAVE_BUTTON = "LEAVE";
	private static final String AVATAR_WITH_IMAGE = "avatarPictureTestAndroid.png";
	private static final String AVATAR_NO_IMAGE = "avatarTestAndroid.png";

	@FindBy(how = How.XPATH, using = AndroidLocators.OtherUserPersonalInfoPage.xpathGroupChatInfoLinearLayout)
	private List<WebElement> linearLayout;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.OtherUserPersonalInfoPage.classNameGridView)
	private WebElement groupChatUsersGrid;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idParticipantsHeader")
	private WebElement groupChatName;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idParticipantsHeaderEditable")
	private WebElement groupChatNameEditable;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idParticipantsHeader")
	private List<WebElement> otherUserName;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idParticipantsSubHeader")
	private List<WebElement> otherUserMail;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idOtherUserPersonalInfoSingleName")
	private List<WebElement> otherUserSingleName;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idOtherUserPersonalInfoSingleMail")
	private List<WebElement> otherUserSingleMail;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idUserProfileConfirmationMenu")
	private WebElement confirmMenu;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idBlockButton")
	private WebElement blockButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idRightActionButton")
	private WebElement rightConversationButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idRenameButton")
	private WebElement renameButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idArchiveButton")
	private WebElement archiveButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idLeaveButton")
	private WebElement leaveButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idSilenceButton")
	private WebElement silenceButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idUnblockBtn")
	private WebElement unblockButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idParticipantsClose")
	private WebElement closeButton;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameFrameLayout)
	private WebElement frameLayout;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameLoginPage)
	private WebElement backGround;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idConfirmBtn")
	private WebElement confirmBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idLeftActionButton")
	private WebElement addContactBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idLeftActionLabel")
	private WebElement addContactLabel;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idParticipantsSubHeader")
	private WebElement participantsSubHeader;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectToHeader")
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
		leaveButton.click();
		return new ContactListPage(this.getLazyDriver());
	}

	public void pressSilenceButton() throws Exception {
		silenceButton.click();
	}

	public void clickBlockBtn() throws Exception {
		blockButton.click();
	}

	public AndroidPage clickUnblockBtn() throws Exception {
		unblockButton.click();
		return new DialogPage(this.getLazyDriver());
	}

	public boolean isUnblockBtnVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(unblockButton);
	}

	public boolean isOneToOneOptionsMenuFullyVisible() throws Exception {
		return (DriverUtils.isElementPresentAndDisplayed(blockButton)
				&& DriverUtils.isElementPresentAndDisplayed(silenceButton) && DriverUtils
					.isElementPresentAndDisplayed(archiveButton));
	}

	private String[] getOptionItemsIdLocators() {
		return new String[] {
				AndroidLocators.OtherUserPersonalInfoPage.idBlockButton,
				AndroidLocators.OtherUserPersonalInfoPage.idSilenceButton,
				AndroidLocators.OtherUserPersonalInfoPage.idArchiveButton };
	}

	public boolean isOneToOneOptionsMenuUIContentNotVisible() throws Exception {
		for (String locator : this.getOptionItemsIdLocators()) {
			if (!DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
					By.id(locator), 15)) {
				return false;
			}
		}
		return true;
	}

	public boolean isOneToOneOptionsMenuUIContentVisible() throws Exception {
		for (String locator : this.getOptionItemsIdLocators()) {
			if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
					By.id(locator))) {
				return true;
			}
		}
		return false;
	}

	private String[] getUserProfileIdLocators() {
		return new String[] {
				AndroidLocators.PeoplePickerPage.idParticipantsClose,
				AndroidLocators.OtherUserPersonalInfoPage.idRightActionButton,
				AndroidLocators.OtherUserPersonalInfoPage.idLeftActionButton,
				AndroidLocators.OtherUserPersonalInfoPage.idLeftActionLabel,
				AndroidLocators.OtherUserPersonalInfoPage.idParticipantsSubHeader,
				AndroidLocators.OtherUserPersonalInfoPage.idParticipantsHeader };
	}

	public boolean isOneToOneUserProfileUIContentNotVisible() throws Exception {
		for (String locator : this.getUserProfileIdLocators()) {
			if (!DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
					By.id(locator), 15)) {
				return false;
			}
		}
		return true;
	}

	public boolean isOneToOneUserProfileUIContentVisible() throws Exception {
		for (String locator : this.getUserProfileIdLocators()) {
			if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
					By.id(locator))) {
				return true;
			}
		}
		return false;
	}

	public boolean isOneToOneUserProfileFullyVisible() throws Exception {
		// FIXME: this might be slow. Need to replace
		// isElementPresentAndDisplayed -> waitUntilLocatorDissapears
		for (WebElement elem : new WebElement[] { closeButton,
				rightConversationButton, addContactBtn, addContactLabel,
				participantsSubHeader, groupChatName }) {
			if (!DriverUtils.isElementPresentAndDisplayed(elem)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		AndroidPage page = null;
		switch (direction) {
		case DOWN: {
			page = new PeoplePickerPage(this.getLazyDriver());
			break;
		}
		case UP: {
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

	public boolean isOtherUserNameVisible(String name) throws Exception {
		String text;
		if (otherUserName.size() > 0) {
			text = otherUserName.get(0).getText().toLowerCase();
		} else {
			try {
				text = otherUserSingleName.get(0).getText().toLowerCase();
			} catch (Exception ex) {
				text = "NONE";
			}
		}
		return text.equals(name.toLowerCase());
	}

	public boolean isOtherUserMailVisible(String mail) throws Exception {
		String text;
		if (otherUserName.size() > 0) {
			text = otherUserMail.get(0).getText().toLowerCase();
		} else {
			try {
				text = otherUserSingleMail.get(0).getText().toLowerCase();
			} catch (Exception ex) {
				text = "NONE";
			}
		}
		return text.equals(mail.toLowerCase());
	}

	public boolean isRemoveFromConversationAlertVisible() {
		return confirmMenu.isDisplayed();
	}

	public OtherUserPersonalInfoPage pressRemoveConfirmBtn() throws Exception {
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
		BufferedImage bgImage = null;
		boolean flag = false;
		bgImage = getElementScreenshot(backGround);
		String path = CommonUtils.getImagesPath(CommonUtils.class);
		BufferedImage realImage = ImageUtil.readImageFromFile(path + imageName);
		double score = ImageUtil.getOverlapScore(realImage, bgImage);
		System.out.println(score);
		if (score >= MIN_ACCEPTABLE_IMAGE_VALUE) {
			flag = true;
		}
		return flag;
	}

	public boolean isContactExists(String contact) throws Exception {
		boolean flag = true;
		this.getWait().until(ExpectedConditions.visibilityOf(groupChatName));
		List<WebElement> cn = this.getDriver().findElements(
				By.xpath(String.format(
						AndroidLocators.ContactListPage.xpathContacts,
						contact.toUpperCase())));
		if (cn.isEmpty()) {
			flag = false;
		}
		return flag;
	}

	public AndroidPage selectContactByName(String contactName)
			throws Exception, InterruptedException {
		boolean flag = false;

		for (WebElement user : linearLayout) {
			List<WebElement> elements = user
					.findElements(By
							.className(AndroidLocators.CommonLocators.classNameTextView));
			for (WebElement element : elements) {
				if (element.getText() != null
						&& element.getText()
								.equals((contactName.toUpperCase()))) {
					user.click();
					flag = true;
					break;
				}
			}
			if (flag) {
				break;
			}
		}
		if (connectToHeader.size() > 0) {
			return new ConnectToPage(this.getLazyDriver());
		} else {
			return new OtherUserPersonalInfoPage(this.getLazyDriver());
		}
	}

	public void tapOnParticipantsHeader() {
		groupChatName.click();
	}

	public void renameGroupChat(String chatName) {
		groupChatNameEditable.sendKeys(chatName + "\n");
	}

	public AndroidPage tapOnContact(String contact) throws Exception {
		this.getWait().until(ExpectedConditions.visibilityOf(groupChatName));
		try {
			WebElement cn = this.getDriver().findElement(
					By.xpath(String.format(
							AndroidLocators.ContactListPage.xpathContacts,
							contact.toUpperCase())));

			cn.click();
		} catch (Exception e) {
			log.debug("Failed to find contact with name " + contact
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
		return participantsSubHeader.getText().toLowerCase();
	}

	public String getConversationName() throws Exception {
		return groupChatName.getText();
	}

	public DialogPage tabBackButton() throws Exception {
		closeButton.click();
		return new DialogPage(this.getLazyDriver());
	}

	public boolean isParticipantAvatars(String contact1, String contact2)
			throws Exception {
		boolean flag1 = false;
		boolean flag2 = false;
		BufferedImage avatarIcon = null;
		String path = CommonUtils.getImagesPath(CommonUtils.class);
		for (int i = 1; i < linearLayout.size() + 1; i++) {
			avatarIcon = getElementScreenshot(this
					.getDriver()
					.findElement(
							By.xpath(String
									.format(AndroidLocators.OtherUserPersonalInfoPage.xpathGroupChatInfoLinearLayoutId,
											i))));
			String avatarName = this
					.getDriver()
					.findElement(
							By.xpath(String
									.format(AndroidLocators.OtherUserPersonalInfoPage.xpathGroupChatInfoContacts,
											i))).getText();
			if (avatarName.equalsIgnoreCase(contact1)) {
				BufferedImage realImage = ImageUtil.readImageFromFile(path
						+ AVATAR_WITH_IMAGE);
				double score = ImageUtil.getOverlapScore(realImage, avatarIcon,
						ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
				if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
					return false;
				}
				flag1 = true;
			}
			if (contact2.toLowerCase().startsWith(avatarName.toLowerCase())) {
				// must be a yellow user with initials AT
				BufferedImage realImage = ImageUtil.readImageFromFile(path
						+ AVATAR_NO_IMAGE);
				double score = ImageUtil.getOverlapScore(realImage, avatarIcon,
						ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
				if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
					return false;
				}
				flag2 = true;
			}
		}
		return (flag1 && flag2);
	}

}
