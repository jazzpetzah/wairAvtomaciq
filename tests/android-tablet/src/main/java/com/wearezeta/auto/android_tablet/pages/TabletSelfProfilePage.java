package com.wearezeta.auto.android_tablet.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletSelfProfilePage extends AndroidTabletPage {
	public static final String idSelfNameInput = "tet__profile__guided";
	@FindBy(id = idSelfNameInput)
	private WebElement selfNameInput;

	public static final String idSelfProfileView = "ll_self_form";
	@FindBy(id = idSelfProfileView)
	private WebElement selfProfileView;

	public static final String idChangePictureButton = "gtv__camera_control__change_image_source";
	@FindBy(id = idChangePictureButton)
	private WebElement changePictureButton;

	public static final String idGalleryButton = "gtv__camera_control__pick_from_gallery";
	@FindBy(id = idGalleryButton)
	private WebElement galleryButton;

	public static final String xpathConfirmPictureButton = "//*[@id='ttv__confirmation__confirm' and @value='OK']";
	@FindBy(xpath = xpathConfirmPictureButton)
	private WebElement confirmPictureButton;

	public static final Function<String, String> xpathSelfNameByContent = content -> String
			.format("//*[@id='ttv__profile__name' and @value='%s']", content);

	public static final Function<String, String> xpathOptionsMenuItemByName = name -> String
			.format("//*[@id='ll__settings_box_container']//*[@value='%s']",
					name.toUpperCase());

	public static final String idOptionsButton = "gtv__profile__settings_button";
	@FindBy(id = idOptionsButton)
	private WebElement optionsButton;

	public TabletSelfProfilePage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isNameVisible(String name) throws Exception {
		final By locator = By.xpath(xpathSelfNameByContent.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void tapOptionsButton() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), optionsButton);
		optionsButton.click();
	}

	public void selectOptionsMenuItem(String itemName) throws Exception {
		final By locator = By.xpath(xpathOptionsMenuItemByName.apply(itemName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("The item '%s' is not present in Options menu",
						itemName);
		getDriver().findElement(locator).click();
	}

	public void tapSelfNameField() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), selfNameInput);
		selfNameInput.click();
	}

	public void changeSelfNameTo(String newName) throws Exception {
		selfNameInput.clear();
		selfNameInput.sendKeys(newName);
		this.hideKeyboard();
	}

	public BufferedImage getScreenshot() throws Exception {
		return this.getElementScreenshot(selfProfileView).orElseThrow(
				IllegalStateException::new);
	}

	public void tapChangePictureButton() {
		changePictureButton.click();
	}

	public void tapGalleryButton() {
		galleryButton.click();
	}

	public void tapConfirmPictureButton() {
		confirmPictureButton.click();
	}

	public void tapInTheCenter() throws Exception {
		DriverUtils.tapInTheCenterOfTheElement(this.getDriver(),
				selfProfileView);
	}
}
