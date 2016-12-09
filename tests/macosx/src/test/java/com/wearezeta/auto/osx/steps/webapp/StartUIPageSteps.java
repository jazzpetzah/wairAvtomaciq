package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.osx.pages.webapp.StartUIPage;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.external.GoogleLoginPage;
import cucumber.api.java.en.When;
import org.openqa.selenium.WebDriverException;

public class StartUIPageSteps {

    private final TestContext webContext;
    
    public StartUIPageSteps(TestContext webContext) {
        this.webContext = webContext;
    }

    @When("^I click button to import contacts from address book via search UI")
    public void IClickAddressbookImportButton() throws Exception {
        webContext.getPagesCollection().getPage(StartUIPage.class).clickImportAddressbookButton();
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
