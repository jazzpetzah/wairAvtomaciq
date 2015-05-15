package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class UnknownUserDetailsPage extends AndroidPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.UnknownUserDetailsPage.CLASS_NAME, locatorKey = "idOtherUsersName")
	private WebElement otherUsersName;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.UnknownUserDetailsPage.CLASS_NAME, locatorKey = "idConnectButton")
	private WebElement connectAndPendingButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.UnknownUserDetailsPage.CLASS_NAME, locatorKey = "idCommonUsersLabel")
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
		final String BUTTON_TEXT = "Connect";
		String connectButtonText = connectAndPendingButton.getText();
		return connectButtonText.equals(BUTTON_TEXT);
	}

	public boolean isPendingButtonVisible() throws Exception {
		final String BUTTON_TEXT = "Pending";
		String connectButtonText = connectAndPendingButton.getText();
		return connectButtonText.equals(BUTTON_TEXT);
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
