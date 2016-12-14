package com.wearezeta.auto.web.steps;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;

import java.awt.image.BufferedImage;
import java.util.List;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.popovers.SingleUserPopoverContainer;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.apache.log4j.Logger;
import org.junit.Assert;

public class SingleUserPopoverPageSteps {

    private static final Logger log = ZetaLogger.getLog(SingleUserPopoverPageSteps.class.getSimpleName());

    private static final String MAILTO = "mailto:";
    private static final String CAPTION_PENDING = "Pending";
    private static final String CAPTION_OPEN_CONVERSATION = "Open Conversation";
    private static final String TOOLTIP_PENDING = "Pending";
    private static final String TOOLTIP_OPEN_CONVERSATION = "Open conversation";

    private final TestContext context;

    public SingleUserPopoverPageSteps(TestContext context) {
        this.context = context;
    }

    @When("^I( do not)? see Single User Profile popover$")
    public void ISeeSingleUserPopup(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                    .waitUntilVisibleOrThrowException();
        } else {
            context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                    .waitUntilNotVisibleOrThrowException();
        }
    }

    @When("^I choose to create conversation from Single User Profile popover$")
    public void IChooseToCreateConversationFromSingleUserPopover()
            throws Exception {
        context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                .clickCreateGroupConversation();
    }

    @When("^I click Add People button on Single User Profile popover$")
    public void IClickAddPeopleButton() throws Exception {
        context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                .clickAddPeopleButton();
    }

    @When("^I input user name (.*) in search field on Single User Profile popover$")
    public void ISearchForUser(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                .searchForUser(name);
    }

    @When("^I select (.*) from Single User Profile popover search results$")
    public void ISelectUserFromSearchResults(String user) throws Exception {
        user = context.getUserManager().replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                .selectUserFromSearchResult(user);
    }

    @When("^I see username (.*) on Single User Profile popover$")
    public void IseeUserNameOnUserProfilePage(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        Assert.assertEquals(name,
                context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                        .getUserName());
    }

    @When("^I see an avatar on Single User Profile popover$")
    public void IseeAvatarOnUserProfilePage() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(
                SingleUserPopoverContainer.class).isAvatarVisible());
    }

    @Then("^I see Add people button on Single User Profile popover$")
    public void ISeeAddButton() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(
                SingleUserPopoverContainer.class).isAddButtonVisible());
    }

    @Then("^I see Block button on Single User Profile popover$")
    public void ISeeBlockButton() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(
                SingleUserPopoverContainer.class).isBlockButtonVisible());
    }

    @Then("^I click Block button on Single User Profile popover$")
    public void IClickBlockButton() throws Exception {
        context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                .clickBlockButton();
    }

    @And("^I confirm user blocking on Single User Profile popover$")
    public void IConfirmBlockUser() throws Exception {
        context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                .clickConfirmButton();
    }

    @Then("^I switch to Devices tab on Single User Profile popover$")
    public void ISwitchToDevices() throws Exception {
        context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                .switchToDevicesTab();
    }

    @Then("^I switch to Details tab on Single User Profile popover$")
    public void ISwitchToDetails() throws Exception {
        context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                .switchToDetailsTab();
    }

    @Then("^I verify system message contains (.*) on Single User Profile popover$")
    public void ISeeSystemMessage(String message) throws Exception {
        assertThat(context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                .getDevicesText(), containsString(message));
    }

    @Then("^I( do not)? see Mail of user (.*) on Single Participant popover$")
    public void ISeeMailOfUser(String not, String userAlias) throws Exception {
        final SingleUserPopoverContainer singleUserPopover = context.getPagesCollection().getPage(
                SingleUserPopoverContainer.class);
        if (not == null) {
            ClientUser user = context.getUserManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
            assertThat(singleUserPopover.getUserMail().toLowerCase(), equalTo(user.getEmail()));
        } else {
            if (singleUserPopover.isUserMailVisible()) {
                assertThat(singleUserPopover.getUserMail(), equalTo(""));
            }
        }
    }

    @Then("^I( do not)? see avatar (.*) of user (.*) on Single Participant popover$")
    public void ISeeAvatarOfUser(String not, String avatar, String userAlias)
            throws Exception {
        final String picturePath = WebCommonUtils.getFullPicturePath(avatar);
        BufferedImage expectedAvatar = ImageUtil.readImageFromFile(picturePath);
        BufferedImage actualAvatar = context.getPagesCollection().getPage(
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

    @Then("^I see Pending button on Single Participant popover$")
    public void ISeePendingButton() throws Exception {
        final String pendingButtonMissingMessage = "Pending button is not visible on Single Participant popover";
        Assert.assertTrue(pendingButtonMissingMessage, context.getPagesCollection()
                .getPage(SingleUserPopoverContainer.class)
                .isPendingButtonVisible());
        Assert.assertTrue(
                pendingButtonMissingMessage,
                context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                        .getPendingButtonCaption().trim()
                        .equalsIgnoreCase(CAPTION_PENDING));
    }

    @Then("^I click Pending button on Single Participant popover$")
    public void IClickPendingButton() throws Exception {
        context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                .clickPendingButton();
    }

    @Then("^I see open conversation button on Single Participant popover$")
    public void ISeeOpenConversationButton() throws Exception {
        final String openConvMissingMessage = "Open conversation button is not visible on Single Participant popover";
        Assert.assertTrue(openConvMissingMessage, context.getPagesCollection()
                .getPage(SingleUserPopoverContainer.class)
                .isOpenConvButtonVisible());
        Assert.assertTrue(
                openConvMissingMessage,
                context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                        .getOpenConvButtonCaption().trim()
                        .equalsIgnoreCase(CAPTION_OPEN_CONVERSATION));
    }

    @Then("^I see Pending text box on Single Participant popover$")
    public void ISeePendingTextBox() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(
                SingleUserPopoverContainer.class).isPendingTextBoxVisible());
    }

    @When("^I click open conversation from Single Participant popover$")
    public void IClickOpenConversation() throws Exception {
        context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                .clickOpenConvButton();
    }

    @Then("^I see correct open conversation button tool tip on Single Participant popover$")
    public void ThenISeeCorrectOpenConvButtonToolTip() throws Exception {
        Assert.assertTrue(context.getPagesCollection()
                .getPage(SingleUserPopoverContainer.class)
                .getOpenConvButtonToolTip().equals(TOOLTIP_OPEN_CONVERSATION));
    }

    @Then("^Would open mail client when clicking mail on Single Participant popover$")
    public void ThenISeeThatClickOnMailWouldOpenMailClient() throws Exception {
        Assert.assertTrue(context.getPagesCollection()
                .getPage(SingleUserPopoverContainer.class).getMailHref()
                .contains(MAILTO));

    }

    @Then("^I see correct pending button tool tip on Single Participant popover$")
    public void ThenISeeCorrectPendingButtonToolTip() throws Exception {
        Assert.assertTrue(context.getPagesCollection()
                .getPage(SingleUserPopoverContainer.class)
                .getPendingButtonToolTip().equals(TOOLTIP_PENDING));
    }

    @Then("^I see Unblock button on Single User Profile popover$")
    public void ISeeUnblockButton() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(
                SingleUserPopoverContainer.class).isUnblockButtonVisible());
    }

    @When("^I click Unblock button on Single User popover$")
    public void IClickUnblockButton() throws Exception {
        context.getPagesCollection().getPage(SingleUserPopoverContainer.class)
                .clickUnblockButton();
    }

    @Then("^I( do not)? see user verified icon on Single User Profile popover$")
    public void ISeeUserVerifiedIcon(String doNot) throws Exception {
        if (doNot == null) {
            assertThat("No blue shield found", context.getPagesCollection().getPage(SingleUserPopoverContainer.class).
                    isUserVerified());
        } else {
            assertThat("Blue shield found :(", !context.getPagesCollection().getPage(SingleUserPopoverContainer.class).
                    isUserVerified());
        }
    }

    @Then("^I see all devices of user (.*) on Single User Profile popover$")
    public void ISeeADeviceNamed(String userAlias) throws Exception {
        ClientUser user = context.getUserManager().findUserByNameOrNameAlias(userAlias);
        List<String> ids = context.getDeviceManager().getDeviceIds(user);
        context.getPagesCollection().getPage(SingleUserPopoverContainer.class).waitForDevices();
        List<String> devices = context.getPagesCollection().getPage(SingleUserPopoverContainer.class).getDeviceIds();
        assertThat("Device id in list", devices, is(ids));
    }

    @Then("^I see device (.*) of user (.*) is verified on Single User Profile popover$")
    public void ISeeADeviceNamed(String deviceName, String userAlias) throws Exception {
        ClientUser user = context.getUserManager().findUserByNameOrNameAlias(userAlias);
        String id = context.getDeviceManager().getDeviceId(user, deviceName + context.getTestname().hashCode());
        context.getPagesCollection().getPage(SingleUserPopoverContainer.class).waitForDevices();
        List<String> devices = context.getPagesCollection().getPage(SingleUserPopoverContainer.class).getVerifiedDeviceIds();
        assertThat("Device id is NOT in verified devices", devices, hasItem(id.toUpperCase()));
    }

    @When("^I click on device (.*) of user (.*) on Single User Profile popover$")
    public void IClickOnDevice(String deviceName, String userAlias) throws Exception {
        ClientUser user = context.getUserManager().findUserByNameOrNameAlias(userAlias);
        String id = context.getDeviceManager().getDeviceId(user, deviceName + context.getTestname().hashCode());
        // TODO: workaround to remove leading zeros
        id = id.replaceFirst("^0+(?!$)", "");
        context.getPagesCollection().getPage(SingleUserPopoverContainer.class).clickDevice(id);
    }

}
