package com.wearezeta.auto.android_tablet.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.util.AccentColorUtil;
import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletSelfProfilePage extends AndroidTabletPage {
	public static final String idSelfNameInput = "tet__profile__guided";
	@FindBy(id = idSelfNameInput)
	private WebElement selfNameInput;

	public static final String idSelfProfileView = "ll_self_form";
	@FindBy(id = idSelfProfileView)
	private WebElement selfProfileView;

	public static final Function<String, String> xpathSelfNameByContent = content -> String
			.format("//*[@id='ttv__profile__name' and @value='%s']", content);

	public static final Function<String, String> xpathOptionsMenuItemByName = name -> String
			.format("//*[@id='fl__profile__settings_box']//*[@value='%s']",
					name.toUpperCase());

	public static final String idOptionsButton = "gtv__profile__settings_button";
	@FindBy(id = idOptionsButton)
	private WebElement optionsButton;

	private static final String idColorPicker = "cpcl__color_picker_layout";

	private static final String xpathSignOutBtn = "//*[@id='ttv__profile__settings_box__signout']";
	@FindBy(xpath = xpathSignOutBtn)
	private WebElement signOutBtn;

	public TabletSelfProfilePage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isNameVisible(String name) throws Exception {
		final By locator = By.xpath(xpathSelfNameByContent.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void tapOptionsButton() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), optionsButton);
		optionsButton.click();
	}

	public void selectOptionsMenuItem(String itemName) throws Exception {
		final By locator = By.xpath(xpathOptionsMenuItemByName.apply(itemName));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("The item '%s' is not present in Options menu",
						itemName);
		getDriver().findElement(locator).click();
	}

	public void tapSelfNameField() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), selfNameInput);
		selfNameInput.click();
	}

	public void changeSelfNameTo(String newName) throws Exception {
		selfNameInput.clear();
		selfNameInput.sendKeys(newName);
		this.getDriver().tapSendButton();
	}

	public BufferedImage getScreenshot() throws Exception {
		return this.getElementScreenshot(selfProfileView).orElseThrow(
				IllegalStateException::new);
	}

	public void tapInTheCenter() throws Exception {
		DriverUtils.tapInTheCenterOfTheElement(this.getDriver(),
				selfProfileView);
	}

	public boolean waitUntilOptionsMenuItemVisible(String itemName)
			throws Exception {
		final By locator = By.xpath(xpathOptionsMenuItemByName.apply(itemName));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void tapSignOutBtn() throws Exception {
		System.out.println(getDriver().getPageSource());
		assert DriverUtils.waitUntilElementClickable(getDriver(), signOutBtn);
		signOutBtn.click();
	}

	public WebElement findCorrectColorPicker() throws Exception {
		List<WebElement> colorPickers = getDriver().findElements(
				By.id(idColorPicker));
		WebElement colorPicker = colorPickers.get(0);
		int x = colorPicker.getLocation().getX();
		if (colorPickers.size() > 1) {
			for (WebElement cp : colorPickers) {
				if (cp.getLocation().getX() < x) {
					colorPicker = cp;
					x = cp.getLocation().getX();
				}
			}
		}
		return colorPicker;
	}

	public void chooseColorOnColorPicker(AccentColor color) throws Exception {
		WebElement colorPicker = findCorrectColorPicker();
		final int NUMBER_OF_COLORS = 7;
		int id = color.getId() - 1;
		int percentX = (100 / NUMBER_OF_COLORS) * id + (100 / NUMBER_OF_COLORS)
				/ 2;
		DriverUtils.tapOnPercentOfElement(getDriver(), colorPicker, percentX,
				50);
	}

	public AccentColor findSelectedAccentColor() throws Exception {
		WebElement colorPicker = findCorrectColorPicker();
		return AccentColorUtil.findSelectedAccentColor(this.getDriver(),
				colorPicker);
	}
}
