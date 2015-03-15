package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

public class SingleUserPopoverContainer extends PeoplePopoverContainer {
	private SingleUserInfoPopoverPage singleUserPopoverPage;

	public SingleUserPopoverContainer(ZetaWebAppDriver driver,
			WebDriverWait wait) throws Exception {
		super(driver, wait);
		this.singleUserPopoverPage = new SingleUserInfoPopoverPage(driver, wait,
				this);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.SingleUserPopover.xpathRootLocator;
	}

	public boolean isAddPeopleButtonVisible() throws Exception {
		return singleUserPopoverPage.isAddPeopleButtonVisible();
	}

	public boolean isBlockButtonVisible() throws Exception {
		return singleUserPopoverPage.isBlockPeopleButtonVisible();
	}
	
	

}
