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

	public boolean isRemoveButtonVisible() {
		return this.singleUserPopoverPage.isRemoveButtonVisible();
	}

	public boolean isAvatarVisible() {
		return this.singleUserPopoverPage.isAvatarVisible();
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

	public String getUserMail() {
		return this.singleUserPopoverPage.getMailText();
	}

	public boolean isPendingButtonToolTipCorrect() {
		return this.pendingParticipantInfoPopoverPage
				.isPendingButtonToolTipCorrect();
	}

	public boolean isRemoveFromGroupChatButtonToolTipCorrect() {
		return this.singleUserPopoverPage
				.isRemoveFromGroupChatButtonToolTipCorrect();
	}
}
