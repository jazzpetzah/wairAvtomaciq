package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class InvitePage extends AndroidPage {

	private static final String idInviteMorePeopleBtn = "zb__conversationlist__show_contacts";
	@FindBy(id = idInviteMorePeopleBtn)
	private WebElement inviteMorePeopleBtn;

	@Override
	protected ZetaAndroidDriver getDriver() throws Exception {
		return (ZetaAndroidDriver) super.getDriver();
	}

	public InvitePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean waitForInviteMorePeopleButtonVisible() throws Exception {
		DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.id(idInviteMorePeopleBtn));
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				inviteMorePeopleBtn);
	}

	public boolean waitForInviteMorePeopleButtonNotVisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(idInviteMorePeopleBtn));
	}
}