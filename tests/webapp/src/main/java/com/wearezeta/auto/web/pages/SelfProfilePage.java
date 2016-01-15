package com.wearezeta.auto.web.pages;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.Browser;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class SelfProfilePage extends WebPage {
	@FindBy(how = How.CSS, using = WebAppLocators.SelfProfilePage.cssGearButton)
	private WebElement gearButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathCameraButton)
	private WebElement cameraButton;

	@FindBy(how = How.CSS, using = WebAppLocators.SelfProfilePage.cssSelectPicture)
	private WebElement selectPictureInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathSelfUserName)
	private WebElement userName;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathSelfUserNameInput)
	private WebElement userNameInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathNameSelfUserMail)
	private WebElement userMail;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathNameSelfUserPhoneNumber)
	private WebElement userPhoneNumber;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathAccentColorPickerChildren)
	private List<WebElement> colorsInColorPicker;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathBackgroundAvatarAccentColor)
	private WebElement backgroundAvatarAccentColor;

	public SelfProfilePage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isSettingsButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.cssSelector(WebAppLocators.SelfProfilePage.cssGearButton));
	}

	public void clickGearButton() throws Exception {
		// Wait until no modal is shown
		DriverUtils.waitUntilLocatorDissapears(getDriver(), By.className("modal-show"));
		DriverUtils.waitUntilElementClickable(this.getDriver(), gearButton);
		gearButton.click();
	}

	public void selectGearMenuItem(String name) throws Exception {
		final String menuXPath = WebAppLocators.SelfProfilePage.xpathGearMenuRoot;
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(menuXPath));
		final String menuItemXPath = WebAppLocators.SelfProfilePage.xpathGearMenuItemByName
				.apply(name);
		final WebElement itemElement = getDriver().findElement(
				By.xpath(menuItemXPath));
		itemElement.click();
	}

	public boolean checkNameInSelfProfile(String name) throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(WebAppLocators.SelfProfilePage.xpathSelfUserName));

		WebDriverWait wait = new WebDriverWait(this.getDriver(), 10);

		return wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				if (userName.getText().equals(name)) {
					return true;
				} else {
					return false;
				}
			}
		});
	}

	public String getUserName() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(WebAppLocators.SelfProfilePage.xpathSelfUserName));
		return userName.getText();
	}

	public String getUserMail() {
		return userMail.getText();
	}

	public String getUserPhoneNumber() {
		return userPhoneNumber.getText();
	}

	public void setUserName(String name) {
		userName.click();
		userNameInput.clear();
		userNameInput.sendKeys(name + "\n");
	}

	public void selectAccentColor(String colorName) throws Exception {
		final int id = AccentColor.getByName(colorName).getId();
		final String xpathAccentColorDiv = WebAppLocators.SelfProfilePage.xpathAccentColorDivById
				.apply(id);
		assert DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(xpathAccentColorDiv));
		final WebElement accentColorDiv = this.getDriver().findElementByXPath(
				xpathAccentColorDiv);
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				accentColorDiv);
		accentColorDiv.click();
	}

	public String getCurrentAccentColor() throws Exception {
		final WebElement accentColorCircleDiv = this
				.getDriver()
				.findElementByXPath(
						WebAppLocators.SelfProfilePage.xpathCurrentAccentColorCircleDiv);
		return accentColorCircleDiv.getCssValue("border-top-color");
	}

	public int getCurrentAccentColorId() {
		int i = 1;
		for (WebElement childDiv : colorsInColorPicker) {
			if (childDiv.getAttribute("class").toLowerCase()
					.contains("selected")) {
				return i;
			}
			i++;
		}
		throw new RuntimeException(
				"No accent color is selected in color picker");
	}

	public void clickCameraButton() throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(), cameraButton);
		cameraButton.click();
	}

	public AccentColor getCurrentAvatarAccentColor() throws Exception {
		final WebElement backgroundAvatarAccentColor = this
				.getDriver()
				.findElementByXPath(
						WebAppLocators.SelfProfilePage.xpathBackgroundAvatarAccentColor);
		return AccentColor.getByRgba(backgroundAvatarAccentColor
				.getCssValue("background-color"));
	}

	public void dropPicture(String pictureName) throws Exception {
		final String srcPicturePath = WebCommonUtils
				.getFullPicturePath(pictureName);
		assert new File(srcPicturePath).exists() : srcPicturePath
				+ " file should exist on hub file system";

		/*
		 * The code below allows to upload the picture to the remote mode
		 * without Selenium interaction This could be useful when we have better
		 * solution for drag and drop
		 */
		// final String dstPicturePathForScp = WebAppConstants.TMP_ROOT + "/"
		// + pictureName;
		// WebCommonUtils.putFileOnExecutionNode(this.getDriver().getNodeIp(),
		// srcPicturePath, dstPicturePathForScp);
		//
		// String dstPicturePath = null;
		// if (WebAppExecutionContext.isCurrentPlatfromWindows()) {
		// dstPicturePath = WebAppConstants.WINDOWS_TMP_ROOT + "\\"
		// + pictureName;§
		// } else {
		// dstPicturePath = dstPicturePathForScp;
		// }

		// http://stackoverflow.com/questions/5188240/using-selenium-to-imitate-dragging-a-file-onto-an-upload-element
		final String inputId = "SelfImageUpload";
		this.getDriver().executeScript(
				inputId + " = window.$('<input id=\"" + inputId
						+ "\"/>').attr({type:'file'}).appendTo('body');");
		// The file is expected to be uploaded automatically by Webdriver
		getDriver().findElement(By.id(inputId)).sendKeys(srcPicturePath);
		this.getDriver().executeScript(
				"e = $.Event('drop'); e.originalEvent = {dataTransfer : { files : "
						+ inputId + ".get(0).files } }; $(\""
						+ WebAppLocators.ProfilePicturePage.cssDropZone
						+ "\").trigger(e);");
	}

	public void uploadPicture(String pictureName) throws Exception {
		final String picturePath = WebCommonUtils
				.getFullPicturePath(pictureName);
		if (WebAppExecutionContext.getBrowser() == Browser.Firefox) {
			this.getDriver()
					.executeScript(
							"$(\""
									+ WebAppLocators.SelfPictureUploadPage.cssChooseYourOwnInput
									+ "\").css({'left': '0', 'opacity': '100', 'z-index': '100'});");
		}
		if (WebAppExecutionContext.getBrowser() == Browser.Safari) {
			WebCommonUtils.sendPictureInSafari(picturePath, this.getDriver()
					.getNodeIp());
		} else {
			selectPictureInput.sendKeys(picturePath);
			// manually trigger change event on input
			this.getDriver().executeScript("e = $.Event('change');$(\""
						+ WebAppLocators.SelfPictureUploadPage.cssChooseYourOwnInput
						+ "\").trigger(e);");
		}
	}
}
