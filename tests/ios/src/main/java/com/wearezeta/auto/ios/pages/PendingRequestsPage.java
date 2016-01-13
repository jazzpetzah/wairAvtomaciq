package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PendingRequestsPage extends IOSPage {

	public static final String namePendingRequestIgnoreButton = "IGNORE";
	@FindBy(name = namePendingRequestIgnoreButton)
	private WebElement ignoreRequestButton;

    public static final String namePendingRequestConnectButton = "CONNECT";
    @FindBy(name = namePendingRequestConnectButton)
	private WebElement connectRequestButton;

    public static final String xpathPendingRequesterName =
            "//UIAWindow[@name='ZClientMainWindow']/UIATableView[1]//UIAStaticText[contains(@name, 'Connect to')]";
    @FindBy(xpath = xpathPendingRequesterName)
	private WebElement requesterName;

    public static final String xpathPendingRequestMessage =
            "//UIAWindow[@name='ZClientMainWindow']/UIATableView[1]//UIAStaticText[3]";
    @FindBy(xpath = xpathPendingRequestMessage)
	private WebElement pendingMessage;

    public static final String xpathYouBothKnowPeopleIcon =
            "//UIAWindow[@name='ZClientMainWindow']/UIATableView[1]/UIATableCell[1]/UIAButton[2]";
    @FindBy(xpath = xpathYouBothKnowPeopleIcon)
	private WebElement youBothKnowPeopleIcon;

    public static final String nameYouBothKnowHeader = "YOU BOTH KNOW";

    public PendingRequestsPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void clickIgnoreButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), ignoreRequestButton);
		ignoreRequestButton.click();
	}

	public void clickIgnoreButtonMultiple(int clicks) throws Exception {
		for (int i = 0; i < clicks; i++) {
			DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.name(namePendingRequestIgnoreButton));
			DriverUtils.waitUntilElementClickable(getDriver(), ignoreRequestButton);
			ignoreRequestButton.click();
		}
	}

	public void clickConnectButton() throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				connectRequestButton);
		connectRequestButton.click();
	}

	public void clickConnectButtonMultiple(int clicks) throws Exception {
		for (int i = 0; i < clicks; i++) {
			DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.name(namePendingRequestConnectButton));
			this.getWait().until(ExpectedConditions.elementToBeClickable(connectRequestButton));
			connectRequestButton.click();
			DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.name(namePendingRequestConnectButton));
		}
	}

	public boolean isConnectButtonDisplayed() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(namePendingRequestConnectButton), 5);
	}

	public String getRequesterName() {
		final String CONNECT_TO = "Connect to ";
		return requesterName.getText().replace(CONNECT_TO, "");
	}

	public boolean isYouBothKnowDisplayed() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(nameYouBothKnowHeader), 5);
	}

	public void clickYouBothKnowPeopleIcon() {
		youBothKnowPeopleIcon.click();
	}

}
