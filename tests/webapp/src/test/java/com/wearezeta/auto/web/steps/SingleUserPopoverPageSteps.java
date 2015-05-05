package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.popovers.SingleUserPopoverContainer;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

public class SingleUserPopoverPageSteps {

	private static final String MAILTO = "mailto:";
	private static final String CAPTION_PENDING = "Pending";
	private static final String CAPTION_OPEN_CONVERSATION = "Open Conversation";
	private static final String TOOLTIP_PENDING = "Pending";
	private static final String TOOLTIP_OPEN_CONVERSATION = "Open conversation";

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verify that Single User Profile popover is visible or not
	 *
	 * @step. ^I( do not)? see Single User Profile popover$
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part is not present
	 * 
	 * @throws Exception
	 *
	 */
	@When("^I( do not)? see Single User Profile popover$")
	public void ISeeSingleUserPopup(String shouldNotBeVisible) throws Exception {
		if (shouldNotBeVisible == null) {
			PagesCollection.popoverPage.waitUntilVisibleOrThrowException();
		} else {
			PagesCollection.popoverPage.waitUntilNotVisibleOrThrowException();
		}
	}

	/**
	 * Creates conversation with selected users from Single User Profile popover
	 *
	 * @step. ^I choose to create conversation from Single User Profile popover$
	 * @throws Exception
	 */
	@When("^I choose to create conversation from Single User Profile popover$")
	public void IChooseToCreateConversationFromSingleUserPopover()
			throws Exception {
		((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.clickCreateGroupConversation();
	}

	/**
	 * Click on add people button on Single User Profile popover
	 *
	 * @step. ^I click Add People button on Single User Profile popover$
	 * @throws Exception
	 *
	 */
	@When("^I click Add People button on Single User Profile popover$")
	public void IClickAddPeopleButton() throws Exception {
		((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.clickAddPeopleButton();
	}

	/**
	 * Input user name in search field on Single User Profile popover
	 *
	 * @step. ^I input user name (.*) in search field on Single User Profile
	 *        popover$
	 *
	 * @param name
	 * @throws Exception
	 */
	@When("^I input user name (.*) in search field on Single User Profile popover$")
	public void ISearchForUser(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.searchForUser(name);
	}

	/**
	 * Select user found in search results
	 *
	 * @step. ^I select (.*) from Single User Profile popover search results$
	 *
	 * @param user
	 * @throws Exception
	 */
	@When("^I select (.*) from Single User Profile popover search results$")
	public void ISelectUserFromSearchResults(String user) throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.selectUserFromSearchResult(user);
	}

	/**
	 * Compares if name on Single User Profile popover is same as expected
	 *
	 * @throws java.lang.Exception
	 * @step. ^I see username (.*) on Single User Profile popover$
	 *
	 * @param name
	 *            user name string
	 */
	@When("^I see username (.*) on Single User Profile popover$")
	public void IseeUserNameOnUserProfilePage(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		Assert.assertEquals(name,
				((SingleUserPopoverContainer) PagesCollection.popoverPage)
						.getUserName());
	}

	/**
	 * Verifies whether the users avatar exists on the popover
	 *
	 * @throws java.lang.Exception
	 * @step. ^I see the users avatar on Single User Profile popover$
	 *
	 */
	@When("^I see an avatar on Single User Profile popover$")
	public void IseeAvatarOnUserProfilePage() throws Exception {
		Assert.assertTrue(((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.isAvatarVisible());
	}

	/**
	 * Verifies whether Add People button exists on the popover
	 *
	 * @step. ^I see Add people button on Single User Profile popover$
	 * @throws Exception
	 *
	 */
	@Then("^I see Add people button on Single User Profile popover$")
	public void ISeeAddButton() throws Exception {
		Assert.assertTrue(((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.isAddButtonVisible());
	}

	/**
	 * Verifies whether Block button exists on the popover
	 *
	 * @step. ^I see Block button on Single User Profile popover$
	 *
	 */
	@Then("^I see Block button on Single User Profile popover$")
	public void ISeeBlockButton() {
		Assert.assertTrue(((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.isBlockButtonVisible());
	}

	/**
	 * Click Block button on Single User Profile popover
	 *
	 * @step. ^I click Block button on Single User Profile popover$
	 *
	 */
	@Then("^I click Block button on Single User Profile popover$")
	public void IClickBlockButton() {
		((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.clickBlockButton();
	}

	/**
	 * Confirm blocking user action on Single User Profile popover
	 *
	 * @step. ^I confirm user blocking on Single User Profile popover$
	 *
	 */
	@And("^I confirm user blocking on Single User Profile popover$")
	public void IConfirmBlockUser() {
		((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.clickConfirmButton();
	}

	/**
	 * Verifies Mail is visible on Single Participant popover or not
	 *
	 * @param not
	 *            * is set to null if "do not" part does not exist
	 * @step. ^I( do not)? see Mail on Single Participant popover$
	 *
	 * @throws Exception
	 */
	@Then("^I( do not)? see Mail on Single Participant popover$")
	public void ISeeMailOfUser(String not) throws Exception {
		if (not == null) {
			Assert.assertFalse(((SingleUserPopoverContainer) PagesCollection.popoverPage)
					.getUserMail().isEmpty());
		} else {
			Assert.assertTrue(((SingleUserPopoverContainer) PagesCollection.popoverPage)
					.getUserMail().isEmpty());
		}

	}

	/**
	 * Verifies whether Pending button is visible on Single Participant popover
	 *
	 * @step. ^I see Pending button on Single Participant popover$
	 *
	 * @throws Exception
	 */
	@Then("^I see Pending button on Single Participant popover$")
	public void ISeePendingButton() throws Exception {
		final String pendingButtonMissingMessage = "Pending button is not visible on Single Participant popover";
		Assert.assertTrue(pendingButtonMissingMessage,
				((SingleUserPopoverContainer) PagesCollection.popoverPage)
						.isPendingButtonVisible());
		Assert.assertTrue(
				pendingButtonMissingMessage,
				((SingleUserPopoverContainer) PagesCollection.popoverPage)
						.getPendingButtonCaption().trim()
						.equalsIgnoreCase(CAPTION_PENDING));
	}

	/**
	 * Click Pending button on Single Participant popover
	 *
	 * @step. ^I click Pending button on Single Participant popover$
	 *
	 * @throws Exception
	 */
	@Then("^I click Pending button on Single Participant popover$")
	public void IClickPendingButton() throws Exception {
		((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.clickPendingButton();
	}

	/**
	 * Verifies whether open conversation button is visible on Single
	 * Participant popover
	 *
	 * @step. ^I see open conversation button on Single Participant popover$
	 *
	 * @throws Exception
	 */
	@Then("^I see open conversation button on Single Participant popover$")
	public void ISeeOpenConversationButton() throws Exception {
		final String openConvMissingMessage = "Open conversation button is not visible on Single Participant popover";
		Assert.assertTrue(openConvMissingMessage,
				((SingleUserPopoverContainer) PagesCollection.popoverPage)
						.isOpenConvButtonVisible());
		Assert.assertTrue(
				openConvMissingMessage,
				((SingleUserPopoverContainer) PagesCollection.popoverPage)
						.getOpenConvButtonCaption().trim()
						.equalsIgnoreCase(CAPTION_OPEN_CONVERSATION));
	}

	/**
	 * Verifies Pending text box is visible on Single Participant popover
	 *
	 * @step. ^I see Pending text box on Single Participant popover$
	 *
	 * @throws Exception
	 */
	@Then("^I see Pending text box on Single Participant popover$")
	public void ISeePendingTextBox() throws Exception {
		Assert.assertTrue(((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.isPendingTextBoxVisible());
	}

	/**
	 * Creates conversation with one user from on Single Participant popover
	 *
	 * @step. ^I click open conversation from Single Participant popover$
	 * @throws Exception
	 */
	@When("^I click open conversation from Single Participant popover$")
	public void IClickOpenConversation() throws Exception {
		((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.clickOpenConvButton();
	}

	/**
	 * Verifies whether open conversation button tool tip is correct or not.
	 *
	 * @step. ^I see correct open conversation button tool tip on Single
	 *        Participant popover$
	 *
	 */
	@Then("^I see correct open conversation button tool tip on Single Participant popover$")
	public void ThenISeeCorrectOpenConvButtonToolTip() {
		Assert.assertTrue(((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.getOpenConvButtonToolTip().equals(TOOLTIP_OPEN_CONVERSATION));
	}

	/**
	 * @throws java.lang.Exception
	 *             * Verifies whether click on mail would open mail client or
	 *             not.
	 *
	 * @step. ^Click on mail on Single Participant popover would open mail
	 *        client$
	 *
	 */
	@Then("^Would open mail client when clicking mail on Single Participant popover$")
	public void ThenISeeThatClickOnMailWouldOpenMailClient() throws Exception {
		Assert.assertTrue(((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.getMailHref().contains(MAILTO));

	}

	/**
	 * Verifies whether pending button tool tip is correct or not.
	 *
	 * @step. ^I see correct pending button tool tip on Single Participant
	 *        popover$
	 *
	 */
	@Then("^I see correct pending button tool tip on Single Participant popover$")
	public void ThenISeeCorrectPendingButtonToolTip() {
		Assert.assertTrue(((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.getPendingButtonToolTip().equals(TOOLTIP_PENDING));
	}
}
