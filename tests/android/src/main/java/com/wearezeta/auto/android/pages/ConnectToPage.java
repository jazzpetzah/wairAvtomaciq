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
}
