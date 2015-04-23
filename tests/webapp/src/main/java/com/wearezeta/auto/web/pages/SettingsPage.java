package com.wearezeta.auto.web.pages;

import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebAppConstants.Browser;
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

	public enum SoundAlertsLevel {
		None("None", 0), Some("Some", 1), All("All", 2);

		private final String stringRepresentation;
		private final int intRepresentation;

		public int getIntRepresenation() {
			return this.intRepresentation;
		}

		private SoundAlertsLevel(String stringRepresentation, int intRepresentation) {
			this.stringRepresentation = stringRepresentation;
			this.intRepresentation = intRepresentation;
		}

		@Override
		public String toString() {
			return this.stringRepresentation;
		}

		public static SoundAlertsLevel fromString(String value) {
			for (SoundAlertsLevel level : SoundAlertsLevel.values()) {
				if (level.toString().equalsIgnoreCase(value)) {
					return level;
				}
			}
			throw new NoSuchElementException(String.format(
					"There is no alert level with name '%s'", value));
		}

		public static SoundAlertsLevel fromInt(int value) {
			for (SoundAlertsLevel level : SoundAlertsLevel.values()) {
				if (level.getIntRepresenation() == value) {
					return level;
				}
			}
			throw new NoSuchElementException(String.format(
					"There is no alert level with index '%s'", value));
		}
	}

	private static final int SLIDER_CIRCLE_SIZE = 20;

	public void setSoundAlertsLevel(SoundAlertsLevel newLevel) {
		assert SoundAlertsLevel.values().length > 1;
		if (WebAppExecutionContext.getCurrentBrowser() == Browser.Firefox) {
			final Actions builder = new Actions(driver);
			final int width = soundAlertsLevel.getSize().width;
			final int height = soundAlertsLevel.getSize().height;
			final int dstX = (width - SLIDER_CIRCLE_SIZE)
					/ (SoundAlertsLevel.values().length - 1)
					* newLevel.getIntRepresenation();
			final int dstY = height / 2;
			builder.clickAndHold(soundAlertsLevel)
					.moveToElement(soundAlertsLevel, dstX, dstY).release()
					.build().perform();
		} else {
			// Workaround for browsers, which don't support native events
			final String[] sliderMoveCode = new String[] {
					"$(\"" + WebAppLocators.SettingsPage.cssSoundAlertsLevel
							+ "\").val(" + newLevel.getIntRepresenation()
							+ ");",
					"wire.app.view.content.self_profile.user_repository.save_sound_settings('"
							+ newLevel.toString().toLowerCase() + "');" };
			driver.executeScript(StringUtils.join(sliderMoveCode, "\n"));
		}
	}

	public SoundAlertsLevel getSoundAlertsLevel() {
		return SoundAlertsLevel.fromInt(Integer.parseInt(soundAlertsLevel
				.getAttribute("value")));
	}

}
