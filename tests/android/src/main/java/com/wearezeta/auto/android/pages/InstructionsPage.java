package com.wearezeta.auto.android.pages;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;
//TODO: remove this page
public class InstructionsPage extends AndroidPage {

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idInstructions")
	private WebElement instructions;
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idConnectRequestDialog")
	private WebElement connectDilog;
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idInstructionsRequestIgnoreBtn")
	private List<WebElement> ignoreBtnList;
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idInstructionsRequestConnectBtn")
	private List<WebElement> connectBtnList;
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idConnectRequestConnectTo")
	private WebElement requestHeader;
	
	private String url;
	private String path;

	public InstructionsPage(String URL, String path) throws Exception {
		super(URL, path);
		url = URL;
		this.path = path;				
	}

	public boolean isConnectDialogDispalayed()
	{
		wait.until(ExpectedConditions.visibilityOf(connectDilog));
		return connectDilog.isDisplayed();
	}

	public void acceptAllConnections()
	{
		for(WebElement connectBtn : connectBtnList){
			connectBtn.click();
		}
	}

	public String getConnectionRequestHeader()
	{
		return requestHeader.getText();
	}
	public void waitInstructionsPage()
	{
		wait.until(ExpectedConditions.visibilityOf(instructions));
	}
	
	public void ignoreAllConnections()
	{
		for(WebElement ignoreBtn : connectBtnList){
			ignoreBtn.click();
		}
	}
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {

		AndroidPage page = null;
		switch (direction){
		case DOWN:
		{
			break;
		}
		case UP:
		{
			break;
		}
		case LEFT:
		{
			page = new PersonalInfoPage(url, path);
			break;
		}
		case RIGHT:
		{
			page = new ContactListPage(url, path);
			break;
		}
		}	
		return page;
	}

}
