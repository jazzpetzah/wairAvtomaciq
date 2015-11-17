package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.common.ImageUtil;
import org.junit.Assert;

import com.wearezeta.auto.android.pages.SettingsPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.awt.image.BufferedImage;

public class SettingsPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private SettingsPage getSettingsPage() throws Exception {
        return pagesCollection.getPage(SettingsPage.class);
    }

    /**
     * Checks to see that the settings page is visible
     *
     * @throws Exception
     * @step. ^I see settings page$
     */
    @Then("^I see settings page$")
    public void ISeeSettingsPage() throws Exception {
        Assert.assertTrue("Settings page is not visible", getSettingsPage()
                .waitUntilVisible());
    }

    /**
     * Tap the corresponding menu item
     *
     * @param name the name of the corresponding menu item
     * @throws Exception
     * @step. ^I select \"(.*)\" settings menu item$
     */
    @When("^I select \"(.*)\" settings menu item$")
    public void ISelectSettingsMenuItem(String name) throws Exception {
        getSettingsPage().selectMenuItem(name);
    }

    /**
     * Click the corresponding button on sign out alert to confirm it
     *
     * @throws Exception
     * @step. ^I confirm sign out$
     */
    @And("^I confirm sign out$")
    public void IConfirmSignOut() throws Exception {
        getSettingsPage().confirmSignOut();
    }

    private BufferedImage previousThemeSwitcherState = null;

    /**
     * Store the current value of Theme setting into variable
     *
     * @throws Exception
     * @step. ^I remember the value of "Theme" setting$
     */
    @And("^I remember the value of \"Theme\" setting$")
    public void IRememberValueOfThemeSetting() throws Exception {
        previousThemeSwitcherState = getSettingsPage().getThemeSwitcherState().orElseThrow(
                IllegalStateException::new);
    }

    /**
     * Verify whether the value of Theme setting has been changed since the list snapshot
     *
     * @throws Exception
     * @step. ^I verify the value of "Theme" setting is changed$
     */
    @Then("^I verify the value of \"Theme\" setting is changed$")
    public void IVerifyValueOfThemeSettingIsChanged() throws Exception {
        if (previousThemeSwitcherState == null) {
            throw new IllegalStateException("Please remember the previous value of Theme setting first");
        }
        final BufferedImage currentThemeSwitcherState = getSettingsPage().getThemeSwitcherState().orElseThrow(
                IllegalStateException::new);
        final double similarity = ImageUtil.getOverlapScore(currentThemeSwitcherState, previousThemeSwitcherState,
                ImageUtil.RESIZE_TO_MAX_SCORE);
        Assert.assertTrue(String.format(
                "The current Theme setting value has not been changed since the last snapshot was taken (%.2f >= %.2f)",
                similarity, 0.97),
                similarity < 0.97);
    }

    /**
     * Switch current theme. The chooser itself should be already visible
     *
     * @throws Exception
     * @step. ^I switch color theme in settings$
     */
    @And("^I switch color theme in settings$")
    public void ISelectUnselectedTheme() throws Exception {
        getSettingsPage().switchTheme();
    }

}
