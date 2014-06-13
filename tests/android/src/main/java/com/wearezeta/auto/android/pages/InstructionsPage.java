package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.AndroidLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class InstructionsPage extends AndroidPage {

	@FindBy(how = How.ID, using = AndroidLocators.idInstructions)
	private WebElement instructions;
	@FindBy(how = How.ID, using = AndroidLocators.idConnectRequestDialog)
	private WebElement connectDilog;
	@FindBy(how = How.ID, using = AndroidLocators.idInstructionsRequestIgnoreBtn)
	private List<WebElement> ignoreBtnList;
	@FindBy(how = How.ID, using = AndroidLocators.idInstructionsRequestConnectBtn)
	private List<WebElement> connectBtnList;
	
	private String url;
	private String path;
	
	public InstructionsPage(String URL, String path) throws IOException {
		super(URL, path);
		url = URL;
		this.path = path;				
	}

	public void waitConnectRequestDialog()
	{
		wait.until(ExpectedConditions.visibilityOf(connectDilog));
	}
	
	public void acceptAllConnections()
	{
		for(WebElement connectBtn : connectBtnList){
			connectBtn.click();
		}
	}
	public void ignoreAllConnections()
	{
		for(WebElement ignoreBtn : connectBtnList){
			ignoreBtn.click();
		}
	}
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws IOException {
		
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
