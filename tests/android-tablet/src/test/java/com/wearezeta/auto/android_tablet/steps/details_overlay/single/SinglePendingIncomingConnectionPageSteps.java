package com.wearezeta.auto.android_tablet.steps.details_overlay.single;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.details_overlay.single.TabletSinglePendingIncomingConnectionPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class SinglePendingIncomingConnectionPageSteps {
    private TabletSinglePendingIncomingConnectionPage getSinglePendingIncomingConnectionPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletSinglePendingIncomingConnectionPage.class);
    }

    /**
     * Scroll to given user in the inbox
     *
     * @param contact The user name of the user to search.
     * @throws Throwable
     * @step. ^I scroll to inbox contact (.*) on Single pending incoming connection page$
     */
    @When("^I scroll to contact (.*) on Single pending incoming connection page$")
    public void IScrollToInboxContact(String contact) throws Throwable {
        contact = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByNameOrNameAlias(contact).getName();
        getSinglePendingIncomingConnectionPage().scrollToContact(contact);
    }

    /**
     * Tap on all buttons with or without specified user
     *
     * @param buttonName button name
     * @param userName   User Name or user name alias
     * @throws Exception
     * @step. ^I tap (ignore|connect) button( for user (.+))? on Single pending incoming connection (?:page|popover)$
     */
    @When("^I tap (ignore|connect) button( for user (.+))? on Single pending incoming connection (?:page|popover)$")
    public void ITapButton(String buttonName, String withSpecifiedUser, String userName) throws Exception {
        if (withSpecifiedUser != null) {
            userName = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager()
                    .findUserByNameOrNameAlias(userName).getName();
            getSinglePendingIncomingConnectionPage().tapButton(userName, buttonName);
        } else {
            getSinglePendingIncomingConnectionPage().tapButton(buttonName);
        }
    }

    /**
     * Verify the button is visible
     *
     * @param shouldNotSee equals null means the button should be visible
     * @param buttonName   button name
     * @throws Exception
     * @step. ^I( do not)? see (connect|ignore) button on Single pending incoming connection page$
     */
    @Then("^I( do not)? see (connect|ignore) button on Single pending incoming connection page$")
    public void ISeeButton(String shouldNotSee, String buttonName) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The button '%s' is invisible", buttonName),
                    getSinglePendingIncomingConnectionPage().waitUntilButtonVisible(buttonName));
        } else {
            Assert.assertTrue(String.format("The button '%s' is still visible", buttonName),
                    getSinglePendingIncomingConnectionPage().waitUntilButtonInvisible(buttonName));
        }
    }

    /**
     * Verify user data (User name, Unique user name)
     *
     * @param shouldNotSee equals null means the item should be visible
     * @param type         which could be user name, unique user name
     * @param text         the name alias
     * @throws Exception
     * @step.^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Single pending incoming connection (?:page|popover)$
     */
    @Then("^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Single pending incoming connection (?:page|popover)$")
    public void ISeeUserNameAndEmail(String shouldNotSee, String type, String text) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", type),
                    getSinglePendingIncomingConnectionPage().waitUntilUserDataVisible(type, text));
        } else {
            Assert.assertTrue(String.format("%s should be invisible", type),
                    getSinglePendingIncomingConnectionPage().waitUntilUserDataInvisible(type, text));
        }
    }

    /**
     * Verify whether Incoming connection popover is visible or not
     *
     * @param shouldNotBeVisible equals to null is "no not" part does not exist in the step
     * @throws Exception
     * @step. ^I( do not)? see Single pending incoming connection popover$
     */
    @Then("^I( do not)? see Single pending incoming connection popover$")
    public void ISeePopover(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Single incoming connection popover is not visible",
                    getSinglePendingIncomingConnectionPage().waitUntilPopoverVisible());
        } else {
            Assert.assertTrue("Single incoming connection popover is still visible",
                    getSinglePendingIncomingConnectionPage().waitUntilPopoverInvisible());
        }
    }
}
