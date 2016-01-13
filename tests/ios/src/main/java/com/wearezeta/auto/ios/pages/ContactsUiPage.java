package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ContactsUiPage extends IOSPage {

	public static final String xpathSearchInput = "//UIATextView[UIAStaticText[@name='SEARCH BY NAME']]";
	@FindBy(xpath = xpathSearchInput)
	private WebElement searchInput;

    public static final String nameInviteOthersButton = "INVITE OTHERS";
    @FindBy(name = nameInviteOthersButton)
	private WebElement inviteOthersButton;

    public static final String xpathContactOnContactsUIList =
            "//UIATableCell[@name='%s'][preceding::UIAButton[@name='ContactsViewCloseButton']]";

    public static final String xpathOpenButtonNextToUser =
            "//UIATableCell[@name='%s'][preceding::UIAButton[@name='ContactsViewCloseButton']]/UIAButton[@name='OPEN']";

    public ContactsUiPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
		// TODO Auto-generated constructor stub
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
		return DriverUtils
				.waitUntilLocatorAppears(
						getDriver(),
						By.xpath(String.format(xpathContactOnContactsUIList, contact)), 5);
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
				By.xpath(String.format(xpathOpenButtonNextToUser, contact)));
		openButton.click();
	}
}
