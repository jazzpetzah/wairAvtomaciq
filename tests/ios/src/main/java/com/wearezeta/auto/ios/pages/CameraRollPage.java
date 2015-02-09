package com.wearezeta.auto.ios.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class CameraRollPage extends IOSPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCameraLibraryButton)
	private WebElement cameraLibraryButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCameraRollCancel)
	private WebElement cameraRollCancel;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathCameraLibraryFirstFolder)
	private WebElement cameraLibraryFirstFolder;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathLibraryFirstPicture)
	private WebElement libraryFirstPicture;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameConfirmPictureButton)
	private WebElement confirmPictureButton;
	
	private String url;
	private String path;
	
	public CameraRollPage(String URL, String path) throws Exception {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}
	
	
	public void pressSelectFromLibraryButton() throws InterruptedException{
		DriverUtils.waitUntilElementAppears(driver, By.name(IOSLocators.nameCameraLibraryButton));
		cameraLibraryButton.click();
	}
	
	public void selectImageFromLibrary() throws Throwable {
		try {
			clickFirstLibraryFolder();
		} catch (NoSuchElementException ex) {
			
		}

		clickFirstImage();
	}
	
	public void clickFirstLibraryFolder() throws Throwable{
		DriverUtils.waitUntilElementAppears(driver, By.xpath(IOSLocators.xpathCameraLibraryFirstFolder));
		cameraLibraryFirstFolder.click();
	}
	
	public void clickFirstImage() throws Throwable{
		DriverUtils.waitUntilElementAppears(driver, By.xpath(IOSLocators.xpathLibraryFirstPicture));
		libraryFirstPicture.click();
	}
	
	public void pressConfirmButton() throws InterruptedException{
		DriverUtils.waitUntilElementAppears(driver, By.name(IOSLocators.nameConfirmPictureButton));
		confirmPictureButton.click();
	}
	
	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		IOSPage page = null;
		switch (direction){
			case DOWN:
			{
				break;
			}
			case UP:
			{
				break;
			}
			case LEFT:
			{
				page = new OtherUserPersonalInfoPage(url, path);
				break;
			}
			case RIGHT:
			{
				page = new ContactListPage(url, path);
				break;
			}
		}	
		return page;
	}
	
	

	
}