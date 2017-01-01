package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.external.VerifyPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class VerifyPageSteps {

    private static final Logger log = ZetaLogger.getLog(VerifyPageSteps.class.getSimpleName());

    private final WebAppTestContext context;
    private String phoneVerificationCode;

    public VerifyPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @When("^I go to verify page for (.*)$")
    public void IGoToVerifyPage(String agent) throws Exception {
        final String website = CommonUtils.getAccountPagesFromConfig(VerifyPageSteps.class);
        context.getPagesCollection().getPage(VerifyPage.class).setUrl(website + "verify/?success&agent=" + agent);
        context.getPagesCollection().getPage(VerifyPage.class).navigateTo();
    }

    @When("^I go to broken verify page for (.*)$")
    public void IGoToBrokenVerifyPage(String agent) throws Exception {
        final String accountPage = CommonUtils.getAccountPagesFromConfig(VerifyPageSteps.class);
        log.info("Account page: " + accountPage);
        context.getPagesCollection().getPage(VerifyPage.class).setUrl(accountPage + "verify/?agent=" + agent);
        context.getPagesCollection().getPage(VerifyPage.class).navigateTo();
    }

    @When("^I generate verification code for user (.*)$")
    public void IGenerateVerificationCode(String name) throws Exception {
        ClientUser user = context.getUserManager().findUserByNameOrNameAlias(name);
        phoneVerificationCode = BackendAPIWrappers.getActivationCodeByPhoneNumber(user.getPhoneNumber());
    }

    @When("^I go to phone verification page for (.*)$")
    public void IGoToPhoneVerificationPage(String agent) throws Exception {
        final String website = CommonUtils.getAccountPagesFromConfig(VerifyPageSteps.class);
        context.getPagesCollection().getPage(VerifyPage.class).setUrl(website + "v/" + phoneVerificationCode + "?agent="
                + agent);
        context.getPagesCollection().getPage(VerifyPage.class).navigateTo();
    }

    @When("^I see 'Open Wire' button with verification code$")
    public void IGoToPhoneVerificationPage() throws Exception {
        assertThat("Phone Verification", context.getPagesCollection().getPage(VerifyPage.class).getWireUrlFromButton(),
                is("wire://verify-phone/" + phoneVerificationCode));
    }

    @Then("^I see download button for (.*)$")
    public void ISeeDownloadButton(String agent) throws Exception {
        String downloadLink = "";
        switch (agent) {
            case "iphone":
                downloadLink = "https://itunes.apple.com/app/wire/id930944768?mt=8";
                break;
            case "android":
                downloadLink = "https://play.google.com/store/apps/details?id=com.wire";
                break;
            case "osx":
                downloadLink = "https://itunes.apple.com/app/wire/id931134707?mt=12";
                break;
            case "windows":
                final String website = CommonUtils.getWebsitePathFromConfig(VerifyPageSteps.class);
                downloadLink = website + "download/";
                break;
            default:
                break;
        }
        assertThat(context.getPagesCollection().getPage(VerifyPage.class).getDownloadUrl(agent), equalTo(downloadLink));
    }

    @Then("^I see webapp button$")
    public void ISeeWebappButton() throws Exception {
        assertThat(context.getPagesCollection().getPage(VerifyPage.class).getWebappUrl(), equalTo(
                "https://wire-webapp-staging.zinfra.io/"));
    }

    @Then("^I see error message$")
    public void ISeeErrorMessage() throws Exception {
        assertThat("No error message on " + context.getDriver().getCurrentUrl(),
                context.getPagesCollection().getPage(VerifyPage.class).isErrorMessageVisible(), is(true));
    }

}
