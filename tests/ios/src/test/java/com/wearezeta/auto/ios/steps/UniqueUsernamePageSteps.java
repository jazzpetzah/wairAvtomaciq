package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.UniqueUsernamePage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class UniqueUsernamePageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private UniqueUsernamePage getUsernamePageSteps() throws Exception {
        return pagesCollection.getPage(UniqueUsernamePage.class);
    }

    /**
     * Verify visibility of Unique Username page
     *
     * @throws Exception
     * @step. ^I see Unique Username page$
     */
    @Then("^I see Unique Username page$")
    public void ISeeUsernamePage() throws Exception {
        Assert.assertTrue("Unique Username page is not visible", getUsernamePageSteps().isUsernamePageVisible());
    }

    /**
     * Tap Save button on Unique Username page
     *
     * @throws Exception
     * @step. ^I tap (Save) button on Unique Username page$
     */
    @When("^I tap (Save) button on Unique Username page$")
    public void ITapButtonOnUniqueUsernamePage(String buttonName) throws Exception {
        getUsernamePageSteps().tapButtonByName(buttonName);
    }

    /**
     * Fill in name input an string
     *
     * @param name string to be input
     * @throws Exception
     * @step. ^I enter "(.*)" name on Unique Username page$
     */
    @When("^I enter \"(.*)\" name on Unique Username page$")
    public void IFillInNameInInputOnUniqueUsernamePage(String name) throws Exception {
        getUsernamePageSteps().inputStringInNameInput(name);
    }

    /**
     * Verify Save button isEnable state
     *
     * @param expectedState Disabe/Enable
     * @throws Exception
     * @step. ^I see Save button state is (Disabled|Enabled) on Unique Username page$
     */
    @When("^I see Save button state is (Disabled|Enabled) on Unique Username page$")
    public void ISeeSaveButtonIsDisabled(String expectedState) throws Exception {
        boolean buttonState = getUsernamePageSteps().isSaveButtonEnabled();
        if (expectedState.equals("Disabled")) {
            Assert.assertFalse(String.format("Wrong Save button state. Should be %s.",expectedState), buttonState);
        } else {
            Assert.assertTrue(String.format("Wrong Save button state. Should be %s.",expectedState), buttonState);
        }

    }

    /**
     * Attempt to enter over max allowed chars amount as name
     *
     * @param count max allowed chars count +1
     * @throws Exception
     * @step. ^I attempt to enter over max allowed (\d+) chars as name on Unique Username page$
     */
    @When("^I attempt to enter over max allowed (\\d+) chars as name on Unique Username page$")
    public void IAttemtToEnterMaxAllowedCharsAsName(int count) throws Exception {
        getUsernamePageSteps().inputXrandomString(count);
    }

    /**
     * Verify that Username in name input is less than X chars
     *
     * @param count max allowed chars count
     * @throws Exception
     * @step. ^I see that name length is less than (\d+) chars on Unique Username page$
     */
    @Then("^I see that name length is less than (\\d+) chars on Unique Username page$")
    public void ISeeNameLenghIsLessThanXChars(int count) throws Exception {
        Assert.assertTrue(String.format("Username in name input is not less than %s chars.", count),
                getUsernamePageSteps().isNameInputTextLengthLessThan(count));
    }
}
