package com.wearezeta.auto.ios.steps;

import cucumber.api.java.en.And;
import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.OtherUserOnPendingProfilePage;

import cucumber.api.java.en.When;

public class OtherUserOnPendingPersonalInfoPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private OtherUserOnPendingProfilePage getOtherUserOnPendingProfilePage() throws Exception {
        return pagesCollection.getPage(OtherUserOnPendingProfilePage.class);
    }

    @When("^I see (.*) user pending profile page$")
    public void WhenISeeOtherUserProfilePage(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        Assert.assertTrue("Username is not displayed",
                getOtherUserOnPendingProfilePage().isUserNameDisplayed(name));
        Assert.assertTrue("Close button not displayed",
                getOtherUserOnPendingProfilePage().isClosePageButtonVisible());
    }

    /**
     * Check if the corresponding button is present on Pending Profile page
     *
     * @param btnName         one of possible button names
     * @param shouldBeVisible equals to null if the button should be visible
     * @throws Exception
     * @step. ^I (do not )?see (Cancel Request|Remove From Group) button on pending profile page$"
     */
    @When("^I (do not )?see (Cancel Request|Remove From Group) button on pending profile page$")
    public void ISeeButton(String shouldBeVisible, String btnName) throws Exception {
        boolean isVisible;
        switch (btnName.toLowerCase()) {
            case "cancel request":
                isVisible = getOtherUserOnPendingProfilePage().isCancelRequestButtonVisible();
                break;
            case "remove from group":
                isVisible = getOtherUserOnPendingProfilePage().isRemoveFromGroupConversationVisible();
                break;
            default:
                throw new IllegalArgumentException(String.format("Button %s is unknown", btnName));
        }
        if (shouldBeVisible == null) {
            Assert.assertTrue(String.format("%s button is not displayed", btnName), isVisible);
        } else {
            // FIXME: Check if buttons are invisible instead
            Assert.assertTrue(String.format("%s button is displayed, but should be hidden", btnName), !isVisible);
        }
    }

    /**
     * Tap the corresponding button on Pending Profile page
     *
     * @param btnName one of possible button names
     * @throws Exception
     * @step. ^I tap (Start Conversation|Connect|Cancel Request) button on pending profile page$
     */
    @When("^I tap (Start Conversation|Connect|Cancel Request) button on pending profile page$")
    public void ITapButton(String btnName) throws Exception {
        switch (btnName.toLowerCase()) {
            case "start conversation":
                getOtherUserOnPendingProfilePage().tapStartConversationButton();
                break;
            case "connect":
                getOtherUserOnPendingProfilePage().tapConnectButton();
                break;
            case "cancel request":
                getOtherUserOnPendingProfilePage().tapCancelRequestButton();
                break;
            default:
                throw new IllegalArgumentException(String.format("Button %s is unknown", btnName));
        }
    }

    /**
     * Click the appropriate button to confirm the action
     *
     * @param action name of the action to confirm
     * @throws Exception
     * @step. ^I confirm (Cancel Request|Connect) action on pending profile page$
     */
    @And("^I confirm (Cancel Request|Connect) action on pending profile page$")
    public void IConfirm(String action) throws Exception {
        switch (action.toLowerCase()) {
            case "cancel request":
                getOtherUserOnPendingProfilePage().confirmCancelRequest();
                break;
            case "connect":
                getOtherUserOnPendingProfilePage().confirmConnect();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown action to confirm: %s", action));
        }
    }
}
