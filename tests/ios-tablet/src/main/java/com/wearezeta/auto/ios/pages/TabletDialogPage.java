package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletDialogPage extends DialogPage {

	public static final By nameOpenConversationDetails = By.name("ComposeControllerConversationDetailButton");
	
	public TabletDialogPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
	
	public void pressAddPictureiPadButton() throws Exception {
		getElement(nameAddPictureButton).click();
		DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameCameraLibraryButton);
	}
	
	public void pressConversationDetailiPadButton() throws Exception{
		getElement(nameOpenConversationDetails).click();
	}
	
	public void pressGroupConversationDetailiPadButton() throws Exception{
        pressConversationDetailiPadButton();
	}
	
}
