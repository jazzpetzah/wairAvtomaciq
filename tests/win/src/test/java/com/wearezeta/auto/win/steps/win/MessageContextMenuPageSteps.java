package com.wearezeta.auto.win.steps.win;


import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.ConversationPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.win.pages.win.MessageContextMenuPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class MessageContextMenuPageSteps {

    private final WinPagesCollection winPagesCollection = WinPagesCollection.getInstance();
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
        webappPagesCollection.getPage(ConversationPage.class).openContextMenuOnLatestMessage();
    }

    /**
     * Click delete local or delete everywhere in message context menu
     *
     * @param everywhere whether to click local delete or delete everywhere
     * @step. ^I click delete (everywhere )?in message context menu$
     * @throws Exception
     */
    @When("^I click delete (everywhere )?in message context menu$")
    public void IClickBlockButtonInContextMenu(String everywhere) throws Exception {
        if (everywhere == null) {
            winPagesCollection.getPage(MessageContextMenuPage.class).clickDelete();
        }else{
            winPagesCollection.getPage(MessageContextMenuPage.class).clickDeleteEverywhere();
        }
        
    }
}
