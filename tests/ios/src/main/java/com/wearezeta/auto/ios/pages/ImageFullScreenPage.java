package com.wearezeta.auto.ios.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;


import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class ImageFullScreenPage extends IOSPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.nameImageFullScreenPage)
	private WebElement imageFullScreen;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameFullScreenCloseButton)
	private WebElement fullScreenCloseButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameFullScreenDownloadButton)
	private WebElement fullScreenDownloadButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameFullScreenSenderName)
	private WebElement fullScreenSenderName;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameFullScreenTimeStamp)
	private WebElement fullScreenTimeStamp;
	
	private String url;
	private String path;
	
	public ImageFullScreenPage(String URL, String path) throws Exception {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}
	
	public boolean isImageFullScreenShown(){
		return imageFullScreen.isDisplayed();
	}
	
	public DialogPage clickCloseButton() throws Exception{
		DialogPage page = null;
		fullScreenCloseButton.click();
		page = new DialogPage(url, path);
		return page;
	}
	
	public boolean isDownloadButtonVisible(){
		return fullScreenDownloadButton.isDisplayed();
	}
	
	public void clickDownloadButton(){
		fullScreenDownloadButton.click();
	}
	
	public ImageFullScreenPage tapOnFullScreenPage(){
		imageFullScreen.click();
		return this;
	}
	
	public boolean isSenderNameVisible(){
		return fullScreenSenderName.isDisplayed();
	}
	
	public String getSenderName(){
		return fullScreenSenderName.getText();
	}
	
	public boolean isSentTimeVisible(){
		return fullScreenTimeStamp.isDisplayed();
	}
	
	public String getTimeStamp(){
		return fullScreenTimeStamp.getText();
	}
	
	
	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		IOSPage page = null;
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
				break;
			}
			case RIGHT:
			{
				break;
			}
		}	
		return page;
	}

}
