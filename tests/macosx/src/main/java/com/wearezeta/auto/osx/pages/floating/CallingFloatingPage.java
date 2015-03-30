package com.wearezeta.auto.osx.pages.floating;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.calling.StartedCallPage;

public class CallingFloatingPage extends OSXPage {

	@FindBy(how = How.XPATH, using = OSXLocators.CallingFloatingPage.xpathWindow)
	private WebElement window;

	@FindBy(how = How.XPATH, using = OSXLocators.CallingFloatingPage.xpathCloseButton)
	private WebElement closeButton;

	@FindBy(how = How.NAME, using = OSXLocators.CallingFloatingPage.nameLaterButton)
	private WebElement laterButton;

	@FindBy(how = How.NAME, using = OSXLocators.CallingFloatingPage.nameAnswerButton)
	private WebElement answerButton;

	public CallingFloatingPage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isWindowVisible() throws Exception {
		String windowXPath = OSXLocators.CallingFloatingPage.xpathWindow;
		return DriverUtils.waitUntilElementAppears(driver,
				By.xpath(windowXPath));
	}

	public boolean isCallFromUserVisible(String userName) throws Exception {
		String userNameXPath = String.format(
				OSXLocators.CallingFloatingPage.xpathFormatCallerNameText,
				userName);
		boolean userNameFound = false;
		try {
			userNameFound = DriverUtils.waitUntilElementAppears(driver,
					By.xpath(userNameXPath));
		} catch (TimeoutException e) {
		}
		return isWindowVisible() && userNameFound;
	}

	public StartedCallPage answerCall() throws Exception {
		answerButton.click();
		return new StartedCallPage(this.getDriver(), this.getWait());
	}

	public void ignoreCall() {
		laterButton.click();
	}
}
