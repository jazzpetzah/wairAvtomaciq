package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletConversationViewPage extends AndroidTabletPage {
	public static final String idRootLocator = "fl__message_stream_land_container";

	public static final Function<String, String> xpathSystemMessageByContent = content -> String
			.format("//*[@id='ltv__row_conversation__message' and contains(@value, '%s')]",
					content.toUpperCase());

	public static final Function<String, String> xpathChatHeaderMessageByContent = content -> String
			.format("//*[@id='ttv__row_conversation__connect_request__chathead_footer__label' and contains(@value, '%s')]",
					content);

	@FindBy(id = AndroidLocators.DialogPage.idParticipantsBtn)
	private WebElement showDetailsButton;

	public TabletConversationViewPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idRootLocator));
	}

	public void tapShowDetailsButton() throws Exception {
		showDetailsButton.click();
	}

	public boolean waitForSystemMessageContains(String expectedMessage)
			throws Exception {
		final By locator = By.xpath(xpathSystemMessageByContent
				.apply(expectedMessage));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitForChatHeaderMessageContains(String expectedMessage)
			throws Exception {
		final By locator = By.xpath(xpathChatHeaderMessageByContent
				.apply(expectedMessage));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}
}
