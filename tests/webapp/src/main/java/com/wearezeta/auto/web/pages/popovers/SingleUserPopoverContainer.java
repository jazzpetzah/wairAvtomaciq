package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

public class SingleUserPopoverContainer extends PeoplePopoverContainer {
	private SingleUserInfoPopoverPage singleUserPopoverPage;
	private AddPeoplePopoverPage addPeoplePopoverPage;
	private BlockUserConfirmationPopoverPage blockUserConfirmationPopoverPage;

	public SingleUserPopoverContainer(ZetaWebAppDriver driver,
			WebDriverWait wait) throws Exception {
		super(driver, wait);
		this.singleUserPopoverPage = new SingleUserInfoPopoverPage(driver,
				wait, this);
		this.addPeoplePopoverPage = new AddPeoplePopoverPage(driver, wait, this);
		this.blockUserConfirmationPopoverPage = new BlockUserConfirmationPopoverPage(
				driver, wait, this);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.SingleUserPopover.xpathRootLocator;
	}

	public void clickAddPeopleButton() throws Exception {
		this.singleUserPopoverPage.clickAddPeopleButton();
	}

	public void selectUserFromSearchResult(String user) {
		this.addPeoplePopoverPage.selectUserFromSearchResult(user);
	}

	public String getUserName() {
		return singleUserPopoverPage.getUserName();
	}

	public boolean isAddButtonVisible() {
		return this.singleUserPopoverPage.isAddButtonVisible();
	}

	public boolean isBlockButtonVisible() {
		return this.singleUserPopoverPage.isBlockButtonVisible();
	}

	public void clickBlockButton() {
		this.singleUserPopoverPage.clickBlockButton();
	}

	public void clickConfirmButton() {
		this.blockUserConfirmationPopoverPage.clickConfirmButton();
	}
}
