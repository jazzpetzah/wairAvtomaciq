package com.wearezeta.auto.android.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.locators.TabletAndroidLocators;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;
import com.wearezeta.auto.common.log.ZetaLogger;

public class TabletContactListPage extends ContactListPage {
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = TabletAndroidLocators.TabletContactListPage.CLASS_NAME, locatorKey = "idProfileLink")
	private WebElement profileLink;
	
	private String url;
	private String path;
	
	private static final Logger log = ZetaLogger.getLog(TabletContactListPage.class.getSimpleName());

	public TabletContactListPage(String URL, String path) throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}
	
	public void tapOnProfileLink() {
		wait.until(ExpectedConditions.visibilityOf(profileLink));
		profileLink.click();
	}
}