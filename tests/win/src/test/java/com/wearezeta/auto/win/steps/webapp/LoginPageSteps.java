package com.wearezeta.auto.win.steps.webapp;

import com.wearezeta.auto.web.common.WebAppTestContext;
import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.pages.LoginPage;
import com.wearezeta.auto.win.pages.win.MainWirePage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.openqa.selenium.Point;

public class LoginPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(LoginPageSteps.class.getName());
    private final WebAppTestContext webContext;

    public LoginPageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }

    @Given("^I Sign in using login (.*) and password (.*)$")
    public void ISignInUsingLoginAndPassword(String login, String password) throws Exception {
        try {
            login = webContext.getUsersManager().findUserByEmailOrEmailAlias(login).getEmail();
        } catch (NoSuchUserException e) {
            try {
                // search for email by name aliases in case name is specified
                login = webContext.getUsersManager().findUserByNameOrNameAlias(login).getEmail();
            } catch (NoSuchUserException ex) {
            }
        }

        try {
            password = webContext.getUsersManager().findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        LOG.debug("Starting to Sign in using login " + login + " and password " + password);
        this.IEnterEmail(login);
        this.IEnterPassword(password);
        this.IPressSignInButton();
    }

    @When("^I press Sign In button$")
    public void IPressSignInButton() throws Exception {
        Point point = webContext.getPagesCollection().getPage(com.wearezeta.auto.web.pages.LoginPage.class)
                .getCenterOfSignInButton();
        // TODO move to page
        // subtracting height of menu bar (there is none on login screen)
        point.move(point.getX(), point.getY() - MainWirePage.MENUBAR_HEIGHT);
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class)
                .clickOnWebView(point);
    }

    @When("^I enter email (\\S+)$")
    public void IEnterEmail(String email) throws Exception {
        try {
            email = webContext.getUsersManager().findUserByEmailOrEmailAlias(email).getEmail();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        webContext.getPagesCollection().getPage(LoginPage.class).inputEmail(email);
    }

    @When("^I enter password \"([^\"]*)\"$")
    public void IEnterPassword(String password) throws Exception {
        try {
            password = webContext.getUsersManager().findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        webContext.getPagesCollection().getPage(LoginPage.class).inputPassword(password);
    }
}
