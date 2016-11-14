package com.wearezeta.auto.osx.steps.osx;


import com.wearezeta.auto.osx.pages.osx.ForeignMessageContextMenuPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.osx.pages.osx.OwnMessageContextMenuPage;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.ConversationPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class MessageContextMenuPageSteps {

    private final TestContext webContext;
    private final TestContext wrapperContext;
    
    public MessageContextMenuPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @Given("^I open context menu of the last message$")
    public void IOpenContextMenuOfLast() throws Exception {
        webContext.getPagesCollection(WebappPagesCollection.class).getPage(ConversationPage.class).clickContextMenuOnMessage(1);
    }

    @When("^I click delete (everywhere )?in message context menu for my own message$")
    public void IClickDeleteButtonInOwnContextMenu(String everywhere) throws Exception {
        if (everywhere == null) {
            wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(OwnMessageContextMenuPage.class).clickDelete();
        }else{
            wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(OwnMessageContextMenuPage.class).clickDeleteEverywhere();
        }
    }
    
    @When("^I click delete in message context menu for foreign message$")
    public void IClickDeleteButtonInForeignContextMenu() throws Exception {
        wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(ForeignMessageContextMenuPage.class).clickDelete();
    }
    
    @When("^I click edit in message context menu for my own message$")
    public void IClickEditButtonInOwnContextMenu() throws Exception {
            wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(OwnMessageContextMenuPage.class).clickEdit();
    }
}
