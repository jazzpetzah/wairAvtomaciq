package com.wearezeta.auto.win.steps.webapp;


import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.win.pages.webapp.ConversationPage;
import cucumber.api.java.en.Then;

import org.apache.log4j.Logger;

public class ConversationPageSteps {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(ConversationPageSteps.class.getName());

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public ConversationPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }
    
    @Then("^I type shortcut combination to ping$")
    public void ITypeShortcutCombinationToPing() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).pressShortCutForPing();
    }
    
    @Then("^I type shortcut combination to undo$")
    public void ITypeShortcutCombinationToUndo() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).pressShortCutForUndo();
    }

    @Then("^I type shortcut combination to redo$")
    public void ITypeShortcutCombinationToRedo() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).pressShortCutForRedo();
    }

    @Then("^I type shortcut combination to select all$")
    public void ITypeShortcutCombinationToSelectAll() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).pressShortCutForSelectAll();
    }

    @Then("^I type shortcut combination to cut$")
    public void ITypeShortcutCombinationToCut() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).pressShortCutForCut();
    }

    @Then("^I type shortcut combination to paste$")
    public void ITypeShortcutCombinationToPaste() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).pressShortCutForPaste();
    }

    @Then("^I type shortcut combination to copy$")
    public void ITypeShortcutCombinationToCopy() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).pressShortCutForCopy();
    }

}
