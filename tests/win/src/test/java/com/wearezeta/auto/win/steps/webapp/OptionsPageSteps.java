package com.wearezeta.auto.win.steps.webapp;

import com.wearezeta.auto.win.common.WrapperTestContext;
import com.wearezeta.auto.win.pages.webapp.OptionsPage;
import cucumber.api.java.en.When;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class OptionsPageSteps {

    private final WrapperTestContext context;

    public OptionsPageSteps() {
        this.context = new WrapperTestContext();
    }

    public OptionsPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @When("^I set sound alerts setting to all$")
    public void ISetSoundAlertsAll() throws Exception {
        context.getWebappPagesCollection().getPage(OptionsPage.class).clickSoundAlertsAll();
    }

    @When("^I set sound alerts setting to some$")
    public void ISetSoundAlertsSome() throws Exception {
        context.getWebappPagesCollection().getPage(OptionsPage.class).clickSoundAlertsSome();
    }

    @When("^I set sound alerts setting to none$")
    public void ISetSoundAlertsNone() throws Exception {
        context.getWebappPagesCollection().getPage(OptionsPage.class).clickSoundAlertsNone();
    }

    @When("^I see Sound Alerts setting is set to (.*)$")
    public void ISeeSoundAlertsSettingIs(String value) throws Exception {
        assertThat("Sound alerts setting", context.getWebappPagesCollection().getPage(OptionsPage.class).
                getSelectedSoundAlertsSetting(), equalTo(value));
    }

    @When("^I click button to import contacts from Gmail$")
    public void IClickImportButton() throws Exception {
        context.getWebappPagesCollection().getPage(OptionsPage.class).clickImportButton();
    }

    @When("^I click button to import contacts from address book via preferences$")
    public void IClickAddressbookImportButton() throws Exception {
        context.getWebappPagesCollection().getPage(OptionsPage.class).clickImportAddressbookButton();
    }

}
