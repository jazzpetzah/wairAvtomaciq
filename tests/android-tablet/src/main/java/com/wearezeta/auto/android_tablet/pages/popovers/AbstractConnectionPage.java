package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

abstract class AbstractConnectionPage extends AbstractPopoverPage {
	public static final Function<String, String> xpathStrNameByValue = value -> String
			.format("//*[@id='%s']//*[@value='%s']", OutgoingConnectionPopover.idRootLocator, value);

	public static final By idCloseButton = By.id("gtv__participants__close");

	public AbstractConnectionPage(Future<ZetaAndroidDriver> lazyDriver,
			AbstractPopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	public boolean isNameVisible(String name) throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathStrNameByValue.apply(name)));
	}

	public void tapCloseButton() throws Exception {
		getElement(idCloseButton).click();
	}

}
