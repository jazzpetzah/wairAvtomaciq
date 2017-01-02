package com.wearezeta.auto.win.steps.webapp;

import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.external.GoogleLoginPage;
import cucumber.api.java.en.When;
import org.openqa.selenium.WebDriverException;

public class StartUIPageSteps {

    private final WebAppTestContext webContext;

    public StartUIPageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }

    @When("^I sign up at Google with email (.*) and password (.*)$")
    public void ISignUpAtGoogleWithEmail(String email, String password) throws Exception {
        GoogleLoginPage googleLoginPage = webContext.getPagesCollection().getPage(GoogleLoginPage.class);
        // we use callable to handle exceptions
        googleLoginPage.waitForWindowClose(() -> {
            // sometimes Google already shows the email
            googleLoginPage.setEmail(email);
            // sometimes google shows a next button and you have to enter the
            // password separately
            if (googleLoginPage.hasNextButton()) {
                googleLoginPage.clickNext();
            }
            googleLoginPage.setPassword(password);
            // sign in might be the last action
            googleLoginPage.clickSignIn();
            try {
                // sometimes we have to allow requested permissions
                if (googleLoginPage.hasApproveButton()) {
                    googleLoginPage.clickApprove();
                }
            } catch (WebDriverException ex) {
                // NOOP window already closed
            }
            // in order to handle exceptions we can't use Runnable thus we have to return something
            return true;
        });
    }
}
