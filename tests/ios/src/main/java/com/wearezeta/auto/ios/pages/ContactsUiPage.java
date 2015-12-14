package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class ContactsUiPage extends IOSPage {

	@FindBy(how = How.XPATH, using = IOSLocators.ContactsUIPage.xpathSearchInput)
	private WebElement searchInput;

	@FindBy(how = How.NAME, using = IOSLocators.ContactsUIPage.nameInviteOthersButton)
	private WebElement inviteOthersButton;

	public ContactsUiPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSearchInputVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				searchInput);
	}

	private void tapSearchInput() {
		searchInput.click();
	}

	public void inputTextToSearch(String text) throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), searchInput);
		tapSearchInput();
		searchInput.sendKeys(text);
	}

	public boolean isContactPresentedInContactsList(String contact)
			throws Exception {
		boolean flag = DriverUtils
				.waitUntilLocatorAppears(
						getDriver(),
						By.xpath(String
								.format(IOSLocators.ContactsUIPage.xpathContactOnContactsUIList,
										contact)), 5);
		return flag;
	}

	public void tapInviteOthersButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), inviteOthersButton);
		inviteOthersButton.click();
	}

	public boolean isInviteOthersButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				inviteOthersButton);
	}

	public void clickOpenButtonNextToUser(String contact) throws Exception {
		WebElement openButton = getDriver().findElement(
				By.xpath(String.format(
						IOSLocators.ContactsUIPage.xpathOpenButtonNextToUser,
						contact)));
		openButton.click();

	}
}
