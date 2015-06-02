package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.tablet.locators.IOSTabletLocators;

public class TabletPeoplePickerPage extends PeoplePickerPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.namePickerSearch)
	private WebElement searchField;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePeoplePickerAddToConversationButton)
	private WebElement addToConversationButtoniPad;
	

	public TabletPeoplePickerPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		// TODO Auto-generated constructor stub
	}

	public boolean isPeoplePickerPageOnPopOverVisible() throws Exception {
		boolean pickerVisible = DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(IOSLocators.namePickerClearButton));
		Thread.sleep(1000);
		return pickerVisible;
	}

	public void pressIntoSearchField() {
		searchField.click();	
	}

	public void selectConnectedUser(String name) throws Exception {
		WebElement el = getDriver().findElement(
				By.xpath(String.format(IOSTabletLocators.TabletPeoplePickerPage.xpathIPADPeoplePickerResultUserName,
						name)));
		el.click();		
	}
	
	public GroupChatPage clickAddToConversationButtonOniPadPopover() throws Throwable {
		addToConversationButtoniPad.click();
		return new GroupChatPage(this.getLazyDriver());	
	}

}
