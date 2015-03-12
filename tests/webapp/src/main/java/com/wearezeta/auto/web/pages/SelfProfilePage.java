package com.wearezeta.auto.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class SelfProfilePage extends WebPage {
	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathGearButton)
	private WebElement gearButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathSelfUserName)
	private WebElement userName;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathSelfUserNameInput)
	private WebElement userNameInput;

	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.SelfProfilePage.classNameSelfUserMail)
	private WebElement userMail;

	public SelfProfilePage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void clickGearButton() throws Exception {
		DriverUtils.waitUntilElementClickable(driver, gearButton);
		gearButton.click();
	}

	public void selectGearMenuItem(String name) throws Exception {
		final String menuXPath = WebAppLocators.SelfProfilePage.xpathGearMenuRoot;
		DriverUtils.waitUntilElementAppears(driver, By.xpath(menuXPath));
		final String menuItemXPath = WebAppLocators.SelfProfilePage.xpathGearMenuItemByName
				.apply(name);
		final WebElement itemElement = driver.findElement(By
				.xpath(menuItemXPath));
		itemElement.click();
	}

	public boolean checkNameInSelfProfile(String name) throws Exception {
		DriverUtils.waitUntilElementAppears(driver,
				By.xpath(WebAppLocators.SelfProfilePage.xpathSelfUserName));

		WebDriverWait wait = new WebDriverWait(driver, 10);

		return wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				if (userName.getText().equals(name)) {
					return true;
				} else {
					return false;
				}
			}
		});
	}

	public String getUserName() throws Exception {
		DriverUtils.waitUntilElementAppears(driver,
				By.xpath(WebAppLocators.SelfProfilePage.xpathSelfUserName));
		return userName.getText();
	}

	public String getUserMail() {
		return userMail.getText();
	}

	public void setUserName(String name) {
		userName.click();
		userNameInput.clear();
		userNameInput.sendKeys(name + "\n");
	}
}
