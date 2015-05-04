package com.wearezeta.auto.osx.pages.calling;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

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

	public IncomingCallPage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isIncomingCallVisible(String subscriberName)
			throws Exception {
		String xpath = String.format(
				OSXLocators.CallPage.xpathFormatUserCallsMessage,
				subscriberName);
		boolean result = DriverUtils.waitUntilElementAppears(this.getDriver(),
				By.xpath(xpath), 30);
		if (!result) {
			log.debug(xpath);
			log.debug(this.getDriver().getPageSource());
		}
		return result;
	}

	public StartedCallPage joinCall() throws Exception {
		joinCallButton.click();
		return new StartedCallPage(this.getLazyDriver());
	}

	public void ignoreCall() throws Exception {
		ignoreCallButton.click();
	}
}
