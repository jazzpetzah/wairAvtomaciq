package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.TabletPeoplePickerPage;

import cucumber.api.java.en.When;

public class TabletPeoplePickerPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private TabletPeoplePickerPage getTabletPeoplePickerPage() throws Exception {
        return pagesCollection.getPage(TabletPeoplePickerPage.class);
    }

    /**
     * Taps in the search field in the people picker popover
     *
     * @throws Exception
     * @step. ^I tap on Search input on People picker on iPad popover$
     */
    @When("^I tap on Search input on People picker on iPad popover$")
    public void ITapOnSearchInputOnPeoplePickerOniPadPopover() throws Exception {
        getTabletPeoplePickerPage().pressIntoSearchField();
    }

    /**
     * Types in the name of the user in the search field
     *
     * @param name name of user to search for
     * @throws Exception
     * @step. ^I input user name (.*) in People picker search field on iPad
     * popover$
     */
    @When("^I input user name (.*) in People picker search field on iPad popover$")
    public void IInputUserNameInPeoplePickerSearchFieldOniPadPopover(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        getTabletPeoplePickerPage().fillTextInTabletPeoplePickerSearchField(
                name);
    }

    /**
     * Verifies correct user is found by search in list
     *
     * @param name name of user you want to see as result of search
     * @throws Exception
     * @step. ^I see user (.*) found on People picker on iPad popover$
     */
    @When("^I see user (.*) found on People picker on iPad popover$")
    public void ISeeUserFoundOnPeoplePickerOniPadPopover(String name) throws Exception {
        try {
            name = usrMgr.findUserByNameOrNameAlias(name).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        Assert.assertTrue("User :" + name
                        + " is not presented on Pepople picker page",
                getTabletPeoplePickerPage().getSearchResultsElement(name).isPresent());
    }

    /**
     * Chooses the connected user to add to conversation
     *
     * @param name name of user to select an click on
     * @throws Exception
     * @step. ^I tap connected user (.*) on People picker on iPad popover$"
     */
    @When("^I tap connected user (.*) on People picker on iPad popover$")
    public void ITapConnectedUserOnPeoplePickerOniPadPopover(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        getTabletPeoplePickerPage().selectConnectedUser(name);
    }

    /**
     * Clicks the Add to Conversation button to create the group chat
     *
     * @throws Exception
     * @param btnName one of available button names
     * @step. ^I tap (Add to Conversation|Create) button on iPad popover$
     */
    @When("^I tap (Add to Conversation|Create) button on iPad popover$")
    public void IClickOnAddToConversationButtonOniPadPopover(String btnName) throws Exception {
        switch (btnName) {
            case "Add to Conversation":
                getTabletPeoplePickerPage().clickAddToConversationButtonOniPadPopover();
                break;
            case "Create":
                getTabletPeoplePickerPage().clickCreateConversationButtonOniPadPopover();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", btnName));
        }
    }

}
