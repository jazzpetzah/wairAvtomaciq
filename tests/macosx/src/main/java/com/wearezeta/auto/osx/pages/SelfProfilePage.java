package com.wearezeta.auto.osx.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.common.ChoosePicturePage;
import com.wearezeta.auto.osx.util.AccentColorPicker;
import com.wearezeta.auto.osx.util.AccentColorUtil;

public class SelfProfilePage extends MainWirePage {

	private static final Logger log = ZetaLogger.getLog(SelfProfilePage.class
			.getSimpleName());

	@FindBy(how = How.XPATH, using = OSXLocators.xpathOpenUserPictureSettingsButton)
	private WebElement userPictureButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPictureFromImageFile)
	private WebElement choosePictureFromImageFileButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPictureFromCamera)
	private WebElement choosePictureFromCameraButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathDoCameraShotButton)
	private WebElement doCameraShotButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPictureConfirmationButton)
	private WebElement confirmPictureChoiceButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPictureSettingsCloseButton)
	private WebElement pictureSettingsCloseButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathRemoveUserPictureCheckBox)
	private WebElement removeUserPictureCheckBox;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathRemoveUserPictureConfirmation)
	private WebElement removeUserPictureConfirmationButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathRemoveUserPictureCancel)
	private WebElement removeUserPictureCancelButton;

	@FindBy(how = How.ID, using = OSXLocators.idSelfProfileEmailTextField)
	private WebElement selfProfileEmailTextField;

	private AccentColorPicker accentColorPicker;

	public SelfProfilePage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void openPictureSettings() throws Exception {
		Actions builder = new Actions(this.getDriver());
		builder.moveToElement(window).build().perform();
		userPictureButton.click();
		DriverUtils.waitUntilElementAppears(this.getDriver(),
				By.xpath(OSXLocators.xpathPictureFromImageFile));
	}

	public ChoosePicturePage openChooseImageFileDialog() throws Exception {
		choosePictureFromImageFileButton.click();
		return new ChoosePicturePage(this.getLazyDriver());
	}

	public void openCameraDialog() {
		choosePictureFromCameraButton.click();
	}

	public void doPhotoInCamera() {
		doCameraShotButton.click();
	}

	public void confirmPictureChoice() throws Exception {
		Thread.sleep(3000);
		confirmPictureChoiceButton.click();
		try {
			pictureSettingsCloseButton.click();
		} catch (NoSuchElementException e) {
		}
		DriverUtils.waitUntilElementDissapear(this.getDriver(),
				By.xpath(OSXLocators.xpathPictureFromImageFile));
	}

	public void chooseToRemovePhoto() {
		removeUserPictureCheckBox.click();
	}

	public void confirmPhotoRemoving() throws Exception {
		removeUserPictureConfirmationButton.click();
		try {
			pictureSettingsCloseButton.click();
		} catch (NoSuchElementException e) {
		}
		DriverUtils.waitUntilElementDissapear(this.getDriver(),
				By.xpath(OSXLocators.xpathPictureFromImageFile));
	}

	public void cancelPhotoRemoving() {
		removeUserPictureCancelButton.click();
	}

	public boolean selfProfileEmailEquals(String email) {
		return email.equals(selfProfileEmailTextField.getText());
	}

	public boolean selfProfileNameEquals(String name) throws Exception {
		String xpath = String.format(
				OSXLocators.xpathFormatSelfProfileNameTextField, name);
		log.debug("Looking for name " + name + " by xpath '" + xpath
				+ "' in user profile.");
		return DriverUtils.waitUntilElementAppears(this.getDriver(),
				By.xpath(xpath), 60);
	}

	public void changeUserName(String oldName, String newName) throws Exception {
		String xpath = String.format(
				OSXLocators.xpathFormatSelfProfileNameTextField, oldName);
		WebElement selfName = getDriver().findElement(By.xpath(xpath));
		selfName.click();
		selfName.clear();
		selfName.sendKeys(newName + "\\n");
	}

	public void changeAccentColor(AccentColor color) throws Exception {
		accentColorPicker = findAccentColorPicker();
		accentColorPicker.changeAccentColor(color);
	}

	public AccentColorPicker findAccentColorPicker() throws Exception {
		return AccentColorPicker.findColorPickerInWindow(this.getDriver(),
				this.window);
	}

	public AccentColor findSelectedAccentColor() throws Exception {
		return AccentColorPicker.findSelectedAccentColor(this.getDriver(),
				this.window);
	}

	public AccentColor findBackgroundSelfProfileColor() throws Exception {
		BufferedImage selfProfileScreen = OSXCommonUtils.takeElementScreenshot(
				window, this.getDriver());
		return AccentColorUtil
				.calculateAccentColorForBackground(selfProfileScreen);
	}
}
