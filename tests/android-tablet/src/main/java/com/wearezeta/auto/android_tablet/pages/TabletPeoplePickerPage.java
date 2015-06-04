package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.pages.PeoplePickerPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletPeoplePickerPage extends AndroidTabletPage {
	@FindBy(id = AndroidLocators.PeoplePickerPage.idPeoplePickerClearbtn)
	private WebElement closePeoplePickerBtn;

	public TabletPeoplePickerPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private PeoplePickerPage getAndroidPeoplePickerPage() {
		return (PeoplePickerPage) this
				.getAndroidPageInstance(PeoplePickerPage.class);
	}

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.PeoplePickerPage.idPeoplePickerClearbtn));
	}

	public boolean waitUntilInvisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(AndroidLocators.PeoplePickerPage.idPeoplePickerClearbtn));
	}

	public void tapCloseButton() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.PeoplePickerPage.idPeoplePickerClearbtn));
		closePeoplePickerBtn.click();
	}

	public void typeTextInPeopleSearch(String searchCriteria) throws Exception {
		getAndroidPeoplePickerPage().typeTextInPeopleSearch(searchCriteria);
	}

	public void tapFoundItem(String item) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.PeoplePickerPage.xpathPeoplePickerContactByName
						.apply(item));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("The item '%s' is not visible in People Picker search list after the defualt timeout expired",
						item);
		getDriver().findElement(locator).click();
	}
}
