package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.pages.popovers.GroupPopoverContainer;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.List;

import org.junit.Assert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class GroupPopoverPageSteps {

	private static final String MAILTO = "mailto:";
	private static final String CAPTION_OPEN_CONVERSATION = "OPEN CONVERSATION";
	private static final String CAPTION_PENDING = "PENDING";
	private static final String CAPTION_UNBLOCK = "UNBLOCK";
	private static final String CAPTION_PROFILE = "PROFILE";
	private static final String TOOLTIP_REMOVE_FROM_CONVERSATION = "Remove from conversation";
	private static final String TOOLTIP_LEAVE_CONVERSATION = "Leave conversation";
	private static final String TOOLTIP_ADD_PEOPLE_TO_CONVERSATION = "Add people to conversation";
	private static final String SHORTCUT_ADD_PEOPLE_TO_CONVERSATION_WIN = "(Ctrl + Shift + K)";
	private static final String SHORTCUT_ADD_PEOPLE_TO_CONVERSATION_MAC = "(⌘⇧K)";
	private static final String TOOLTIP_BACK = "Back";
	private static final String TOOLTIP_OPEN_CONVERSATION = "Open conversation";
	private static final String TOOLTIP_CHANGE_CONVERSATION_NAME = "Change conversation name";
	private static final String TOOLTIP_PENDING = "Pending";
	private static final String TOOLTIP_UNBLOCK = "Unblock";
	private static final String TOOLTIP_OPEN_YOUR_PROFILE = "Open your profile";

        private final TestContext context;
        
        
    public GroupPopoverPageSteps() {
        this.context = new TestContext();
    }

    public GroupPopoverPageSteps(TestContext context) {
        this.context = context;
    }

	/**
	 * Verify that Group Participants popover is shown or not
	 *
	 * @step. ^I( do not)? see Group Participants popover$
	 * 
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part does not exist
	 * 
	 * @throws Exception
	 *
	 */
	@When("^I( do not)? see Group Participants popover$")
	public void ISeeUserProfilePopupPage(String shouldNotBeVisible)
			throws Exception {
		if (shouldNotBeVisible == null) {
			context.getPagesCollection().getPage(GroupPopoverContainer.class)
					.waitUntilVisibleOrThrowException();
		} else {
			context.getPagesCollection().getPage(GroupPopoverContainer.class)
					.waitUntilNotVisibleOrThrowException();
		}
	}

	/**
	 * Click leave group chat button on Group Participants popover
	 *
	 * @step. ^I click Leave button on Group Participants popover$
	 *
	 */
	@When("^I click Leave button on Group Participants popover$")
	public void IClickLeaveGroupChat() throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.clickLeaveGroupChat();
	}

	/**
	 * Verifies that leave group chat button on Group Participants popover is not shown
	 *
	 * @step. ^I do not see Leave button on Group Participants popover$
	 *
	 */
	@When("^I do not see Leave button on Group Participants popover$")
	public void IDoNotSeeLeaveButtonOnGroupPopoverC() throws Exception {
		Assert.assertTrue("Leave button is still visible", context.getPagesCollection()
				.getPage(GroupPopoverContainer.class).isLeaveButtonOnGroupPopoverInvisible());
	}

	/**
	 * Verifies that Add People button on Group Participants popover is not shown
	 *
	 * @step. ^I do not see Add People button on Group Participants popover$
	 *
	 */
	@When("^I do not see Add People button on Group Participants popover$")
	public void IDoNotSeeAddPeopleButtonOnGroupPopoverC() throws Exception {
		Assert.assertTrue("Add people button is still visible", context.getPagesCollection()
				.getPage(GroupPopoverContainer.class).isAddPeopleButtonOnGroupPopoverInvisible());
	}

	/**
	 * Confirm leaving group chat by clicking LEAVE button on Group Participants
	 * popover
	 *
	 * @step. ^I click confirm leave group conversation on Group Participants
	 *        popover$
	 * @throws Exception
	 *
	 */
	@When("^I click confirm leave group conversation on Group Participants popover$")
	public void IClickConfirmLeaveGroupChat() throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.confirmLeaveGroupChat();
	}

	/**
	 * Verifies whether back button tool tip is correct or not.
	 *
	 * @step. ^I see correct back button tool tip on Group Participants popover$
	 *
	 */
	@Then("^I see correct back button tool tip on Group Participants popover$")
	public void ThenISeeCorrectBackButtonToolTip() throws Exception {
		Assert.assertTrue(context.getPagesCollection()
				.getPage(GroupPopoverContainer.class).getBackButtonToolTip()
				.equals(TOOLTIP_BACK));
	}

	/**
	 * Click on back button.
	 *
	 * @step. ^I click back button on Group Participants popover$
	 *
	 */
	@When("^I click back button on Group Participants popover$")
	public void WhenIClickBackButton() throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.clickBackButton();
	}

	/**
	 * Verifies whether pending button tool tip is correct or not.
	 *
	 * @step. ^I see correct pending button tool tip on Group Participants
	 *        popover$
	 *
	 */
	@Then("^I see correct pending button tool tip on Group Participants popover$")
	public void ThenISeeCorrectPendingButtonToolTip() throws Exception {
		Assert.assertTrue(context.getPagesCollection()
				.getPage(GroupPopoverContainer.class).getPendingButtonToolTip()
				.equals(TOOLTIP_PENDING));
	}

	/**
	 * Click on a participant on Group Participants popover
	 *
	 * @step. ^I click on participant (.*) on Group Participants popover$
	 *
	 * @param name
	 *            user name string
	 * @throws Exception
	 */
	@When("^I click on participant (.*) on Group Participants popover$")
	public void IClickOnParticipant(String name) throws Exception {
		name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.clickOnParticipant(name);
	}

	/**
	 * Verifies whether Pending button is visible on Group Participants popover
	 *
	 * @step. ^I see Pending button on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I see Pending button on Group Participants popover$")
	public void ISeePendingButton() throws Exception {
		final String pendingButtonMissingMessage = "Pending button is not visible on Group Participants popover";
		final String pendingButtonWrongCaptionMessage = "Pending button has wrong caption on Group Participants popover";
		Assert.assertTrue(pendingButtonMissingMessage, context.getPagesCollection()
				.getPage(GroupPopoverContainer.class).isPendingButtonVisible());
		Assert.assertTrue(
				pendingButtonWrongCaptionMessage,
				context.getPagesCollection().getPage(GroupPopoverContainer.class)
						.getPendingButtonCaption().trim()
						.equals(CAPTION_PENDING));
	}

	/**
	 * Click Pending button on Group Participants popover
	 *
	 * @step. ^I click Pending button on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I click Pending button on Group Participants popover$")
	public void IClickPendingButton() throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.clickPendingButton();
	}

	/**
	 * Verifies whether profile button is visible on Group Participants popover
	 *
	 * @step. ^I see profile button on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I see profile button on Group Participants popover$")
	public void ISeeProfileButton() throws Exception {
		final String pendingButtonMissingMessage = "Profile button is not visible on Group Participants popover";
		final String pendingButtonWrongCaptionMessage = "Profile button has wrong caption on Group Participants popover";
		Assert.assertTrue(pendingButtonMissingMessage, context.getPagesCollection()
				.getPage(GroupPopoverContainer.class).isProfileButtonVisible());
		Assert.assertTrue(
				pendingButtonWrongCaptionMessage,
				context.getPagesCollection().getPage(GroupPopoverContainer.class)
						.getProfileButtonCaption().trim()
						.equals(CAPTION_PROFILE));
	}

	/**
	 * Click profile button on Group Participants popover
	 *
	 * @step. ^I click profile button on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I click profile button on Group Participants popover$")
	public void IClickProfileButton() throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.clickProfileButton();
	}

	/**
	 * Verifies whether profile button tool tip is correct or not.
	 *
	 * @step. ^I see correct profile button tool tip on Group Participants
	 *        popover$
	 *
	 */
	@Then("^I see correct profile button tool tip on Group Participants popover$")
	public void ThenISeeCorrectProfileButtonToolTip() throws Exception {
		Assert.assertTrue(context.getPagesCollection()
				.getPage(GroupPopoverContainer.class).getProfileButtonToolTip()
				.equals(TOOLTIP_OPEN_YOUR_PROFILE));
	}

	/**
	 * Click Unblock button on Group Participants popover
	 *
	 * @step. ^I click Unblock button on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I click Unblock button on Group Participants popover$")
	public void IClickUnblockButton() throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.clickUnblockButton();
	}

	/**
	 * Verifies whether Unblock button tool tip is correct or not.
	 *
	 * @step. ^I see correct Unblock button tool tip on Group Participants
	 *        popover$
	 *
	 */
	@Then("^I see correct Unblock button tool tip on Group Participants popover$")
	public void ThenISeeCorrectUnblockButtonToolTip() throws Exception {
		Assert.assertTrue(context.getPagesCollection()
				.getPage(GroupPopoverContainer.class).getUnblockButtonToolTip()
				.equals(TOOLTIP_UNBLOCK));
	}

	/**
	 * Verifies whether Unblock button is visible on Group Participants popover
	 *
	 * @step. ^I see Unblock button on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I see Unblock button on Group Participants popover$")
	public void ISeeUnblockButton() throws Exception {
		final String openUnblockButtonMissingMessage = "Unblock button is not visible on Group Participants popover";
		final String openUnblockButtonWrongCaptionMessage = "Unblock button has wrong caption on Group Participants popover";
		Assert.assertTrue(openUnblockButtonMissingMessage,
				context.getPagesCollection().getPage(GroupPopoverContainer.class)
						.isUnblockButtonVisible());
		Assert.assertTrue(
				openUnblockButtonWrongCaptionMessage,
				context.getPagesCollection().getPage(GroupPopoverContainer.class)
						.getUnblockButtonCaption().trim()
						.equals(CAPTION_UNBLOCK));
	}

	/**
	 * Confirm Unblock from group chat by clicking UNBLOCK button
	 *
	 * @step. ^I confirm Unblock from group chat on Group Participants popover$
	 * @throws Exception
	 *
	 */
	@When("^I confirm Unblock from group chat on Group Participants popover$")
	public void IConfirmUnblockUser() throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.clickConfirmUnblockButton();
	}

	/**
	 * Click confirm connect button on Group Participants popover
	 *
	 * @step. ^I click confirm connect button on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I click confirm connect button on Group Participants popover$")
	public void IClickConfirmConnectButton() throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.clickConfirmConnectButton();
	}

	/**
	 * Click ignore connect button on Group Participants popover
	 *
	 * @step. ^I click ignore connect button on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I click ignore connect button on Group Participants popover$")
	public void IClickIgnoreConnectButton() throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.clickIgnoreConnectButton();
	}

	/**
	 * Remove participant from group chat by clicking "exit" button
	 *
	 * @step. ^I click Remove button on Group Participants popover$
	 * @throws Exception
	 *
	 */
	@When("^I click Remove button on Group Participants popover$")
	public void IRemoveUserFromGroupChat() throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.clickRemoveFromGroupChat();
	}

	/**
	 * Verifies whether Remove button is visible on Group Participants popover
	 *
	 * @step. ^I (do not )?see Remove button on Group Participants popover$
	 * @param doNot  is set to null if "do not" part does not exist
	 *
	 * @throws Exception
	 */
	@When("^I (do not )?see Remove button on Group Participants popover$")
	public void ISeeRemoveUserFromGroupChat(String doNot) throws Exception {
		if (doNot == null){
		 Assert.assertTrue("Remove Button is not visible on Group Participants Popover",
					context.getPagesCollection().getPage(GroupPopoverContainer.class).isRemoveButtonVisible());
		}
		else {
			Assert.assertTrue("Remove Button is visible on Group Participants Popover",
					context.getPagesCollection().getPage(GroupPopoverContainer.class).isRemoveButtonInvisible());
		}
	}

	/**
	 * Confirm removing from group chat by clicking REMOVE button
	 *
	 * @step. ^I confirm remove from group chat on Group Participants popover$
	 * @throws Exception
	 *
	 */
	@When("^I confirm remove from group chat on Group Participants popover$")
	public void IConfirmRemoveFromGroupChat() throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.confirmRemoveFromGroupChat();
	}

	/**
	 * Verifies that contact is displayed on Group Participants popover
	 *
	 * @step. ^I see (.*) displayed on Group Participants popover$
	 *
	 * @param contactsAliases
	 * @throws Exception
	 */
	@When("^I see (.*) displayed on Group Participants popover$")
	public void ISeeContactsDisplayed(String contactsAliases) throws Exception {
		List<String> contacts = CommonSteps.splitAliases(contactsAliases);
		for (String contact : contacts) {
			contact = context.getUserManager().replaceAliasesOccurences(contact,
					FindBy.NAME_ALIAS);
			Assert.assertTrue(context.getPagesCollection().getPage(
					GroupPopoverContainer.class).isParticipantVisible(contact));
		}
	}

	/**
	 * Verifies that contact is displayed in the verified section
	 *
	 * @param contactsAliases
	 * @param donot
	 * @throws Exception
	 * @step. ^I( do not)? see (.*) in verified section$
	 */
	@When("^I( do not)? see user (.*) in verified section$")
	public void ISeeUserInVerifiedSection(String donot, String contactsAliases) throws Exception {
		List<String> contacts = CommonSteps.splitAliases(contactsAliases);
		for (String contact : contacts) {
			contact = context.getUserManager().replaceAliasesOccurences(contact,
					FindBy.NAME_ALIAS);
			if (donot == null) {
				Assert.assertTrue(context.getPagesCollection().getPage(
						GroupPopoverContainer.class).isParticipantVerified(contact));
			} else {
				Assert.assertFalse(context.getPagesCollection().getPage(
						GroupPopoverContainer.class).isParticipantVerified(contact));
			}
		}
	}

	/**
	 * Set new title for conversation on Group Participants popover
	 *
	 * @step. I change group conversation title to (.*) on Group Participants
	 *        popover
	 *
	 * @param title
	 *            new conversation title string
	 */
	@When("^I change group conversation title to (.*) on Group Participants popover$")
	public void IChangeGroupChatTitleTo(String title) throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.setConversationTitle(title);
	}

	/**
	 * Verify conversation title on Group Participants popover
	 *
	 * @step. ^I see conversation title (.*) on Group Participants popover$
	 *
	 * @param title
	 *            expected title string
	 */
	@Then("^I see conversation title (.*) on Group Participants popover$")
	public void ISeeConversationTitle(String title) throws Exception {
		Assert.assertEquals(title,
				context.getPagesCollection().getPage(GroupPopoverContainer.class)
						.getConversationTitle());
	}

	/**
	 * Click on add people button
	 *
	 * @step. ^I click Add People button on Group Participants popover$
	 * @throws Exception
	 *
	 */
	@When("^I click Add People button on Group Participants popover$")
	public void IClickAddPeopleButton() throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.clickAddPeopleButton();
	}

	@When("^I see message that everyone is already added on Group Participants popover$")
	public void ISeeEveryoneAlreadyAddedMessage() throws Exception {
		Assert.assertTrue(context.getPagesCollection().getPage(
				GroupPopoverContainer.class).isEveryoneAlreadyAddedMessageShown());
	}

	@Then("I see (\\d+) participants in the Group Participants popover")
	public void ISeeXParticipants(int amount) throws Exception {
		assertThat("People information under conversation name",
				context.getPagesCollection().getPage(GroupPopoverContainer.class)
						.getPeopleCountInfo(), equalTo(String.valueOf(amount)
						+ " PEOPLE"));
		assertThat("Actual amount of people in popover", context.getPagesCollection()
						.getPage(GroupPopoverContainer.class).getPeopleCount(),
				equalTo(amount));
	}

	/**
	 * Input user name in search field
	 *
	 * @step. ^I input user name (.*) in search field on Group Participants
	 *        popover$
	 *
	 * @param name
	 * @throws Exception
	 */
	@When("^I input user name (.*) in search field on Group Participants popover$")
	public void ISearchForUser(String name) throws Exception {
		name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.searchForUser(name);
	}

	/**
	 * Select user found in search results
	 *
	 * @step. ^I select user (.*) from Group Participants popover search
	 *        results$
	 *
	 * @param user
	 * @throws Exception
	 */
	@When("^I select user (.*) from Group Participants popover search results$")
	public void ISelectUserFromSearchResults(String user) throws Exception {
		user = context.getUserManager().replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.selectUserFromSearchResult(user);
	}

	/**
	 * Selects the first X participants from Group Participants popover search
	 * results
	 *
	 * @step. ^I select the first (\\d+) participants from Group Participants
	 *        popover search results$
	 *
	 * @param amount
	 *            number of participants to select
	 * @throws Exception
	 */
	@When("^I select the first (\\d+) participants from Group Participants popover search results$")
	public void ISelectFirstUsersFromSearchResults(int amount) throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.selectUsersFromSearchResult(amount);
	}

	/**
	 * Creates conversation with selected users from on Group Participants
	 * popover
	 *
	 * @step. ^I choose to create conversation from Group Participants popover$
	 * @throws Exception
	 */
	@When("^I choose to create group conversation from Group Participants popover$")
	public void IChooseToCreateGroupConversation() throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.clickCreateGroupConversation();
	}

	/**
	 * Creates conversation with one user from on Group Participants popover
	 *
	 * @step. ^I click open conversation from Group Participants popover$
	 * @throws Exception
	 */
	@When("^I click open conversation from Group Participants popover$")
	public void IClickOpenConversation() throws Exception {
		context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.clickOpenConvButton();
	}

	/**
	 * Verifies whether add people button tool tip is correct or not.
	 *
	 * @throws java.lang.Exception
	 * @step. ^I see correct add people button tool tip$
	 *
	 */
	@Then("^I see correct add people button tool tip$")
	public void ThenISeeCorrectAddPeopleButtonToolTip() throws Exception {
		String tooltip = TOOLTIP_ADD_PEOPLE_TO_CONVERSATION + " ";
		if (WebAppExecutionContext.isCurrentPlatformWindows()) {
			tooltip = tooltip + SHORTCUT_ADD_PEOPLE_TO_CONVERSATION_WIN;
		} else {
			tooltip = tooltip + SHORTCUT_ADD_PEOPLE_TO_CONVERSATION_MAC;
		}
		assertThat(context.getPagesCollection().getPage(GroupPopoverContainer.class)
				.getAddPeopleButtonToolTip(), equalTo(tooltip));
	}

	/**
	 * Verifies whether leave conversation button tool tip is correct or not.
	 *
	 * @throws java.lang.Exception
	 * @step. ^I see correct leave conversation button tool tip$
	 *
	 */
	@Then("^I see correct leave conversation button tool tip$")
	public void ThenISeeCorrectLeaveConversationButtonToolTip()
			throws Exception {
		Assert.assertTrue(context.getPagesCollection()
				.getPage(GroupPopoverContainer.class)
				.getLeaveGroupChatButtonToolTip()
				.equals(TOOLTIP_LEAVE_CONVERSATION));
	}

	/**
	 * Verifies whether rename conversation button tool tip is correct or not.
	 *
	 * @throws java.lang.Exception
	 * @step. ^I see correct rename conversation button tool tip$
	 *
	 */
	@Then("^I see correct rename conversation button tool tip$")
	public void ThenISeeCorrectRenameConversationButtonToolTip()
			throws Exception {
		Assert.assertTrue(context.getPagesCollection()
				.getPage(GroupPopoverContainer.class)
				.getRenameConversationToolTip()
				.equals(TOOLTIP_CHANGE_CONVERSATION_NAME));
	}

	/**
	 * Verifies whether remove from group button tool tip is correct or not.
	 *
	 * @step. ^I see correct remove from group button tool tip on Group
	 *        Participants popover$
	 *
	 */
	@Then("^I see correct remove from group button tool tip on Group Participants popover$")
	public void ThenISeeCorrectRemoveFromGroupChatButtonToolTip()
			throws Exception {
		Assert.assertTrue(context.getPagesCollection()
				.getPage(GroupPopoverContainer.class)
				.getRemoveFromGroupChatButtonToolTip()
				.equals(TOOLTIP_REMOVE_FROM_CONVERSATION));
	}

	/**
	 * Compares if name on Single User Profile popover on Group Participants
	 * popover is same as expected
	 *
	 * @throws java.lang.Exception
	 * @step. ^I see username (.*) on Single User Profile popover on Group
	 *        Participants popover$
	 *
	 * @param name
	 *            user name string
	 */
	@When("^I see username (.*) on Group Participants popover$")
	public void IseeUserNameOnUserProfilePage(String name) throws Exception {
		name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		Assert.assertEquals(name,
				context.getPagesCollection().getPage(GroupPopoverContainer.class)
						.getUserName());
	}

	/**
	 * Verifies whether the users avatar exists on the popover
	 *
	 * @throws java.lang.Exception
	 * @step. ^I see the users avatar on Group Participants User Profile
	 *        popover$
	 *
	 */
	@When("^I see an avatar on Group Participants popover")
	public void IseeAvatarOnUserProfilePage() throws Exception {
		Assert.assertTrue(context.getPagesCollection().getPage(
				GroupPopoverContainer.class).isAvatarVisible());
	}

	/**
	 * Verifies Mail is visible on Group Participants popover or not
	 *
	 * @param not
	 *            is set to null if "do not" part does not exist
	 * @param mailAlias
	 *            the mail alias to test for when mail alias is shown
	 * @step. ^I( do not)? see Mail on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I( do not)? see Mail (.*)on Group Participants popover$")
	public void ISeeMailOfUser(String not, String mailAlias) throws Exception {
		mailAlias = mailAlias.trim();
		GroupPopoverContainer groupPopoverPage = context.getPagesCollection()
				.getPage(GroupPopoverContainer.class);
		if (not == null) {
			if ("".equals(mailAlias)) {
				// no mail given. just check if any text is in mail field
				Assert.assertFalse(groupPopoverPage.getUserMail().isEmpty());
			} else {
				// mail given. strict check for mail
				String email = null;
				try {
					email = context.getUserManager().findUserByEmailOrEmailAlias(mailAlias)
							.getEmail().toUpperCase();
				} catch (NoSuchUserException e) {
					// Ignore silently
				}
				Assert.assertTrue(groupPopoverPage.getUserMail().equals(email));
			}
		} else {
			// check for no mail, ignores the given mail alias
			Assert.assertTrue(groupPopoverPage.getUserMail().isEmpty());
		}

	}

	/**
	 * Verifies whether click on mail would open mail client or not.
	 *
	 * @throws java.lang.Exception
	 * @step. ^Would open mail client when clicking mail on Group Participants
	 *        popover$
	 *
	 */
	@Then("^Would open mail client when clicking mail on Group Participants popover$")
	public void ThenISeeThatClickOnMailWouldOpenMailClient() throws Exception {
		Assert.assertTrue(context.getPagesCollection()
				.getPage(GroupPopoverContainer.class).getMailHref()
				.contains(MAILTO));

	}

	/**
	 * Verifies Pending text box is visible on Group Participant popover
	 *
	 * @step. ^I see Pending text box on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I see Pending text box on Group Participants popover$")
	public void ISeePendingTextBox() throws Exception {
		Assert.assertTrue(context.getPagesCollection().getPage(
				GroupPopoverContainer.class).isPendingTextBoxVisible());
	}

	/**
	 * Verifies whether open conversation button tool tip is correct or not.
	 *
	 * @throws java.lang.Exception
	 * @step. ^I see correct leave conversation button tool tip on Group
	 *        Participants popover$
	 *
	 */
	@Then("^I see correct open conversation button tool tip on Group Participants popover$")
	public void ThenISeeCorrectOpenConversationButtonToolTip() throws Exception {
		Assert.assertTrue(context.getPagesCollection()
				.getPage(GroupPopoverContainer.class)
				.getOpenConvButtonToolTip().equals(TOOLTIP_OPEN_CONVERSATION));
	}

	/**
	 * Verifies whether open conversation button is visible on Group
	 * Participants popover
	 *
	 * @step. ^I see open conversation button on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I see open conversation button on Group Participants popover$")
	public void ISeeOpenConversationButton() throws Exception {
		final String openConvButtonMissingMessage = "Open conversation button is not visible on Group Participants popover";
		final String openConvButtonWrongCaptionMessage = "Open conversation button has wrong caption on Group Participants popover";
		Assert.assertTrue(openConvButtonMissingMessage, context.getPagesCollection()
				.getPage(GroupPopoverContainer.class).isOpenConvButtonVisible());
		Assert.assertTrue(
				openConvButtonWrongCaptionMessage,
				context.getPagesCollection().getPage(GroupPopoverContainer.class)
						.getOpenConvButtonCaption().trim()
						.equals(CAPTION_OPEN_CONVERSATION));
	}
}
