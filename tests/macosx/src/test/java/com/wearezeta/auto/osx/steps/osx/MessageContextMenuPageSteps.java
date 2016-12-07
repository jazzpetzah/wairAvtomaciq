package com.wearezeta.auto.osx.steps.osx;


import com.wearezeta.auto.osx.pages.osx.ForeignMessageContextMenuPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.osx.pages.osx.OwnMessageContextMenuPage;
import com.wearezeta.auto.web.common.TestContext;
import cucumber.api.java.en.When;

public class MessageContextMenuPageSteps {

    private final TestContext webContext;
    
    public MessageContextMenuPageSteps(TestContext webContext) {
        this.webContext = webContext;
    }

    @When("^I click delete (everywhere )?in message context menu for my own message$")
    public void IClickDeleteButtonInOwnContextMenu(String everywhere) throws Exception {
        if (everywhere == null) {
            webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(OwnMessageContextMenuPage.class).clickDelete();
        }else{
            webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(OwnMessageContextMenuPage.class).clickDeleteEverywhere();
        }
    }
    
    @When("^I click delete in message context menu for foreign message$")
    public void IClickDeleteButtonInForeignContextMenu() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(ForeignMessageContextMenuPage.class).clickDelete();
    }
    
    @When("^I click edit in message context menu for my own message$")
    public void IClickEditButtonInOwnContextMenu() throws Exception {
            webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(OwnMessageContextMenuPage.class).clickEdit();
    }
}
