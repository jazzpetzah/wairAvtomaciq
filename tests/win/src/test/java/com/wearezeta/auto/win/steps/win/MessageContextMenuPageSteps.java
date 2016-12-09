package com.wearezeta.auto.win.steps.win;


import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.win.pages.win.ForeignMessageContextMenuPage;
import com.wearezeta.auto.win.pages.win.OwnMessageContextMenuPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;

import cucumber.api.java.en.When;

public class MessageContextMenuPageSteps {

    private final TestContext webContext;

    public MessageContextMenuPageSteps(TestContext webContext) {
        this.webContext = webContext;
    }

    @When("^I click delete (everywhere )?in message context menu for my own message$")
    public void IClickDeleteButtonInContextMenu(String everywhere) throws Exception {
        if (everywhere == null) {
            webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(OwnMessageContextMenuPage.class).clickDelete();
        }else{
            webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(OwnMessageContextMenuPage.class).clickDeleteEverywhere();
        }
    }
    
    @When("^I click delete in message context menu for foreign message$")
    public void IClickDeleteButtonInForeignContextMenu() throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(ForeignMessageContextMenuPage.class).clickDelete();
    }
    
    @When("^I click edit in message context menu for my own message$")
    public void IClickEditButtonInContextMenu() throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(OwnMessageContextMenuPage.class).clickEdit();
    }
}
