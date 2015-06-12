package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class PersonalInfoPage extends AndroidPage {

	private static final String xpathParentSelfProfileOverlay = "//*[@id='fl__conversation_list__profile_overlay']";

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(PeoplePickerPage.class
			.getSimpleName());

	private static final String idBackgroundOverlay = "v_background_dark_overlay";
	@FindBy(id = idBackgroundOverlay)
	private WebElement backgroundOverlay;

	private static final String idTakePhotoButton = "gtv__camera_control__take_a_picture";
	@FindBy(id = idTakePhotoButton)
	private WebElement takePhotoBtn;

	private static final String xpathSettingsBox = xpathParentSelfProfileOverlay
			+ "//*[@id='ll__settings_box_container']";
	@FindBy(xpath = xpathSettingsBox)
	private WebElement settingBox;

	private static final String xpathEmailField = xpathParentSelfProfileOverlay
			+ "//*[@id='ttv__profile__email']";
	@FindBy(xpath = xpathEmailField)
	private WebElement emailField;

	public static final String xpathNameField = xpathParentSelfProfileOverlay
			+ "//*[@id='ttv__profile__name']";
	@FindBy(xpath = xpathNameField)
	private WebElement nameField;

	private static final String xpathSettingsBtn = xpathParentSelfProfileOverlay
			+ "//*[@id='ttv__profile__settings_box__settings']";
	@FindBy(xpath = xpathSettingsBtn)
	private WebElement settingsButton;

	private static final String xpathNameEdit = xpathParentSelfProfileOverlay
			+ "//*[@id='tet__profile__guided']";
	@FindBy(xpath = xpathNameEdit)
	private WebElement nameEdit;

	private static final String idChangePhotoBtn = "gtv__camera_control__change_image_source";
	@FindBy(id = idChangePhotoBtn)
	private WebElement changePhotoBtn;

	@FindBy(id = idGalleryBtn)
	private WebElement galleryBtn;

	@FindBy(xpath = DialogPage.xpathConfirmOKButton)
	private WebElement confirmBtn;

	private static final String xpathProfileOptionsButton = xpathParentSelfProfileOverlay
			+ "//*[@id='gtv__profile__settings_button']";
	@FindBy(xpath = xpathProfileOptionsButton)
	private WebElement optionsButton;

	private static final String xpathAboutButton = xpathParentSelfProfileOverlay
			+ "//*[@id='ttv__profile__settings_box__about']";
	@FindBy(xpath = xpathAboutButton)
	private WebElement aboutButton;

	private static final String xpathSelfProfileClose = xpathParentSelfProfileOverlay
			+ "//*[@id='gtv__profile__close_button']";
	@FindBy(xpath = xpathSelfProfileClose)
	private WebElement selfProfileClose;

	@FindBy(id = idPager)
	private WebElement page;

	private static final String xpathSignOutBtn = xpathParentSelfProfileOverlay
			+ "//*[@id='ttv__profile__settings_box__signout']";
	@FindBy(xpath = xpathSignOutBtn)
	private WebElement signOutBtn;

	private static final String idOpenFrom = "tiles";

	@FindBy(id = idOpenFrom)
	private List<WebElement> openFrom;

	public PersonalInfoPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isPersonalInfoVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(emailField);
	}

	public void waitForEmailFieldVisible() throws Exception {
		this.getWait().until(ExpectedConditions.visibilityOf(emailField));
	}

	public void tapOnPage() throws Exception {
		DriverUtils.androidMultiTap(this.getDriver(), page, 1, 500);
	}

	public void tapChangePhotoButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				changePhotoBtn);
		changePhotoBtn.click();
	}

	public void tapTakePhotoButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), takePhotoBtn);
		takePhotoBtn.click();
	}

	public void tapGalleryButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), galleryBtn);
		galleryBtn.click();
	}

	public void tapConfirmButton() throws Exception {
		this.hideKeyboard();
		assert DriverUtils.waitUntilElementClickable(getDriver(), confirmBtn);
		confirmBtn.click();
	}

	public void tapSignOutBtn() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), signOutBtn);
		signOutBtn.click();
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		switch (direction) {
		case RIGHT: {
			return new ContactListPage(this.getLazyDriver());
		}
		default:
			return null;
		}
	}

	public void tapOptionsButton() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), optionsButton);
		try {
			optionsButton.click();
		} catch (ElementNotVisibleException e) {
			// pass silently, this throws exception due to some internal
			// Selendroid (or AUT %) ) issue
		}
	}

	public SettingsPage tapSettingsButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				settingsButton);
		settingsButton.click();
		return new SettingsPage(this.getLazyDriver());
	}

	public void waitForConfirmBtn() throws Exception {
		this.getWait().until(ExpectedConditions.visibilityOf(confirmBtn));
	}

	public void tapOnMyName() throws Exception {
		this.getWait().until(ExpectedConditions.visibilityOf(nameField));
		nameField.click();
		if (!DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(xpathNameEdit))) {
			DriverUtils.mobileTapByCoordinates(getDriver(), nameField);
		}
	}

	public boolean isNameEditVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.xpath(xpathNameEdit));
	}

	public void clearSelfName() {
		nameEdit.clear();
	}

	public void changeSelfNameTo(String newName) throws Exception {
		nameEdit.sendKeys(newName);
		this.getDriver().navigate().back();
	}

	@Override
	public ContactListPage navigateBack() throws Exception {
		super.navigateBack();
		return new ContactListPage(this.getLazyDriver());
	}

	public String getUserName() throws Exception {
		return nameField.getText();
	}

	public AboutPage tapAboutButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), aboutButton);
		aboutButton.click();
		return new AboutPage(this.getLazyDriver());
	}

	public boolean isSettingsVisible() {
		return DriverUtils.isElementPresentAndDisplayed(settingBox);
	}

	public boolean waitForSettingsDissapear() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.xpath(xpathProfileOptionsButton));
	}

	public ContactListPage pressCloseButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				selfProfileClose);
		selfProfileClose.click();
		return new ContactListPage(getLazyDriver());
	}

}
