package com.wearezeta.auto.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ConnectToPopupPage extends WebPage {

	public ConnectToPopupPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	@FindBy(how = How.ID, using = WebAppLocators.ConnectToPopup.idConnectionPopupWindow)
	private WebElement connectPopupWindow;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConnectToPopup.xpathUserName)
	private WebElement userName;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConnectToPopup.xpathNameConnectionMessage)
	private WebElement connectionText;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConnectToPopup.xpathConnectButton)
	private WebElement connectButton;

	public boolean isConntectPopupVisible() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.id(WebAppLocators.ConnectToPopup.idConnectionPopupWindow));
	}

	public String getUserName() {
		return userName.getText();
	}

	public void clickConnectButton() {
		connectButton.click();
	}

}
