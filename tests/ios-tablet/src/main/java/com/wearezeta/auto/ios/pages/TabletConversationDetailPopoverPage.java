package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletConversationDetailPopoverPage extends OtherUserPersonalInfoPage{
	public static final String nameAddContactToChatButton = "metaControllerLeftButton";
	@FindBy(name = nameAddContactToChatButton)
	private WebElement addPopOverButton;

    public static final String nameShareContactsButton = "SHARE CONTACTS";

    public TabletConversationDetailPopoverPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
		// TODO Auto-generated constructor stub
	}
	
	public void catchShareContactsAlert() throws Exception {
		try {
			WebElement el = this.getDriver().findElementByName(nameShareContactsButton);
			el.click();
		} catch (NoSuchElementException ex) {
			// do nothing
		}
	}

	public TabletPeoplePickerPage addContactTo1on1Chat() throws Exception {
		addPopOverButton.click();
		//catchShareContactsAlert();
		return new TabletPeoplePickerPage(this.getLazyDriver());
	}
}
