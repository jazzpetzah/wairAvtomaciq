package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.locators.TabletAndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class TabletContactListPage extends AndroidTabletPage {

	@FindBy(id = AndroidLocators.ContactListPage.idYourName)
	private WebElement profileLink;

	@FindBy(id = AndroidLocators.ContactListPage.idOpenStartUIButton)
	private WebElement peoplePickerButton;

	@FindBy(id = TabletAndroidLocators.TabletContactListPage.idRootLeftView)
	private WebElement rootLeftView;

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(TabletContactListPage.class.getSimpleName());

	public TabletContactListPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void tapOnProfileLink() throws Exception {
		getWait().until(ExpectedConditions.visibilityOf(profileLink));
		profileLink.click();
	}

	public TabletDialogPage initDialogPage() throws Exception {
		return new TabletDialogPage(getLazyDriver());
	}

	public boolean isPeoplePickerButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.ContactListPage.idOpenStartUIButton));
	}

	@Override
	public AndroidTabletPage swipeDown(int time) throws Exception {
		getAndroidPageInstance(ContactListPage.class).elementSwipeDown(
				rootLeftView, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	@Override
	public AndroidTabletPage returnBySwipe(SwipeDirection direction)
			throws Exception {
		switch (direction) {
		case DOWN: {
			return new TabletPeoplePickerPage(this.getLazyDriver());
		}
		default:
			return null;
		}
	}

	public String getSelfName() {
		return profileLink.getText();
	}

	public TabletPeoplePickerPage initPeoplePickerPage() throws Exception {
		return new TabletPeoplePickerPage(getLazyDriver());
	}
}
