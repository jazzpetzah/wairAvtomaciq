package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

public class GroupPopoverContainer extends PeoplePopoverContainer {

	private ParticipantsListPopoverPage participantsListPopoverPage;
	private ConnectedParticipantInfoPopoverPage connectedParticipantInfoPopoverPage;
	private NonConnectedParticipantInfoPopoverPage nonConnectedParticipantInfoPopoverPage;
	private PendingParticipantInfoPopoverPage pendingParticipantInfoPopoverPage;
	private BlockedParticipantInfoPopoverPage blockedParticipantInfoPopoverPage;
	private LeaveGroupConfirmationPopoverPage leaveGroupConfirmationPopoverPage;
	private RemoveFromGroupConfirmationPopoverPage removeFromGroupConfirmationPopoverPage;
	private UnblockUserConfirmationPopoverPage unblockUserConfirmationPopoverPage;
	private PendingUserConfirmationPopoverPage pendingUserConfirmationPopoverPage;
	private SelfInfoPopoverPage selfInfoPopoverPage;

	public GroupPopoverContainer(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		this.participantsListPopoverPage = new ParticipantsListPopoverPage(
				lazyDriver, this);
		this.connectedParticipantInfoPopoverPage = new ConnectedParticipantInfoPopoverPage(
				lazyDriver, this);
		this.nonConnectedParticipantInfoPopoverPage = new NonConnectedParticipantInfoPopoverPage(
				lazyDriver, this);
		this.pendingParticipantInfoPopoverPage = new PendingParticipantInfoPopoverPage(
				lazyDriver, this);
		this.blockedParticipantInfoPopoverPage = new BlockedParticipantInfoPopoverPage(
				lazyDriver, this);
		this.leaveGroupConfirmationPopoverPage = new LeaveGroupConfirmationPopoverPage(
				lazyDriver, this);
		this.removeFromGroupConfirmationPopoverPage = new RemoveFromGroupConfirmationPopoverPage(
				lazyDriver, this);
		this.unblockUserConfirmationPopoverPage = new UnblockUserConfirmationPopoverPage(
				lazyDriver, this);
		this.pendingUserConfirmationPopoverPage = new PendingUserConfirmationPopoverPage(
				lazyDriver, this);
		this.selfInfoPopoverPage = new SelfInfoPopoverPage(lazyDriver, this);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.xpathRootLocator;
	}

	private AbstractUserInfoPopoverPage getCurrentUserInfoPage()
			throws Exception {
		if (this.connectedParticipantInfoPopoverPage.isCurrent()) {
			return this.connectedParticipantInfoPopoverPage;
		} else if (this.nonConnectedParticipantInfoPopoverPage.isCurrent()) {
			return this.nonConnectedParticipantInfoPopoverPage;
		} else if (this.pendingParticipantInfoPopoverPage.isCurrent()) {
			return this.pendingParticipantInfoPopoverPage;
		} else if (this.blockedParticipantInfoPopoverPage.isCurrent()) {
			return this.blockedParticipantInfoPopoverPage;
		} else if (this.selfInfoPopoverPage.isCurrent()) {
			return this.selfInfoPopoverPage;
		} else {
			throw new RuntimeException(
					"The current popover page is neither connected user info ,non-connected nor pending connection user info.");
		}
	}

	public void setConversationTitle(String newTitle) {
		this.participantsListPopoverPage.setConversationTitle(newTitle);
	}

	public void clickLeaveGroupChat() {
		this.participantsListPopoverPage.clickLeaveGroupChat();
	}

	public String getPeopleCountInfo() {
		return this.participantsListPopoverPage.getPeopleCountInfo();
	}

	public int getPeopleCount() throws Exception {
		return this.participantsListPopoverPage.getPeopleCount();
	}

	public void confirmLeaveGroupChat() throws Exception {
		this.leaveGroupConfirmationPopoverPage.confirmLeaveGroupChat();
	}

	public void clickOnParticipant(String name) throws Exception {
		this.participantsListPopoverPage.clickOnParticipant(name);
	}

	public void clickRemoveFromGroupChat() throws Exception {
		getCurrentUserInfoPage().clickRemoveFromGroupChat();
	}

	public void confirmRemoveFromGroupChat() throws Exception {
		this.removeFromGroupConfirmationPopoverPage
				.confirmRemoveFromGroupChat();
	}

