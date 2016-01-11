package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.tablet.locators.IOSTabletLocators;

public class TabletContactListPage extends ContactListPage {
	
	@FindBy(how = How.XPATH, using = IOSTabletLocators.TabletConversatonListPage.xpathConversationListPage)
	private WebElement conversationListPage;

	public TabletContactListPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public IOSPage swipeDown(int time) throws Exception {
		Point coords = mainWindow.getLocation();
		Dimension elementSize = mainWindow.getSize();
		this.getDriver().swipe(coords.x + 50,
				coords.y + 150, coords.x + 50,
				coords.y + elementSize.height - 150, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	@Override
	public IOSPage swipeUp(int time) throws Exception {
		DriverUtils.swipeUp(this.getDriver(), conversationListPage, time, 50, 70);
		return returnBySwipe(SwipeDirection.UP);
	}
}
