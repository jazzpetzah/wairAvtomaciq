package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.UniqueUsernamePage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class UniqueUsernamePageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private UniqueUsernamePage getUniqueUsernamePage() throws Exception {
        return pagesCollection.getPage(UniqueUsernamePage.class);
    }

    /**
     * Checks if username edit is visible or not on Settings page
     *
     * @param shouldNotSee null if should see
     * @return true if visible
     * @throws Exception
     */
    @Then("^I(do not)? see username edit field on Settings page$")
    public void iSeeUsernameEdit(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Username edit is not visible", getUniqueUsernamePage().isUsernameEditVisible());
        } else {
            Assert.assertTrue("Username edit is visible", getUniqueUsernamePage().isUsernameEditInvisible());
        }
    }

    /**
     * Enter username and check if there is error with specific message
     *
     * @param usernameDatatable datatable (| UsernameTyped | DisplayedUsername |), that provides testing data
     * @throws Exception
     * @step. ^I enter new username on Settings page, according to datatable$
     */
    @Then("^I enter new username on Settings page, according to datatable$")
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

    private class UsernameDatatable {
        private String usernameTyped;
        private String isShownAsCorrect;
    }
}
