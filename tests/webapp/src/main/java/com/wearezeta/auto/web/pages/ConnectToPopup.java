package com.wearezeta.auto.web.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ConnectToPopup extends WebPage{

	private String url;
	private String path;
	
	public ConnectToPopup(String URL, String path) throws Exception {
		super(URL, path);
		
		this.url = URL;
		this.path = path;		
	}
	
	@FindBy(how = How.ID, using = WebAppLocators.ConnectToPopup.idConnectionPopupWindow)
	private WebElement connectPopupWindow;
	
	@FindBy(how = How.XPATH, using = WebAppLocators.ConnectToPopup.xpathUserName)
	private WebElement userName;
	
	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.ConnectToPopup.classNameConnectionMessage)
	private WebElement connectionText;
	
	@FindBy(how = How.XPATH, using = WebAppLocators.ConnectToPopup.xpathConnectButton)
	private WebElement connectButton;
	
	
	public boolean isConntectPopupVisible() {
		return DriverUtils.isElementDisplayed(connectPopupWindow);
	}
	
	public String getUserName() {
		return userName.getText();
	}
	
	public void clickConnectButton() {
		connectButton.click();
	}

}
