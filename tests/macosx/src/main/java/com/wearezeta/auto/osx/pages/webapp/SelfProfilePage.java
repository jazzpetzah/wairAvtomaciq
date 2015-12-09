package com.wearezeta.auto.osx.pages.webapp;

import java.awt.Robot;
import java.awt.event.KeyEvent;
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
import com.wearezeta.auto.web.pages.WebPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

public class SelfProfilePage extends WebPage {

	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	// TODO hide behind driver impl
	private final Robot robot = new Robot();

	@FindBy(how = How.CSS, using = WebAppLocators.SelfProfilePage.cssGearButton)
	private WebElement gearButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathCameraButton)
	private WebElement cameraButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathSelfUserName)
	private WebElement userName;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathSelfUserNameInput)
	private WebElement userNameInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathNameSelfUserMail)
	private WebElement userMail;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathNameSelfUserPhoneNumber)
	private WebElement userPhoneNumber;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathAccentColorPickerChildren)
	private List<WebElement> colorsInColorPicker;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathBackgroundAvatarAccentColor)
	private WebElement backgroundAvatarAccentColor;

	public SelfProfilePage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isSettingsButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.cssSelector(WebAppLocators.SelfProfilePage.cssGearButton));
	}

	public boolean isCameraButtonClickable() throws Exception {
		return DriverUtils.waitUntilElementClickable(getDriver(), cameraButton);
	}

	public void selectGearMenuItem(String name) throws Exception {
		final String menuXPath = WebAppLocators.SelfProfilePage.xpathGearMenuRoot;
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(menuXPath));
		final String menuItemXPath = WebAppLocators.SelfProfilePage.xpathGearMenuItemByName
				.apply(name);
		final WebElement itemElement = getDriver().findElement(
				By.xpath(menuItemXPath));
		itemElement.click();
	}

	public boolean checkNameInSelfProfile(String name) throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
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
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(WebAppLocators.SelfProfilePage.xpathSelfUserName));
		return userName.getText();
	}

	public String getUserMail() {
		return userMail.getText();
	}

	public String getUserPhoneNumber() {
		return userPhoneNumber.getText();
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
		assert DriverUtils.waitUntilLocatorAppears(this.getDriver(),
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
		return webappPagesCollection.getPage(ProfilePicturePage.class);
	}

	public AccentColor getCurrentAvatarAccentColor() throws Exception {
		final WebElement backgroundAvatarAccentColor = this
				.getDriver()
				.findElementByXPath(
						WebAppLocators.SelfProfilePage.xpathBackgroundAvatarAccentColor);
		return AccentColor.getByRgba(backgroundAvatarAccentColor
				.getCssValue("background-color"));
	}

	public void pressShortCutForPreferences() {
		robot.keyPress(KeyEvent.VK_META);// command key
		robot.keyPress(KeyEvent.VK_COMMA);
		robot.keyRelease(KeyEvent.VK_COMMA);
		robot.keyRelease(KeyEvent.VK_META);
	}
}
