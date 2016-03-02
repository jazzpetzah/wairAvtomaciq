package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletConversationDetailPopoverPage extends OtherUserPersonalInfoPage{
	public static final By nameAddContactToChatButton = MobileBy.AccessibilityId("metaControllerLeftButton");

    public TabletConversationDetailPopoverPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
	
	public void addContactTo1on1Chat() throws Exception {
		getElement(nameAddContactToChatButton).click();
	}
}
