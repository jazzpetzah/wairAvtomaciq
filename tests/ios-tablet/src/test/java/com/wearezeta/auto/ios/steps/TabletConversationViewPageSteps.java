package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.TabletConversationViewPage;

import cucumber.api.java.en.When;

public class TabletConversationViewPageSteps {
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private TabletConversationViewPage getTabletConversationViewPage() throws Exception {
        return pagesCollection.getPage(TabletConversationViewPage.class);
    }

    /**
     * Presses the conversation detail button on iPad to open a
     * ConversationDetailPopoverPage
     *
     * @throws Exception
     * @step. ^I open (group )?conversation details on iPad$
     */
    @When("^I open (group )?conversation details on iPad$")
    public void IOpenConversationDetailsOniPad(String isGroup) throws Exception {
        getTabletConversationViewPage().tapConversationDetailsIPadButton();
    }

    /**
     * Tap the corresponding item on file transfer menu
     *
     * @step. ^I tap on iPad file transfer menu item (.*)
     * @param name menu item name or a shortcut
     * @throws Exception
     */
    @When("^I tap on iPad file transfer menu item (.*)")
    public void ITapFileTransferMenuItem(String name) throws Exception {
        getTabletConversationViewPage().tapFileTransferMenuItem(name);
    }
}
