package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.PeoplePickerPage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletConversationsListPage extends AndroidTabletPage {
	public TabletConversationsListPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private ContactListPage getContactListPage() throws Exception {
		return (ContactListPage) this
				.getAndroidPageInstance(ContactListPage.class);
	}

	private PeoplePickerPage getPeoplePickerPage() throws Exception {
		return (PeoplePickerPage) this
				.getAndroidPageInstance(PeoplePickerPage.class);
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

	public void verifyConversationsListIsLoaded() throws Exception {
		try {
			getContactListPage().waitForConversationListLoad();
		} finally {
			// FIXME: Workaround for android bug AN-2238
			this.fixOrientation();
		}
	}

	final static int MAX_ORIENTATION_FIX_RETRIES = 3;

	private void fixOrientation() throws Exception {
		final ScreenOrientation currentOrientation = ScreenOrientationHelper
				.getInstance().fixOrientation(getDriver());
		if (currentOrientation == ScreenOrientation.LANDSCAPE) {
			// No need to swipe right in landscape orientation
			return;
		}

		final By overlayLocator = By
				.id(TabletSelfProfilePage.idSelfProfileView);
		final int screenWidth = getDriver().manage().window().getSize()
				.getWidth();
		int ntry = 1;
		while (getDriver().findElement(overlayLocator).getLocation().getX() < screenWidth / 2
				&& ntry <= MAX_ORIENTATION_FIX_RETRIES) {
			this.tapOnCenterOfScreen();
			this.tapOnCenterOfScreen();
			DriverUtils.swipeRight(getDriver(),
					getDriver().findElement(overlayLocator), 1000);
			ntry++;
		}
		if (ntry > MAX_ORIENTATION_FIX_RETRIES) {
			throw new IllegalStateException("Conversations list is not visible");
		}
	}

	public TabletSelfProfilePage tapMyAvatar() throws Exception {
		getContactListPage().tapOnMyAvatar();
		return new TabletSelfProfilePage(this.getLazyDriver());
	}

	public void tapSearchInput() throws Exception {
		getPeoplePickerPage().tapPeopleSearch();
	}

	public boolean waitUntilConversationIsVisible(String name) throws Exception {
		final By locator = By.xpath(ContactListPage.xpathContactByName
				.apply(name));
		return DriverUtils
				.waitUntilLocatorIsDisplayed(getDriver(), locator, 40);
	}

	public boolean waitUntilConversationIsInvisible(String name)
			throws Exception {
		final By locator = By.xpath(ContactListPage.xpathContactByName
				.apply(name));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
	}

	public void tapConversation(String name) throws Exception {
		final By locator = By.xpath(ContactListPage.xpathContactByName
				.apply(name));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("The conversation '%s' does not exist in the conversations list",
						name);
		getDriver().findElement(locator).click();
	}

	public boolean waitUntilConversationIsSilenced(String name)
			throws Exception {
		final By locator = By.xpath(ContactListPage.xpathMutedIconByConvoName
				.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitUntilConversationIsNotSilenced(String name)
			throws Exception {
		final By locator = By.xpath(ContactListPage.xpathMutedIconByConvoName
				.apply(name));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
	}

	public boolean isAnyConversationVisible() throws Exception {
		return getContactListPage().isAnyConversationVisible();
	}

	public boolean isNoConversationsVisible() throws Exception {
		return getContactListPage().isNoConversationsVisible();
	}

}
