package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.UniqueUsernameTakeoverPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;


public class UniqueUsernameTakeoverPageSteps {
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private UniqueUsernameTakeoverPage getPage() throws Exception {
        return pagesCollection.getPage(UniqueUsernameTakeoverPage.class);
    }

    /**
     * Tap the corresponding button щn Unique Username Takeover page$
     *
     * @param buttonName one of possible button names
     * @throws Exception
     * @step. ^I tap (Choose Yours|Keep This One) button on Unique Username Takeover page$
     */
    @When("^I tap (Choose Yours|Keep This One) button on Unique Username Takeover page$")
    public void ITapButton(String buttonName) throws Exception {
        getPage().tapButton(buttonName);
    }

    /**
     * Verify visibility of username/unique name on the takeover page
     *
     * @param shouldNotSee     equals to null if the name should be visible
     * @param isUnique         if present then unique username will be verified otherwise the 'simple' one
     * @param expectedUsername name, unique username or an alias
     * @throws Exception
     * @step. ^I (do not )?see (unique )?username (?:starts with )?(.*) on Unique Username Takeover page$
     */
    @Then("^I (do not )?see (unique )?username (?:starts with )?(.*) on Unique Username Takeover page$")
    public void ISeeUniqueUsername(String shouldNotSee, String isUnique, String expectedUsername) throws Exception {
        if (isUnique == null) {
            expectedUsername = usrMgr.replaceAliasesOccurences(expectedUsername,
                    ClientUsersManager.FindBy.NAME_ALIAS);
            if (shouldNotSee == null) {
                Assert.assertTrue(String.format("Username '%s' is not visible", expectedUsername),
                        getPage().isUsernameVisible(expectedUsername));
            } else {
                Assert.assertTrue(String.format("Username '%s' should not be visible", expectedUsername),
                        getPage().isUsernameInvisible(expectedUsername));
            }
        } else {
            if (expectedUsername.startsWith("@")) {
                expectedUsername = "@" + usrMgr.replaceAliasesOccurences(
                        expectedUsername.substring(1, expectedUsername.length()),
                        ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
            } else {
                expectedUsername = "@" + usrMgr.replaceAliasesOccurences(expectedUsername,
                        ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
            }
            if (shouldNotSee == null) {
                Assert.assertTrue(String.format("Unique username '%s' is not visible", expectedUsername),
                        getPage().isUniqueUsernameVisible(expectedUsername));
            } else {
                Assert.assertTrue(String.format("Unique username '%s' is not visible", expectedUsername),
                        getPage().isUniqueUsernameInvisible(expectedUsername));
            }
        }
    }
}
