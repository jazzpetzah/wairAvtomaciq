package com.wearezeta.auto.win.pages.webapp;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class PeoplePickerPage extends
		com.wearezeta.auto.web.pages.PeoplePickerPage {

	@SuppressWarnings("unused")
	private static final Logger LOG = ZetaLogger.getLog(PeoplePickerPage.class
			.getName());

	public PeoplePickerPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.className(WebAppLocators.PeoplePickerPage.classNamePeoplePickerVisible));
	}

}
