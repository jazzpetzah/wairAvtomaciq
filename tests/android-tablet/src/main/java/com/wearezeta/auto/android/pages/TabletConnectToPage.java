package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletConnectToPage extends ConnectToPage {

	public TabletConnectToPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean connectToHeaderContains(String contact) throws Exception {
		refreshUITree();
		this.getWait().until(ExpectedConditions.visibilityOf(connectToHeader));
		return connectToHeader.getText().toLowerCase()
				.contains(contact.toLowerCase());
	}

}
