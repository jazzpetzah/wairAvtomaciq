package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class SingleConnectedUserDetalsPage extends
		AbstractConversationDetailsPage {
	public final static Function<String, String> xpathNameByValue = value -> String
			.format("//*[@id='ttv__participants__header' and @value='%s']",
					value);

	public final static Function<String, String> xpathEmailByValue = value -> String
			.format("//*[@id='ttv__participants__sub_header' and @value='%s']",
					value);

	public final static String xpathAddPeopleButton = "//*[@id='ttv__participants__left_label' and @value='ADD PEOPLE']";
	@FindBy(xpath = xpathAddPeopleButton)
	private WebElement addPeopleButton;

	public SingleConnectedUserDetalsPage(Future<ZetaAndroidDriver> lazyDriver,
			SingleUserPopover container) throws Exception {
		super(lazyDriver, container);
	}

	public boolean waitUntilUserNameVisible(String expectedName)
			throws Exception {
		final By locator = By.xpath(xpathNameByValue.apply(expectedName));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitUntilUserEmailVisible(String expectedEmail)
			throws Exception {
		final By locator = By.xpath(xpathEmailByValue.apply(expectedEmail));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void tapAddPeopleButton() {
		addPeopleButton.click();
	}

}
