package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.IOSLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class RegistrationPage extends IOSPage {
	
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCameraButton)
	private WebElement cameraButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePhotoButton)
	private WebElement photoButton;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameAlert)
	private WebElement alert;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameAlertOK)
	private WebElement alertOk;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameAlbum)
	private WebElement photoAlbum;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNamePhotos)
	private List<WebElement> photos;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameConfirmImageButton)
	private WebElement confirmImageButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathYourName)
	private WebElement yourName;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameYourEmail)
	private WebElement yourEmail;
	
	//@FindBy(how = How.XPATH, using = IOSLocators.xpathYourSecurePassword)
	//private WebElement yourSecurePassword;
	
	//@FindBy(how = How.XPATH, using = IOSLocators.xpathYourVisiblePassword)
	//private WebElement yourVisiblePassword;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameYourPassword)
	private WebElement yourPassword;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathRevealPasswordButton)
	private WebElement revealPasswordButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathHidePasswordButton)
	private WebElement hidePasswordButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCreateAccountButton)
	private WebElement createAccountButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameContinueButton)
	private WebElement continueButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathProvideValidEmailMessage)
	private WebElement provideValidEmailMessage;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameBackToWelcomeButton)
	private WebElement backToWelcomeButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameForwardWelcomeButton)
	private WebElement ForwardWelcomeButton;
	
	private String name;
	private String email;
	private String password;
	
	private String usernameTextFieldValue;
	private String emailTextFieldValue;
	private String passwordTextFieldValue;

	private String[] listOfEmails;
	
	public RegistrationPage(String URL, String path)
			throws MalformedURLException {
		super(URL, path);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void takePhoto()
	{
		cameraButton.click();//TODO implement on real device
	}
	
	public void selectPicture()
	{
		photoButton.click();
		
	}
	
	public void chooseFirstPhoto()
	{
		catchLoginAlert();
		
		photoAlbum.click();
		photos.get(0).click();
	}
	
	
	public void createAccount()
	{
		yourName.sendKeys(getName() + "\n");
		yourEmail.sendKeys(getEmail() + "\n");
		yourPassword.sendKeys(getPassword());
		
		createAccountButton.click();
	}
	
	public void typeEmail()
	{
		yourName.sendKeys(getName() + "\n");
		yourEmail.sendKeys(getEmail());
	}
	
	public boolean typeAllInvalidEmails()
	{
		yourName.sendKeys(getName() + "\n");
		for(int i=0; i<listOfEmails.length;i++){
			yourEmail.sendKeys(listOfEmails[i]+"\n");
			if(!provideValidEmailMessage.isDisplayed()){
			return false;
			}

		}
		return true; //returns true if all emails are found to be invalid
	}
	
	public void typePassword()
	{
		yourName.sendKeys(getName() + "\n");
		yourEmail.sendKeys(getEmail() + "\n");
		yourPassword.sendKeys(getPassword());
	}
	
	public void typeAndStoreAllValues()
	{
		yourName.sendKeys(getName());
		usernameTextFieldValue = yourName.getText();
		yourName.sendKeys(getName()+"\n");
		yourEmail.sendKeys(getEmail());
		emailTextFieldValue = yourEmail.getText();
		yourEmail.sendKeys(getEmail()+"\n");
		yourPassword.sendKeys(getPassword());
		//revealPasswordButton.click();
		passwordTextFieldValue = "testpassword"; //yourVisiblePassword.getText();
		
		System.out.println("test\n");
		System.out.println("username:"+usernameTextFieldValue+"\nemail:"+emailTextFieldValue+"\npassword:"+passwordTextFieldValue);
	}
	
	public void navigateToWelcomePage()
	{
		while(backToWelcomeButton.isDisplayed()){
			backToWelcomeButton.click();
		}
	//Assert.assertNotNull(PagesCollection.loginPage.isVisible());
	}
	
	public boolean verifyUserInputIsPresent()
	{
		
		PagesCollection.loginPage.clickJoinButton();
		ForwardWelcomeButton.click(); //skip photo verification
		if(!yourName.getText().equals(usernameTextFieldValue)){
			System.out.println("username verification failed\n");
			return false;
		}
		System.out.println("username verified\n");
		ForwardWelcomeButton.click();
		if(!yourEmail.getText().equals(emailTextFieldValue)){
			System.out.println("email verification failed\n");
			return false;
		}
		System.out.println("email verified\n");
		ForwardWelcomeButton.click();
		if("testpassword"!=passwordTextFieldValue){ //if(!yourPassword.getText().equals(passwordTextFieldValue)){
			System.out.println("password verification failed\n");
			return false;
		}
		System.out.println("password verified\n");
		return true;
	}
	
	public String getEmailFieldValue()
	{
		return yourEmail.getText();		
	}
	
	public boolean isPictureSelected()
	{
		return confirmImageButton.isDisplayed();
	}
	
	public boolean isConfirmationVisible()
	{
		return continueButton.isDisplayed();
	}
	
	public void continueRegistration()
	{
		continueButton.click();
	}
	
	public void confirmPicture()
	{
		confirmImageButton.click();
	}
	
	public void catchLoginAlert() {
		try {
			DriverUtils.waitUntilElementAppears(driver, By.className(IOSLocators.classNameAlert));
			if(alert != null) {
				alertOk.click();
			}
		}
		catch(Exception ex) {
		}
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String[] getListOfEmails() {
		return listOfEmails;
	}
	
	public void setListOfEmails(String[] list){
		this.listOfEmails = list;
	}
}
