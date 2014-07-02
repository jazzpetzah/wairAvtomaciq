package com.wearezeta.auto.android.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.AndroidLocators;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.IOSLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class RegistrationPage extends AndroidPage {

	@FindBy(how = How.ID, using = AndroidLocators.idDialogTakePhotoButton)
	private WebElement cameraButton;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConfirmButton)
	private WebElement confirmImageButton;
	
	@FindBy(how = How.ID, using = AndroidLocators.idNameField)
	private WebElement nameField;
	
	@FindBy(how = How.ID, using = AndroidLocators.idEmailField)
	private WebElement emailField;
	
	@FindBy(how = How.ID, using = AndroidLocators.idNewPasswordField)
	private WebElement passwordField;
	
	@FindBy(how = How.ID, using = AndroidLocators.idCreateUserBtn)
	private WebElement createUserBtn;
	
	@FindBy(how = How.ID, using = AndroidLocators.idVerifyEmailBtn)
	private WebElement verifyEmailBtn;
	
	private String url;
	private String path;
	
	public RegistrationPage(String URL, String path) throws IOException {
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

	public InstructionsPage continueRegistration() throws IOException {
		verifyEmailBtn.click();
		return new InstructionsPage(url, path);
	}

}
