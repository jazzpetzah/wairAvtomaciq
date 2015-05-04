package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.pages.PersonalInfoPage;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.locators.TabletAndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class TabletPersonalInfoPage extends PersonalInfoPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idOpenStartUIButton")
	private WebElement peoplePickerButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idYourName")
	private WebElement profileLink;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idProfileOptionsButton")
	private WebElement optionsButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = TabletAndroidLocators.TabletPersonalInfoPage.CLASS_NAME, locatorKey = "idSelfForm")
	private WebElement page;
	
	public TabletPersonalInfoPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {

		AndroidPage page = null;
		switch (direction) {
		case DOWN: {
			break;
		}
		case UP: {
			page = this;
			break;
		}
		case LEFT: {
			break;
		}
		case RIGHT: {
			page = new TabletContactListPage(this.getLazyDriver());
			break;
		}
		}
		return page;
	}

	public TabletContactListPage initContactListPage() throws Exception {
		return new TabletContactListPage(getLazyDriver());
	}

	public boolean isPeoplePickerButtonVisible() throws NoSuchElementException {
		try {
			return peoplePickerButton.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void clickOnPage() throws Exception {
		DriverUtils.androidMultiTap(this.getDriver(), page, 1, 0.2);
	}
	
	public boolean isOptionsButtonVisible() throws Exception {
		return profileLink.isDisplayed();
	}

}
