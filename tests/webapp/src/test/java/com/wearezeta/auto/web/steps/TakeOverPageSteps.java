package com.wearezeta.auto.web.steps;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.AccountPage;
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

    private String rememberedUsername = null;

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
        name = context.getUserManager().replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertThat("Username on take over screen",
                context.getPagesCollection().getPage(TakeOverPage.class).getUniqueUsername(), startsWith(name));
    }

    @Then("^I remember unique user name on take over screen$")
    public void IRememberUniqueUsername() throws Throwable {
        rememberedUsername = context.getPagesCollection().getPage(TakeOverPage.class).getUniqueUsername();
    }

    @Then("^I see remembered unique username in account preferences$")
    public void ISeeRememberedUsername() throws Throwable {
        if (rememberedUsername == null) {
            throw new Exception("Please use step to remember unique username before this step");
        }
        Assert.assertTrue("Username in settings not equal to username on take over screen",
                context.getPagesCollection().getPage(AccountPage.class).getUniqueUsername().equals(rememberedUsername));
    }

    @Then("^I see remembered unique username contains a random adjective and noun$")
    public void ISeeUsernameContainsX() throws Exception {
        if (rememberedUsername == null) {
            throw new Exception("Please use step to remember unique username before this step");
        }
        String file = "adjectives.txt";
        Scanner s = new Scanner(new File(WebCommonUtils.getFullFilePath(file)));
        ArrayList<String> adjectiveList = new ArrayList<>();
        while (s.hasNextLine()) {
            adjectiveList.add(s.nextLine());
        }
        s.close();

        file = "nouns.txt";
        s = new Scanner(new File(WebCommonUtils.getFullFilePath(file)));
        ArrayList<String> nounList = new ArrayList<>();
        while (s.hasNextLine()) {
            nounList.add(s.nextLine());
        }
        s.close();

        int charPointer = 1;
        String adjective = rememberedUsername.substring(0, charPointer);
        while (!adjectiveList.contains(adjective)) {
            charPointer ++;
            if (charPointer > rememberedUsername.length()) {
                throw new Exception("No such adjective: " + adjective);
            }
            adjective = rememberedUsername.substring(0, charPointer);
        }
        int newStart = charPointer;
        charPointer++;
        String noun = rememberedUsername.substring(newStart, charPointer);
        while (!nounList.contains(noun)) {
            charPointer ++;
            if (charPointer > rememberedUsername.length()) {
                throw new Exception("No such noun: " + noun);
            }
            noun = rememberedUsername.substring(newStart, charPointer);
        }
        Assert.assertTrue("Unique Username " + adjective + noun + " does not exist.",
                rememberedUsername.equals(adjective.concat(noun)));
    }
}
