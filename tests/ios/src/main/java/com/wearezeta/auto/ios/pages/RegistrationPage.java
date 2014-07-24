package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.ArrayList;

import javax.mail.MessagingException;

import org.apache.xalan.xsltc.runtime.MessageHandler;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.CreateZetaUser;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.EmailHeaders;
import com.wearezeta.auto.common.IMAPMailbox;
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
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathProvideValidEmailMessage)
	private WebElement provideValidEmailMessage;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameBackToWelcomeButton)
	private WebElement backToWelcomeButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameForwardWelcomeButton)
	private WebElement ForwardWelcomeButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathReSendButton)
	private WebElement reSendButton;
	
	private String name;
	private String email;
	private String password;
	
	private String usernameTextFieldValue;
	private String emailTextFieldValue;
	private String passwordTextFieldValue;

	private String[] listOfEmails;
	private String recipientInboxCount;
	
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
		driver.tap(1, revealPasswordButton.getLocation().x + 1, revealPasswordButton.getLocation().y + 1, 1);
		passwordTextFieldValue = yourPassword.getText();
	}
	
	public void navigateToWelcomePage()
	{
		while(backToWelcomeButton.isDisplayed()){
			backToWelcomeButton.click();
		}
	}
	
	public boolean verifyUserInputIsPresent()
	//this test skips photo verification
	{
		
		PagesCollection.loginPage.clickJoinButton();
		ForwardWelcomeButton.click(); //skip photo
		if(!yourName.getText().equals(usernameTextFieldValue)){
			return false;
		}
		ForwardWelcomeButton.click();
		if(!yourEmail.getText().equals(emailTextFieldValue)){
			return false;
		}
		ForwardWelcomeButton.click();
		if(!yourPassword.getText().equals(passwordTextFieldValue)){
			driver.tap(1, revealPasswordButton.getLocation().x + 1, revealPasswordButton.getLocation().y + 1, 1);
			if(!yourPassword.getText().equals(passwordTextFieldValue)){
			return false;
			}
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
		return confirmationText.isDisplayed();
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
	
	public void reSendEmail() {
		driver.tap(1, (reSendButton.getLocation().x)
				+ (reSendButton.getSize().width / 2),
				(reSendButton.getLocation().y)
						+ (reSendButton.getSize().height - 5), 1);
	}
	
	public boolean emailInboxCount() throws IOException {
	  List<EmailHeaders> messageHeaders = CreateZetaUser.getLastMailHeaders(CommonUtils.getDefaultEmailFromConfig(CreateZetaUser.class),CommonUtils.getDefaultPasswordFromConfig(CreateZetaUser.class),0);
      String inboxCount = messageHeaders.get(messageHeaders.size()).getLastUserEmail();
    if (inboxCount.equals(null)){
    	return false;
    }
    else {
    	System.out.println(inboxCount);
    	return true;
    }
    	  
    }
	
	 public int getRecentEmailsCountForRecipient(int allRecentEmailsCnt, String expectedRecipient) throws MessagingException, IOException {
	     IMAPMailbox mailbox = new IMAPMailbox(CreateZetaUser.MAIL_SERVER, CreateZetaUser.MAILS_FOLDER,
	    		 CommonUtils.getDefaultEmailFromConfig(RegistrationPage.class), 
	    		 CommonUtils.getDefaultPasswordFromConfig(RegistrationPage.class));
		 int actualCnt = 0;
		 try {
			 mailbox.open();
			 List<EmailHeaders> allEmailsHeaders = mailbox.getLastMailHeaders(allRecentEmailsCnt);
			 for (EmailHeaders emailHeaders: allEmailsHeaders) {
				 if (emailHeaders.getLastUserEmail().equals(expectedRecipient)) {
					 actualCnt++; 
				 }
			 }
		 } finally {
			 mailbox.close();
		 }
		 return actualCnt;
	 }
	
}
 