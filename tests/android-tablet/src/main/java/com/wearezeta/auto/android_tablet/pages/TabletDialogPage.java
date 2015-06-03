package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android.pages.OtherUserPersonalInfoPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletDialogPage extends AndroidTabletPage {

	public static final String idProfileIcon = "tv__cursor_participants";
	@FindBy(id = idProfileIcon)
	private WebElement profileButton;

	public static final String idParticipantsClose = "gtv__participants__close";
	@FindBy(id = idParticipantsClose)
	private WebElement participantsClose;

	public static final String idRootParticipantContainer = "fl__root__participant_container";
	@FindBy(id = idRootParticipantContainer)
	private WebElement participantContainer;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idParticipantsHeader)
	private WebElement otherUserName;

	@FindBy(id = AndroidLocators.OtherUserPersonalInfoPage.idLeftActionButton)
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
	public AndroidTabletPage swipeUp(int time) throws Exception {
		getAndroidPageInstance(DialogPage.class).elementSwipeUp(
				participantContainer, time);
		Thread.sleep(1000);
		return returnBySwipe(SwipeDirection.UP);
	}
}
