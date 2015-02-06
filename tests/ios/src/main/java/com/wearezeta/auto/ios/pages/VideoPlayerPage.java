package com.wearezeta.auto.ios.pages;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class VideoPlayerPage extends IOSPage{
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathVideoMainPage)
	private WebElement videoPlayerMainWindow;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameVideoDoneButton)
	private WebElement videoDoneButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameVideoSlider)
	private WebElement videoSlider;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameVideoFullScreenButton)
	private WebElement videoFullScreenButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameVideoPreviousButton)
	private WebElement videoPreviousButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameVideoPauseButton)
	private WebElement videoPauseButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameVideoNextButton)
	private WebElement videoNextButton;
	
	private String url;
	private String path;
	
	public VideoPlayerPage(String URL, String path) throws Exception {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}
	
	public void waitForVideoPlayerPage(){
		DriverUtils.waitUntilElementAppears(driver, By.name(IOSLocators.nameVideoFullScreenButton));
	}
	
	public boolean isVideoPlayerPageOpened() {
		return DriverUtils.waitUntilElementAppears(driver, By.xpath(IOSLocators.xpathVideoMainPage));
	}
	
	public void tapVideoPage(){
		videoPlayerMainWindow.click();
	}
	
	public DialogPage clickVideoDoneButton() throws Exception{
		DialogPage page = null;
		DriverUtils.mobileTapByCoordinates(driver, videoDoneButton);
		try {
			DriverUtils.mobileTapByCoordinates(driver, videoDoneButton);
		}catch (WebDriverException e) {
			
		}
		page = new DialogPage(url, path);
		return page;
		
	}
	
	public void clickPauseButton(){
		if (DriverUtils.isElementDisplayed(videoPauseButton)) {
			videoPauseButton.click();
		}
		else {
			tapVideoPage();
			videoPauseButton.click();
		}
		
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
