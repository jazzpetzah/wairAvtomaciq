package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletContactListPage extends ContactListPage {
	private static final By xpathConversationListPage =
			By.xpath("//UIAApplication[1]/UIAWindow[2]/UIACollectionView[1]");

	public TabletContactListPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}
	
	@Override
	public void swipeDown(int time) throws Exception {
        DriverUtils.swipeElementPointToPoint(this.getDriver(), getElement(xpathConversationListPage), time,
                20, 15, 20, 90);
	}

	@Override
	public void swipeUp(int time) throws Exception {
		DriverUtils.swipeElementPointToPoint(this.getDriver(), getElement(xpathConversationListPage), time,
				20, 90, 20, 10);
	}
}
