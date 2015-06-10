package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class TabletDialogPage extends DialogPage {
	
	@FindBy(how = How.NAME, using = IOSLocators.nameAddPictureButton)
	private WebElement addPictureButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameOpenConversationDetails)
	protected WebElement openConversationDetails;
	
	public TabletDialogPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
	
	public CameraRollTabletPopoverPage pressAddPictureiPadButton() throws Exception {
		addPictureButton.click();
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(IOSLocators.xpathCameraLibraryButton));
		return new CameraRollTabletPopoverPage(this.getLazyDriver());
	}
	
	public TabletConversationDetailPopoverPage pressConversationDetailiPadButton() throws Exception{
		openConversationDetails.click();
		return new TabletConversationDetailPopoverPage(this.getLazyDriver());
	}

}
