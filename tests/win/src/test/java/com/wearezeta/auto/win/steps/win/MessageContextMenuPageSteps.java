package com.wearezeta.auto.win.steps.win;


import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.ConversationPage;
import com.wearezeta.auto.win.pages.win.ForeignMessageContextMenuPage;
import com.wearezeta.auto.win.pages.win.OwnMessageContextMenuPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class MessageContextMenuPageSteps {

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public MessageContextMenuPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @Given("^I click context menu of the last message$")
    public void IClickContextMenuOfLast() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).clickContextMenuOnMessage(1);
    }

    @When("^I click delete (everywhere )?in message context menu for my own message$")
    public void IClickDeleteButtonInContextMenu(String everywhere) throws Exception {
        if (everywhere == null) {
            wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(OwnMessageContextMenuPage.class).clickDelete();
        }else{
            wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(OwnMessageContextMenuPage.class).clickDeleteEverywhere();
        }
    }
    
    @When("^I click delete in message context menu for foreign message$")
    public void IClickDeleteButtonInForeignContextMenu() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(ForeignMessageContextMenuPage.class).clickDelete();
    }
    
    @When("^I click edit in message context menu for my own message$")
    public void IClickEditButtonInContextMenu() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(OwnMessageContextMenuPage.class).clickEdit();
    }
}
