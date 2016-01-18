package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletDialogPage extends DialogPage {
	@FindBy(name = nameAddPictureButton)
	private WebElement addPictureButton;

	public static final String nameOpenConversationDetails = "ComposeControllerConversationDetailButton";
	@FindBy(name = nameOpenConversationDetails)
	protected WebElement openConversationDetails;
	
	public TabletDialogPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
	
	public void pressAddPictureiPadButton() throws Exception {
		addPictureButton.click();
		DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(nameCameraLibraryButton));
	}
	
	public void pressConversationDetailiPadButton() throws Exception{
		openConversationDetails.click();
	}
	
	public void pressGroupConversationDetailiPadButton() throws Exception{
		openConversationDetails.click();
	}
	
}
