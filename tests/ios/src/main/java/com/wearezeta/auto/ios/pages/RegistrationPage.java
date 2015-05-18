package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;

public class RegistrationPage extends IOSPage {

	@FindBy(how = How.NAME, using = IOSLocators.nameRegistrationCameraButton)
	private WebElement cameraButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameCameraShootButton)
	private WebElement cameraShootButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPhotoButton)
	private WebElement photoButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameSwitchCameraButton)
	private WebElement switchCameraButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameCameraFlashButton)
	private WebElement cameraFlashButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathAlbum)
	private WebElement photoAlbum;

	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNamePhotos)
	private List<WebElement> photos;

	@FindBy(how = How.NAME, using = IOSLocators.nameConfirmImageButton)
	private WebElement confirmImageButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameCancelImageButton)
	private WebElement cancelImageButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathYourName)
	private WebElement yourName;

	@FindBy(how = How.NAME, using = IOSLocators.nameYourEmail)
	private WebElement yourEmail;

	@FindBy(how = How.NAME, using = IOSLocators.nameYourPassword)
	private WebElement yourPassword;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathRevealPasswordButton)
	private WebElement revealPasswordButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathHidePasswordButton)
	private WebElement hidePasswordButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameCreateAccountButton)
	private WebElement createAccountButton;

	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameConfirmationMessage)
	private WebElement confirmationText;

	@FindBy(how = How.ID, using = IOSLocators.idProvideValidEmailMessage)
	private WebElement provideValidEmailMessage;

	@FindBy(how = How.NAME, using = IOSLocators.nameBackToWelcomeButton)
	private WebElement backToWelcomeButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameForwardWelcomeButton)
	private WebElement forwardWelcomeButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameKeyboardNextButton)
	private WebElement keyboardNextButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathTakePhotoSmile)
	private WebElement takePhotoSmile;

	@FindBy(how = How.NAME, using = IOSLocators.nameTakePhotoHintLabel)
	private WebElement takePhotoHintLabel;

	@FindBy(how = How.NAME, using = IOSLocators.nameVignetteOverlay)
	private WebElement vignetteLayer;

	@FindBy(how = How.NAME, using = IOSLocators.nameErrorPageButton)
	private WebElement errorPageButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathCloseColorModeButton)
	private WebElement closeColorModeButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathReSendButton)
	private WebElement reSendButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathEmailVerifPrompt)
	private WebElement emailVerifPrompt;
	
	@FindBy(how = How.XPATH, using = IOSLocators.RegistrationPage.xpathPhoneNumber)
	private WebElement phoneNumber;
	
	@FindBy(how = How.XPATH, using = IOSLocators.RegistrationPage.xpathActivationCode)
	private WebElement activationCode;
	
	@FindBy(how = How.XPATH, using = IOSLocators.RegistrationPage.xpathCountry)
	private WebElement selectCountry;
	
	@FindBy(how = How.XPATH, using = IOSLocators.RegistrationPage.xpathCountryList)
	private WebElement countryList;
	
	@FindBy(how = How.XPATH, using = IOSLocators.RegistrationPage.xpathConfirmPhoneNumber)
	private WebElement confirmInput;
	
	@FindBy(how = How.NAME, using = IOSLocators.RegistrationPage.nameAgreeButton)
	private WebElement agreeButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.RegistrationPage.nameSelectPictureButton)
	private WebElement selectPictureButton;

	private String name;
	private String email;
	private String password;

	private String defaultPassFieldValue = "Password";

	private String confirmMessage = "We sent an email to %s. Check your Inbox and follow the link to verify your address. You won’t be able to use Wire until you do.\n\nDidn’t get the message?\n\nRe-send";

	private String[] listOfEmails;

	public RegistrationPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void clickAgreeButton() {
		agreeButton.click();
	}
	
	private void selectCountryByCode(String code) throws Exception {
		selectCountry.click();
		boolean result = false; int count = 0;
		while (!result && count < 10) {
			WebElement el = null;
			boolean flag = false;
			count ++;
			try {
				el = getDriver().findElementByName(code);	
			} catch (NoSuchElementException ex) {
				flag = true;
			}
			if (!el.isDisplayed() || flag) {
				List<WebElement> elementsList = countryList.findElements(By.className("UIATableCell"));
				WebElement last = elementsList.get(elementsList.size() - 1);
				DriverUtils.scrollToElement(getDriver(), last);
				continue;
			}
			el.click();
			result = true;
		}
	}
	
	public void inputPhoneNumber(String number, String code) throws Exception {
		selectCountryByCode(code);
		phoneNumber.sendKeys(number);
		confirmInput.click();
	}
	
	public void inputActivationCode(String code) throws Exception {
		getWait().until(ExpectedConditions.elementToBeClickable(activationCode));
		activationCode.sendKeys(code);
		confirmInput.click();
	}
   
	public boolean isTakePhotoSmileDisplayed() {
		return takePhotoSmile.isEnabled();
	}

	public boolean isTakeOrSelectPhotoLabelVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.nameTakePhotoHintLabel));
	}

	public boolean isNameLabelVisible() {
		return yourName.isDisplayed();
	}

	public void clickCameraButton() {
		cameraButton.click();
	}

	public void clickCameraShootButton() {
		cameraShootButton.click();
	}

	public void takePhotoByFrontCamera() {
		clickCameraShootButton();
	}

	public void clickSwitchCameraButton() {
		switchCameraButton.click();
	}

	public void switchToFrontCamera() {
		clickSwitchCameraButton();
	}

	public void takePhotoByRearCamera() {
		switchToRearCamera();
		clickCameraShootButton();
	}

	public void switchToRearCamera() {
		clickSwitchCameraButton();
	}

	public CameraRollPage selectPicture() throws Exception {
		selectPictureButton.click();
		photoButton.click();
		return new CameraRollPage(this.getLazyDriver());
	}

	public void chooseFirstPhoto() {
		photoAlbum.click();
		photos.get(0).click();
	}

	public void clickVignetteLayer() {
		vignetteLayer.click();
	}

	public void dismissVignetteBakground() throws Exception {
		vignetteLayer.click();
		this.getDriver().tap(1, vignetteLayer.getLocation().x + 10,
				vignetteLayer.getLocation().y + 10, 1);
	}

	public boolean isVignetteOverlayVisible() {
		return vignetteLayer.isDisplayed();
	}

	public void tapCloseColorModeButton() {
		closeColorModeButton.click();
	}

	public boolean isColorModeVisible() {
		return closeColorModeButton.isDisplayed();
	}

	public PeoplePickerPage waitForConfirmationMessage() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.className(IOSLocators.classNameConfirmationMessage));

		return new PeoplePickerPage(this.getLazyDriver());
	}

	public boolean isConfirmationShown() {
		String expectedMessage = null;
		String actualMessage = null;
		expectedMessage = String.format(confirmMessage, getEmail());
		actualMessage = confirmationText.getText();
		return actualMessage.equals(expectedMessage);
	}

	public void confirmPicture() {
		confirmImageButton.click();
	}

	public void cancelImageSelection() {
		cancelImageButton.click();
	}

	public void hideKeyboard() throws Exception {
		this.getDriver().hideKeyboard();
	}

	public void inputName() {
		confirmInput.click();
	}

	public void inputEmail() {
		yourEmail.sendKeys("\n");
	}

	public void scriptInputEmail(String val) throws Exception {
		String script = String.format(
				IOSLocators.scriptRegistrationEmailInputPath
						+ ".setValue(\"%s\");", val);
		this.getDriver().executeScript(script);
	}

	public void scriptInputAndConfirmEmail(String val) throws Exception {
		String script = String.format(
				IOSLocators.scriptRegistrationEmailInputPath
						+ ".setValue(\"%s\");"
						+ IOSLocators.scriptKeyboardReturnKeyPath + ".tap();",
				val);
		this.getDriver().executeScript(script);
	}

	public void clearEmailInput() throws Exception {
		scriptInputEmail("");
	}

	public void inputPassword() {
		yourPassword.sendKeys("\n");
	}

	public void clickCreateAccountButton() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(IOSLocators.nameCreateAccountButton));
		createAccountButton.click();
	}

	public void createAccount() {
		try {
			if (ExpectedConditions.presenceOfElementLocated(By
					.xpath(IOSLocators.xpathYourName)) != null) {
				yourName.sendKeys(getName() + "\n");
			}
		} catch (NoSuchElementException e) {
		}
		if (ExpectedConditions.presenceOfElementLocated(By
				.name(IOSLocators.nameYourEmail)) != null) {
			yourEmail.sendKeys(getEmail() + "\n");
		}
		if (ExpectedConditions.presenceOfElementLocated(By
				.name(IOSLocators.nameYourPassword)) != null) {
			yourPassword.sendKeys(getPassword());
		}
		createAccountButton.click();
	}

	public void typeEmail() {
		yourEmail.sendKeys(getEmail());
	}

	public void retypeEmail() {
		if (ExpectedConditions.presenceOfElementLocated(By
				.name(IOSLocators.nameYourEmail)) != null) {
			yourEmail.sendKeys(getEmail());
		}
	}

	public void returnToConfirmRegistration() {
		forwardWelcomeButton.click();
		createAccountButton.click();
	}

	public boolean typeAllInvalidEmails() {

		for (int i = 0; i < listOfEmails.length; i++) {
			yourEmail.sendKeys(listOfEmails[i] + "\n");
			if (!provideValidEmailMessage.isDisplayed()) {
				return false;
			}
			yourEmail.clear();
		}
		return true;
	}

	public void typeInRegistrationData() {
		yourName.sendKeys(getName() + "\n");
		yourEmail.sendKeys(getEmail() + "\n");
		yourPassword.sendKeys(getPassword());
	}

	public boolean isCreateAccountEnabled() {
		return createAccountButton.isEnabled();
	}

	public void verifyUserInputIsPresent(String name, String email)
	// this test skips photo verification
	{

		PagesCollection.loginPage.clickJoinButton();
		forwardWelcomeButton.click(); // skip photo
		Assert.assertEquals("Name is not same as previously entered.", name,
				yourName.getText());
		forwardWelcomeButton.click();
		Assert.assertEquals("Email is not same as previously entered.", email,
				yourEmail.getText());
		forwardWelcomeButton.click();
		Assert.assertEquals("Preciously entered email shouln't be shown",
				defaultPassFieldValue, yourPassword.getText());
	}

	public void navigateToCreateAccount() {
		forwardWelcomeButton.click();
	}

	public void typeUsername() throws Exception {
		this.getWait().until(ExpectedConditions.elementToBeClickable(yourName));
		yourName.sendKeys(getName());
	}

	public String getUsernameFieldValue() {
		return yourName.getText();
	}

	public String getEmailFieldValue() {
		return yourEmail.getText();
	}

	public boolean isPictureSelected() {
		return confirmImageButton.isDisplayed();
	}

	public boolean isConfirmationVisible() {
		return confirmationText.isDisplayed();
	}

	public boolean confirmErrorPage() {
		return errorPageButton.isDisplayed();
	}

	public void backToEmailPageFromErrorPage() {
		backToWelcomeButton.click();
		backToWelcomeButton.click();
	}

	public void navigateToWelcomePage() {
		while (backToWelcomeButton.isDisplayed()) {
			backToWelcomeButton.click();
		}
	}

	public boolean isNextButtonPresented() {
		return forwardWelcomeButton.isDisplayed();
	}

	public void tapBackButton() {
		backToWelcomeButton.click();
	}

	public Boolean isBackButtonVisible() {

		return (ExpectedConditions.visibilityOf(backToWelcomeButton) != null);
	}

	public void tapForwardButton() {
		forwardWelcomeButton.click();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws Exception {
		this.name = name;
		typeUsername();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		typeEmail();
		this.email = email.replace('\n', ' ').trim();
	}

	public String getPassword() {
		return password;
	}

	private void typePassword() {
		yourPassword.sendKeys(getPassword());
	}

	public void setPassword(String password) {
		this.password = password;
		typePassword();

	}

	public String[] getListOfEmails() {
		return listOfEmails;
	}

	public void setListOfEmails(String[] list) {
		this.listOfEmails = list;
	}

	public void reSendEmail() throws Exception {
		Point p = reSendButton.getLocation();
		Dimension k = reSendButton.getSize();
		this.getDriver().tap(1, (p.x) + (k.width / 2), (p.y) + (k.height - 5),
				1);
	}

	public void waitUntilEmailsCountReachesExpectedValue(int expectedMsgsCount,
			String recipient, int timeoutSeconds) throws Exception {
		IMAPSMailbox mailbox = IMAPSMailbox.getInstance();
		mailbox.waitUntilMessagesCountReaches(recipient, expectedMsgsCount,
				timeoutSeconds);
	}

	public boolean isEmailVerifPromptVisible() {
		return emailVerifPrompt.isDisplayed();
	}

}
