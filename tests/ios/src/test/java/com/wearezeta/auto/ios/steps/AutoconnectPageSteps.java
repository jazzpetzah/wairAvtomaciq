package com.wearezeta.auto.ios.steps;


import com.wearezeta.auto.common.misc.IOSDistributable;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.tools.ABProvisioner.ABContact;
import com.wearezeta.auto.ios.tools.ABProvisioner.ABProvisionerAPI;
import com.wearezeta.auto.common.driver.device_helpers.IOSSimulatorHelpers;
import cucumber.api.java.en.Given;

import java.util.*;


public class AutoconnectPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();
    private final ABProvisionerAPI addressbookProvisioner = ABProvisionerAPI.getInstance();

    /**
     * Installs the Addressbook helper app on to the simulator
     *
     * @throws Exception
     * @step. ^I install Address Book Helper app$
     */
    @Given("^I install Address Book Helper app$")
    public void IInstallAddressbookHelperApp() throws Exception {
        final IOSDistributable addressBookHelperDist =
                IOSDistributable.getInstance(CommonIOSSteps.getiOSAddressbookAppPath());
        // remove the previous version of this app if present
        pagesCollection.getCommonPage().uninstallApp(addressBookHelperDist.getBundleId());
        pagesCollection.getCommonPage().installApp(addressBookHelperDist.getAppRoot());
    }

    private static final String ADDRESSBOOK_APP_BUNDLE = "com.wire.addressbookautomation";

    /**
     * Launches the Addressbook Helper App on to the simluator
     *
     * @throws Exception
     * @step. ^I launch Address Book Helper app$
     */
    @Given("^I launch Address Book Helper app$")
    public void ILaunchAddressbookHelperApp() throws Exception {
        IOSSimulatorHelpers.launchApp(ADDRESSBOOK_APP_BUNDLE);
        Thread.sleep(2000);
        //To be sure its get pressed tap a second time, if it got pressed 1st time nothing will happen
        //there is no ui in the app...sometimes it fails here, so the second press
        for (int i = 0; i <= 1; i++) {
            IOSSimulatorHelpers.clickAt("0.68", "0.58", "1");
        }
    }

    /**
     * Addressbook helper app deletes all contacts from simulator Address Book
     *
     * @throws Exception
     * @step. ^I delete all contacts from Address Book$
     */
    @Given("^I delete all contacts from Address Book$")
    public void IDeleteAllContactsFromAddressbook() throws Exception {
        addressbookProvisioner.clearContacts();
    }

    /**
     * Uploads name and phone number of contact to the simulator addressbook
     *
     * @param name        name of contact to be added to addressbook
     * @param phoneNumber phone number of contact to be added to addressbook
     * @throws Exception
     * @step. ^I add name (.*) and phone (.*) to Address Book$
     */
    @Given("^I add name (.*) and phone (.*) to Address Book$")
    public void IAddNameAndPhoneToAddressBook(String name, String phoneNumber) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        phoneNumber = usrMgr.replaceAliasesOccurences(phoneNumber, ClientUsersManager.FindBy.PHONENUMBER_ALIAS);
        ABContact contact = new ABContact(name, Optional.empty(), Optional.of(Collections.singletonList(phoneNumber)));
        addressbookProvisioner.addContacts(Collections.singletonList(contact));
    }

    /**
     * Uploads name and email of contact to the simulator addressbook
     *
     * @param name  name of contact to be added to addressbook
     * @param email email of contact to be added to addressbook
     * @throws Throwable
     * @step. ^I add name (.*) and email (.*) to Address Book$
     */
    @Given("^I add name (.*) and email (.*) to Address Book$")
    public void IAddNameAndEmailToAddressBook(String name, String email) throws Throwable {
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        email = usrMgr.replaceAliasesOccurences(email, ClientUsersManager.FindBy.EMAIL_ALIAS);
        ABContact contact = new ABContact(name, Optional.of(Collections.singletonList(email)), Optional.empty());
        addressbookProvisioner.addContacts(Collections.singletonList(contact));
    }
}