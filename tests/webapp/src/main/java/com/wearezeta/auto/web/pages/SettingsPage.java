package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class SettingsPage extends WebPage {

	private static final Logger log = ZetaLogger.getLog(SettingsPage.class
			.getSimpleName());
	
	private final int ALERT_SETTINGS_X_OFFSET = 0;
	private final int ALERT_SETTINGS_Y_OFFSET = -10;

	@FindBy(how = How.XPATH, using = WebAppLocators.SettingsPage.xpathSettingsCloseButton)
	private WebElement settingsCloseButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.SettingsPage.xpathSoundAlertsSettings)
	private WebElement soundAllertSettings;
	
	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.SettingsPage.classNameSoundNoneLabel)
	private WebElement soundNoneLabel;
	
	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.SettingsPage.classNameSoundSomeLabel)
	private WebElement soundSomeLabel;
	
	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.SettingsPage.classNameSoundAllLabel)
	private WebElement soundAllLabel;

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

	public void setSoundSettingItem(String set) {
		switch (set) {
		case "None":
			clickSoundSettings(soundNoneLabel);
			break;
		case "Some":
			clickSoundSettings(soundSomeLabel);
			break;
		case "All":
			clickSoundSettings(soundAllLabel);
			break;
		default:
			log.debug("Next values can be used for this step -> 'None, Some, All'. Please modify your step");
			break;
		}
	}

	private void clickSoundSettings(WebElement el) {
		Actions builder = new Actions(driver);
		builder.moveToElement(el, ALERT_SETTINGS_X_OFFSET, ALERT_SETTINGS_Y_OFFSET).click().build().perform();
	}

	public String getSoundAlertSettings() {
		String setting = null;
		String attr = soundAllertSettings.getAttribute("data-uie-value");
		switch (attr) {
		case "0":
			setting="None";
			break;
		case "1":
			setting="Some";
			break;
		case "2":
			setting="All";
			break;
		default:
			log.debug("Sound allert setting value is " + attr);
			break;
		}
		
		return setting;
	}
	
}
