package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class GiphyPreviewPage extends IOSPage {
	@FindBy(how = How.NAME, using = IOSLocators.GiphyPreviewPage.nameGiphySendButton)
	private WebElement giphySendButton;

	public GiphyPreviewPage(Future<ZetaIOSDriver> driver) throws Exception {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void tapSendGiphyButton() throws Exception {
		giphySendButton.click();
	}
}