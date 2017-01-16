package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.OptionsPage;
import cucumber.api.java.en.When;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class OptionsPageSteps {

    private final WebAppTestContext context;

    public OptionsPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @When("^I set sound alerts setting to all$")
    public void ISetSoundAlertsAll() throws Exception {
        context.getPagesCollection().getPage(OptionsPage.class).clickSoundAlertsAll();
    }

    @When("^I set sound alerts setting to some$")
    public void ISetSoundAlertsSome() throws Exception {
        context.getPagesCollection().getPage(OptionsPage.class).clickSoundAlertsSome();
    }

    @When("^I set sound alerts setting to none$")
    public void ISetSoundAlertsNone() throws Exception {
        context.getPagesCollection().getPage(OptionsPage.class).clickSoundAlertsNone();
    }

    @When("^I see Sound Alerts setting is set to (.*)$")
    public void ISeeSoundAlertsSettingIs(String value) throws Exception {
        assertThat("Sound alerts setting",
                context.getPagesCollection().getPage(OptionsPage.class).getSelectedSoundAlertsSetting(), equalTo(value));
    }

    @When("^I click button to import contacts from Gmail$")
    public void IClickImportButton() throws Exception {
        context.getPagesCollection().getPage(OptionsPage.class).clickImportButton();
    }

    @When("^I see option to send reports is (checked|unchecked)$")
    public void ISeeOptionToSendReports(String checked) throws Exception {
        boolean isChecked = "checked".equals(checked);
        if (isChecked) {
            assertTrue("Checkbox is not checked", context.getPagesCollection().getPage(OptionsPage.class).isReportOptionChecked());
        } else {
            assertFalse("Checkbox is checked",context.getPagesCollection().getPage(OptionsPage.class).isReportOptionChecked());
        }
    }

    @When("^I click on option to send reports$")
    public void ICheckOptionToSendReports() throws Exception {
        context.getPagesCollection().getPage(OptionsPage.class).clickReportOption();
    }

}
