package com.wearezeta.auto.web.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;

import java.awt.image.BufferedImage;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.popovers.SingleUserPopoverContainer;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.apache.log4j.Logger;
import org.junit.Assert;

public class SingleUserPopoverPageSteps {

	public static final Logger log = ZetaLogger
			.getLog(SingleUserPopoverPageSteps.class.getSimpleName());

	private static final String MAILTO = "mailto:";
	private static final String CAPTION_PENDING = "Pending";
	private static final String CAPTION_OPEN_CONVERSATION = "Open Conversation";
	private static final String TOOLTIP_PENDING = "Pending";
	private static final String TOOLTIP_OPEN_CONVERSATION = "Open conversation";

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

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
			webappPagesCollection.getPage(SingleUserPopoverContainer.class)
					.waitUntilVisibleOrThrowException();
		} else {
			webappPagesCollection.getPage(SingleUserPopoverContainer.class)
					.waitUntilNotVisibleOrThrowException();
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
		webappPagesCollection.getPage(SingleUserPopoverContainer.class)
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
		webappPagesCollection.getPage(SingleUserPopoverContainer.class)
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
		webappPagesCollection.getPage(SingleUserPopoverContainer.class)
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
		webappPagesCollection.getPage(SingleUserPopoverContainer.class)
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
				webappPagesCollection.getPage(SingleUserPopoverContainer.class)
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
		Assert.assertTrue(webappPagesCollection.getPage(
				SingleUserPopoverContainer.class).isAvatarVisible());
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
		Assert.assertTrue(webappPagesCollection.getPage(
				SingleUserPopoverContainer.class).isAddButtonVisible());
	}

	/**
	 * Verifies whether Block button exists on the popover
	 *
	 * @step. ^I see Block button on Single User Profile popover$
	 *
	 */
	@Then("^I see Block button on Single User Profile popover$")
	public void ISeeBlockButton() throws Exception {
		Assert.assertTrue(webappPagesCollection.getPage(
				SingleUserPopoverContainer.class).isBlockButtonVisible());
	}

	/**
	 * Click Block button on Single User Profile popover
	 *
	 * @step. ^I click Block button on Single User Profile popover$
	 *
	 */
	@Then("^I click Block button on Single User Profile popover$")
	public void IClickBlockButton() throws Exception {
		webappPagesCollection.getPage(SingleUserPopoverContainer.class)
				.clickBlockButton();
	}

	/**
	 * Confirm blocking user action on Single User Profile popover
	 *
	 * @step. ^I confirm user blocking on Single User Profile popover$
	 *
	 */
	@And("^I confirm user blocking on Single User Profile popover$")
	public void IConfirmBlockUser() throws Exception {
		webappPagesCollection.getPage(SingleUserPopoverContainer.class)
				.clickConfirmButton();
	}

	/**
	 * Verifies Mail is correct on Single Participant popover or not
	 *
	 * @param not
	 *            * is set to null if "do not" part does not exist
	 * @param userAlias
	 *            name of user
	 * @step. ^I( do not)? see Mail of user (.*) on Single Participant popover$
	 *
	 * @throws Exception
	 */
	@Then("^I( do not)? see Mail of user (.*) on Single Participant popover$")
	public void ISeeMailOfUser(String not, String userAlias) throws Exception {
		if (not == null) {
			ClientUser user = usrMgr.findUserBy(userAlias, FindBy.NAME_ALIAS);
			assertThat(
					webappPagesCollection
							.getPage(SingleUserPopoverContainer.class)
							.getUserMail().toLowerCase(),
					equalTo(user.getEmail()));
		} else {
			assertThat(
					webappPagesCollection.getPage(
							SingleUserPopoverContainer.class).getUserMail(),
					equalTo(""));
		}
	}

	/**
	 * Verifies avatar is correct on Single Participant popover or not
	 *
	 * @param not
	 *            * is set to null if "do not" part does not exist
	 * @param avatar
	 *            file name of image file in resources/images
	 * @param userAlias
	 *            name of user
	 * @step. ^I( do not)? see avatar of user (.*) on Single Participant
	 *        popover$
	 *
	 * @throws Exception
	 */
	@Then("^I( do not)? see avatar (.*) of user (.*) on Single Participant popover$")
	public void ISeeAvatarOfUser(String not, String avatar, String userAlias)
			throws Exception {
		final String picturePath = WebCommonUtils.getFullPicturePath(avatar);
		BufferedImage expectedAvatar = ImageUtil.readImageFromFile(picturePath);
		BufferedImage actualAvatar = webappPagesCollection.getPage(
				SingleUserPopoverContainer.class).getAvatar();
		double overlapScore = ImageUtil.getOverlapScore(actualAvatar,
				expectedAvatar,
				ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
		log.info("Overlap score: " + overlapScore);
		if (not == null) {
			assertThat("Overlap score of image comparsion", overlapScore,
					greaterThanOrEqualTo(0.3));
		} else {
			assertThat("Overlap score of image comparsion", overlapScore,
					lessThan(0.3));
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
		Assert.assertTrue(pendingButtonMissingMessage, webappPagesCollection
				.getPage(SingleUserPopoverContainer.class)
				.isPendingButtonVisible());
		Assert.assertTrue(
				pendingButtonMissingMessage,
				webappPagesCollection.getPage(SingleUserPopoverContainer.class)
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
		webappPagesCollection.getPage(SingleUserPopoverContainer.class)
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
		Assert.assertTrue(openConvMissingMessage, webappPagesCollection
				.getPage(SingleUserPopoverContainer.class)
				.isOpenConvButtonVisible());
		Assert.assertTrue(
				openConvMissingMessage,
				webappPagesCollection.getPage(SingleUserPopoverContainer.class)
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
		Assert.assertTrue(webappPagesCollection.getPage(
				SingleUserPopoverContainer.class).isPendingTextBoxVisible());
	}

	/**
	 * Creates conversation with one user from on Single Participant popover
	 *
	 * @step. ^I click open conversation from Single Participant popover$
	 * @throws Exception
	 */
	@When("^I click open conversation from Single Participant popover$")
	public void IClickOpenConversation() throws Exception {
		webappPagesCollection.getPage(SingleUserPopoverContainer.class)
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
	public void ThenISeeCorrectOpenConvButtonToolTip() throws Exception {
		Assert.assertTrue(webappPagesCollection
				.getPage(SingleUserPopoverContainer.class)
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
		Assert.assertTrue(webappPagesCollection
				.getPage(SingleUserPopoverContainer.class).getMailHref()
				.contains(MAILTO));

	}

	/**
	 * Verifies whether pending button tool tip is correct or not.
	 *
	 * @step. ^I see correct pending button tool tip on Single Participant
	 *        popover$
	 *
	 */
	@Then("^I see correct pending button tool tip on Single Participant popover$")
	public void ThenISeeCorrectPendingButtonToolTip() throws Exception {
		Assert.assertTrue(webappPagesCollection
				.getPage(SingleUserPopoverContainer.class)
				.getPendingButtonToolTip().equals(TOOLTIP_PENDING));
	}

	/**
	 * Verifies whether Block button exists on the popover
	 *
	 * @step. ^I see Block button on Single User Profile popover$
	 *
	 */
	@Then("^I see Unblock button on Single User Profile popover$")
	public void ISeeUnblockButton() throws Exception {
		Assert.assertTrue(webappPagesCollection.getPage(
				SingleUserPopoverContainer.class).isUnblockButtonVisible());
	}

	/**
	 * Click Unblock button on popover
	 *
	 * @step. ^I click Unblock button on popover$
	 *
	 * @throws Exception
	 *             /** Click Unblock button on popover
	 *
	 * @step. ^I click Unblock button on popover$
	 *
	 * @throws Exception
	 */

	@When("^I click Unblock button on Single User popover$")
	public void IClickUnblockButton() throws Exception {
		webappPagesCollection.getPage(SingleUserPopoverContainer.class)
				.clickUnblockButton();
	}
}
