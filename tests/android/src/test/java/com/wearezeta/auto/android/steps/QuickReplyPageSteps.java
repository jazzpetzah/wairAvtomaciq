package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.pages.QuickReplyPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class QuickReplyPageSteps {
    private QuickReplyPage getQuickReplyPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(QuickReplyPage.class);
    }

    /**
     * I reply message in Quick reply
     *
     * @param message the message that you want to reply
     * @throws Exception
     * @step. I reply message "(.*)" on Quick reply page$
     */
    @When("^I reply message \"(.*)\" on Quick reply page$")
    public void IReplyMessage(String message) throws Exception {
        getQuickReplyPage().typeAndSendMessage(message);
    }

    /**
     * I tap on Open wire button
     *
     * @throws Exception
     * @step. ^I tap (Open wire) button on Quick reply page$
     */
    @When("^I tap (Open wire) button on Quick reply page$")
    public void ITapOnButton(String buttonName) throws Exception {
        getQuickReplyPage().tapButton(buttonName);
    }

    /**
     * Verify the content is visible for specified item
     *
     * @param contentType   which could be reply name/received message counter/current show message
     * @param expectedValue the related value
     * @throws Exception
     * @step. ^I see (Reply name|Received message counter|Received message) with value \"(.*)\" on Quick reply page$
     */
    @Then("^I see (Reply name|Received message counter|Received message) with value \"(.*)\" on Quick reply page$")
    public void ISeeContent(String contentType, String expectedValue) throws Exception {
        if(contentType.toLowerCase().equals("reply name")) {
            expectedValue = AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                    .replaceAliasesOccurences(expectedValue, ClientUsersManager.FindBy.NAME_ALIAS);
        }
        Assert.assertTrue(String.format("The %s with value '%s' is still invisible", contentType, expectedValue),
                getQuickReplyPage().waitUntilContentVisible(contentType, expectedValue));
    }

    /**
     * Verify I see the quick reply Activity
     *
     * @param shouldNotSee equals null means the Quick reply Activity should be visible
     * @throws Exception
     * @step. ^I (do not )?see quick reply page$
     */
    @Then("^I (do not )?see quick reply page$")
    public void ISeeQuickReplyPage(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The quick reply page is invisible", getQuickReplyPage().waitUntilVisible());
        } else {
            Assert.assertTrue("The quick reply page is still visible", getQuickReplyPage().waitUntilInvisible());
        }
    }

}
