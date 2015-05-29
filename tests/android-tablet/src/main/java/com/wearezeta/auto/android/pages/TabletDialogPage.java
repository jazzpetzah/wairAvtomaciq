package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.locators.TabletAndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletDialogPage extends DialogPage {

	@FindBy(id = TabletAndroidLocators.TabletDialogPage.idProfileIcon)
	private WebElement profileButton;

	@FindBy(id = TabletAndroidLocators.TabletDialogPage.idParticipantsClose)
	private WebElement participantsClose;

	@FindBy(id = TabletAndroidLocators.TabletDialogPage.idRootParticipantContainer)
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
	public AndroidPage swipeUp(int time) throws Exception {
		elementSwipeUp(participantContainer, time);// TODO workaround
		Thread.sleep(1000);
		return returnBySwipe(SwipeDirection.UP);
	}
}
