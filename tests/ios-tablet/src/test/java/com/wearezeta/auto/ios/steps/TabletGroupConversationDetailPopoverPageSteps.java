package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.TabletGroupConversationDetailPopoverPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TabletGroupConversationDetailPopoverPageSteps {
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
	 * Clicks on the SILENCE button in the iPad popover ellipsis menu
	 * 
	 * @step. ^I click SILENCE button on iPad ellipsis menu$
	 * @throws Throwable
	 */
	@When("^I click SILENCE button on iPad ellipsis menu$")
	public void IClickSILENCEButtonOniPadEllipsisMenu() throws Throwable {
		getTabletGroupConversationDetailPopoverPage()
				.pressSilenceEllipsisButton();
	}

	/**
	 * Clicks on the NOTIFY button in the iPad popover ellipsis menu
	 * 
	 * @step. ^I click NOTIFY button on iPad ellipsis menu$
	 * @throws Throwable
	 */
	@When("^I click NOTIFY button on iPad ellipsis menu$")
	public void IClickNOTIFYButtonOniPadEllipsisMenu() throws Throwable {
		getTabletGroupConversationDetailPopoverPage()
				.pressNotifyEllipsisButton();
	}

	/**
	 * Waits until popover is closed
	 * 
	 * @step. ^I wait until popover is closed$
	 * 
	 * @throws Exception
	 */
	@When("^I wait until popover is closed$")
	public void IWaitUntilPopoverIsClosed() throws Exception {
		Assert.assertTrue("Popover is still shown",
				getTabletGroupConversationDetailPopoverPage()
						.waitConversationInfoPopoverToClose());
	}

	/**
	 * Tap on the screen to dismiss popover
	 * 
	 * @step I dismiss popover on iPad$
	 * 
	 * @throws Exception
	 */
	@When("^I dismiss popover on iPad$")
	public void IDismissPopover() throws Exception {
		getTabletGroupConversationDetailPopoverPage().dismissPopover();
	}

}
