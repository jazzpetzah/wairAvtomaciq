package com.wearezeta.auto.osx.steps.webapp;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.LoginPage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class LoginPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(LoginPageSteps.class.getName());

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public LoginPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @Given("^I Sign in using login (.*) and password (.*)$")
    public void ISignInUsingLoginAndPassword(String login, String password)
            throws Exception {
        try {
            login = webContext.getUserManager().findUserByEmailOrEmailAlias(login).getEmail();
        } catch (NoSuchUserException e) {
            try {
                // search for email by name aliases in case name is specified
                login = webContext.getUserManager().findUserByNameOrNameAlias(login).getEmail();
            } catch (NoSuchUserException ex) {
            }
        }

        try {
            password = webContext.getUserManager().findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        LOG.debug("Starting to Sign in using login " + login + " and password "
                + password);
        this.IEnterEmail(login);
        this.IEnterPassword(password);
        Thread.sleep(1000);
        this.IPressSignInButton();
        Thread.sleep(1000);
    }

    @When("^I press Sign In button$")
    public void IPressSignInButton() throws Exception {
        Thread.sleep(1000);
        webContext.getPagesCollection().getPage(LoginPage.class).clickSignInButton();
    }

    @When("^I enter email (\\S+)$")
    public void IEnterEmail(String email) throws Exception {
        try {
            email = webContext.getUserManager().findUserByEmailOrEmailAlias(email).getEmail();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        webContext.getPagesCollection().getPage(LoginPage.class).inputEmail(email);
    }

    @When("^I enter password \"([^\"]*)\"$")
    public void IEnterPassword(String password) throws Exception {
        try {
            password = webContext.getUserManager().findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        webContext.getPagesCollection().getPage(LoginPage.class).inputPassword(password);
    }
}
