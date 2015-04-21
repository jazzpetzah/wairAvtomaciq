package com.wearezeta.auto.android.pages;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletConnectToPage extends ConnectToPage  {

	public TabletConnectToPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}
	
	public boolean connectToHeaderContains(String contact) {
		refreshUITree();
		this.getWait().until(ExpectedConditions.visibilityOf(connectToHeader));
		return connectToHeader.getText().toLowerCase().contains(contact.toLowerCase());
	}

}
