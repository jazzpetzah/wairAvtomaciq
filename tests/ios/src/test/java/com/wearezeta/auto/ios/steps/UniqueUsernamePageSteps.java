package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.SettingsPage;
import com.wearezeta.auto.ios.pages.UniqueUsernamePage;
import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.List;

public class UniqueUsernamePageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private UniqueUsernamePage getUniqueUsernamePage() throws Exception {
        return pagesCollection.getPage(UniqueUsernamePage.class);
    }

    private String newUniqueName;

    /**
     * Verify visibility of Unique Username page
     *
     * @throws Exception
     * @step. ^I see Unique Username page$
     */
    @Then("^I see Unique Username page$")
    public void ISeeUsernamePage() throws Exception {
        Assert.assertTrue("Unique Username page is not visible", getUniqueUsernamePage().isUsernamePageVisible());
    }

    /**
     * Tap Save button on Unique Username page
     *
     * @throws Exception
     * @step. ^I tap (Save) button on Unique Username page$
     */
    @When("^I tap (Save) button on Unique Username page$")
    public void ITapButtonOnUniqueUsernamePage(String buttonName) throws Exception {
        getUniqueUsernamePage().tapButtonByName(buttonName);
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
        getUniqueUsernamePage().inputStringInNameInput(name);
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
        boolean buttonState = getUniqueUsernamePage().isSaveButtonEnabled();
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
     * @step. ^I attempt to enter (\d+) random chars as name on Unique Username page$
     */
    @When("^I attempt to enter (\\d+) random chars as name on Unique Username page$")
    public void IAttemtToEnterMaxAllowedCharsAsName(int count) throws Exception {
        this.newUniqueName = getUniqueUsernamePage().inputXrandomString(count);
    }

    /**
     * Verify Username length in name input
     *
     * @param count chars count to compare with
     * @throws Exception
     * @step. ^I see that name length is (less than|equal to) (\d+) chars? on Unique Username page$
     */
    @Then("^I see that name length is (less than|equal to) (\\d+) chars? on Unique Username page$")
    public void ISeeNameLenghIsLessThanXChars(String compareState, int count) throws Exception {
        if (compareState.equals("less than")) {
            Assert.assertTrue(String.format("Username length in name input is not less than %s chars.", count),
                    getUniqueUsernamePage().getNameInputTextLength() < count);
        } else {
            Assert.assertTrue(String.format("Username length in name input is not equal to %s chars.", count),
                    getUniqueUsernamePage().getNameInputTextLength() == count);
        }
    }

    /**
     * Verify that name input stays empty if user try input unacceptable symbols from the table
     *
     * @param table table of symbols
     * @step. ^I type unique usernames from the data table and verify they cannot be committed on Unique Username page$
     */
    @When("^I type unique usernames from the data table and verify they cannot be committed on Unique Username page$")
    public void IFillInInputDataAndVerify(DataTable table) throws Exception {
        final List<List<String>> data = table.raw();
        for (int i = 1; i < data.size(); i++) {
            String newName = data.get(i).get(1);
            getUniqueUsernamePage().inputStringInNameInput(newName);
            Assert.assertTrue(
                    String.format("Name input after enter of '%s' charset is not empty on Unique Username page", newName),
                    getUniqueUsernamePage().isNameInputEmpty());
        }
    }

    /**
     * Verify expected previously set unique username is displayed on Settings page
     *
     * @throws Exception
     * @step. ^I see new unique username is displayed on Settings Page$
     */
    @Then("^I see new unique username is displayed on Settings Page$")
    public void ISeeNewUniqueUsernameOnSettingsPage() throws Exception {
        getUniqueUsernamePage().isUniqueUsernameInSettingsDisplayed(this.newUniqueName);
    }
}
