package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.AndroidLocators;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.SwipeDirection;

public class PersonalInfoPage extends AndroidPage
{
	@FindBy(how = How.ID, using = AndroidLocators.idEmailField)
	private WebElement emailField;
	
	@FindBy(how = How.ID, using = AndroidLocators.idNameField)
	private WebElement nameField;

	@FindBy(how = How.ID, using = AndroidLocators.idChangePhotoBtn)
	private WebElement changePhotoBtn;

	@FindBy(how = How.ID, using = AndroidLocators.idGalleryBtn)
	private WebElement galleryBtn;

	@FindBy(how = How.ID, using = AndroidLocators.idConfirmButton)
	private WebElement confirmBtn;

	@FindBy(how = How.XPATH, using = AndroidLocators.xpathOptionsButton)
	private WebElement optionsButton;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameLoginPage)
	private WebElement page;

	@FindBy(how = How.XPATH, using = AndroidLocators.xpathImagesFrameLayout)
	private List<WebElement> frameLayouts;

	@FindBy(how = How.ID, using = AndroidLocators.idSignOutBtn)
	private WebElement signOutBtn;



	public PersonalInfoPage(String URL, String path) throws IOException {
		super(URL, path);

	}

	public void waitForEmailFieldVisible(){

		wait.until(ExpectedConditions.visibilityOf(emailField));
	}

	public void clickOnPage() throws InterruptedException{
		DriverUtils.androidMultiTap(driver, page,1,0.2);
	}

	public void selectPhoto(){
		frameLayouts.get(0).click();
	}

	public void tapChangePhotoButton(){
		changePhotoBtn.click();
	}

	public void tapGalleryButton(){
		galleryBtn.click();
	}

	public void tapConfirmButton() throws IOException{
		confirmBtn.click();
	}

	public void tapSignOutBtn(){

		signOutBtn.click();
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) {

		AndroidPage page = null;
		switch (direction){
		case DOWN:
		{
			break;
		}
		case UP:
		{
			page = this;
			break;
		}
		case LEFT:
		{
			break;
		}
		case RIGHT:
		{
			break;
		}
		}	
		return page;
	}

	public void tapOptionsButton() throws InterruptedException {
		optionsButton.click();
	}
	
	public void waitForConfirmBtn(){
		wait.until(ExpectedConditions.visibilityOf(confirmBtn));
	}

	public void tapOnMyName() {
		nameField.click();
	}

}
