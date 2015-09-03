package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.backend.AccentColor;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

import java.util.List;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class PendingConnectionsPage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(PendingConnectionsPage.class
			.getSimpleName());

	private static final String CSS_BACKGROUND_COLOR = "background-color";
	private static final String CSS_BORDER_TOP_COLOR = "border-top-color";

	@FindBy(how = How.CSS, using = WebAppLocators.ConnectToPage.cssAllConnectionRequests)
	private List<WebElement> pendingRequests;

	public PendingConnectionsPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);

	}

	public int getNumberOfRequests() {
		return pendingRequests.size();
	}

	public String getEmailByName(String userId) throws Exception {
		String locator = WebAppLocators.ConnectToPage.cssRequestById
				.apply(userId)
				+ WebAppLocators.ConnectToPage.cssRequestEmailPartial;
		DriverUtils
				.waitUntilLocatorAppears(this.getDriver(), By.cssSelector(locator));

		WebElement email = getDriver().findElement(By.cssSelector(locator));
		return email.getText();
	}

	public String getMessageByName(String userName) throws Exception {
		String locator = WebAppLocators.ConnectToPage.cssRequestById
				.apply(userName)
				+ WebAppLocators.ConnectToPage.cssRequestMessagePartial;
		DriverUtils
				.waitUntilLocatorAppears(this.getDriver(), By.cssSelector(locator));

		WebElement message = getDriver().findElement(By.cssSelector(locator));
		return message.getText();
	}

	public boolean isAvatarByIdVisible(String userId) throws Exception {
		String locator = WebAppLocators.ConnectToPage.cssRequestAvatarByUserId
				.apply(userId);
		return DriverUtils
				.waitUntilLocatorIsDisplayed(this.getDriver(), By.cssSelector(locator));
	}

	public boolean isAcceptRequestButtonForUserVisible(String userId)
			throws Exception {
		String css = WebAppLocators.ConnectToPage.cssAcceptRequestButtonByUserId
				.apply(userId);
		final WebElement acceptButton = getDriver()
				.findElement(By.cssSelector(css));
		DriverUtils.waitUntilElementClickable(this.getDriver(), acceptButton);
		return acceptButton.isDisplayed();
	}

	public boolean isIgnoreRequestButtonForUserVisible(String userId)
			throws Exception {
		String css = WebAppLocators.ConnectToPage.cssIgnoreRequestButtonById
				.apply(userId);
		final WebElement ignoreButton = getDriver()
				.findElement(By.cssSelector(css));
		DriverUtils.waitUntilElementClickable(this.getDriver(), ignoreButton);
		return ignoreButton.isDisplayed();
	}

	public AccentColor getAcceptRequestButtonBgColor(String userId)
			throws Exception {
		String css = WebAppLocators.ConnectToPage.cssAcceptRequestButtonByUserId
				.apply(userId);
		final WebElement acceptButton = getDriver()
				.findElement(By.cssSelector(css));
		DriverUtils.waitUntilElementClickable(this.getDriver(), acceptButton);

		String colorRgba = acceptButton.getCssValue(CSS_BACKGROUND_COLOR);
		return AccentColor.getByRgba(colorRgba);
	}

	public AccentColor getIgnoreRequestButtonBorderColor(String userId)
			throws Exception {
		String css = WebAppLocators.ConnectToPage.cssIgnoreRequestButtonById
				.apply(userId);
		final WebElement ignoreButton = getDriver()
				.findElement(By.cssSelector(css));
		DriverUtils.waitUntilElementClickable(this.getDriver(), ignoreButton);

		String colorRgba = ignoreButton.getCssValue(CSS_BORDER_TOP_COLOR);
		return AccentColor.getByRgba(colorRgba);
	}

	public void acceptRequestFromUser(String userId) throws Exception {
		String css = WebAppLocators.ConnectToPage.cssAcceptRequestButtonByUserId
				.apply(userId);
		final WebElement acceptButton = getDriver()
				.findElement(By.cssSelector(css));
		DriverUtils.waitUntilElementClickable(this.getDriver(), acceptButton);
		acceptButton.click();
		// it takes some time to refresh the conversations list
		Thread.sleep(1000);
	}

	public void ignoreRequestFromUser(String userId) throws Exception {
		String css = WebAppLocators.ConnectToPage.cssIgnoreRequestButtonById
				.apply(userId);
		final WebElement ignoreButton = getDriver()
				.findElement(By.cssSelector(css));
		DriverUtils.waitUntilElementClickable(this.getDriver(), ignoreButton);
		// Waiting till self-profile page elements will not be clickable
		Thread.sleep(1000);
		ignoreButton.click();
	}

	public int getAmountOfKnownConnectionAvatars(String userId)
			throws Exception {
		String css = WebAppLocators.ConnectToPage.cssKnownConnectionAvatarsById
				.apply(userId);
		return getDriver().findElements(By.cssSelector(css)).size();
	}

	public String getOthersTextOfKnownConnections(String userId)
			throws Exception {
		String css = WebAppLocators.ConnectToPage.cssKnownConnectionOthersTextById
				.apply(userId);
		return getDriver().findElement(By.cssSelector(css)).getText();
	}
}
