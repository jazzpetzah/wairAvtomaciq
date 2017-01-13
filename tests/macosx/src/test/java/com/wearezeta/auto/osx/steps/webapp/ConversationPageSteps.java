package com.wearezeta.auto.osx.steps.webapp;

import static org.hamcrest.Matchers.*;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.pages.webapp.ConversationPage;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConversationPageSteps {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(ConversationPageSteps.class.getSimpleName());
    private final WebAppTestContext webContext;

    public ConversationPageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }

    @Given("^I open context menu of the last message$")
    public void IOpenContextMenuOfLast() throws Exception {
        webContext.getPagesCollection(WebappPagesCollection.class).getPage(com.wearezeta.auto.web.pages.ConversationPage.class).
                clickContextMenuOnMessage(1);
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
}
