package com.wearezeta.auto.android.pages;


import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;

public class ConnectToPage extends AndroidPage {

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectToHeader")
	private WebElement connectToHeader;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectRequestAccept")
	private WebElement connectAcceptBtn;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectRequestIgnore")
	private WebElement connectIgnoreBtn;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idPaticipantsPendingLabel")
	private List<WebElement> pendingText;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idConnectionRequiesMessage")
	private WebElement connectionRequestMessage;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idSendConnectionRequestButton")
	private WebElement sendConnectionRequestButton;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectToCharCounter")
	private WebElement connectCharCounter;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idParticipantsClose")
	private WebElement closeButton;	
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idRightActionButton")
	private WebElement blockButton; 
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idConfirmBtn")
	private WebElement confirmBtn;
	
	private String url;
	private String path;
	
	public ConnectToPage(String URL, String path) throws Exception {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		return null;
	}
	
	public void clickBlockBtn() {
		blockButton.click();
	}
	
	public void pressConfirmBtn() throws Exception {
		refreshUITree();
		wait.until(ExpectedConditions.elementToBeClickable(confirmBtn));
		confirmBtn.click();
	}

	public String getConnectToHeader() {
		refreshUITree();
	    wait.until(ExpectedConditions.visibilityOf(connectToHeader));
		return connectToHeader.getText().toLowerCase();
	}

	public DialogPage pressAcceptConnectButton() throws Exception {
		connectAcceptBtn.click();
		return new DialogPage(url, path);
	}

	public ContactListPage pressIgnoreButton() throws Exception {
		refreshUITree();
		connectIgnoreBtn.click();
		return new ContactListPage(url, path);
	}
	
	public boolean isIgnoreConnectButtonVisible() throws Exception {

		return connectIgnoreBtn.isDisplayed() && connectAcceptBtn.isDisplayed();
	}

	public boolean isPending() {
		return pendingText.size() > 0;
	}
	

	public void tapEditConnectionRequies() {
		refreshUITree();
		connectionRequestMessage.click();
		int length = connectionRequestMessage.getText().length();
		for(int i = 0; i < length; i++) {
			driver.sendKeyEvent(67);
		}
	}
	
	public void typeConnectionRequies(String message) {
		connectionRequestMessage.sendKeys(message);
		
	}
	
	public ContactListPage pressConnectButton() throws Exception {
		sendConnectionRequestButton.click();
		return new ContactListPage(url, path);
	}
	
	public boolean getConnectButtonState() {
		String state =  sendConnectionRequestButton.getAttribute("enabled");
		return Boolean.parseBoolean(state);
	}
	
	public int getCharCounterValue() {
		return Integer.parseInt(connectCharCounter.getText());
	}

	public PeoplePickerPage clickCloseButton() throws Exception {
		closeButton.click();
		return new PeoplePickerPage(url,path);
	}
	
}
