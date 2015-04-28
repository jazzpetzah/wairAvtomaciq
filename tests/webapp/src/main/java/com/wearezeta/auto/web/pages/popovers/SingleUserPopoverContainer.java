package com.wearezeta.auto.web.pages.popovers;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SingleUserPopoverContainer extends PeoplePopoverContainer {

	private SingleUserInfoPopoverPage singleUserPopoverPage;
	private AddPeoplePopoverPage addPeoplePopoverPage;
	private BlockUserConfirmationPopoverPage blockUserConfirmationPopoverPage;
	private PendingParticipantInfoPopoverPage pendingParticipantInfoPopoverPage;

	public SingleUserPopoverContainer(ZetaWebAppDriver driver,
			WebDriverWait wait) throws Exception {
		super(driver, wait);
		this.singleUserPopoverPage = new SingleUserInfoPopoverPage(driver,
				wait, this);
		this.addPeoplePopoverPage = new AddPeoplePopoverPage(driver, wait, this);
		this.blockUserConfirmationPopoverPage = new BlockUserConfirmationPopoverPage(
				driver, wait, this);
		this.pendingParticipantInfoPopoverPage = new PendingParticipantInfoPopoverPage(
				driver, wait, this);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.SingleUserPopover.xpathRootLocator;
	}

	private AbstractUserInfoPopoverPage getCurrentUserInfoPage()
			throws Exception {
		if (this.singleUserPopoverPage.isCurrent()) {
			return this.singleUserPopoverPage;
		} else if (this.pendingParticipantInfoPopoverPage.isCurrent()) {
			return this.pendingParticipantInfoPopoverPage;
		} else {
			throw new RuntimeException(
					"The current popover page is neither connected user info nor pending-connected user info.");
		}
	}

	public void clickAddPeopleButton() throws Exception {
		this.singleUserPopoverPage.clickAddPeopleButton();
	}

	public void selectUserFromSearchResult(String user) {
		this.addPeoplePopoverPage.selectUserFromSearchResult(user);
	}

	public String getUserName() throws Exception {
		return getCurrentUserInfoPage().getUserName();
	}

	public boolean isAddButtonVisible() {
		return this.singleUserPopoverPage.isAddButtonVisible();
	}

	public boolean isBlockButtonVisible() {
		return this.singleUserPopoverPage.isBlockButtonVisible();
	}

	public boolean isRemoveButtonVisible() throws Exception {
		return getCurrentUserInfoPage().isRemoveButtonVisible();
	}

	public boolean isAvatarVisible() throws Exception {
		return getCurrentUserInfoPage().isAvatarVisible();
	}

	public void clickBlockButton() {
		this.singleUserPopoverPage.clickBlockButton();
	}

	public void clickConfirmButton() {
		this.blockUserConfirmationPopoverPage.clickConfirmButton();
	}

	public boolean isPendingButtonVisible() {
		return this.pendingParticipantInfoPopoverPage.isPendingButtonVisible();
	}

	public void clickPendingButton() {
		this.pendingParticipantInfoPopoverPage.clickPendingButton();
	}

	public String getPendingButtonCaption() {
		return this.pendingParticipantInfoPopoverPage.getPendingButtonCaption();
	}

	public boolean isPendingTextBoxVisible() {
		return this.pendingParticipantInfoPopoverPage
				.isPendingTextBoxDisplayed();
	}

	public String getUserMail() throws Exception {
		return getCurrentUserInfoPage().getMailText();
	}

	public String getPendingButtonToolTip() {
		return this.pendingParticipantInfoPopoverPage.getPendingButtonToolTip();
	}
}
