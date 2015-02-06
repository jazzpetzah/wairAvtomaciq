package com.wearezeta.auto.osx.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class ChoosePicturePage extends OSXPage {
	private static final Logger log = ZetaLogger.getLog(ChoosePicturePage.class
			.getSimpleName());

	@FindBy(how = How.ID, using = OSXLocators.idChooseImageCancelButton)
	private WebElement cancelButton;

	@FindBy(how = How.ID, using = OSXLocators.idChooseImageOpenButton)
	private WebElement openButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathFileListScrollArea)
	private WebElement fileListArea;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathSelectColumnViewButton)
	private WebElement selectColumnViewButton;

	public ChoosePicturePage(String URL, String path) throws Exception {
		super(URL, path, false);
	}

	public Boolean isVisible() {
		boolean isVisible = (cancelButton != null);
		if (isVisible)
			log.debug("Choose picture page opened");
		return isVisible;
	}

	public void searchForImage(String imageName) {
		DriverUtils.setDefaultImplicitWait(driver);
		String xpath = String.format(OSXLocators.xpathFormatFinderImageFile,
				imageName);

		WebElement el = driver.findElement(By.xpath(xpath));
		el.click();
	}

	public boolean selectColumnView() {
		try {
			selectColumnViewButton.click();
			return true;
		} catch (NoSuchElementException e) {
			log.debug("Can't find column view selector.\n"
					+ driver.getPageSource());
			return false;
		}
	}

	public void goToSelectedFavoritesFolder(String folderName) {
		try {
			String xpath = String.format(
					OSXLocators.xpathFormatFavoritesFolderPopUp, folderName);
			WebElement folder = driver.findElement(By.xpath(xpath));
			folder.click();
		} catch (NoSuchElementException e) {
			log.debug("No " + folderName + " folder found in favorites.\n"
					+ driver.getPageSource());
		}
	}

	public boolean isOpenButtonEnabled() {
		String value = openButton.getAttribute("AXEnabled");
		if (value.equals("0")) {
			return false;
		} else if (value.equals("1")) {
			return true;
		}
		return false;
	}

	public void openImage(String filename) throws Exception {
		if (selectColumnView()) {
			goToSelectedFavoritesFolder(OSXLocators.IMAGES_SOURCE_DIRECTORY);
			searchForImage(filename);
		} else {
			String openImageScript = "tell application \"System Events\" to tell application process \"Wire\"\n"
					+ "set frontmost to true\n"
					+ "end tell\n"
					+ "delay 3\n"
					+ "tell application \"System Events\" to keystroke \""
					+ filename
					+ "\"\n"
					+ "tell application \"System Events\"\n"
					+ "key code 36\n"
					+ "end tell";
			driver.executeScript(openImageScript);
			driver.navigate()
					.to(CommonUtils
							.getOsxApplicationPathFromConfig(ChoosePicturePage.class));
		}
	}
}
