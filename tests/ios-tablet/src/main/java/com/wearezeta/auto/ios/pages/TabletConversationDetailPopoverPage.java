package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.tablet.locators.IOSTabletLocators;

public class TabletConversationDetailPopoverPage extends OtherUserPersonalInfoPage {
	
	@FindBy(how = How.NAME, using = IOSLocators.nameAddContactToChatButton)
	private WebElement addPopOverButton;
	
	public TabletConversationDetailPopoverPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
		// TODO Auto-generated constructor stub
	}
	
	public void catchShareContactsAlert() throws Exception {
		try {
			WebElement el = this.getDriver().findElementByName(
					IOSTabletLocators.TabletPeoplePickerPage.nameShareContactsButton);
			el.click();
		} catch (NoSuchElementException ex) {
			// do nothing
		}
	}

	public TabletPeoplePickerPage addContactTo1on1Chat() throws Exception {
		addPopOverButton.click();
		catchShareContactsAlert();
		return new TabletPeoplePickerPage(this.getLazyDriver());
	}

}
