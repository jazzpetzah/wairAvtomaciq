package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class PersonalInfoPage extends AndroidPage
{

	private String url;
	private String path;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idBackgroundOverlay")
	private WebElement backgroundOverlay;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idSettingsBox")
	private WebElement settingBox;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idEmailField")
	private WebElement emailField;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idNameField")
	private WebElement nameField;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idSettingsBtn")
	private WebElement settingsButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idNameEdit")
	private WebElement nameEdit;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idChangePhotoBtn")
	private WebElement changePhotoBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idGalleryBtn")
	private WebElement galleryBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConfirmButton")
	private WebElement confirmBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idProfileOptionsButton")
	private WebElement optionsButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idAboutButton")
	private WebElement aboutButton;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameLoginPage)
	private WebElement page;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idSignOutBtn")
	private WebElement signOutBtn;

	@FindBy(how = How.ID, using = AndroidLocators.PersonalInfoPage.idOpenFrom)
	private List<WebElement> openFrom;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idProfileOptionsButton")
	private List<WebElement> settingsButtonList;

	public PersonalInfoPage(String URL, String path) throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;

	}

	public boolean isPersonalInfoVisible() {
		refreshUITree();
		return emailField.isDisplayed();
	}

	public void waitForEmailFieldVisible(){

		wait.until(ExpectedConditions.visibilityOf(emailField));
	}

	public void clickOnPage() throws InterruptedException{
		DriverUtils.androidMultiTap(driver, page,1,0.2);
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

		refreshUITree();
		signOutBtn.click();
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {

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
			page = new ContactListPage(url,path);
			break;
		}
		}	
		return page;
	}

	public void tapOptionsButton() throws InterruptedException {
		refreshUITree();
		optionsButton.click();
	}

	public SettingsPage tapSettingsButton() throws Exception {

		refreshUITree();
		settingsButton.click();
		return new SettingsPage (url, path);
	}

	public void waitForConfirmBtn(){
		wait.until(ExpectedConditions.visibilityOf(confirmBtn));
	}

	public void tapOnMyName() {
		wait.until(ExpectedConditions.visibilityOf(nameField));
		nameField.click();
	}

	public void changeName(String name, String newName) throws Exception {
		DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.PersonalInfoPage.idNameField));
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOf(nameEdit));
		nameEdit.sendKeys(newName);
		driver.navigate().back();
		Thread.sleep(1000);
	}
	
	@Override
	public ContactListPage navigateBack() throws Exception{
		driver.navigate().back();
		return new ContactListPage(url, path);
	}
	public String getUserName() {
		refreshUITree();
		return nameField.getText();
	}

	public AboutPage tapAboutButton() throws Exception {
		refreshUITree();
		aboutButton.click();
		return new AboutPage(url, path);
	}

	public boolean isSettingsVisible() {

		return settingBox.isDisplayed();
	}

	public boolean isSettingsButtonNotVisible() {
		boolean flag = false;
		refreshUITree();
		DriverUtils.waitUntilElementDissapear(driver, AndroidLocators.PersonalInfoPage.getByForProfileOptionsButton());
		if(settingsButtonList == null || settingsButtonList.isEmpty())
		{
			flag = true;
		}
		return flag;
	}
	
	public boolean waitForSettingsDissapear() {

		return DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.PersonalInfoPage.idProfileOptionsButton));
	}

}
