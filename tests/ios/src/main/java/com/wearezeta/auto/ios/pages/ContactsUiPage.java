package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ContactsUiPage extends IOSPage {

	private static final String xpathSearchInput = "//UIATextView[UIAStaticText[@name='SEARCH BY NAME']]";
	@FindBy(xpath = xpathSearchInput)
	private WebElement searchInput;

	private static final String nameInviteOthersButton = "INVITE OTHERS";
    @FindBy(name = nameInviteOthersButton)
	private WebElement inviteOthersButton;

	private static final Function<String, String> xpathConvoCellByName = name ->
            String.format("//UIATableCell[@name='%s'][preceding::UIAButton[@name='ContactsViewCloseButton']]", name);

	private static final Function<String, String> xpathOpenButtonByConvoName = name ->
            String.format("//UIATableCell[@name='%s']" +
                    "[preceding::UIAButton[@name='ContactsViewCloseButton']]/UIAButton[@name='OPEN']", name);

    public ContactsUiPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
		// TODO Auto-generated constructor stub
	}

	public boolean isSearchInputVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathSearchInput));
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
        final By locator = By.xpath(xpathConvoCellByName.apply(contact));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 5);
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
        final By locator = By.xpath(xpathOpenButtonByConvoName.apply(contact));
		getDriver().findElement(locator).click();
	}
}
