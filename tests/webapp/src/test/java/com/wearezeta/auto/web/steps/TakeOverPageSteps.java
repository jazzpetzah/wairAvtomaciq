package com.wearezeta.auto.web.steps;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.TakeOverPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;

import static org.hamcrest.CoreMatchers.startsWith;

public class TakeOverPageSteps {

    private static final Logger log = ZetaLogger.getLog(TakeOverPageSteps.class
            .getSimpleName());

    private final TestContext context;

    public TakeOverPageSteps() {
        this.context = new TestContext();
    }

    public TakeOverPageSteps(TestContext context) {
        this.context = context;
    }

    @Given("^I see name (.*) on take over screen$")
    public void ISeeNameOnTakeOverScreen(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        Assert.assertEquals(name, context.getPagesCollection().getPage(TakeOverPage.class).getName());
    }

    @When("^I click (ChooseYourOwn|TakeThisOne) button on take over screen$")
    public void IClickButtonsOnTakeOverScreen(String toggle) throws Exception {
        if (toggle.equals("ChooseYourOwn")) {
            context.getPagesCollection().getPage(TakeOverPage.class).clickChooseYourOwnButton();
        } else {
            context.getPagesCollection().getPage(TakeOverPage.class).clickTakeThisOneButton();
        }
    }

    @Given("^I see take over screen page$")
    public void ISeeTakeOverScreenPage() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(TakeOverPage.class)
                .isTakeOverScreenVisible());
    }

    @Then("^I see (ChooseYourOwn|TakeThisOne) button on take over screen$")
    public void ISeeXButtonOnTakeOverScreen(String toggle) throws Exception {
        if (toggle.equals("ChooseYourOwn")) {
            Assert.assertTrue(context.getPagesCollection().getPage(TakeOverPage.class).isChooseYourOwnButtonVisible());
        } else {
            Assert.assertTrue(context.getPagesCollection().getPage(TakeOverPage.class).isTakeThisOneButtonVisible());
            ;
        }
    }

    @Then("^I see unique username starts with (.*) on take over screen$")
    public void ISeeTextAboutUsernamesSayingX(String name) throws Throwable {
        Assert.assertThat("Username on take over screen",
                context.getPagesCollection().getPage(TakeOverPage.class).getUniqueUsername(), startsWith(name));
    }
}
