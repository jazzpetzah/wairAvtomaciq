package com.wearezeta.auto.ios.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.IOSLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class OtherUserPersonalInfoPage extends IOSPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.nameProfileOtherUserNameField)
	private WebElement otherUserName;
	
	@FindBy(how = How.NAME, using = IOSLocators.classNameProfileOtherUserEmailField)
	private WebElement otherUserEmail;
	
	private String url;
	private String path;
	
	public OtherUserPersonalInfoPage(String URL, String path) throws IOException {
		super(URL, path);
		this.url = URL;
		this.path = path;

	}
	
	public String getOtherUserProfileName(){
		return otherUserName.getText();
	}
	
	public String getOtherUserProfileEmail(){
		return otherUserEmail.getText();
	}
	
	public boolean isOtherUserNameVisible(String name){
		return getOtherUserProfileName().equals(name);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		IOSPage page = null;
		switch (direction){
		case DOWN:
		{
			page = new PeoplePickerPage(url, path);
			break;
		}
		case UP:
		{
			break;
		}
		case LEFT:
		{
			page = new DialogPage(url, path);
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
