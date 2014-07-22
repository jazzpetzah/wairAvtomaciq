package com.wearezeta.auto.ios.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.*;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class CameraRollPage extends IOSPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCameraRollButton)
	private WebElement cameraRollButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathCameraRollAlertOK)
	private WebElement cameraRollAlertOK;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCameraRollCancel)
	private WebElement cameraRollCancel;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathCameraRollTableCell)
	private WebElement cameraRollTableCell;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathCameraRollPicture)
	private WebElement cameraRollPicture;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameConfirmPictureButton)
	private WebElement confirmPictureButton;
	
	private String url;
	private String path;
	
	public CameraRollPage(String URL, String path) throws IOException {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}
	
	
	public void pressCameraRollButton() throws InterruptedException{
		cameraRollButton.click();
		Thread.sleep(1000);
	}
	
	public void openCameraRoll() throws IOException, InterruptedException{
		
		System.out.print("CAMERA ROLL");
		cameraRollAlertOK.click();
		cameraRollTableCell.click();
		cameraRollPicture.click();

	}
	
	public void pressConfirmButton() throws InterruptedException{
		confirmPictureButton.click();
		Thread.sleep(2000);
	}
	
	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
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