package com.wearezeta.auto.win.steps.webapp;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.pages.LoginPage;
import com.wearezeta.auto.web.pages.RegistrationPage;
import com.wearezeta.auto.win.common.WrapperTestContext;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class RegistrationPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(RegistrationPageSteps.class.getName());
    private ClientUser userToRegister = null;
    private Future<String> activationMessage;
    public static final int maxCheckCnt = 2;
    private final WrapperTestContext context;

    public RegistrationPageSteps() {
        this.context = new WrapperTestContext();
    }

    public RegistrationPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @When("^I enter user name (.*) on Registration page$")
    public void IEnterName(String name) throws Exception {
        context.getWebappPagesCollection().getPage(RegistrationPage.class).waitForRegistrationPageToFullyLoad();
        try {
            this.userToRegister = context.getUserManager().findUserByNameOrNameAlias(name);
        } catch (NoSuchUserException e) {
            if (this.userToRegister == null) {
                this.userToRegister = new ClientUser();
            }
            this.userToRegister.setName(name);
            this.userToRegister.clearNameAliases();
            this.userToRegister.addNameAlias(name);
        }
        context.getWebappPagesCollection().getPage(RegistrationPage.class).enterName(this.userToRegister.getName());
    }

    @When("^I enter user email (.*) on Registration page$")
    public void IEnterEmail(String email) throws Exception {
        boolean flag = false;
        try {
            String realEmail = context.getUserManager().findUserByEmailOrEmailAlias(email).getEmail();
            this.userToRegister.setEmail(realEmail);
        } catch (NoSuchUserException e) {
            if (this.userToRegister == null) {
                this.userToRegister = new ClientUser();
            }
            flag = true;
        }
        if (flag) {
            context.getWebappPagesCollection().getPage(RegistrationPage.class).enterEmail(email);
        } else {
            context.getWebappPagesCollection().getPage(RegistrationPage.class).enterEmail(this.userToRegister.getEmail());
        }
    }

    @When("^I enter user password \"(.*)\" on Registration page$")
    public void IEnterPassword(String password) throws Exception {
        try {
            this.userToRegister.setPassword(context.getUserManager().findUserByPasswordAlias(password).getPassword());
        } catch (NoSuchUserException e) {
            this.userToRegister.setPassword(password);
            this.userToRegister.addPasswordAlias(password);
        }
        context.getWebappPagesCollection().getPage(RegistrationPage.class).enterPassword(this.userToRegister.getPassword());
    }

    @When("^I accept the Terms of Use$")
    public void IAcceptTermsOfUse() throws Exception {
        context.getWebappPagesCollection().getPage(RegistrationPage.class).acceptTermsOfUse();
    }

    @When("^I submit registration form$")
    public void ISubmitRegistration() throws Exception {
        context.getWebappPagesCollection().getPage(RegistrationPage.class).submitRegistration();
    }

    @When("^I start activation email monitoring$")
    public void IStartActivationEmailMonitoring() throws Exception {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
        this.activationMessage = IMAPSMailbox.getInstance(userToRegister.getEmail(), userToRegister.getPassword()).getMessage(
                expectedHeaders, BackendAPIWrappers.ACTIVATION_TIMEOUT);
    }

    @Then("^I see email (.*) on [Vv]erification page$")
    public void ISeeVerificationEmail(String email) throws Exception {
        email = context.getUserManager().findUserByEmailOrEmailAlias(email).getEmail();
        assertThat(context.getWebappPagesCollection().getPage(RegistrationPage.class).getVerificationEmailAddress(),
                containsString(email));
    }

    @Then("^I see error \"(.*)\" on [Vv]erification page$")
    public void ISeeErrorMessageOnVerificationPage(String message) throws Throwable {
        assertThat(context.getWebappPagesCollection().getPage(RegistrationPage.class).getErrorMessages(), hasItem(message));
    }

    @Then("^I verify that a red dot is( not)? shown inside the email field on the registration form$")
    public void ARedDotIsShownOnTheEmailField(String not) throws Exception {
        if (not == null) {
            assertThat("Red dot on email field", context.getWebappPagesCollection().getPage(RegistrationPage.class).
                    isEmailFieldMarkedAsError());
        } else {
            assertThat("Red dot on email field", context.getWebappPagesCollection().getPage(RegistrationPage.class).
                    isEmailFieldMarkedAsValid());
        }
    }

    @Then("^I verify that an envelope icon is shown$")
    public void IVerifyThatAnEnvelopeIconIsShown() throws Exception {
        assertThat("Envelope icon not shown", context.getWebappPagesCollection().getPage(RegistrationPage.class).
                isEnvelopeShown());
    }

    @Then("^I verify registration email$")
    public void IVerifyRegistrationEmail() throws Exception {
        BackendAPIWrappers.activateRegisteredUserByEmail(this.activationMessage);
    }

    @Then("^I activate user by URL$")
    public void WhenIActivateUserByUrl() throws Exception {
        final String link = BackendAPIWrappers.getUserActivationLink(this.activationMessage);
        LOG.info("Get activation link from " + link);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(link);
        HttpEntity entity = httpclient.execute(httpGet).getEntity();
        if (entity != null) {
            String content = EntityUtils.toString(entity);
            Pattern p = Pattern.compile("data-url=\"(.*?)\"");
            Matcher m = p.matcher(content);
            while (m.find()) {
                String activationLink = m.group(1);
                LOG.info("Activation link: " + activationLink);
                httpGet = new HttpGet(activationLink);
                httpclient.execute(httpGet);
            }
        }

        // indexes in aliases start from 1
        final int userIndex = context.getUserManager().appendCustomUser(userToRegister) + 1;
        userToRegister.addEmailAlias(ClientUsersManager.EMAIL_ALIAS_TEMPLATE.apply(userIndex));
        userToRegister.addNameAlias(ClientUsersManager.NAME_ALIAS_TEMPLATE.apply(userIndex));
        userToRegister.addPasswordAlias(ClientUsersManager.PASSWORD_ALIAS_TEMPLATE.apply(userIndex));
        context.getWebappPagesCollection().getPage(LoginPage.class).waitForLogin();
    }

    @Given("^I switch to [Ss]ign [Ii]n page$")
    public void ISwitchToLoginPage() throws Exception {
        context.getWebappPagesCollection().getPage(RegistrationPage.class).switchToLoginPage();
    }

    @Then("^I click on Verify later button on Verification page$")
    public void IClickVerifyLaterButton() throws Exception {
        context.getWebappPagesCollection().getPage(RegistrationPage.class).clickVerifyLaterButton();
    }
}
