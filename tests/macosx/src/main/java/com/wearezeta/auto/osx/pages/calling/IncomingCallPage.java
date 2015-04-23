package com.wearezeta.auto.osx.pages.calling;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class IncomingCallPage extends CallPage {

	private static final Logger log = ZetaLogger.getLog(IncomingCallPage.class
			.getSimpleName());

	@FindBy(how = How.ID, using = OSXLocators.CallPage.idJoinCallButton)
	private WebElement joinCallButton;

	@FindBy(how = How.ID, using = OSXLocators.CallPage.idIgnoreCallButton)
	private WebElement ignoreCallButton;

	public IncomingCallPage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isIncomingCallVisible(String subscriberName)
			throws Exception {
		String xpath = String.format(
				OSXLocators.CallPage.xpathFormatUserCallsMessage,
				subscriberName);
		boolean result = DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath), 10);
		if (!result) {
			log.debug(xpath);
			log.debug(driver.getPageSource());
		}
		return result;
	}

	public StartedCallPage joinCall() throws Exception {
		joinCallButton.click();
		return new StartedCallPage(this.getDriver(), this.getWait());
	}

	public void ignoreCall() throws Exception {
		ignoreCallButton.click();
	}
}
