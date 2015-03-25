package com.wearezeta.auto.osx.pages.calling;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.pages.OSXPage;

public abstract class CallPage extends OSXPage {

	public CallPage(ZetaOSXDriver driver, WebDriverWait wait) throws Exception {
		super(driver, wait);
	}
}
