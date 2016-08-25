package com.wearezeta.auto.osx.steps.osx;


import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.osx.pages.osx.MessageContextMenuPage;
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
     * @step. ^I click delete (everywhere )?in message context menu$
     * @throws Exception
     */
    @When("^I click delete (everywhere )?in message context menu$")
    public void IClickBlockButtonInContextMenu(String everywhere) throws Exception {
        if (everywhere == null) {
            osxPagesCollection.getPage(MessageContextMenuPage.class).clickDelete();
        }else{
            osxPagesCollection.getPage(MessageContextMenuPage.class).clickDeleteEverywhere();
        }
        
    }
}
