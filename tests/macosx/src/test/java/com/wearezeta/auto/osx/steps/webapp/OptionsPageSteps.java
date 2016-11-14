package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.osx.pages.webapp.OptionsPage;
import com.wearezeta.auto.web.common.TestContext;
import cucumber.api.java.en.When;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class OptionsPageSteps {
    
    private final TestContext webContext;
    private final TestContext wrapperContext;

    public OptionsPageSteps() {
        this.webContext = new TestContext();
        this.wrapperContext = new TestContext();
    }
    
    public OptionsPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @When("^I set sound alerts setting to all$")
    public void ISetSoundAlertsAll() throws Exception {
        webContext.getPagesCollection().getPage(OptionsPage.class).clickSoundAlertsAll();
    }

    @When("^I set sound alerts setting to some$")
    public void ISetSoundAlertsSome() throws Exception {
        webContext.getPagesCollection().getPage(OptionsPage.class).clickSoundAlertsSome();
    }

    @When("^I set sound alerts setting to none$")
    public void ISetSoundAlertsNone() throws Exception {
        webContext.getPagesCollection().getPage(OptionsPage.class).clickSoundAlertsNone();
    }

    @When("^I see Sound Alerts setting is set to (.*)$")
    public void ISeeSoundAlertsSettingIs(String value) throws Exception {
        assertThat("Sound alerts setting",
                webContext.getPagesCollection().getPage(OptionsPage.class).getSelectedSoundAlertsSetting(), equalTo(value));
    }

    @When("^I click button to import contacts from Gmail$")
    public void IClickImportButton() throws Exception {
        webContext.getPagesCollection().getPage(OptionsPage.class).clickImportButton();
    }
    
    @When("^I click button to import contacts from address book via preferences$")
    public void IClickAddressbookImportButton() throws Exception {
        webContext.getPagesCollection().getPage(OptionsPage.class).clickImportAddressbookButton();
    }

}
