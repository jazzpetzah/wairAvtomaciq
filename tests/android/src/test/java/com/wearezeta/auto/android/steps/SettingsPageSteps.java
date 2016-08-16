package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.SettingsPage;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.Optional;

public class SettingsPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

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
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.EMAIL_ALIAS);
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.PHONENUMBER_ALIAS);
        getSettingsPage().selectMenuItem(name);
    }

    /**
     * Verify whether the particular settings menu item is visible or not
     *
     * @param shouldNotSee equals to null if the corresponding menu item should be visible
     * @param name         the name of the corresponding menu item
     * @throws Exception
     * @step. ^I (do not )?see \"(.*)\" settings menu item$
     */
    @When("^I (do not )?see \"(.*)\" settings menu item$")
    public void ISeeSettingsMenuItem(String shouldNotSee, String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.EMAIL_ALIAS);
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.PHONENUMBER_ALIAS);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("Settings menu item '%s' is not visible", name),
                    getSettingsPage().waitUntilMenuItemVisible(name));
        } else {
            Assert.assertTrue(String.format("Settings menu item '%s' is visible, but should be hidden", name),
                    getSettingsPage().waitUntilMenuItemInvisible(name));
        }
    }

    /**
     * Click the corresponding button on sign out alert to confirm it
     *
     * @throws Exception
     * @step. ^I confirm sign out$
     */
    @And("^I confirm sign out$")
    public void IConfirmSignOut() throws Exception {
        getSettingsPage().confirmLogout();
    }

    /**
     * Type the password into the confirmation dialog
     *
     * @param passwordAlias password string or an alias
     * @throws Exception
     * @step. ^I enter (.*) into the device removal password confirmation dialog$
     */
    @When("^I enter (.*) into the device removal password confirmation dialog$")
    public void IEnterPassword(String passwordAlias) throws Exception {
        final String password = usrMgr.replaceAliasesOccurences(passwordAlias,
                ClientUsersManager.FindBy.PASSWORD_ALIAS);
        getSettingsPage().enterConfirmationPassword(password);
    }

    /**
     * Tap OK button on the device removal password confirmation dialog
     *
     * @throws Exception
     * @step. ^I tap OK button on the device removal password confirmation dialog$
     */
    @And("^I tap OK button on the device removal password confirmation dialog$")
    public void ITapOKButtonOnPasswordConfirmationDialog() throws Exception {
        getSettingsPage().tapOKButtonOnPasswordConfirmationDialog();
    }

    /**
     * Tap on current device in Devices settings menu
     *
     * @throws Exception
     * @step. ^I tap current device in devices settings menu$
     */
    @And("^I tap current device in devices settings menu$")
    public void ITapCurrentDeviceInDevicesSettingsMenu() throws Exception {
        getSettingsPage().tapCurrentDevice();
    }

    /**
     * Enter new value into the corresponding user settings input dialog and commit it
     *
     * @param what     either name|email|phone number
     * @param newValue the new self user name/email or phone number
     * @throws Exception
     * @step. ^I commit my new (name|email|phone number) "(.*)"( with password (.*))?$
     */
    @And("^I commit my new (name|email|phone number) \"(.*)\"( with password (.*))?$")
    public void ICommitNewUSerName(String what, String newValue, String withPassword, String password) throws Exception {
        final ClientUser self = usrMgr.getSelfUserOrThrowError();
        password = withPassword != null ?
                usrMgr.replaceAliasesOccurences(password, ClientUsersManager.FindBy.PASSWORD_ALIAS) : null;

        switch (what) {
            case "name":
                newValue = usrMgr.replaceAliasesOccurences(newValue, ClientUsersManager.FindBy.NAME_ALIAS);
                getSettingsPage().commitNewName(newValue);
                self.setName(newValue);
                break;
            case "email":
                newValue = usrMgr.replaceAliasesOccurences(newValue, ClientUsersManager.FindBy.EMAIL_ALIAS);
                getSettingsPage().commitNewEmailWithPassword(newValue, Optional.ofNullable(password));
                self.setEmail(newValue);
                if (password != null) {
                    self.setPassword(password);
                }
                break;
            case "phone number":
                newValue = usrMgr.replaceAliasesOccurences(newValue, ClientUsersManager.FindBy.PHONENUMBER_ALIAS);
                final PhoneNumber newNumber = new PhoneNumber(PhoneNumber.WIRE_COUNTRY_PREFIX,
                        newValue.replace(PhoneNumber.WIRE_COUNTRY_PREFIX, ""));
                getSettingsPage().commitNewPhoneNumber(newNumber);
                self.setPhoneNumber(newNumber);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown property '%s'", what));
        }
    }

    /**
     * Get the verification code for a phone number and types it into the corresponding
     * UI field
     *
     * @param number phone number to use
     * @throws Exception
     * @step. ^I commit verification code for phone number (.*)
     */
    @And("^I commit verification code for phone number (.*)")
    public void ICommitVerificationCodeForPhoneNumber(String number) throws Exception {
        number = usrMgr.replaceAliasesOccurences(number, ClientUsersManager.FindBy.PHONENUMBER_ALIAS);
        final String activationCode = BackendAPIWrappers.getActivationCodeByPhoneNumber(new PhoneNumber(
                PhoneNumber.WIRE_COUNTRY_PREFIX, number.replace(PhoneNumber.WIRE_COUNTRY_PREFIX, "")
        ));
        getSettingsPage().commitPhoneNumberVerificationCode(activationCode);
    }
}
