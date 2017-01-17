package com.wearezeta.auto.web.steps;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.wearezeta.auto.common.driver.DriverUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.LoginPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class LoginPageSteps {

    private static final Logger log = ZetaLogger.getLog(LoginPageSteps.class.getSimpleName());

    private final WebAppTestContext context;

    public LoginPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @Given("^I Sign in( temporary)? using login (.*) and password (.*)$")
    public void ISignInUsingLoginAndPassword(String temporary, String login, String password)
            throws Exception {
        try {
            login = context.getUsersManager().findUserByEmailOrEmailAlias(login).getEmail();
        } catch (NoSuchUserException e) {
            try {
                // search for email by name aliases in case name is specified
                login = context.getUsersManager().findUserByNameOrNameAlias(login).getEmail();
            } catch (NoSuchUserException ex) {
            }
        }

        try {
            password = context.getUsersManager().findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        log.debug("Starting to Sign in using login " + login + " and password "
                + password);
        this.IEnterEmail(login);
        this.IEnterPassword(password);
        if (temporary != null) {
            this.IPressSignInButton();
        } else {
            this.ICheckOptionToRememberMe(null);
            this.IPressSignInButton();
        }
    }

    @When("^I press Sign In button$")
    public void IPressSignInButton() throws Exception {
        context.getPagesCollection().getPage(LoginPage.class).clickSignInButton();
    }

    @When("^Sign In button is disabled$")
    public void SignInButtonIsDisabled() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(LoginPage.class)
                .isSignInButtonDisabled());
    }

    @Then("^I am signed in properly$")
    public void IAmSignedInProperly() throws Exception {
        Assert.assertTrue(
                "Sign In button/login progress spinner are still visible",
                context.getPagesCollection().getPage(LoginPage.class).waitForLogin());
    }

    @Then("^the sign in error message reads (.*)")
    public void TheSignInErrorMessageReads(String message) throws Exception {
        assertThat("sign in error message",
                context.getPagesCollection().getPage(LoginPage.class)
                        .getErrorMessage(), equalTo(message));
    }

    @Then("^the email field on the sign in form is marked as error$")
    public void ARedDotIsShownOnTheEmailField() throws Exception {
        assertThat("Email field not marked",
                context.getPagesCollection().getPage(LoginPage.class)
                        .isEmailFieldMarkedAsError());
    }

    @Then("^the password field on the sign in form is marked as error$")
    public void ARedDotIsShownOnThePasswordField() throws Exception {
        assertThat("Password field not marked",
                context.getPagesCollection().getPage(LoginPage.class)
                        .isPasswordFieldMarkedAsError());
    }

    @When("^I enter email \"([^\"]*)\"$")
    public void IEnterEmail(String email) throws Exception {
        try {
            email = context.getUsersManager().findUserByEmailOrEmailAlias(email).getEmail();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        context.getPagesCollection().getPage(LoginPage.class).inputEmail(email);
    }

    @When("^I enter phone number \"([^\"]*)\"$")
    public void IEnterPhoneNumber(String phoneNumber) throws Exception {
        phoneNumber = context.getUsersManager().replaceAliasesOccurences(phoneNumber,
                ClientUsersManager.FindBy.PHONENUMBER_ALIAS);
        context.getPagesCollection().getPage(LoginPage.class).inputEmail(phoneNumber);
    }

    @When("^I enter password \"([^\"]*)\"$")
    public void IEnterPassword(String password) throws Exception {
        try {
            password = context.getUsersManager().findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        context.getPagesCollection().getPage(LoginPage.class).inputPassword(password);
    }

    @When("I (un)?check option to remember me")
    public void ICheckOptionToRememberMe(String uncheck) throws Exception {
        if (uncheck == null) {
            context.getPagesCollection().getPage(LoginPage.class).checkRememberMe();
        } else {
            context.getPagesCollection().getPage(LoginPage.class).uncheckRememberMe();
        }
    }

    @Given("^I see Sign In page$")
    public void ISeeSignInPage() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(LoginPage.class)
                .isSignInFormVisible());
    }

    @When("^I click Change Password button$")
    public void IClickChangePassword() throws Exception {
        context.getPagesCollection().getPage(LoginPage.class)
                .clickChangePasswordButton(context.getPagesCollection());
    }

    @When("^I switch to Change Password page$")
    public void ISwitchToChangePasswordPage() throws Exception {
        WebDriver driver = context.getDriver();
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(
                DriverUtils.getDefaultLookupTimeoutSeconds(), TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS);
        try {
            wait.until(drv -> {
                return (drv.getWindowHandles().size() > 1);
            });
        } catch (TimeoutException e) {
            throw new TimeoutException("No password change page was found", e);
        }
        Set<String> handles = driver.getWindowHandles();
        handles.remove(driver.getWindowHandle());
        driver.switchTo().window(handles.iterator().next());
    }

    @Then("^I see login error \"(.*)\"$")
    public void ISeeLoginError(String expectedError) throws Exception {
        final String loginErrorText = context.getPagesCollection().getPage(
                LoginPage.class).getErrorMessage();
        Assert.assertTrue(
                String.format(
                        "The actual login error '%s' is not equal to the expected one: '%s'",
                        loginErrorText, expectedError), loginErrorText
                        .equals(expectedError));
    }

    @When("^I switch to phone number sign in page$")
    public void i_switch_to_phone_number_sign_in_page() throws Throwable {
        context.getPagesCollection().getPage(LoginPage.class)
                .switchToPhoneNumberLoginPage();
    }

    @Given("^I open (.*) session expired login page$")
    public void i_open_session_expired_login_page(String lang) throws Throwable {
        String langKey;
        switch (lang) {
            case "english":
                langKey = "en";
                break;
            case "german":
                langKey = "de";
                break;
            default:
                throw new IllegalArgumentException("Please specify a language for the session expired login page");
        }
        context.getPagesCollection().getPage(LoginPage.class)
                .visitSessionExpiredPage(langKey);
    }

    @Then("^I verify session expired message is visible$")
    public void i_verify_session_expired_message_is_visible() throws Throwable {
        Assert.assertTrue("session expired message is not visible", context.getPagesCollection().getPage(LoginPage.class)
                .isSessionExpiredErrorMessageVisible());
    }

    @Then("^I verify session expired message is equal to (.*)$")
    public void i_verify_session_expired_message_is_x(String sessionExpiredMessage) throws Throwable {
        Assert.assertEquals("session expired message does not match expected value", sessionExpiredMessage,
                context.getPagesCollection().getPage(LoginPage.class)
                        .getSessionExpiredErrorMessage());
    }

    @Given("^I open (.*) login page as if I was redirected from get.wire.com$")
    public void IOpenLoginPageRedirected(String lang) throws Throwable {
        String langKey;
        switch (lang) {
            case "english":
                langKey = "en";
                break;
            case "german":
                langKey = "de";
                break;
            default:
                throw new IllegalArgumentException("Please specify a language for the login page");
        }
        context.getPagesCollection().getPage(LoginPage.class).visitRedirectedPage(langKey);
    }

    @Then("^I verify text about Wire is visible$")
    public void IVerifyTextAboutWireIsvisible() throws Throwable {
        Assert.assertTrue("description message is not visible on " + context.getDriver().getCurrentUrl(),
                context.getPagesCollection().getPage(LoginPage.class).isDescriptionMessageVisible());
    }

    @Then("^I see intro about Wire saying (.*)$")
    public void ISeeIntroAboutWireSayingX(String expectedIntro) throws Throwable {
        Assert.assertEquals("description message does not match expected value", expectedIntro,
                context.getPagesCollection().getPage(LoginPage.class)
                        .getDescriptionMessage());
    }

    @Given("^I switch to registration page$")
    public void ISwitchToRegistrationPage() throws Exception {
        context.getPagesCollection().getPage(LoginPage.class)
                .switchToRegistrationPage();
    }
}
