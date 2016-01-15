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
	
	public CameraRollTabletPopoverPage pressAddPictureiPadButton() throws Exception {
		addPictureButton.click();
		DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.name(nameCameraLibraryButton));
		return new CameraRollTabletPopoverPage(this.getLazyDriver());
	}
	
	public TabletConversationDetailPopoverPage pressConversationDetailiPadButton() throws Exception{
		openConversationDetails.click();
		return new TabletConversationDetailPopoverPage(this.getLazyDriver());
	}
	
	public TabletGroupConversationDetailPopoverPage pressGroupConversationDetailiPadButton() throws Exception{
		openConversationDetails.click();
		return new TabletGroupConversationDetailPopoverPage(this.getLazyDriver());
	}
	
}
