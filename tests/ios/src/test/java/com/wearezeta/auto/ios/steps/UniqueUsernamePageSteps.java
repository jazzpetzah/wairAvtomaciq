package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.UniqueUsernamePage;
import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.List;

public class UniqueUsernamePageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private UniqueUsernamePage getUniqueUsernamePageSteps() throws Exception {
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
        Assert.assertTrue("Unique Username page is not visible", getUniqueUsernamePageSteps().isUsernamePageVisible());
    }

    /**
     * Tap Save button on Unique Username page
     *
     * @throws Exception
     * @step. ^I tap (Save) button on Unique Username page$
     */
    @When("^I tap (Save) button on Unique Username page$")
    public void ITapButtonOnUniqueUsernamePage(String buttonName) throws Exception {
        getUniqueUsernamePageSteps().tapButtonByName(buttonName);
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
        getUniqueUsernamePageSteps().inputStringInNameInput(name);
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
        boolean buttonState = getUniqueUsernamePageSteps().isSaveButtonEnabled();
        if (expectedState.equals("Disabled")) {
            Assert.assertFalse(String.format("Wrong Save button state. Should be %s.", expectedState), buttonState);
        } else {
            Assert.assertTrue(String.format("Wrong Save button state. Should be %s.", expectedState), buttonState);
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
        getUniqueUsernamePageSteps().inputXrandomString(count);
    }

    /**
     * Verify that Username in name input is less than X chars
     *
     * @param count max allowed chars count
     * @throws Exception
     * @step. ^I see that name length is less than (\d+) chars? on Unique Username page$
     */
    @Then("^I see that name length is less than (\\d+) chars? on Unique Username page$")
    public void ISeeNameLenghIsLessThanXChars(int count) throws Exception {
        Assert.assertTrue(String.format("Username in name input is not less than %s chars.", count),
                getUniqueUsernamePageSteps().getNameInputTextLength() < count);
    }

    /**
     * Verify that name input is emtpy on Unique Username page
     *
     * @throws Exception
     * @step. ^I see that name input is empty on Unique Username page$
     */
    @Then("^I see that name input is empty on Unique Username page$")
    public void ISeeNameInputIsEmptyOnUniqueUsernamePage() throws Exception {
        Assert.assertTrue("Name input is not empty on Unique Username page",
                getUniqueUsernamePageSteps().isNameInputEmpty());
    }

    /**
     * Verify that name input stays empty if user try input unacceptable symbols from the table
     *
     * @param table table of symbols
     * @step. ^I fill in unacceptable symbols from table and verify name input stays empty on Unique Username page$
     */
    @When("^I fill in unacceptable symbols from table and verify name input stays empty on Unique Username page$")
    public void IFillInInputDataAndVerify(DataTable table) throws Exception {
        List<List<String>> data = table.raw();

        for (int i = 1; i <= data.size(); i++) {
            getUniqueUsernamePageSteps().inputStringInNameInput(data.get(i).get(1));
            Assert.assertTrue("Name input is not empty on Unique Username page",
                    getUniqueUsernamePageSteps().isNameInputEmpty());
        }
    }
}
