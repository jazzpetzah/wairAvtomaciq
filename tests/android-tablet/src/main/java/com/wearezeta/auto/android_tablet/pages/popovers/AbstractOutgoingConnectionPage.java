package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

abstract class AbstractOutgoingConnectionPage extends AbstractConnectionPage {
	public static final String idMessage = "cet__send_connect_request__first_message";
	@FindBy(id = idMessage)
	private WebElement message;

	public AbstractOutgoingConnectionPage(Future<ZetaAndroidDriver> lazyDriver,
			AbstractPopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	protected abstract By getConnectButtonLocator();

	public String getMessage() {
		return message.getText();
	}

	public void setMessage(String text) throws Exception {
		message.clear();
		message.sendKeys(text);
		this.getDriver().hideKeyboard();
	}

	public void tapConnectButton() throws Exception {
		final WebElement connectButton = getDriver().findElement(
				getConnectButtonLocator());
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), connectButton);
		connectButton.click();
	}

}
