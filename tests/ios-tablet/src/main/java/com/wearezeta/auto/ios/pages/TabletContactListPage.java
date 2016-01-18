package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletContactListPage extends ContactListPage {
	public static final String xpathConversationListPage = "//UIAApplication[1]/UIAWindow[2]/UIACollectionView[1]";
	@FindBy(xpath = xpathConversationListPage)
	private WebElement conversationListPage;

	public TabletContactListPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}
	
	@Override
	public void swipeDown(int time) throws Exception {
        DriverUtils.swipeElementPointToPoint(this.getDriver(), conversationListPage, time,
                20, 15, 20, 90);
	}

	@Override
	public void swipeUp(int time) throws Exception {
		DriverUtils.swipeElementPointToPoint(this.getDriver(), conversationListPage, time,
				20, 90, 20, 10);
	}
}
