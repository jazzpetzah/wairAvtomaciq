package com.wearezeta.auto.win.pages.webapp;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.WebPage;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PeoplePickerPage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger LOG = ZetaLogger.getLog(PeoplePickerPage.class
			.getName());

	@FindBy(xpath = "//*[contains(@class,'people-picker-list-suggestions')]//div[@data-uie-name='item-user']")
	private List<WebElement> suggestions;

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

	public int getNumberOfSuggestions() {
		return suggestions.size();
	}

}
