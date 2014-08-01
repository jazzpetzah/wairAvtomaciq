package com.wearezeta.auto.osx.pages;

import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class ChoosePicturePage extends OSXPage {

	@FindBy(how = How.ID, using = OSXLocators.idChooseImageCancelButton)
	private WebElement cancelButton;

	@FindBy(how = How.ID, using = OSXLocators.idChooseImageOpenButton)
	private WebElement openButton;
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathFileListScrollArea)
	private WebElement fileListArea;
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathSelectColumnViewButton)
	private WebElement selectColumnViewButton;
	
	public ChoosePicturePage(String URL, String path) throws MalformedURLException {
		super(URL, path);
	}

	public Boolean isVisible() {
		return cancelButton != null;
	}
	
	public void searchForImage(String imageName) {
		DriverUtils.setDefaultImplicitWait(driver);
		String xpath = String.format(OSXLocators.xpathFormatFinderImageFile, imageName);

		WebElement el = driver.findElement(By.xpath(xpath));
		el.click();
	}
	
	public void selectColumnView() {
		selectColumnViewButton.click();
	}
	
	public void goToSelectedFavoritesFolder(String folderName) {
		String xpath = String.format(OSXLocators.xpathFormatFavoritesFolderPopUp, folderName);
		WebElement folder = driver.findElement(By.xpath(xpath));
		folder.click();
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
	
	public void openImage(String filename) {
		selectColumnView();
		goToSelectedFavoritesFolder(OSXLocators.IMAGES_SOURCE_DIRECTORY);
		searchForImage(filename);
	}
}
