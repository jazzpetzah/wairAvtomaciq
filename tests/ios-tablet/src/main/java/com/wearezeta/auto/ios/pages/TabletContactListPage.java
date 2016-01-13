package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
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
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void swipeDown(int time) throws Exception {
		Point coords = mainWindow.getLocation();
		Dimension elementSize = mainWindow.getSize();
		this.getDriver().swipe(coords.x + 50,
				coords.y + 150, coords.x + 50,
				coords.y + elementSize.height - 150, time);
	}

	@Override
	public void swipeUp(int time) throws Exception {
		DriverUtils.swipeUp(this.getDriver(), conversationListPage, time, 50, 70);
	}
}
