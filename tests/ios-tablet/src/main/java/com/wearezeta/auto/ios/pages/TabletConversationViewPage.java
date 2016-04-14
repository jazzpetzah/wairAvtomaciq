package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletConversationViewPage extends ConversationViewPage {

	public static final By nameOpenConversationDetails = MobileBy.AccessibilityId("ComposeControllerConversationDetailButton");
	
	public TabletConversationViewPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
	
	public void pressAddPictureiPadButton() throws Exception {
		getElement(nameAddPictureButton).click();
	}
	
	public void pressConversationDetailiPadButton() throws Exception{
		getElement(nameOpenConversationDetails).click();
	}
	
	public void pressGroupConversationDetailiPadButton() throws Exception{
        pressConversationDetailiPadButton();
	}
	
}
