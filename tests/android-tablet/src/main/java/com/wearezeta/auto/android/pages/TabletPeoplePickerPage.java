package com.wearezeta.auto.android.pages;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletPeoplePickerPage extends PeoplePickerPage{

	public TabletPeoplePickerPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
		// TODO Auto-generated constructor stub
	}

	public TabletConnectToPage initConnectToPage() throws Exception {
		return new TabletConnectToPage(getDriver(), getWait());
	}
}
