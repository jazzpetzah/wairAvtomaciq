package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.locators.TabletAndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class TabletDialogPage extends DialogPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = TabletAndroidLocators.TabletDialogPage.CLASS_NAME, locatorKey = "idProfileIcon")
	private WebElement profileButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = TabletAndroidLocators.TabletDialogPage.CLASS_NAME, locatorKey = "idParticipantsClose")
	private WebElement participantsClose;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = TabletAndroidLocators.TabletDialogPage.CLASS_NAME, locatorKey = "idRootParticipantContainer")
	private WebElement participantContainer;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idParticipantsHeader")
	private WebElement otherUserName;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idLeftActionButton")
	private WebElement addContactBtn;

	public TabletDialogPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isProfileButtonDisplayed() throws Exception {
		this.getWait().until(ExpectedConditions.visibilityOf(profileButton));
		return DriverUtils.isElementPresentAndDisplayed(profileButton);
	}

	public void tapOnProfileButton() throws Exception {
		getWait().until(ExpectedConditions.visibilityOf(profileButton));
		profileButton.click();
	}

	public void tapOnParticipantsClose() throws Exception {
		getWait().until(ExpectedConditions.visibilityOf(participantsClose));
		participantsClose.click();
	}

	public OtherUserPersonalInfoPage initOtherUserPersonalInfoPage()
			throws Exception {
		return new OtherUserPersonalInfoPage(getLazyDriver());
	}

	public boolean isPopOverDisplayed() throws Exception {
		return (DriverUtils.isElementPresentAndDisplayed(profileButton)
				&& DriverUtils.isElementPresentAndDisplayed(otherUserName) && DriverUtils
					.isElementPresentAndDisplayed(addContactBtn));
	}

	@Override
	public AndroidPage swipeUp(int time) throws Exception {
		elementSwipeUp(participantContainer, time);// TODO workaround
		Thread.sleep(1000);
		return returnBySwipe(SwipeDirection.UP);
	}
}
