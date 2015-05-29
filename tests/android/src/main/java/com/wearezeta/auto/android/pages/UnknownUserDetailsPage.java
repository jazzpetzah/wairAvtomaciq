package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class UnknownUserDetailsPage extends AndroidPage {

	@FindBy(id = AndroidLocators.UnknownUserDetailsPage.idOtherUsersName)
	private WebElement otherUsersName;

	@FindBy(id = AndroidLocators.UnknownUserDetailsPage.idConnectButton)
	private WebElement connectAndPendingButton;

	@FindBy(id = AndroidLocators.UnknownUserDetailsPage.idCommonUsersLabel)
	private WebElement commonUsersLabel;

	public UnknownUserDetailsPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		return null;
	}

	public boolean isConnectButtonVisible() throws Exception {
		final String expectedText = "Connect";
		String buttonText = connectAndPendingButton.getText();
		return buttonText.equals(expectedText);
	}

	public boolean isPendingButtonVisible() throws Exception {
		final String expectedText = "Pending";
		String buttonText = connectAndPendingButton.getText();
		return buttonText.equals(expectedText);
	}

	public String getOtherUsersName() {
		return otherUsersName.getText();
	}

	public ConnectToPage tapConnectAndPendingButton() throws Exception {
		if (isConnectButtonVisible()) {
			connectAndPendingButton.click();
			return new ConnectToPage(this.getLazyDriver());
		}
		throw new RuntimeException(
				"Connect button is not visible after timeout expired");
	}
}
