package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

class NonConnectedParticipantInfoPopoverPage extends AbstractUserInfoPopoverPage {
	public NonConnectedParticipantInfoPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container) throws Exception {
		super(driver, wait, container);
	}
}
