package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

class OutgoingConnectionPage extends AbstractPopoverPage {
	public static final Function<String, String> xpathNameByValue = value -> String
			.format("//*[@id='%s']//*[@value='%s']",
					OutgoingConnectionPopover.idRootLocator, value);

	public static final String idMessage = "cet__send_connect_request__first_message";
	@FindBy(id = idMessage)
	private WebElement message;

	public static final String idConnectButton = "zb__send_connect_request__connect_button";
	@FindBy(id = idConnectButton)
	private WebElement connectButton;

	public static final String idCloseButton = "gtv__participants__close";
	@FindBy(id = idCloseButton)
	private WebElement closeButton;

	public OutgoingConnectionPage(Future<ZetaAndroidDriver> lazyDriver,
			OutgoingConnectionPopover container) throws Exception {
		super(lazyDriver, container);
	}

	public boolean isNameVisible(String name) throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathNameByValue.apply(name)));
	}

	public String getMessage() {
		return message.getText();
	}

	public void setMessage(String text) throws Exception {
		message.clear();
		message.sendKeys(text);
		this.getDriver().hideKeyboard();
	}

	public void tapConnectButton() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), connectButton);
		connectButton.click();
	}

	public void tapCloseButton() {
		closeButton.click();
	}

}
