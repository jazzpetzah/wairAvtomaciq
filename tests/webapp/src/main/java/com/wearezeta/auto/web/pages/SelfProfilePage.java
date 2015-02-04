package com.wearezeta.auto.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class SelfProfilePage extends WebPage {
	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathGearButton)
	private WebElement gearButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathSelfUserName)
	private WebElement userName;

	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.SelfProfilePage.classNameSelfUserMail)
	private WebElement userMail;

	public SelfProfilePage(String URL, String path) throws Exception {
		super(URL, path);
	}

	public void clickGearButton() {
		gearButton.click();
	}

	public void selectGearMenuItem(String name) {
		final String menuXPath = WebAppLocators.SelfProfilePage.xpathGearMenuRoot;
		DriverUtils.waitUntilElementAppears(driver, By.xpath(menuXPath));
		final String menuItemXPath = WebAppLocators.SelfProfilePage.xpathGearMenuItemByName
				.apply(name);
		final WebElement itemElement = driver.findElement(By
				.xpath(menuItemXPath));
		itemElement.click();
	}

	public String getUserName() {
		return userName.getText();
	}

	public String getUserMail() {
		return userMail.getText();
	}
}
