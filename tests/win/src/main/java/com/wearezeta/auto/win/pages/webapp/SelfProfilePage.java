package com.wearezeta.auto.win.pages.webapp;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class SelfProfilePage extends
		com.wearezeta.auto.web.pages.SelfProfilePage {

	// TODO hide behind driver impl
	private final Robot robot = new Robot();

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathCameraButton)
	private WebElement cameraButton;

	public SelfProfilePage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isSettingsButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(WebAppLocators.SelfProfilePage.cssGearButton));
	}

	public boolean isCameraButtonClickable() throws Exception {
		return DriverUtils.waitUntilElementClickable(getDriver(), cameraButton);
	}

	public void pressShortCutForPreferences() {
		robot.keyPress(KeyEvent.VK_CONTROL);// command key
		robot.keyPress(KeyEvent.VK_COMMA);
		robot.keyRelease(KeyEvent.VK_COMMA);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}
}
