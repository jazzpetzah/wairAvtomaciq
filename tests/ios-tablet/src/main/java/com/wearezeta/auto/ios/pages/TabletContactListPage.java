package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletContactListPage extends ContactListPage {

	public TabletContactListPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public IOSPage swipeDown(int time) throws Exception {
		Point coords = content.getLocation();
		Dimension elementSize = content.getSize();
		this.getDriver().swipe(coords.x + 50,
				coords.y + 150, coords.x + 50,
				coords.y + elementSize.height - 150, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}

}
