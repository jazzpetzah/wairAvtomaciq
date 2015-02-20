package com.wearezeta.auto.android.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.locators.TabletAndroidLocators;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;
import com.wearezeta.auto.common.log.ZetaLogger;

public class TabletContactListPage extends ContactListPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = TabletAndroidLocators.TabletContactListPage.CLASS_NAME, locatorKey = "idProfileLink")
	private WebElement profileLink;
	
	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(TabletContactListPage.class.getSimpleName());

	public TabletContactListPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
		// TODO Auto-generated constructor stub
	}
	
	public void tapOnProfileLink() {
		getWait().until(ExpectedConditions.visibilityOf(profileLink));
		profileLink.click();
	}
}