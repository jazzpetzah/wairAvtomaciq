package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

abstract class AbstractConnectionPage extends AbstractPopoverPage {
	public static final Function<String, String> xpathNameByValue = value -> String
			.format("//*[@id='%s']//*[@value='%s']",
					OutgoingConnectionPopover.idRootLocator, value);

	public static final String idCloseButton = "gtv__participants__close";
	@FindBy(id = idCloseButton)
	private WebElement closeButton;

	public AbstractConnectionPage(Future<ZetaAndroidDriver> lazyDriver,
			AbstractPopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	public boolean isNameVisible(String name) throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathNameByValue.apply(name)));
	}

	public void tapCloseButton() {
		closeButton.click();
	}

}
