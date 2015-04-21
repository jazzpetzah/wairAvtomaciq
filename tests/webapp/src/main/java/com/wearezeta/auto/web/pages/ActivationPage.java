package com.wearezeta.auto.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ActivationPage extends WebPage {
	public ActivationPage(ZetaWebAppDriver driver, WebDriverWait wait,
			String url) throws Exception {
		super(driver, wait, url);
	}

	public void openInNewTab() throws Exception {
		WebCommonUtils.openUrlInNewTab(
				PagesCollection.registrationPage.getDriver(), this.getUrl(),
				this.getDriver().getNodeIp());
	}

	public ContactListPage verifyActivation(int timeoutSeconds)
			throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ActivationPage.xpathBtnOpenWebApp);
		assert DriverUtils.isElementDisplayed(driver, locator, timeoutSeconds) : "It seems there was some failure while verifying registered account";
		driver.findElement(locator).click();
		return new ContactListPage(getDriver(), getWait());
	}

	@Override
	public void close() throws Exception {
		WebCommonUtils.switchToPreviousTab(driver);
		super.close();
	}
}
