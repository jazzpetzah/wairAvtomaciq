package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.TabletSearchUIPage;

import cucumber.api.java.en.When;

public class TabletSearchUIPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private TabletSearchUIPage getSearchUIPage() throws Exception {
        return pagesCollection.getPage(TabletSearchUIPage.class);
    }

    /**
     * Chooses the connected user to add to conversation
     *
     * @param name name of user to select an click on
     * @throws Exception
     * @step. ^I tap connected user (.*) in Search UI on iPad popover$"
     */
    @When("^I tap connected user (.*) in Search UI on iPad popover$")
    public void ITapConnectedUserOnPeoplePickerOniPadPopover(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        getSearchUIPage().selectConnectedUser(name);
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
                getSearchUIPage().clickAddToConversationButtonOniPadPopover();
                break;
            case "Create":
                getSearchUIPage().clickCreateConversationButtonOniPadPopover();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", btnName));
        }
    }

}
