package com.wearezeta.auto.android.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;

public class RegistrationPage extends AndroidPage {

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idDialogTakePhotoButton")
	private WebElement cameraButton;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idConfirmButton")
	private WebElement confirmImageButton;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idNameField")
	private WebElement nameField;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idEmailField")
	private WebElement emailField;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idNewPasswordField")
	private WebElement passwordField;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idCreateUserBtn")
	private WebElement createUserBtn;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idVerifyEmailBtn")
	private WebElement verifyEmailBtn;
	
	private String url;
	private String path;
	
	public RegistrationPage(String URL, String path) throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void takePhoto() {
		cameraButton.click();
		
	}

	public void selectPicture() {
		// TODO Auto-generated method stub
		
	}

	public void chooseFirstPhoto() {
		// TODO Auto-generated method stub
		
	}

	public boolean isPictureSelected() {
		
		return DriverUtils.isElementDisplayed(confirmImageButton);
	}

	public void confirmPicture() {
		confirmImageButton.click();
	}

	public void setName(String name) {
		DriverUtils.setTextForChildByClassName(nameField, "android.widget.EditText", name);
		
	}

	public void setEmail(String email) {
		DriverUtils.setTextForChildByClassName(emailField, "android.widget.EditText", email);
		
	}

	public void setPassword(String password) {
		DriverUtils.setTextForChildByClassName(passwordField, "android.widget.EditText", password);
		
	}

	public void createAccount() {
		createUserBtn.click();
		
	}

	public boolean isConfirmationVisible() {
		return DriverUtils.isElementDisplayed(verifyEmailBtn);
	}

	public InstructionsPage continueRegistration() throws Exception {
		verifyEmailBtn.click();
		return new InstructionsPage(url, path);
	}

}
