package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.TabletGroupConversationDetailPopoverPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TabletGroupConversationDetailPopoverPageSteps {
	@SuppressWarnings("unused")
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private TabletGroupConversationDetailPopoverPage getTabletGroupConversationDetailPopoverPage()
			throws Exception {
		return (TabletGroupConversationDetailPopoverPage) pagesCollecton
				.getPage(TabletGroupConversationDetailPopoverPage.class);
	}

	/**
	 * Opens the ellipses menu on the ipad popover
	 * 
	 * @step. ^I press conversation menu button on iPad$
	 * @throws Throwable
	 */
	@When("^I press conversation menu button on iPad$")
	public void IPressConversationMenuButtonOniPad() throws Throwable {
		getTabletGroupConversationDetailPopoverPage()
				.openConversationMenuOnPopover();
	}

	/**
	 * Presses leave button in ellipsis menu
	 * 
	 * @step. ^I press leave converstation button on iPad$
	 * @throws Throwable
	 */
	@When("^I press leave converstation button on iPad$")
	public void IPressLeaveConverstationButtonOniPad() throws Throwable {
		getTabletGroupConversationDetailPopoverPage().leaveConversation();
	}

	/**
	 * Presses the confirmation leave button
	 * 
	 * @step. ^I press leave on iPad$
	 * @throws Throwable
	 */
	@Then("^I press leave on iPad$")
	public void i_press_leave_on_iPad() throws Throwable {
		getTabletGroupConversationDetailPopoverPage()
				.confirmLeaveConversation();
	}

	/**
	 * Selects a user on the group conversation popover
	 * 
	 * @step. ^I select user on iPad group popover (.*)$
	 * @param name
	 *            of user I select
	 * @throws Throwable
	 */
	@When("^I select user on iPad group popover (.*)$")
	public void ISelectUserOniPadGroupPopover(String name) throws Throwable {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		getTabletGroupConversationDetailPopoverPage()
				.selectUserByNameOniPadPopover(name);
	}

	/**
	 * Veryfies that there is one less user on the group conversation popover
	 * 
	 * @step. ^I see that contact (.*) is not present on group popover on iPad$
	 * @param name
	 *            of user that got removed, should not be there
	 * @throws Throwable
	 */
	@Then("^I see that contact (.*) is not present on group popover on iPad$")
	public void ISeeThatContactIsNotPresentOnGroupPopoverOniPad(String name)
			throws Throwable {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		Assert.assertTrue(getTabletGroupConversationDetailPopoverPage()
				.waitForContactToDisappearOniPadPopover(name));
	}

	/**
	 * Presses in the ellipses menu on the RENAME button
	 * 
	 * @step. ^I press RENAME on the menu on iPad$
	 * @throws Throwable
	 */
	@When("^I press RENAME on the menu on iPad$")
	public void IPressRENAMEOnTheMenuOniPad() throws Throwable {
		getTabletGroupConversationDetailPopoverPage()
				.pressRenameEllipsesButton();
	}

	/**
	 * Changes name of the group on the ipad popover
	 * 
	 * @step. ^I change group conversation name on iPad popover to (.*)$
	 * @param groupname
	 *            new name of the conversation
	 * @throws Throwable
	 */
	@When("^I change group conversation name on iPad popover to (.*)$")
	public void IChangeGroupConversationNameOniPadPopoverTo(String groupname)
			throws Throwable {
		getTabletGroupConversationDetailPopoverPage().changeConversationName(
				groupname);
	}

	/**
	 * Closes the group info popover
	 * 
	 * @step. ^I exit the group info iPad popover$
	 * @throws Throwable
	 */
	@When("^I exit the group info iPad popover$")
	public void IExitTheGroupInfoiPadPopover() throws Throwable {
		getTabletGroupConversationDetailPopoverPage().exitGroupChatPopover();
	}

	/**
	 * Checks that it can read the correct group name
	 * 
	 * @step. ^I can read the group name (.*) on the iPad popover$
	 * @throws Throwable
	 */
	@Then("^I can read the group name (.*) on the iPad popover$")
	public void ICanReadTheGroupNameOnTheiPadPopover(String groupname)
			throws Throwable {
		Assert.assertEquals(getTabletGroupConversationDetailPopoverPage()
				.getGroupChatName(), groupname);
	}

	/**
	 * Checks that the number of participants in a group is correct on the
	 * popover
	 * 
	 * @step. ^I see that number of participants (\\d+) is correct on iPad
	 *        popover$
	 * @param participantsNumber
	 *            number of participants in that group
	 * @throws Throwable
	 */
	@Then("^I see that number of participants (\\d+) is correct on iPad popover$")
	public void ISeeThatNumberOfParticipantsIsCorrectOniPadPopover(
			int participantsNumber) throws Throwable {
		int actualNumberOfPeople = getTabletGroupConversationDetailPopoverPage()
				.numberOfPeopleInGroupConversationOniPad();
		Assert.assertTrue("Actual number of people in chat ("
				+ actualNumberOfPeople + ") is not the same as expected ("
				+ participantsNumber + ")",
				actualNumberOfPeople == participantsNumber);
	}

	/**
	 * Checks that the contact has the correct avatar picture
	 * 
	 * @step. ^I see the correct avatar picture for user (.*) on iPad$
	 * @param contact
	 *            the avatar picture gets checked of
	 * @throws Throwable
	 */
	@Then("^I see the correct avatar picture for user (.*) on iPad$")
	public void ISeeTheCorrectAvatarPictureForUserOniPad(String contact)
			throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(getTabletGroupConversationDetailPopoverPage()
				.areParticipantAvatarCorrectOniPadPopover(contact));
	}

}
