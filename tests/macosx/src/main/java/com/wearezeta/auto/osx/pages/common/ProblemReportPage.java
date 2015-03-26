package com.wearezeta.auto.osx.pages.common;

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
import com.wearezeta.auto.osx.pages.OSXPage;

public class ProblemReportPage extends OSXPage {

	private static final Logger log = ZetaLogger.getLog(ProblemReportPage.class
			.getSimpleName());

	@FindBy(how = How.ID, using = OSXLocators.ProblemReportPage.idWindow)
	private WebElement window;

	@FindBy(how = How.NAME, using = OSXLocators.ProblemReportPage.nameCancelButton)
	private WebElement cancelButton;

	@FindBy(how = How.NAME, using = OSXLocators.ProblemReportPage.nameSendButton)
	private WebElement sendButton;

	public ProblemReportPage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.id(OSXLocators.ProblemReportPage.idWindow), 3);
	}

	public void cancelReportSending() {
		cancelButton.click();
	}

	public void sendReport() {
		sendButton.click();
	}

	public void sendReportIfAppears() throws Exception {
		if (isVisible()) {
			cancelReportSending();
			log.debug("Wire crashed on previous run. Problem report sent.");
		}
	}
}
