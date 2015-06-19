package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class GiphyPreviewPage extends AndroidPage {
	public static final String giphyPreviewButtonId = "gtv_cursor_multiuse_button";
	@FindBy(id = giphyPreviewButtonId)
	private WebElement giphyPreviewButton;
	
	public static final String sendButtonId = "ttv__confirmation__confirm";
	@FindBy(id = sendButtonId)
	private WebElement sendButton;
	
	public GiphyPreviewPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void clickOnGIFButton() {
		giphyPreviewButton.click();
		
	}

	public void clickeSendButton() {
		sendButton.click();		
	}
	
}
