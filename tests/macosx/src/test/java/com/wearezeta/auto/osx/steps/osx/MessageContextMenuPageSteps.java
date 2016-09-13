package com.wearezeta.auto.osx.steps.osx;


import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.osx.pages.osx.ForeignMessageContextMenuPage;
import com.wearezeta.auto.osx.pages.osx.OwnMessageContextMenuPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.pages.ConversationPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class MessageContextMenuPageSteps {

    private final OSXPagesCollection osxPagesCollection = OSXPagesCollection.getInstance();
    @SuppressWarnings("unused")
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection.getInstance();

    @SuppressWarnings("unused")
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    /**
     * Opens the context menu for the last message in the current conversation
     *
     * @step. ^I open context menu of the last message$
     *
     * @throws java.lang.Exception
     */
    @Given("^I open context menu of the last message$")
    public void IOpenContextMenuOfLast() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class).clickContextMenuOnLatestMessage();
    }

    /**
     * Click delete local or delete everywhere in message context menu
     *
     * @param everywhere whether to click local delete or delete everywhere
     * @step. ^I click delete (everywhere )?in message context menu for my own message$
     * @throws Exception
     */
    @When("^I click delete (everywhere )?in message context menu for my own message$")
    public void IClickDeleteButtonInOwnContextMenu(String everywhere) throws Exception {
        if (everywhere == null) {
            osxPagesCollection.getPage(OwnMessageContextMenuPage.class).clickDelete();
        }else{
            osxPagesCollection.getPage(OwnMessageContextMenuPage.class).clickDeleteEverywhere();
        }
        
    }
    
    /**
     * Click delete local or delete everywhere in message context menu
     *
     * @step. ^I click delete in message context menu for foreign message$
     * @throws Exception
     */
    @When("^I click delete in message context menu for foreign message$")
    public void IClickDeleteButtonInForeignContextMenu() throws Exception {
        osxPagesCollection.getPage(ForeignMessageContextMenuPage.class).clickDelete();
    }
    
    /**
     * Click edit in own messages context menu
     *
     * @step. ^I click edit in message context menu for my own message$
     * @throws Exception
     */
    @When("^I click edit in message context menu for my own message$")
    public void IClickEditButtonInOwnContextMenu() throws Exception {
            osxPagesCollection.getPage(OwnMessageContextMenuPage.class).clickEdit();
    }
}
