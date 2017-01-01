package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.AccountDeletionMessage;
import com.wearezeta.auto.common.email.WireMessage;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.SettingsPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class SettingsPageSteps {
    private SettingsPage getSettingsPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(SettingsPage.class);
    }

    /**
     * Verify whether Settings page is visible
     *
     * @throws Exception
     * @step. ^I see settings page$
     */
    @Then("^I see settings page$")
    public void ISeeSettingsPage() throws Exception {
        Assert.assertTrue("Settings page is not visible", getSettingsPage().waitUntilVisible());
    }

    /**
     * Select the corresponding Settings menu item
     *
     * @param itemName the name of an item
     * @throws Exception
     * @step. ^I select settings item (.*)
     */
    @When("^I select settings item (.*)")
    public void ISelectItem(String itemName) throws Exception {
        getSettingsPage().selectItem(itemName);
    }

    /**
     * Verify the current value of a setting
     *
     * @param itemName      setting option name
     * @param expectedValue the expected value. Can be user name/email/phone number alias
     * @throws Exception
     * @step. ^I verify the value of settings item (.*) equals to "(.*)"
     */
    @Then("^I verify the value of settings item (.*) equals to \"(.*)\"")
    public void IVerifySettingsItemValue(String itemName, String expectedValue) throws Exception {
        expectedValue = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(expectedValue, ClientUsersManager.FindBy.EMAIL_ALIAS);
        expectedValue = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(expectedValue, ClientUsersManager.FindBy.NAME_ALIAS);
        expectedValue = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(expectedValue, ClientUsersManager.FindBy.PHONENUMBER_ALIAS);
        Assert.assertTrue(String.format("The value of '%s' setting item is not equal to '%s'", itemName, expectedValue),
                getSettingsPage().isSettingItemValueEqualTo(itemName, expectedValue));
    }

    /**
     * Verify whether the corresponding settings menu item is visible
     *
     * @param itemName the expected item name
     * @throws Exception
     * @step. ^I (dont )?see settings item (.*)$
     */
    @Then("^I (do not )?see settings item (.*)$")
    public void ISeeSettingsItem(String shouldNot, String itemName) throws Exception {
        if (shouldNot == null) {
            Assert.assertTrue(String.format("Settings menu item '%s' is not visible", itemName),
                    getSettingsPage().isItemVisible(itemName));
        } else {
            Assert.assertTrue(String.format("Settings menu item %s is visible", itemName),
                    getSettingsPage().isItemInvisible(itemName));
        }
    }

    /**
     * Verify Device label (Verified|Not Verified)
     *
     * @param label      label of device
     * @param deviceName name of device
     * @throws Exception
     * @step. ^I see the label (Verified|Not Verified) is shown for the device (.*)$
     */
    @Then("^I see the label (Verified|Not Verified) is shown for the device (.*)$")
    public void ISeeForDeviceALabelB(String label, String deviceName) throws Exception {
        Assert.assertTrue(String.format("Label '%s' is not visible for device '%s'", label, deviceName), getSettingsPage()
                .isVerificationLabelVisible(deviceName, label));

    }

    private Future<String> accountRemovalConfirmation;

    /**
     * Start monitoring for account removal email confirmation
     *
     * @param name user name/alias
     * @throws Exception
     * @step. ^I start waiting for (.*) account removal notification$
     */
    @When("^I start waiting for (.*) account removal notification$")
    public void IStartWaitingForAccountRemovalConfirmation(String name) throws Exception {
        final ClientUser forUser = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .findUserByNameOrNameAlias(name);
        Map<String, String> additionalHeaders = new HashMap<>();
        additionalHeaders.put(WireMessage.ZETA_PURPOSE_HEADER_NAME, AccountDeletionMessage.MESSAGE_PURPOSE);
        accountRemovalConfirmation = BackendAPIWrappers.initMessageListener(forUser, additionalHeaders);
    }

    /**
     * Make sure the account removal link is received
     *
     * @throws Exception
     * @step. ^I verify account removal notification is received$
     */
    @Then("^I verify account removal notification is received$")
    public void IVerifyAccountRemovalNotificationIsReceived() throws Exception {
        if (accountRemovalConfirmation == null) {
            throw new IllegalStateException("Please init email confirmation listener first");
        }
        new AccountDeletionMessage(accountRemovalConfirmation.get());
    }

    private ElementState previousProfilePictureScreenshot = new ElementState(
            () -> getSettingsPage().takeScreenshot().
                    orElseThrow(() -> new IllegalStateException("Cannot take a screenshot of self profile page"))
    );

    /**
     * Take a screenshot of self profile page and save it into internal var
     *
     * @throws Exception
     * @step. ^I remember my current profile picture$
     */
    @When("^I remember my current profile picture$")
    public void IRememberMyProfilePicture() throws Exception {
        previousProfilePictureScreenshot.remember();
    }

    @Then("I wait up to (\\d+) seconds? until my profile picture is changed")
    public void IWaitUntilProfilePictureIsChanged(int secondsTimeout) throws Exception {
        if (previousProfilePictureScreenshot == null) {
            throw new IllegalStateException("Please take a screenshot of previous profile picture first");
        }
        final double minScore = 0.87;
        Assert.assertTrue("The previous and the current profile pictures seem to be the same",
                this.previousProfilePictureScreenshot.isChanged(Timedelta.fromSeconds(secondsTimeout), minScore));
    }

    /**
     * Tap navigation button on Setitngs page
     *
     * @param name name of the button
     * @throws Exception
     * @step. ^I tap (Done|Back|Edit) navigation button on Settings page$
     */
    @And("^I tap (Done|Back|Edit) navigation button on Settings page$")
    public void ITapNavigationButton(String name) throws Exception {
        getSettingsPage().tapNavigationButton(name);
    }

    /**
     * Verify whether Reset Password page is opened in browser
     *
     * @throws Exception
     * @step. ^I see Reset Password page$
     */
    @Then("^I see Reset Password page$")
    public void ISeeResetPasswordPage() throws Exception {
        Assert.assertTrue("Change Password button is not shown", getSettingsPage().isResetPasswordPageVisible());
    }

    /**
     * Verifies that it sees the Support web page
     *
     * @throws Exception
     * @step. ^I see Support web page$
     */
    @Then("^I see Support web page$")
    public void ISeeSupportWebPage() throws Exception {
        Assert.assertTrue("Customer support page has not been loaded", getSettingsPage().isSupportWebPageVisible());
    }

    /**
     * Resets the content of Self Name input
     *
     * @throws Exception
     * @step. ^I clear Name input field on Settings page$
     */
    @When("^I clear Name input field on Settings page$")
    public void IClearSelfName() throws Exception {
        getSettingsPage().clearSelfName();
    }

    /**
     * Sets the value of Self Name input to a new one
     *
     * @param newValue can be also an alias
     * @throws Exception
     * @step. ^I set "(.*)" value to Name input field on Settings page$
     */
    @When("^I set \"(.*)\" value to Name input field on Settings page$")
    public void ISetSelfName(String newValue) throws Exception {
        newValue = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(newValue, ClientUsersManager.FindBy.NAME_ALIAS);
        getSettingsPage().setSelfName(newValue);
    }

    private ElementState colorPickerState = new ElementState(
            () -> getSettingsPage().getColorPickerStateScreenshot()
    );

    private static final Timedelta COLOR_PICKER_STATE_CHANGE_TIMEOUT = Timedelta.fromSeconds(10);
    private static final double MIN_COLOR_PICKER_SIMILARITY_SCORE = 0.999;

    /**
     * Get and remember the screenshot of People Picker
     *
     * @throws Exception
     * @step. ^I remember the state of Color Picker$
     */
    @When("^I remember the state of Color Picker$")
    public void IRememberColorPickerState() throws Exception {
        colorPickerState.remember();
    }

    /**
     * Verify that color picker state has been changed
     *
     * @throws Exception
     * @step. ^I verify the state of Color Picker is changed$
     */
    @Then("^I verify the state of Color Picker is changed$")
    public void IVerifyColorPickerState() throws Exception {
        Assert.assertTrue("Color Picker state has not been changed",
                colorPickerState.isChanged(COLOR_PICKER_STATE_CHANGE_TIMEOUT, MIN_COLOR_PICKER_SIMILARITY_SCORE));
    }

    /**
     * Changes the accent color by clicking the color picker
     *
     * @param color one of possible color values
     * @throws Exception
     * @step. ^I set my accent color to (StrongBlue|StrongLimeGreen|BrightYellow|VividRed|BrightOrange|SoftPink|Violet)
     * on Settings page$$
     */
    @When("^I set my accent color to (StrongBlue|StrongLimeGreen|BrightYellow|VividRed|BrightOrange|SoftPink|Violet)" +
            " on Settings page$")
    public void IChangeMyAccentColor(String color) throws Exception {
        getSettingsPage().selectAccentColor(AccentColor.getByName(color));
    }

    /**
     * Verify expected unique username is displayed on Settings page
     *
     * @param wasSet detect if unique name was previously set
     * @param name   unique name or name alias
     * @throws Exception
     * @step. ^I see (previously set|"(.*)") unique username is displayed on Settings Page$
     */
    @Then("^I see (previously set|\"(.*)\") unique username is displayed on Settings Page$")
    public void ISeeUniqueUsernameOnSettingsPage(String wasSet, String name) throws Exception {
        if (wasSet.equals("previously set")) {
            name = UniqueUsernamePageSteps.getNewUniqueName();
            Assert.assertTrue(String.format("New previously set unique username %s is not displayed on Settings Page", name),
                    getSettingsPage().isUniqueUsernameInSettingsDisplayed(name));
        } else {
            name = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                    .replaceAliasesOccurences(name, ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
            Assert.assertTrue(String.format("New previously set unique username %s is not displayed on Settings Page", name),
                    getSettingsPage().isUniqueUsernameInSettingsDisplayed(name));
        }
    }
}
