package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.UniqueUsernamePage;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

public class UniqueUsernamePageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final CommonSteps commonSteps = CommonSteps.getInstance();

    private UniqueUsernamePage getUniqueUsernamePage() throws Exception {
        return pagesCollection.getPage(UniqueUsernamePage.class);
    }

    /**
     * Checks if username edit is visible or not on Settings page
     *
     * @param shouldNotSee null if should see
     * @param uniqueUsernameExpected non-null if uniqueUsername check should be activated
     * @param uniqueUsername value of uniqueUsername to check
     * @param in does nothing
     * @throws Exception
     * @step. ^I( do not)? see [Uu]nique [Uu]sername ("(.*)")?( in )?edit field on Settings page$
     */
    @Then("^I( do not)? see [Uu]nique [Uu]sername (\"(.*)\")?( in )?edit field on Settings page$")
    public void iSeeUsernameEdit(String shouldNotSee, String uniqueUsernameExpected, String uniqueUsername, String
            in) throws Exception {
        if (shouldNotSee == null) {
            if (uniqueUsernameExpected == null) {
                Assert.assertTrue("Username edit should be visible", getUniqueUsernamePage()
                        .isUsernameEditVisible());
                return;
            }
            uniqueUsername = usrMgr.replaceAliasesOccurences(uniqueUsername, FindBy.UNIQUE_USERNAME_ALIAS);
            Assert.assertTrue(String.format("Username edit should be visible with value %s", uniqueUsername),
                    getUniqueUsernamePage().isUsernameEditVisible(uniqueUsername));
        } else {
            if (uniqueUsernameExpected == null) {
                Assert.assertTrue("Username edit should not be visible", getUniqueUsernamePage()
                        .isUsernameEditInvisible());
                return;
            }
            uniqueUsername = usrMgr.replaceAliasesOccurences(uniqueUsername, FindBy.UNIQUE_USERNAME_ALIAS);
            Assert.assertTrue(String.format("Username edit should not be visible with value %s", uniqueUsername),
                    getUniqueUsernamePage().isUsernameEditVisible(uniqueUsername));
        }
    }

    /**
     * Enter unique username and check if there is error with specific message
     *
     * @param usernameDatatable datatable (| UsernameTyped | DisplayedUsername |), that provides testing data
     * @throws Exception
     * @step. ^I enter new [Uu]nique [Uu]sername on Settings page, according to datatable$
     */
    @Then("^I enter new [Uu]nique [Uu]sername on Settings page, according to datatable$")
    public void iEnterNewUsernameOnSettingsPageAccordingToDatatable(List<UsernameDatatable> usernameDatatable) throws
            Exception {
        final List<String> assertionErrorList = new ArrayList<>();
        for (UsernameDatatable datatableRow : usernameDatatable) {
            String usernameTyped = datatableRow.usernameTyped;
            getUniqueUsernamePage().enterNewUsername(usernameTyped);

            boolean shouldDisplayedCorrectly = Boolean.parseBoolean(datatableRow.isShownAsCorrect);
            boolean isValid = getUniqueUsernamePage().isUsernameEditVisible(usernameTyped) == shouldDisplayedCorrectly;
            if (!isValid) {
                String displayText = shouldDisplayedCorrectly ? "visible" : " invisible";
                assertionErrorList.add(String.format("Username '%s' should be %s",
                        usernameTyped, displayText));
            }
        }
        if (!assertionErrorList.isEmpty()) {
            if (assertionErrorList.size() == 1) {
                Assert.fail(assertionErrorList.get(0));
            }
            Assert.fail(String.join("\n", assertionErrorList));
        }
    }

    /**
     * Enter username and try to save it
     *
     * @param username username to type
     * @throws Exception
     * @step. ^I set new [Uu]nique [Uu]sername "(.*)" on Settings page$
     */
    @Then("^I set new [Uu]nique [Uu]sername \"(.*)\" on Settings page$")
    public void iEnterNewUsernameOnSettingsPage(String username) throws Exception {
        username = usrMgr.replaceAliasesOccurences(username, FindBy.NAME_ALIAS);
        username = usrMgr.replaceAliasesOccurences(username, FindBy.UNIQUE_USERNAME_ALIAS);
        UniqueUsernamePage uniqueUsernamePage = getUniqueUsernamePage();
        uniqueUsernamePage.enterNewUsername(username);
        uniqueUsernamePage.tapOkButton();
    }

    /**
     * Tap OK or Cancel button on unique username settings page
     * @param buttonName OK or Cancel
     * @throws Exception
     */
    @Then("^I tap (OK|Cancel) on [Uu]nique [Uu]sername Settings page$")
    public void iTapOkOnUniqueUsernameSettingsPage(String buttonName) throws Exception {
        UniqueUsernamePage uniqueUsernamePage = getUniqueUsernamePage();
        if (buttonName.equals("OK")) {
            uniqueUsernamePage.tapOkButton();
            return;
        }
        uniqueUsernamePage.tapCancelButton();
    }


    /**
     * Enter random username and try to save it
     *
     * @param usernameSize amount of chars, that new username should have
     * @throws Exception
     * @step. ^I try to set new random (\d+) chars [Uu]nique [Uu]sername on Settings page, but change it to "(.*)
     * then$"
     */
    @Then("^I try to set new random (\\d+) chars [Uu]nique [Uu]sername on Settings page, but change it to \"(.*)\" " +
            "then$")
    public void iTryToEnterNewRandomUsernameOnSettingsPageButSaveAnotherOne(int usernameSize, String uniqueUsername)
            throws Exception {
        try {
            UniqueUsernamePage uniqueUsernamePage = getUniqueUsernamePage();
            uniqueUsernamePage.enterNewRandomUsername(usernameSize);
            uniqueUsernamePage.tapOkButton();
            Assert.assertTrue("Username edit is visible", getUniqueUsernamePage().isUsernameEditInvisible());
        } finally {
            uniqueUsername = usrMgr.replaceAliasesOccurences(uniqueUsername,
                    FindBy.UNIQUE_USERNAME_ALIAS);
            commonSteps.IChangeUniqueUsername("Myself", uniqueUsername);
        }
    }

    private class UsernameDatatable {
        private String usernameTyped;
        private String isShownAsCorrect;
    }
}
