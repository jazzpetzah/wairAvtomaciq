package com.wearezeta.auto.android.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.locators.TabletAndroidLocators;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;
import com.wearezeta.auto.common.log.ZetaLogger;

public class TabletContactListPage extends ContactListPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idYourName")
	private WebElement profileLink;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idOpenStartUIButton")
	private WebElement peoplePickerButton;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = TabletAndroidLocators.TabletContactListPage.CLASS_NAME, locatorKey = "idRootLeftView")
	private WebElement rootLeftView;

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(TabletContactListPage.class.getSimpleName());

	public TabletContactListPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void tapOnProfileLink() {
		getWait().until(ExpectedConditions.visibilityOf(profileLink));
		profileLink.click();
	}

	public TabletDialogPage initDialogPage() throws Exception {
		return new TabletDialogPage(getDriver(), getWait());
	}

	public boolean isPeoplePickerButtonVisible() throws NoSuchElementException {
		try {
			return peoplePickerButton.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	@Override
	public AndroidPage swipeDown(int time) throws Exception {
		refreshUITree();
		elementSwipeDown(rootLeftView, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {

		AndroidPage page = null;
		switch (direction) {
		case DOWN: {
			page = new PeoplePickerPage(this.getDriver(), this.getWait());
			break;
		}
		case UP: {
			break;
		}
		case LEFT: {
			break;
		}
		case RIGHT: {
			break;
		}
		}
		return page;
	}
	
	public String getSelfName() {
		return profileLink.getText();
	}


	public TabletPeoplePickerPage initPeoplePickerPage() throws Exception {
		return new TabletPeoplePickerPage(getDriver(), getWait());
	}
}