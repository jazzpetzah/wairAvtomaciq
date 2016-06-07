package com.wearezeta.auto.web.steps;

import java.util.List;

import com.wearezeta.auto.web.pages.ConversationPage;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.ContactListPage;
import com.wearezeta.auto.web.pages.StartUIPage;
import com.wearezeta.auto.web.pages.external.GoogleLoginPage;
import com.wearezeta.auto.web.pages.popovers.BringYourFriendsPopoverPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class StartUIPageSteps {

    private final TestContext context;

    private static String rememberedUser;

    public StartUIPageSteps() {
        this.context = new TestContext();
    }

    public StartUIPageSteps(TestContext context) {
        this.context = context;
    }

	/**
	 * Verifies the presence of the People Picker
	 *
	 * @step. ^I see [Pp]eople [Pp]icker$
	 *
	 * @throws Exception
	 */
	@When("^I see [Pp]eople [Pp]icker$")
	public void ISeeStartUI() throws Exception {
		assertTrue("Start UI is NOT visible", context.getPagesCollection().getPage(StartUIPage.class).isVisible());
	}

	/**
	 * Selects user from search results in People Picker
	 *
	 * @step. ^I select (.*) from People Picker results$
	 *
	 * @param user
	 *            user name or email
	 * @throws Exception
	 */
	@When("^I select (.*) from People Picker results$")
	public void ISelectUserFromStartUIResults(String user)
			throws Exception {
		user = context.getUserManager().replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		context.getPagesCollection().getPage(StartUIPage.class)
				.selectUserFromSearchResult(user);
	}

	/**
	 * Workaround for bug WEBAPP-1386
	 *
	 * @step. ^I wait for the search field of People Picker to be empty$
	 *
	 * @throws Exception
	 */
	@When("^I wait for the search field of People Picker to be empty$")
	public void IWaitForSearchFieldToBeEmpty() throws Exception {
		context.getPagesCollection().getPage(StartUIPage.class)
				.waitForSearchFieldToBeEmpty();
	}

	/**
	 * Input user name/email in search field of People Picker
	 *
	 * @step. ^I type (.*) in search field of People Picker$
	 *
	 * @param nameOrEmail
	 * @throws Exception
	 */
	@When("^I type (.*) in search field of People Picker$")
	public void ISearchForUser(String nameOrEmail) throws Exception {
		nameOrEmail = context.getUserManager().replaceAliasesOccurences(nameOrEmail,
				FindBy.NAME_ALIAS);
		nameOrEmail = context.getUserManager().replaceAliasesOccurences(nameOrEmail,
				FindBy.EMAIL_ALIAS);
		// adding spaces to ensure trimming of input
		context.getPagesCollection().getPage(StartUIPage.class).searchForUser(
				" " + nameOrEmail + " ");
	}

	/**
	 * Verify if user is found by Search in People Picker
	 *
	 * @step. ^I( do not)? see user (.*) found in People Picker$
	 *
	 * @param donot
	 *            if null method returns true if found otherwise true if not
	 *            found
	 * @param name
	 *            user name string
	 * @throws Exception
	 */
	@When("^I( do not)? see user (.*) found in People Picker$")
	public void ISeeUserFoundInStartUI(String donot, String name)
			throws Exception {
		name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);

		if (donot == null) {
			Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isUserFound(name));
		} else {
			Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isUserNotFound(name));
		}
	}

	/**
	 * Click on the X button next to the suggested contact
	 *
	 * @step. ^I remove user (.*) from suggestions in People Picker$
	 *
	 * @param contact
	 *            name of contact
	 * @throws Exception
	 */
	@When("^I remove user (.*) from suggestions in People Picker$")
	public void IClickRemoveButton(String contact) throws Exception {
		contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		context.getPagesCollection().getPage(StartUIPage.class)
				.clickRemoveButtonOnSuggestion(contact);
	}

	/**
	 * Click on the + button next to the suggested contact
	 *
	 * @step. ^I make a connection request for user (.*) directly from People
	 *        Picker$
	 *
	 * @param contact
	 *            name of contact
	 * @throws Exception
	 */
	@When("^I make a connection request for user (.*) directly from People Picker$")
	public void IClickPlusButton(String contact) throws Exception {
		contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		context.getPagesCollection().getPage(StartUIPage.class)
				.clickPlusButtonOnSuggestion(contact);
	}

	/**
	 * Click X button to close People Picker page
	 *
	 * @step. ^I close People Picker page$
	 *
	 * @throws Exception
	 */
	@When("^I close People Picker$")
	public void ICloseStartUI() throws Exception {
		context.getPagesCollection().getPage(StartUIPage.class).closeStartUI();
	}

	/**
	 * Clicks on user found by search to open connect dialog
	 *
	 * @step. ^I click on (not connected|pending) user (.*) found in People
	 *        Picker$
	 *
	 * @param userType
	 *            either "not connected" or "pending"
	 * @param name
	 *            user name string
	 *
	 * @throws Exception
	 */
	@When("^I click on (not connected|pending) user (.*) found in People Picker$")
	public void IClickNotConnecteUserFoundInStartUI(String userType,
			String name) throws Exception {
		name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		if (userType.equalsIgnoreCase("not connected")) {
			context.getPagesCollection().getPage(StartUIPage.class)
					.clickNotConnectedUserName(name);
		} else if (userType.equalsIgnoreCase("pending")) {
			context.getPagesCollection().getPage(StartUIPage.class)
					.clickPendingUserName(name);
		}
	}

	@When("^I click on remembered (not connected|pending) contact found in People Picker$")
	public void IClickRememberedNotConnecteUserFoundInStartUI(String userType) throws Exception {
		rememberedUser = context.getUserManager().replaceAliasesOccurences(rememberedUser, FindBy.NAME_ALIAS);
		if (userType.equalsIgnoreCase("not connected")) {
			context.getPagesCollection().getPage(StartUIPage.class)
					.clickNotConnectedUserName(rememberedUser);
		} else if (userType.equalsIgnoreCase("pending")) {
			context.getPagesCollection().getPage(StartUIPage.class)
					.clickPendingUserName(rememberedUser);
		}
	}

	/**
	 * Creates conversation with users selected in People Picker
	 *
	 * @step. ^I choose to create conversation from People Picker$
	 * @throws Exception
	 */
	@When("^I choose to create conversation from People Picker$")
	public void IChooseToCreateConversationFromStartUI() throws Exception {
		context.getPagesCollection().getPage(StartUIPage.class)
				.createConversation();
	}

	@Then("^I see more than (\\d+) suggestions? in people picker$")
	public void ISeeMoreThanXSuggestionsInStartUI(int count)
			throws Exception {
		assertThat("people suggestions",
				context.getPagesCollection().getPage(StartUIPage.class)
						.getNumberOfSuggestions(), greaterThan(count));
	}

	/**
	 * Verify whether Bring Your Friends or Invite People button is visible
	 *
	 * @step. ^I see Bring Your Friends or Invite People button$
	 *
	 * @throws Exception
	 */
	@When("^I see Bring Your Friends or Invite People button$")
	public void ISeeSendInvitationButton() throws Exception {
		context.getPagesCollection().getPage(StartUIPage.class)
				.waitUntilBringYourFriendsOrInvitePeopleButtonIsVisible();
	}

	/**
	 * Verify whether Gmail Import button is visible on People Picker page
	 *
	 * @step. ^I do not see Gmail Import button on People Picker page$
	 *
	 * @throws Exception
	 */
	@When("^I do not see Gmail Import button on People Picker page$")
	public void IDoNotSeeGmailImportButton() throws Exception {
		context.getPagesCollection().getPage(BringYourFriendsPopoverPage.class)
				.waitUntilGmailImportButtonIsNotVisible();
	}

	/**
	 * Click button to bring friends from Gmail
	 *
	 * @step. ^I click button to bring friends from Gmail$
	 *
	 */
	@And("^I click button to bring friends from Gmail$")
	public void IClickButtonToBringFriendsFromGmail() throws Exception {
		context.getPagesCollection().getPage(StartUIPage.class)
				.clickBringFriendsFromGmailButton();
	}

	/**
	 * Verifies whether Google login prompt is visible
	 *
	 * @step. ^I see Google login popup$
	 *
	 * @throws Exception
	 */
	@And("^I see Google login popup$")
	public void ISeeGoogleLoginPopup() throws Exception {
		context.getPagesCollection().getPage(StartUIPage.class)
				.switchToGooglePopup();
	}

	/**
	 * Enter gmail login and password into corresponding window
	 *
	 * @step. ^I sign up at Google with email (.*) and password (.*)$"
	 *
	 * @param email
	 * @param password
	 * @throws Exception
	 */
	@When("^I sign up at Google with email (.*) and password (.*)$")
	public void ISignUpAtGoogleWithEmail(String email, String password) throws Exception {
		GoogleLoginPage googleLoginPage = context.getPagesCollection()
                    .getPage(GoogleLoginPage.class);
                // sometimes Google already shows the email
                googleLoginPage.setEmail(email);
                // sometimes google shows a next button and you have to enter the
                // password separately
                if (googleLoginPage.hasNextButton()) {
                    googleLoginPage.clickNext();
                }
                googleLoginPage.setPassword(password);
                googleLoginPage.clickSignInWithWindowSwitch();
	}

	/**
	 * Click Bring Your Friends or Invite People button on People Picker page
	 *
	 * @step. ^I click Bring Your Friends or Invite People button$
	 *
	 * @throws Exception
	 */
	@When("^I click Bring Your Friends or Invite People button$")
	public void IClickBringYourFriendsOrInvitePeopleButton() throws Exception {
		context.getPagesCollection().getPage(StartUIPage.class)
				.clickBringYourFriendsOrInvitePeopleButton();
	}

	/**
	 * Click Call button on People Picker page
	 *
	 * @step. ^I click Call button on People Picker page$
	 *
	 * @throws Exception
	 */
	@When("^I click Call button on People Picker page$")
	public void IClickCallButton() throws Exception {
		context.getPagesCollection().getPage(StartUIPage.class).clickCallButton();
	}

	/**
	 * Closes and opens People Picker until Top People list is visible on People
	 * Picker page
	 *
	 * @step. ^I wait till Top People list appears$
	 *
	 * @throws Exception
	 */
	@When("^I wait till Top People list appears$")
	public void IwaitTillTopPeopleListAppears() throws Exception {
		if (!context.getPagesCollection().getPage(StartUIPage.class)
				.isTopPeopleLabelVisible())
			context.getPagesCollection().getPage(StartUIPage.class).closeStartUI();
		context.getPagesCollection().getPage(ContactListPage.class).openStartUI();
		Assert.assertTrue("Top People list is not shown", context.getPagesCollection()
				.getPage(StartUIPage.class).isTopPeopleLabelVisible());
	}

	/**
	 * Selects users from Top People in People Picker
	 *
	 * @step. ^I select (.*) from Top People$
	 *
	 * @param namesOfTopPeople
	 *            comma separated list of names of top people to select
	 * @throws Exception
	 */
	@When("^I select (.*) from Top People$")
	public void ISelectUsersFromTopPeople(String namesOfTopPeople)
			throws Exception {
		for (String alias : CommonSteps.splitAliases(namesOfTopPeople)) {
			final String userName = context.getUserManager().findUserByNameOrNameAlias(alias)
					.getName();
			context.getPagesCollection().getPage(StartUIPage.class)
					.clickNameInTopPeople(userName);
		}
	}

	private static List<String> selectedTopPeople;

	public static List<String> getSelectedTopPeople() {
		return selectedTopPeople;
	}

	@When("^I remember user names selected in Top People$")
	public void IRememberUserNamesSelectedInTopPeople() throws Exception {
		selectedTopPeople = context.getPagesCollection().getPage(StartUIPage.class).getNamesOfSelectedTopPeople();
	}

    @When("^I remember first suggested user$")
    public void IRememberSuggestedUser() throws Exception {
        List<String> suggestedUsers = context.getPagesCollection().getPage(StartUIPage.class).getNamesOfSuggestedContacts();
		rememberedUser = suggestedUsers.get(0);
    }

    @When("^I( do not)? see (.*) remembered user in People Picker$")
    public void ISeeRememberedUserInStartUI(String donot, String count) throws Exception {
        if (donot != null && count.contains("first")) {
            Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isUserNotFound(rememberedUser));
		} else if (count.contains("first")) {
            Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isUserFound(rememberedUser));
        }
    }

    @When("^I remove first remembered user from suggestions in People Picker$")
    public void IRemoveFirstRememberedUser() throws Exception {
		rememberedUser = context.getUserManager().replaceAliasesOccurences(rememberedUser, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(StartUIPage.class)
                .clickRemoveButtonOnSuggestion(rememberedUser);
    }

    @When("^I make a connection request for remembered user directly from People Picker$")
    public void IMakeAConnectionRequestForSecondRememberedUser() throws Exception {
		rememberedUser = context.getUserManager().replaceAliasesOccurences(rememberedUser, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(StartUIPage.class)
                .clickPlusButtonOnSuggestion(rememberedUser);
    }

    @When("^I( do not)? see Contact list with remembered user$")
    public void ISeeContactListWithSecondRememberedUser(String donot) throws Exception {
		if (donot == null) {
			Assert.assertTrue(context.getPagesCollection().getPage(ContactListPage.class)
					.isConvoListEntryWithNameExist(rememberedUser));
		} else {
			Assert.assertFalse(context.getPagesCollection().getPage(ContactListPage.class)
				.isConvoListEntryWithNameExist(rememberedUser));
		}
    }

    @When("^I open remembered users conversation$")
    public void IOpenSecondRememberedUsersConversation() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class).openConversation(rememberedUser);
    }

    @When("^I see connecting message in conversation with remembered contact$")
    public void ISeeConnectingMsgFromSecondRememberedUser() throws Exception {
        assertThat("User name", context.getPagesCollection().getPage(ConversationPage.class).getConnectedMessageUser(),
                equalTo(rememberedUser));
        assertThat("Label", context.getPagesCollection().getPage(ConversationPage.class).getConnectedMessageLabel(),
                equalTo("CONNECTING"));
    }

	/**
	 * Verifies whether Search is opened on People Picker Page
	 *
	 * @step. I see Search is opened$
	 *
	 * @throws Exception
	 */
	@Then("^I see Search is opened$")
	public void ISeeSearchIsOpened() throws Exception {
		final String searchMissingMessage = "Search is not visible on Start UI Page";
		Assert.assertTrue(searchMissingMessage,
				context.getPagesCollection().getPage(StartUIPage.class)
						.isSearchOpened());
	}

	/**
	 * Verify if group conversation is found by Search in People Picker
	 *
	 * @step. ^I( do not)? see group conversation (.*) found in People Picker$
	 *
	 * @param donot
	 *            if null method returns true if found otherwise true if not
	 *            found
	 * @param name
	 *            group conversation name string
	 * @throws Exception
	 */
	@When("^I( do not)? see group conversation (.*) found in People Picker$")
	public void ISeeGroupFoundInStartUI(String donot, String name)
			throws Exception {

		if (donot == null) {
			Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isGroupConversationFound(name));
		} else {
			Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isGroupConversationNotFound(name));
		}
	}

	/**
	 * Verify if (\\d+) people is/are shown in People Picker
	 *
	 * @step. ^I see (\\d+) people in Top people list$
	 *
	 */
	@Then("^I see (\\d+) people in Top people list$")
	public void ISeeXPeopleInTopPeopleList(int count) throws Exception {
		assertThat("people suggestions",
				context.getPagesCollection().getPage(StartUIPage.class)
						.getNumberOfTopPeople(), equalTo(count));
	}

	/**
	 * Click Video Call button on People Picker page
	 *
	 * @step. ^I click Video Call button on People Picker page$
	 *
	 * @throws Exception
	 */
	@When("^I click Video Call button on People Picker page$")
	public void IClickVideoCallButton() throws Exception {
		context.getPagesCollection().getPage(StartUIPage.class).clickVideoCallButton();
	}

	/**
	 * Verify if Video Call button is shown on People Picker page
	 *
	 * @step. ^I( do not)? see Video Call button on People Picker page$
	 *
	 * @throws Exception
	 */
	@And("^I( do not)? see Video Call button on People Picker page$")
	public void ISeeVideoCallButton(String doNot) throws Exception {
		if (doNot == null) {
			final String searchMissingMessage = "Video Call button is not shown on Start UI Page";
			Assert.assertTrue(searchMissingMessage,
					context.getPagesCollection().getPage(StartUIPage.class)
							.isVideoCallButtonVisible());
		}
		else
		{
			final String searchMissingMessage = "Video Call button is shown on Start UI Page";
			Assert.assertTrue(searchMissingMessage,
                    context.getPagesCollection().getPage(StartUIPage.class)
					.isVideoCallButtonNotVisible());
		}

	}
}
