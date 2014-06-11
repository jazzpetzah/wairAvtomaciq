package com.wearezeta.auto.ios.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.IOSLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class OtherUserPersonalInfoPage extends IOSPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.nameLoginPage)
	private WebElement viewPager;
	
	public OtherUserPersonalInfoPage(String URL, String path) throws IOException {
		super(URL, path);
		

	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
