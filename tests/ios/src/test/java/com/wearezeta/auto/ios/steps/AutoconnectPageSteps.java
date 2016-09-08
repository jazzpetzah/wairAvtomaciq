package com.wearezeta.auto.ios.steps;


import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.RegistrationStrategy;
import com.wearezeta.auto.ios.pages.ConversationsListPage;
import com.wearezeta.auto.ios.tools.ABProvisioner.ABContact;
import com.wearezeta.auto.ios.tools.ABProvisioner.ABProvisionerAPI;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.junit.Assert;

import java.io.File;
import java.util.*;


public class AutoconnectPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();
    private final ABProvisionerAPI addressbookProvisioner = ABProvisionerAPI.getInstance();
    final CommonIOSSteps commonIOSSteps = new CommonIOSSteps();

    private ConversationsListPage getConversationsListPage() throws Exception {
        return pagesCollection.getPage(ConversationsListPage.class);
    }

    /**
     * Installs the Addressbook helper app on to the simulator
     *
     * @throws Exception
     * @step. ^I install Address Book Helper app$
     */
    @Given("^I install Address Book Helper app$")
    public void IInstallAddressbookHelperApp() throws Exception {
        final File app = new File(commonIOSSteps.getiOSAddressbookAppPath());
        pagesCollection.getCommonPage().installIpa(app);
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
        IOSSimulatorHelper.launchApp(ADDRESSBOOK_APP_BUNDLE);
        Thread.sleep(2000);
        //To be sure its get pressed tap a second time, if it got pressed 1st time nothing will happen
        //there is no ui in the app...sometimes it fails here, so the second press
        for (int i = 0; i <= 1; i++) {
            IOSSimulatorHelper.clickAt("0.68", "0.58", "1");
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

    List<ABContact> contactsInAddressbook = new ArrayList<>();

    /**
     * Reads the list of contacts out of the Address Book
     *
     * @throws Exception
     * @step. ^I read list of Contacts in Address Book$
     */
    @Given("^I read list of Contacts in Address Book$")
    public void IReadListOfContactsInAddressbook() throws Exception {
        contactsInAddressbook = addressbookProvisioner.getContacts();
    }

    List<List<ABContact>> conatctBatches = new ArrayList<>();
    /**
     * Separates the address book contacts list into number of chunks
     *
     * @param numberOfChunks number of chunks adressbook get divided into
     * @throws Exception
     * @step. ^I separate list of contacts into (\d+) chunks$
     */
    @Given("^I separate list of contacts into (\\d+) chunks$")
    public void ISeparateListOfContactsIntoChunks(int numberOfChunks) throws Exception {
        int sizeOfBatch = contactsInAddressbook.size()/numberOfChunks;
        for (int i = 0; i < contactsInAddressbook.size(); i += sizeOfBatch) {
            conatctBatches.add(contactsInAddressbook.subList(i, Math.min(i + sizeOfBatch, contactsInAddressbook.size())));
        }
    }

    List<ClientUser> usersToAutoconnect = new ArrayList<>();
    /**
     * Picks a number of random user of a specific batch to register at the BE
     *
     * @param numberContactsToRegister number of contacts to register
     * @param numberOfChunk number of the batch to register from
     * @throws Exception
     */
    @Then("^I pick (\\d+) random contact of chunk (\\d+) to register at BE$")
    public void IPickRandomContactOfChunkToRegisterAtBE(int numberContactsToRegister, int numberOfChunk)
            throws Exception {
        for (int i = 1; i<=numberContactsToRegister;i++){
            int randomNumber = new Random().nextInt(conatctBatches.get(numberOfChunk-1).size());
            ABContact contactToRegister = conatctBatches.get(numberOfChunk-1).get(randomNumber);
            ClientUser userToRegsiter = usrMgr.findUserByNameOrNameAlias(contactToRegister.name);
            usrMgr.createSpecificUsersOnBackend(Collections.singletonList(userToRegsiter), RegistrationStrategy.ByPhoneNumberOnly);
            usersToAutoconnect.add(userToRegsiter);
        }
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


    @Given("^I add all pre created users to Address Book$")
    public void IAddAllPrecreatedUsersToAddressBook() throws Exception {
        for (int i = 2; i <= usrMgr.MAX_USERS; i++){
            ClientUser user = usrMgr.findUserByNameOrNameAlias(String.format("user%sName",i));
            String name = user.getName();
            String phoneNumber = user.getPhoneNumber().toString();
            ABContact contact = new ABContact(name, Optional.empty(), Optional.of(Collections.singletonList(phoneNumber)));
            addressbookProvisioner.addContacts(Collections.singletonList(contact));
        }
    }


    @Then("^I see (\\d+) autoconnected conversations? in conversations list$")
    public void ISeeAutoconnectedConversationInConversationsList(int numberOfAutoconnection) throws Throwable {
        //String user = usersToAutoconnect.get(numberOfAutoconnection-1).toString();
        String user = usrMgr.replaceAliasesOccurences(usersToAutoconnect.get(numberOfAutoconnection-1).toString(),
                ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("The conversation '%s' is not visible in the conversation list",
                user), getConversationsListPage().isConversationInList(user));

    }
}
