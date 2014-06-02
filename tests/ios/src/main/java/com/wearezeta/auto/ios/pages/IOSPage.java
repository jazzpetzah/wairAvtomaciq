package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.IOSLocators;
import com.wearezeta.auto.common.SwipeDirection;

public abstract class IOSPage extends BasePage {
	
	@FindBy(how = How.NAME, using = IOSLocators.nameLoginPage)
	private WebElement content;
	
	public IOSPage(String URL, String path) throws MalformedURLException {
		
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("app", path);
        super.InitConnection(URL, capabilities);
	}

	@Override
	public void Close() throws IOException {
		super.Close();
	}
	
	public abstract IOSPage returnBySwipe (SwipeDirection direction) throws IOException;
	
	public IOSPage swipeLeft(int time) throws IOException
	{
		DriverUtils.swipeLeft(driver, content, time);
		return returnBySwipe(SwipeDirection.LEFT);
	}
	
	public IOSPage swipeRight(int time) throws IOException
	{
		DriverUtils.swipeRight(driver, content, time);
		return returnBySwipe(SwipeDirection.RIGHT);
	}
	
	public IOSPage swipeUp(int time) throws IOException
	{
		DriverUtils.swipeUp(driver, content, time);
		return returnBySwipe(SwipeDirection.UP);
	}
}
