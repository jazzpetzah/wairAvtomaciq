package com.wearezeta.auto.win.steps.webapp;


import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.win.pages.webapp.ConversationPage;
import cucumber.api.java.en.Then;
import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class ConversationPageSteps {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(ConversationPageSteps.class.getName());

    private final WebAppTestContext webContext;

    public ConversationPageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }
    
    @Then("^I (do not )?see a picture (.*) from link preview$")
    public void ISeePictureInLinkPreview(String doNot, String pictureName) throws Exception {
        if (doNot == null) {
            assertThat("I do not see a picture from link preview in the conversation",
                    webContext.getPagesCollection().getPage(ConversationPage.class).isImageFromLinkPreviewVisible());

            BufferedImage expectedImage = ImageUtil.readImageFromFile(WebCommonUtils.getFullPicturePath(pictureName));
            BufferedImage actualImage = webContext.getPagesCollection().getPage(ConversationPage.class).
                    getImageFromLastLinkPreview();
            
            // Image matching with SIFT does not work very well on really small images
            // because it defines the maximum number of matching keys
            // so we scale them to double size to get enough matching keys
            final int scaleMultiplicator = 2;
            expectedImage = ImageUtil.resizeImage(expectedImage, scaleMultiplicator);
            actualImage = ImageUtil.resizeImage(actualImage, scaleMultiplicator);

            assertThat("Not enough good matches", ImageUtil.getMatches(expectedImage, actualImage), greaterThan(80));
        } else {
            assertThat("I see a picture in the conversation", webContext.getPagesCollection().getPage(ConversationPage.class)
                    .isImageFromLinkPreviewNotVisible());
        }
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
