package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

public class GroupPopoverContainer extends PeoplePopoverContainer {
	private ParticipantsListPopoverPage participantsListPopoverPage;
	private ConnectedParticipantInfoPopoverPage connectedParticipantInfoPopoverPage;
	private NonConnectedParticipantInfoPopoverPage nonConnectedParticipantInfoPopoverPage;
	private AddPeopleConfirmationPopoverPage addPeopleConfirmationPopoverPage;
	private LeaveGroupConfirmationPopoverPage leaveGroupConfirmationPopoverPage;
	private RemoveFromGroupConfirmationPopoverPage removeFromGroupConfirmationPopoverPage;

	public GroupPopoverContainer(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
		this.participantsListPopoverPage = new ParticipantsListPopoverPage(
				driver, wait, this);
		this.connectedParticipantInfoPopoverPage = new ConnectedParticipantInfoPopoverPage(
				driver, wait, this);
		this.nonConnectedParticipantInfoPopoverPage = new NonConnectedParticipantInfoPopoverPage(
				driver, wait, this);
		this.addPeopleConfirmationPopoverPage = new AddPeopleConfirmationPopoverPage(
				driver, wait, this);
		this.leaveGroupConfirmationPopoverPage = new LeaveGroupConfirmationPopoverPage(
				driver, wait, this);
		this.removeFromGroupConfirmationPopoverPage = new RemoveFromGroupConfirmationPopoverPage(
				driver, wait, this);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.xpathRootLocator;
	}

	public void setConversationTitle(String newTitle) {
		this.participantsListPopoverPage.setConversationTitle(newTitle);
	}

	public void clickLeaveGroupChat() {
		this.participantsListPopoverPage.clickLeaveGroupChat();
	}

	public void confirmLeaveGroupChat() throws Exception {
		this.leaveGroupConfirmationPopoverPage.confirmLeaveGroupChat();
	}

	public void clickOnParticipant(String name) throws Exception {
		this.participantsListPopoverPage.clickOnParticipant(name);
	}

	public void clickRemoveFromGroupChat() throws Exception {
		if (this.connectedParticipantInfoPopoverPage.isCurrent()) {
			this.connectedParticipantInfoPopoverPage.clickRemoveFromGroupChat();
		} else if (this.nonConnectedParticipantInfoPopoverPage.isCurrent()) {
			this.nonConnectedParticipantInfoPopoverPage
					.clickRemoveFromGroupChat();
		} else {
			throw new RuntimeException(
					"The current popover page is neither connected user info nor non-connected user info.");
		}
	}

	public void confirmRemoveFromGroupChat() throws Exception {
		this.removeFromGroupConfirmationPopoverPage
				.confirmRemoveFromGroupChat();
	}

	public void clickAddPeopleButton() {
		this.participantsListPopoverPage.clickAddPeopleButton();
	}

	public boolean isParticipantVisible(String name) throws Exception {
		return this.participantsListPopoverPage.isParticipantVisible(name);
	}

	public boolean isAddPeopleMessageShown() throws Exception {
		return this.addPeopleConfirmationPopoverPage.isCurrent();
	}

	public String getConversationTitle() {
		return this.participantsListPopoverPage.getConversationTitle();
	}

	public void selectUserFromSearchResult(String name) {
		this.addPeoplePopoverPage.selectUserFromSearchResult(name);
	}

	public void confirmAddPeople() {
		this.addPeopleConfirmationPopoverPage.clickContinueButton();
	}
}
