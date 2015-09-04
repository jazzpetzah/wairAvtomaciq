package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.popovers.GroupPopover;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupPopoverSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private GroupPopover getGroupPopover() throws Exception {
		return (GroupPopover) pagesCollection.getPage(GroupPopover.class);
	}

	/**
	 * Verifies whether group popover page is currently visible or not
	 * 
	 * @step. ^I (do not )?see (?:the |\\s*)[Gg]roup popover$
	 * @param shouldNotBeVisible
	 *            equals to null if "do not" part does not exist in the step
	 * 
	 * @throws Exception
	 */
	@Then("^I (do not )?see (?:the |\\s*)[Gg]roup popover$")
	public void ISeeThePopover(String shouldNotBeVisible) throws Exception {
		if (shouldNotBeVisible == null) {
			Assert.assertTrue("The group popover is not currently visible",
					getGroupPopover().waitUntilVisible());
		} else {
			Assert.assertTrue(
					"The group popover is still visible, but should be hidden",
					getGroupPopover().waitUntilInvisible());
		}
	}

	/**
	 * Tap Options button on Group popover
	 * 
	 * @step. ^I tap Options button on [Gg]roup popover$
	 * 
	 * @throws Exception
	 */
	@When("^I tap Options button on [Gg]roup popover$")
	public void ITapOptionsButton() throws Exception {
		getGroupPopover().tapOptionsButton();
	}

	/**
	 * Tap the corresponding menu item name on Group popover (the menu has to be
	 * already opened)
	 * 
	 * @step. ^I select (.*) menu item on [Gg]roup popover$
	 * 
	 * @param itemName
	 *            the name of menu item
	 * @throws Exception
	 */
	@When("^I select (.*) menu item on [Gg]roup popover$")
	public void ISelectMenuItem(String itemName) throws Exception {
		getGroupPopover().selectMenuItem(itemName);
	}

	/**
	 * Tap confirm leave button on the Group popover
	 * 
	 * @step. ^I confirm leaving (?:the |\\s*)group chat on [Gg]roup popover$
	 * 
	 * @throws Exception
	 */
	@And("^I confirm leaving (?:the |\\s*)group chat on [Gg]roup popover$")
	public void IConfirmLeave() throws Exception {
		getGroupPopover().tapConfirmLeaveButton();
	}

	/**
	 * Tap the Remove button on Group popover
	 * 
	 * @step. ^I tap Remove button on [Gg]roup popover$
	 * 
	 * @throws Exception
	 */
	@When("^I tap Remove button on [Gg]roup popover$")
	public void ITapRemoveButton() throws Exception {
		getGroupPopover().tapRemoveButton();
	}

	/**
	 * Tap confirm remove button on the Group popover
	 * 
	 * @step. ^I confirm removal from the group chat on [Gg]roup popover$"
	 * 
	 * @throws Exception
	 */
	@And("^I confirm removal from the group chat on [Gg]roup popover$")
	public void IConfirmRemove() throws Exception {
		getGroupPopover().tapConfirmRemovalButton();
	}

	/**
	 * Verify whether the avatars of a particular group convo participants are
	 * visible or not on the popover
	 * 
	 * @step ^I see the participant avatars? (.*) on [Gg]roup popover$
	 * 
	 * @param names
	 *            comma-separated participant names/aliases
	 * @param shouldNotBeVisible
	 *            equals to null if "do not" part is not present in the step
	 * @throws Exception
	 */
	@Then("^I (do not )?see the participant avatars? (.*) on [Gg]roup popover$")
	public void ISeeParticipantAvatar(String shouldNotBeVisible, String names)
			throws Exception {
		for (String name : CommonSteps.splitAliases(names)) {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
			if (shouldNotBeVisible == null) {
				Assert.assertTrue(
						String.format(
								"The avatar of '%s' is not visible in the conversation details popover",
								name), getGroupPopover()
								.waitForParticipantAvatarVisible(name));
			} else {
				Assert.assertTrue(
						String.format(
								"The avatar of '%s' is still visible in the conversation details popover",
								name), getGroupPopover()
								.waitForParticipantAvatarNotVisible(name));
			}
		}
	}

	/**
	 * Tap the particular participant avatar on Group popover
	 * 
	 * @step. ^I tap the participant avatar (.*) on [Gg]roup popover$
	 * 
	 * @param name
	 *            participant name/alias
	 * @throws Exception
	 */
	@And("^I tap the participant avatar (.*) on [Gg]roup popover$")
	public void ITapParticipantAvatar(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		getGroupPopover().tapParticipantAvatar(name);
	}

	/**
	 * Type the corresponding connection message on Group popover
	 * 
	 * @step. ^I enter connection message \"(.*)\" on [Gg]roup popover$
	 * 
	 * @param text
	 *            the invitation message content
	 * @throws Exception
	 */
	@And("^I enter connection message \"(.*)\" on [Gg]roup popover$")
	public void IEnterConnectionMessage(String text) throws Exception {
		getGroupPopover().setConnectionMessage(text);
	}

	/**
	 * Tap Connect button on Group popover
	 * 
	 * @step. ^I tap Connect button on [Gg]roup popover$
	 *
	 * @throws Exception
	 */
	@And("^I tap Connect button on [Gg]roup popover$")
	public void ITapConnectButton() throws Exception {
		getGroupPopover().tapConnectButton();
	}

	/**
	 * Verify Pending button is visible on Group popover
	 * 
	 * @step. ^I see Pending button on [Gg]roup popover$
	 *
	 * @throws Exception
	 */
	@And("^I see Pending button on [Gg]roup popover$")
	public void ISeePendingButton() throws Exception {
		Assert.assertTrue(
				"Pending button does not exists on the Group popover after the timeout",
				getGroupPopover().waitUntilPendingButtonIsVisible());
	}

	/**
	 * Tap Close button on the group popover
	 * 
	 * @step. ^I tap Close button on [Gg]roup popover$
	 * 
	 * @throws Exception
	 */
	@When("^I tap Close button on [Gg]roup popover$")
	public void ITapCloseButton() throws Exception {
		getGroupPopover().tapCloseButton();
	}

	/**
	 * Change the name of group conversation from the popover
	 * 
	 * @step. ^I rename the conversation to \"(.*)\" on [Gg]roup popover$
	 * 
	 * @param newName
	 *            the new conversation name
	 * @throws Exception
	 */
	@When("^I rename the conversation to \"(.*)\" on [Gg]roup popover$")
	public void IRenameConversation(String newName) throws Exception {
		getGroupPopover().renameConversation(newName);
	}

	/**
	 * Tap Open Conversation button on the group popover
	 * 
	 * @step. ^I tap Open Conversation button on [Gg]roup popover$
	 * 
	 * @throws Exception
	 */
	@When("^I tap Open Conversation button on [Gg]roup popover$")
	public void ITapOpenConversationButton() throws Exception {
		getGroupPopover().tapOpenConversationButton();
	}

	/**
	 * Verify whether the particular user name is visible on Group popover
	 * 
	 * @step. ^I see (?:the |\\s*)user name (.*) on [Gg]roup popover$"
	 * 
	 * @param expectedName
	 *            user name/alias
	 * @throws Exception
	 */
	@Then("^I see (?:the |\\s*)user name (.*) on [Gg]roup popover$")
	public void ISeeUserName(String expectedName) throws Exception {
		expectedName = usrMgr.findUserByNameOrNameAlias(expectedName).getName();
		Assert.assertTrue(String.format(
				"The user name '%s' is not displayed on Group popover",
				expectedName),
				getGroupPopover().waitUntilUserNameVisible(expectedName));
	}

	/**
	 * Verify whether the particular user email is visible on Group popover
	 * 
	 * @step. ^I see (?:the |\\s*)user email (.*) on [Gg]roup popover$"
	 * 
	 * @param expectedEmail
	 *            user email/alias
	 * @throws Exception
	 */
	@Then("^I see (?:the |\\s*)user email (.*) on [Gg]roup popover$")
	public void ISeeUserEmail(String expectedEmail) throws Exception {
		expectedEmail = usrMgr.findUserByEmailOrEmailAlias(expectedEmail)
				.getEmail();
		Assert.assertTrue(String.format(
				"The user email '%s' is not displayed on Group popover",
				expectedEmail),
				getGroupPopover().waitUntilUserEmailVisible(expectedEmail));
	}

	/**
	 * Verify whether the expected conversation name string is visible on group
	 * popover
	 * 
	 * @step. ^I see the conversation name \"(.*)\" on [Gg]roup popover$"
	 * 
	 * @param expectedName
	 *            expected group conversation name
	 * @throws Exception
	 */
	@Then("^I see the conversation name \"(.*)\" on [Gg]roup popover$")
	public void ISeeConversationName(String expectedName) throws Exception {
		Assert.assertTrue(String.format(
				"The conversation name '%s' is not visible on Group popover",
				expectedName), getGroupPopover()
				.waitUntilConversationNameVisible(expectedName));
	}

	/**
	 * Verify whether the expected subheader is visible on group popover
	 * 
	 * @step. ^I see \"(.*)\" subheader on [Gg]roup popover$
	 * 
	 * @param expectedText
	 *            the text of the label to verify
	 * @throws Exception
	 */
	@Then("^I see \"(.*)\" subheader on [Gg]roup popover$")
	public void ISeeLabel(String expectedText) throws Exception {
		Assert.assertTrue(String.format(
				"The subheader '%s' is not visible on Group popover",
				expectedText),
				getGroupPopover().waitUntilSubheaderIsVisible(expectedText));
	}
}
