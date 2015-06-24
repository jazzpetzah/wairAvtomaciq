package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class TabletGroupConversationDetailPopoverPage extends GroupChatInfoPage {

	public TabletGroupConversationDetailPopoverPage(

	Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
		// TODO Auto-generated constructor stub
	}

	@FindBy(how = How.NAME, using = IOSLocators.nameConversationMenu)
	private WebElement conversationMenuButton;

	public void openConversationMenuOnPopover() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(IOSLocators.nameConversationMenu));
		conversationMenuButton.click();
	}
	
	public TabletOtherUserInfoPage selectUserByNameOniPadPopover(String name) throws Exception{
		DriverUtils.mobileTapByCoordinates(this.getDriver(), getDriver()
				.findElementByName(name.toUpperCase()));

		return new TabletOtherUserInfoPage(this.getLazyDriver());
	}
	
	public boolean waitForContactToDisappearOniPadPopover(String name) throws Exception{
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.name(name));
	}

}
