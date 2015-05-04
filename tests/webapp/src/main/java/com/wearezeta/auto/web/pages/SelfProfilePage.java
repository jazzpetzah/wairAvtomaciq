package com.wearezeta.auto.web.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class SelfProfilePage extends WebPage {
	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathGearButton)
	private WebElement gearButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathCameraButton)
	private WebElement cameraButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathSelfUserName)
	private WebElement userName;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathSelfUserNameInput)
	private WebElement userNameInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathNameSelfUserMail)
	private WebElement userMail;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathAccentColorPickerChildren)
	private List<WebElement> colorsInColorPicker;

	public SelfProfilePage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void clickGearButton() throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(), gearButton);
		gearButton.click();
	}

	public void selectGearMenuItem(String name) throws Exception {
		final String menuXPath = WebAppLocators.SelfProfilePage.xpathGearMenuRoot;
		DriverUtils.waitUntilElementAppears(this.getDriver(),
				By.xpath(menuXPath));
		final String menuItemXPath = WebAppLocators.SelfProfilePage.xpathGearMenuItemByName
				.apply(name);
		final WebElement itemElement = getDriver().findElement(
				By.xpath(menuItemXPath));
		itemElement.click();
	}

	public boolean checkNameInSelfProfile(String name) throws Exception {
		DriverUtils.waitUntilElementAppears(this.getDriver(),
				By.xpath(WebAppLocators.SelfProfilePage.xpathSelfUserName));

		WebDriverWait wait = new WebDriverWait(this.getDriver(), 10);

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
		DriverUtils.waitUntilElementAppears(this.getDriver(),
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

	public void selectAccentColor(String colorName) throws Exception {
		final int id = AccentColor.getByName(colorName).getId();
		final String xpathAccentColorDiv = WebAppLocators.SelfProfilePage.xpathAccentColorDivById
				.apply(id);
		assert DriverUtils.waitUntilElementAppears(this.getDriver(),
				By.xpath(xpathAccentColorDiv));
		final WebElement accentColorDiv = this.getDriver().findElementByXPath(
				xpathAccentColorDiv);
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				accentColorDiv);
		accentColorDiv.click();
	}

	public String getCurrentAccentColor() throws Exception {
		final WebElement accentColorCircleDiv = this
				.getDriver()
				.findElementByXPath(
						WebAppLocators.SelfProfilePage.xpathCurrentAccentColorCircleDiv);
		return accentColorCircleDiv.getCssValue("border-top-color");
	}

	public int getCurrentAccentColorId() {
		int i = 1;
		for (WebElement childDiv : colorsInColorPicker) {
			if (childDiv.getAttribute("class").toLowerCase()
					.contains("selected")) {
				return i;
			}
			i++;
		}
		throw new RuntimeException(
				"No accent color is selected in color picker");
	}

	public ProfilePicturePage clickCameraButton() throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(), cameraButton);
		cameraButton.click();
		return new ProfilePicturePage(getLazyDriver());
	}
}
