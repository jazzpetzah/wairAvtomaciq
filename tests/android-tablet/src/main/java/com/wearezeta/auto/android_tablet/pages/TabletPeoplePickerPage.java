package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.PeoplePickerPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletPeoplePickerPage extends AndroidTabletPage {
	@FindBy(id = PeoplePickerPage.idPeoplePickerClearbtn)
	private WebElement closePeoplePickerBtn;

	public static final String xpathTopPeopleHeader = "//*[@id='ttv_pickuser__list_header_title' and @value='TOP PEOPLE']";

	public static final Function<String, String> xpathTopPeopleAvatarByName = name -> String
			.format("//*[@id='ttv_pickuser__list_header_title']/parent::*/parent::*//*[@value='%s']/parent::*/parent::*",
					name.toUpperCase());

	public static final String idCreateConversationButton = "ll_pickuser_confirmbutton";
	@FindBy(id = idCreateConversationButton)
	private WebElement createConversationButton;

	public TabletPeoplePickerPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private PeoplePickerPage getAndroidPeoplePickerPage() throws Exception {
		return (PeoplePickerPage) this
				.getAndroidPageInstance(PeoplePickerPage.class);
	}

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(PeoplePickerPage.idPeoplePickerClearbtn));
	}

	public boolean waitUntilInvisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(PeoplePickerPage.idPeoplePickerClearbtn));
	}

	public void tapCloseButton() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(PeoplePickerPage.idPeoplePickerClearbtn));
		closePeoplePickerBtn.click();
	}

	public void typeTextInPeopleSearch(String searchCriteria) throws Exception {
		getAndroidPeoplePickerPage().typeTextInPeopleSearch(searchCriteria);
	}

	public void tapFoundItem(String item) throws Exception {
		final By locator = By
				.xpath(PeoplePickerPage.xpathPeoplePickerContactByName
						.apply(item));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("The item '%s' is not visible in People Picker search list after the defualt timeout expired",
						item);
		getDriver().findElement(locator).click();
	}

	public boolean waitUntilTopPeopleIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathTopPeopleHeader));
	}

	public void tapTopPeopleAvatar(String name) throws Exception {
		final By locator = By.xpath(xpathTopPeopleAvatarByName.apply(name));
		getDriver().findElement(locator).click();
	}

	public void tapCreateConversationButton() throws Exception {
		this.hideKeyboard();
		createConversationButton.click();
	}
}
