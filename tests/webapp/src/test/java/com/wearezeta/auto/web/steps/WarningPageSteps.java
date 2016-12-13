package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.TestContext;
import org.junit.Assert;

import com.wearezeta.auto.web.pages.WarningPage;

import cucumber.api.java.en.Then;

public class WarningPageSteps {

    private final TestContext context;

    public WarningPageSteps(TestContext context) {
        this.context = context;
    }

    @Then("^I( do not)? see a warning$")
    public void ISeeWarning(String doNot) throws Exception {
        if (doNot == null) {
            Assert.assertTrue(
                    "Missing WebRTC support warning bar is not visible",
                    context.getPagesCollection().getPage(WarningPage.class)
                            .isMissingWebRTCSupportWarningBarVisible());
        } else {
            Assert.assertTrue("Missing WebRTC support warning bar is visible",
                    context.getPagesCollection().getPage(WarningPage.class)
                            .isMissingWebRTCSupportWarningBarInvisible());
        }
    }

    @Then("^I close the warning$")
    public void IClickCloseMissingWebRTCSupportWarning() throws Exception {
        context.getPagesCollection().getPage(WarningPage.class)
                .clickCloseMissingWebRTCSupportWarningBar();
    }

    @Then("^I see \"(.*)\" link in warning$")
    public void ISeeLinkInMessage(String linkCaption) throws Exception {
        context.getPagesCollection().getPage(WarningPage.class)
                .isLinkWithCaptionInMissingWebRTCSupportWarningBarVisible(
                        linkCaption);
    }

    @Then("^I( do not)? see another call warning modal$")
    public void ISeeAnotherCallWarningModal(String doNot) throws Exception {
        if (doNot == null) {
            Assert.assertTrue("Another call warning modal is not visible",
                    context.getPagesCollection().getPage(WarningPage.class)
                            .isAnotherCallWarningModalVisible());
        } else {
            Assert.assertTrue("Another call warning modal is visible",
                    context.getPagesCollection().getPage(WarningPage.class)
                            .isAnotherCallWarningModalInvisible());
        }
    }

    @Then("^I close the another call warning modal$")
    public void IClickCloseAnotherCallWarningModal() throws Exception {
        context.getPagesCollection().getPage(WarningPage.class)
                .clickCloseAnotherCallWarningModal();
    }

    @Then("^I click on \"(.*)\" button in another call warning modal$")
    public void IClickButtonInAnotherCallWarningModal(String buttonCaption)
            throws Exception {
        Assert.assertTrue(
                "Button '" + buttonCaption
                + "' in another call warning modal was not clickable",
                context.getPagesCollection().getPage(WarningPage.class)
                        .clickButtonWithCaptionInAnotherCallWarningModal(
                                buttonCaption));
    }

    @Then("^I( do not)? see full call warning modal$")
    public void ISeeFullCallWarningModal(String doNot) throws Exception {
        if (doNot == null) {
            Assert.assertTrue("Full call warning modal is not visible",
                    context.getPagesCollection().getPage(WarningPage.class)
                            .isFullCallWarningModalVisible());
        } else {
            Assert.assertTrue("Full call warning modal is visible",
                    context.getPagesCollection().getPage(WarningPage.class)
                            .isFullCallWarningModalInvisible());
        }
    }

    @Then("^I close the full call warning modal$")
    public void IClickCloseFullCallWarningModal() throws Exception {
        context.getPagesCollection().getPage(WarningPage.class)
                .clickCloseFullCallWarningModal();
    }

    @Then("^I click on \"(.*)\" button in full call warning modal$")
    public void IClickButtonInFullCallWarningModal(String buttonCaption)
            throws Exception {
        Assert.assertTrue(
                "Button '" + buttonCaption
                + "' in full call warning modal was not clickable",
                context.getPagesCollection().getPage(WarningPage.class)
                        .clickButtonWithCaptionInFullCallWarningModal(
                                buttonCaption));
    }

    @Then("^I( do not)? see full call conversation warning modal$")
    public void ISeeFullCallConversationWarningModal(String doNot) throws Exception {
        if (doNot == null) {
            Assert.assertTrue("Full call conversation warning modal is not visible",
                    context.getPagesCollection().getPage(WarningPage.class)
                            .isFullConversationWarningModalVisible());
        } else {
            Assert.assertTrue("Full call conversation warning modal is visible",
                    context.getPagesCollection().getPage(WarningPage.class)
                            .isFullConversationWarningModalInvisible());
        }
    }

    @Then("^I close the full call conversation warning modal$")
    public void IClickCloseFullCallConversationWarningModal() throws Exception {
        context.getPagesCollection().getPage(WarningPage.class)
                .clickCloseFullConversationWarningModal();
    }

    @Then("^I click on \"(.*)\" button in full call conversation warning modal$")
    public void IClickButtonInFullCallConversationWarningModal(String buttonCaption)
            throws Exception {
        Assert.assertTrue(
                "Button '" + buttonCaption
                + "' in full call conversation warning modal was not clickable",
                context.getPagesCollection().getPage(WarningPage.class)
                        .clickButtonWithCaptionInFullConversationWarningModal(buttonCaption));
    }

    @Then("^I( do not)? see file transfer limit warning modal$")
    public void ISeeFileTransferLimitWarningModal(String doNot) throws Exception {
        if (doNot == null) {
            Assert.assertTrue("File transfer limit warning modal is not visible",
                    context.getPagesCollection().getPage(WarningPage.class)
                            .isFileTransferLimitWarningModalVisible());
        } else {
            Assert.assertTrue("File transfer limit warning modal is visible",
                    context.getPagesCollection().getPage(WarningPage.class)
                            .isFileTransferLimitWarningModalInvisible());
        }
    }

    @Then("^I click on \"(.*)\" button in file transfer limit warning modal$")
    public void IClickButtonInFileTransferLimitWarningModal(String buttonCaption)
            throws Exception {
        context.getPagesCollection().getPage(WarningPage.class).clickOKInFileTransferLimitWarningModal();
    }

    @Then("^I( do not)? see full house warning modal$")
    public void ISeeFullHouseWarningModal(String doNot) throws Exception {
        if (doNot == null) {
            Assert.assertTrue("Full house warning modal is not visible",
                    context.getPagesCollection().getPage(WarningPage.class).isFullHouseWarningModalVisible());
        } else {
            Assert.assertTrue("Full house warning modal is visible",
                    context.getPagesCollection().getPage(WarningPage.class).isFullHouseWarningModalInvisible());
        }
    }

    @Then("^I click on close button in full house warning modal$")
    public void IClickButtonInFileTransferLimitWarningModal() throws Exception {
        context.getPagesCollection().getPage(WarningPage.class).clickCloseOnFullHouseWarningModal();
    }
}