	public boolean isRemoveButtonVisible() throws Exception {
		return getCurrentUserInfoPage().isRemoveButtonVisible();
	}

	public String getLeaveGroupChatButtonToolTip() throws Exception {
		return this.participantsListPopoverPage
				.getLeaveGroupChatButtonToolTip();
	}

	public void clickAddPeopleButton() throws Exception {
		this.participantsListPopoverPage.clickAddPeopleButton();
	}

	public void clickUnblockButton() throws Exception {
		this.blockedParticipantInfoPopoverPage.clickUnblockButton();
	}

	public boolean isUnblockButtonVisible() {
		return this.blockedParticipantInfoPopoverPage.isUnblockButtonVisible();
	}

	public String getUnblockButtonCaption() {
		return this.blockedParticipantInfoPopoverPage.getUnblockButtonCaption();
	}

	public String getUnblockButtonToolTip() {
		return this.blockedParticipantInfoPopoverPage.getUnblockButtonToolTip();
	}

	public void clickConfirmUnblockButton() throws Exception {
		this.unblockUserConfirmationPopoverPage.clickConfirmUnblockButton();
	}

	public String getAddPeopleButtonToolTip() throws Exception {
		return this.participantsListPopoverPage.getAddPeopleButtonToolTip();
	}

    public boolean isParticipantVisible(String name) throws Exception {
        return this.participantsListPopoverPage.isParticipantVisible(name);
    }

	public boolean isParticipantVerified(String name) throws Exception {
		return this.participantsListPopoverPage.isParticipantVerified(name);
	}

	public boolean isEveryoneAlreadyAddedMessageShown() throws Exception {
		return this.participantsListPopoverPage.isEveryoneAlreadyAddedMessageShown();
	}

	public String getConversationTitle() {
		return this.participantsListPopoverPage.getConversationTitle();
	}

	public void selectUserFromSearchResult(String name) throws Exception {
		this.addPeoplePopoverPage.selectUserFromSearchResult(name);
	}

	public void selectUsersFromSearchResult(int amount) throws Exception {
		this.addPeoplePopoverPage.selectUsersFromSearchResult(amount);
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

	public String getRenameConversationToolTip() {
		return this.participantsListPopoverPage.getRenameConversationToolTip();
	}

	public String getOpenConvButtonCaption() {
		return this.connectedParticipantInfoPopoverPage
				.getOpenConvButtonCaption();
	}

	public boolean isOpenConvButtonVisible() {
		return this.connectedParticipantInfoPopoverPage
				.isOpenConvButtonVisible();
	}

	public String getOpenConvButtonToolTip() {
		return this.connectedParticipantInfoPopoverPage
				.getOpenConvButtonToolTip();
	}

	public void clickOpenConvButton() {
		connectedParticipantInfoPopoverPage.clickOpenConversationButton();
	}

	public String getMailHref() throws Exception {
		return getCurrentUserInfoPage().getMailHref();
	}

	public String getPendingButtonToolTip() {
		return this.pendingParticipantInfoPopoverPage.getPendingButtonToolTip();
	}

	public boolean isPendingTextBoxVisible() {
		return this.pendingParticipantInfoPopoverPage
				.isPendingTextBoxDisplayed();
	}

	public String getRemoveFromGroupChatButtonToolTip() {
		return this.connectedParticipantInfoPopoverPage
				.getRemoveFromGroupChatButtonToolTip();
	}

	public String getUserName() throws Exception {
		return getCurrentUserInfoPage().getUserName();
	}

	public boolean isAvatarVisible() throws Exception {
		return getCurrentUserInfoPage().isAvatarVisible();
	}

	public String getUserMail() throws Exception {
		return getCurrentUserInfoPage().getMailText();
	}

	public void clickConfirmConnectButton() {
		pendingUserConfirmationPopoverPage.clickConfirmConnectButton();
	}

	public void clickIgnoreConnectButton() {
		pendingUserConfirmationPopoverPage.clickIgnoreConnectButton();
	}

	public boolean isProfileButtonVisible() {
		return selfInfoPopoverPage.isProfileButtonVisible();
	}

	public void clickProfileButton() {
		selfInfoPopoverPage.clickProfileButton();
	}

	public String getProfileButtonCaption() {
		return selfInfoPopoverPage.getProfileButtonCaption();
	}

	public String getProfileButtonToolTip() {
		return selfInfoPopoverPage.getProfileButtonToolTip();
	}

}
