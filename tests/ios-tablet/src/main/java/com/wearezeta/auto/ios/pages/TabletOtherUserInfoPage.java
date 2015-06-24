package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.tablet.locators.IOSTabletLocators;

public class TabletOtherUserInfoPage extends OtherUserPersonalInfoPage{
	
	@FindBy(how = How.NAME, using = IOSTabletLocators.TabletOtherUserInfoPage.nameOtherUserMetaControllerRightButtoniPadPopover)
	private WebElement removeFromGroupChat;

	public TabletOtherUserInfoPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		// TODO Auto-generated constructor stub
	}
	
	public void removeFromConversationOniPad() throws Exception{
		removeFromGroupChat.click();
	}

}
