package com.wearezeta.auto.android.pages;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.SwipeDirection;

public class ConnectToPage extends AndroidPage {

	@FindBy(how = How.ID, using = AndroidLocators.idConnectToHeader)
	private WebElement connectToHeader;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConnectRequestAccept)
	private WebElement connectAcceptBtn;

	@FindBy(how = How.ID, using = AndroidLocators.idConnectRequestIgnore)
	private WebElement connectIgnoreBtn;
	
	
	
	private String url;
	private String path;
	
	public ConnectToPage(String URL, String path) throws Exception {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getConnectToHeader() {
		refreshUITree();
	    wait.until(ExpectedConditions.visibilityOf(connectToHeader));
		return connectToHeader.getText().toLowerCase();
	}

	public DialogPage pressConnectButton() throws Exception {
		connectAcceptBtn.click();
		return new DialogPage(url, path);
	}

	public ContactListPage pressIgnorButton() throws Exception {
		connectIgnoreBtn.click();
		return new ContactListPage(url, path);
	}

}
