package com.wearezeta.auto.web.pages;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class SettingsPage extends WebPage {
	@FindBy(how = How.XPATH, using = WebAppLocators.SettingsPage.xpathSettingsCloseButton)
	private WebElement settingsCloseButton;

	@FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssSoundAlertsLevel)
	private WebElement soundAlertsLevel;

	public SettingsPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isVisible() throws Exception {
		final String xpath = WebAppLocators.SettingsPage.xpathSettingsDialogRoot;
		return DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath));
	}

	public void clickCloseButton() {
		settingsCloseButton.click();
	}

	public enum AlertsLevel {
		None("None", 0), Some("Some", 1), All("All", 2);

		private final String stringRepresentation;
		private final int intRepresentation;

		public int getIntRepresenation() {
			return this.intRepresentation;
		}

		private AlertsLevel(String stringRepresentation, int intRepresentation) {
			this.stringRepresentation = stringRepresentation;
			this.intRepresentation = intRepresentation;
		}

		@Override
		public String toString() {
			return this.stringRepresentation;
		}

		public static AlertsLevel fromString(String value) {
			for (AlertsLevel level : AlertsLevel.values()) {
				if (level.toString().equalsIgnoreCase(value)) {
					return level;
				}
			}
			throw new NoSuchElementException(String.format(
					"There is no alert level with name '%s'", value));
		}

		public static AlertsLevel fromInt(int value) {
			for (AlertsLevel level : AlertsLevel.values()) {
				if (level.getIntRepresenation() == value) {
					return level;
				}
			}
			throw new NoSuchElementException(String.format(
					"There is no alert level with index '%s'", value));
		}
	}

	private static final int SLIDER_CIRCLE_SIZE = 20;

	public void setSoundAlertsLevel(AlertsLevel newLevel) {
		assert AlertsLevel.values().length > 1;
		Actions builder = new Actions(driver);
		builder.clickAndHold(soundAlertsLevel)
				.moveToElement(
						soundAlertsLevel,
						(soundAlertsLevel.getSize().width - SLIDER_CIRCLE_SIZE)
								/ (AlertsLevel.values().length - 1)
								* newLevel.getIntRepresenation(),
						soundAlertsLevel.getSize().height / 2).release()
				.build().perform();
	}

	public AlertsLevel getSoundAlertsLevel() {
		return AlertsLevel.fromInt(Integer.parseInt(soundAlertsLevel
				.getAttribute("value")));
	}

}
