package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import android.view.KeyEvent;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class RegistrationPage extends AndroidPage {

	@FindBy(xpath = AndroidLocators.Chrome.xpathChrome)
	private WebElement chromeBrowser;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.Chrome.CLASS_NAME, locatorKey = "idUrlBar")
	private WebElement urlBar;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idDialogTakePhotoButton")
	private WebElement cameraButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConfirmButton")
	private WebElement confirmImageButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idNameEdit")
	protected WebElement nameField;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idEmailField")
	private WebElement emailField;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.RegistrationPage.CLASS_NAME, locatorKey = "idNewPasswordField")
	private WebElement passwordField;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.RegistrationPage.CLASS_NAME, locatorKey = "idCreateUserBtn")
	private WebElement createUserBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.RegistrationPage.CLASS_NAME, locatorKey = "idVerifyEmailBtn")
	private WebElement verifyEmailBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.RegistrationPage.CLASS_NAME, locatorKey = "idNextArrow")
	protected WebElement nextArrow;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idConfirmCancelButton")
	private WebElement laterBtn;

	private static final String YOUR_NAME = "your full name";
	private static final String EMAIL = "email";

	public RegistrationPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void takePhoto() {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(cameraButton));
		cameraButton.click();
	}

	public void selectPicture() {
		// TODO Auto-generated method stub
	}

	public void chooseFirstPhoto() {
		// TODO Auto-generated method stub
	}

	public boolean isPictureSelected() throws Exception {
		refreshUITree();
		DriverUtils.waitUntilElementAppears(driver,
				AndroidLocators.DialogPage.getByForDialogConfirmImageButtn());
		return DriverUtils.isElementDisplayed(confirmImageButton);
	}

	public void confirmPicture() {
		refreshUITree();
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(confirmImageButton));
		confirmImageButton.click();
	}

	public void setName(String name) {
		refreshUITree();
		this.getWait()
				.until(ExpectedConditions.elementToBeClickable(nameField));
		// TABLET fix
		if (nameField.getText().toLowerCase().contains(YOUR_NAME)) {
			nameField.sendKeys(name);
			refreshUITree();
			this.getWait().until(
					ExpectedConditions.elementToBeClickable(nextArrow));
			nextArrow.click();
		}
	}

	public void setEmail(String email) {
		refreshUITree();
		// TABLET fix
		if (nameField.getText().toLowerCase().contains(EMAIL)) {
			nameField.sendKeys(email);
			refreshUITree();
			this.getWait().until(
					ExpectedConditions.elementToBeClickable(nextArrow));
			nextArrow.click();
		}
	}

	public void setPassword(String password) {
		refreshUITree();
		passwordField.sendKeys(password);
	}

	public void createAccount() {
		createUserBtn.click();

	}

	public boolean isConfirmationVisible() {
		refreshUITree();
		this.getWait().until(ExpectedConditions.visibilityOf(verifyEmailBtn));
		return DriverUtils.isElementDisplayed(verifyEmailBtn);
	}

	public ContactListPage continueRegistration() throws Exception {
		try {
			this.getWait().until(ExpectedConditions.visibilityOf(laterBtn));
		} catch (NoSuchElementException e) {

		} catch (TimeoutException e) {

		}
		return new ContactListPage(this.getDriver(), this.getWait());
	}

	public PeoplePickerPage activateByLink(String link) throws Exception {
		refreshUITree();
		this.getWait().until(ExpectedConditions.visibilityOf(chromeBrowser));
		chromeBrowser.click();
		DriverUtils.waitUntilElementDissapear(driver,
				By.xpath(AndroidLocators.Chrome.xpathChrome));
		refreshUITree();
		if (CommonUtils.getAndroidApiLvl(RegistrationPage.class) < 43) {
			int ln = urlBar.getText().length();
			urlBar.click();
			for (int i = 0; i < ln; i++) {
				this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_DEL);
			}
		} else {
			urlBar.clear();
		}
		urlBar.sendKeys(link);
		this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_ENTER);
		String text = urlBar.getText();
		while (!text.contains("wire://email-verified")) {
			Thread.sleep(500);
			text = urlBar.getText();
		}
		return new PeoplePickerPage(this.getDriver(), this.getWait());
	}

}
