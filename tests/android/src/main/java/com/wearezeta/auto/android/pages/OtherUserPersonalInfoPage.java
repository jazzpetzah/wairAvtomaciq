package com.wearezeta.auto.android.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.AndroidLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class OtherUserPersonalInfoPage extends AndroidPage {

	@FindBy(how = How.ID, using = AndroidLocators.idOtherUserPersonalInfoName)
	private WebElement otherUserName;
	
	
	private String url;
	private String path;
	
	public OtherUserPersonalInfoPage(String URL, String path)
			throws IOException {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		AndroidPage page = null;
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
				break;
			}
			case RIGHT:
			{
				break;
			}
		}	
		return page;
	}

	public boolean isOtherUserNameVisible(String name) {
		wait.until(ExpectedConditions.visibilityOf(otherUserName));
		return otherUserName.getText().equals(name);
	}
	

}
