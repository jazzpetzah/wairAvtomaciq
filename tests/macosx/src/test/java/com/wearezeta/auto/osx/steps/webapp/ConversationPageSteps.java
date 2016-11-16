package com.wearezeta.auto.osx.steps.webapp;

import static org.hamcrest.Matchers.*;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.pages.webapp.ConversationPage;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import cucumber.api.java.en.Then;
import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConversationPageSteps {
    
    @SuppressWarnings("unused")
    private static final Logger log = ZetaLogger.getLog(ConversationPageSteps.class.getSimpleName());
    private final TestContext webContext;
    private final TestContext wrapperContext;
    
    public ConversationPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }
    
    @Then("^I (do not )?see a picture (.*) from link preview$")
    public void ISeePictureInLinkPreview(String doNot, String pictureName) throws Exception {
        if (doNot == null) {
            assertThat("I do not see a picture from link preview in the conversation",
                    webContext.getPagesCollection().getPage(ConversationPage.class).isImageFromLinkPreviewVisible());

            BufferedImage expectedImage = ImageUtil.readImageFromFile(WebCommonUtils.getFullPicturePath(pictureName));
            BufferedImage actualImage = webContext.getPagesCollection().getPage(ConversationPage.class).
                    getImageFromLastLinkPreview();

            assertThat("Not enough good matches", ImageUtil.getMatches(expectedImage, actualImage), greaterThan(100));
        } else {
            assertThat("I see a picture in the conversation", webContext.getPagesCollection().getPage(ConversationPage.class)
                    .isImageFromLinkPreviewNotVisible());
        }
    }
}
