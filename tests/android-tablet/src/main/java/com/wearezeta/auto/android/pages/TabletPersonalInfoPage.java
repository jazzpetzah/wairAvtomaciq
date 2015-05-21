package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.PersonalInfoPage;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.locators.TabletAndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletPersonalInfoPage extends PersonalInfoPage {

	@FindBy(id = AndroidLocators.ContactListPage.idOpenStartUIButton)
	private WebElement peoplePickerButton;

	@FindBy(id = AndroidLocators.ContactListPage.idYourName)
	private WebElement profileLink;

	@FindBy(xpath = AndroidLocators.PersonalInfoPage.xpathProfileOptionsButton)
	private WebElement optionsButton;

	@FindBy(id = TabletAndroidLocators.TabletPersonalInfoPage.idSelfForm)
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
