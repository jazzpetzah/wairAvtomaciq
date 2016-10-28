package com.wearezeta.auto.osx.steps.osx;


import com.wearezeta.auto.osx.common.WrapperTestContext;
import com.wearezeta.auto.osx.pages.osx.ForeignMessageContextMenuPage;
import com.wearezeta.auto.osx.pages.osx.OwnMessageContextMenuPage;
import com.wearezeta.auto.web.pages.ConversationPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class MessageContextMenuPageSteps {

    private final WrapperTestContext context;

    public MessageContextMenuPageSteps() {
        this.context = new WrapperTestContext();
    }

    public MessageContextMenuPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @Given("^I open context menu of the last message$")
    public void IOpenContextMenuOfLast() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).clickContextMenuOnMessage(1);
    }

    @When("^I click delete (everywhere )?in message context menu for my own message$")
    public void IClickDeleteButtonInOwnContextMenu(String everywhere) throws Exception {
        if (everywhere == null) {
            context.getOSXPagesCollection().getPage(OwnMessageContextMenuPage.class).clickDelete();
        }else{
            context.getOSXPagesCollection().getPage(OwnMessageContextMenuPage.class).clickDeleteEverywhere();
        }
    }
    
    @When("^I click delete in message context menu for foreign message$")
    public void IClickDeleteButtonInForeignContextMenu() throws Exception {
        context.getOSXPagesCollection().getPage(ForeignMessageContextMenuPage.class).clickDelete();
    }
    
    @When("^I click edit in message context menu for my own message$")
    public void IClickEditButtonInOwnContextMenu() throws Exception {
            context.getOSXPagesCollection().getPage(OwnMessageContextMenuPage.class).clickEdit();
    }
}
