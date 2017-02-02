package com.wearezeta.auto.osx.steps.webapp;

import static org.hamcrest.Matchers.*;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.pages.osx.MainWirePage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.osx.pages.webapp.ConversationPage;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import java.awt.image.BufferedImage;

import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.openqa.selenium.Point;

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

    @When("^I click on ephemeral button$")
    public void IClickEphemeralButton() throws Exception {
        Point point = webContext.getPagesCollection(WebappPagesCollection.class).
                getPage(com.wearezeta.auto.web.pages.ConversationPage.class).
                getCenterOfEphemeralButton();

        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).
                clickOnWebView(point);
    }

    @When("^I click context menu of the (second |third )?last message$")
    public void IClickContextMenuOfThirdLastMessage(String indexNumber) throws Exception {
        int messageId = getXLastMessageIndex(indexNumber);
        Point point = webContext.getPagesCollection(WebappPagesCollection.class).
                getPage(com.wearezeta.auto.web.pages.ConversationPage.class).
                getCenterOfMessageElement(messageId);

        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).
                clickOnWebView(point);
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

            assertThat("Not enough good matches", ImageUtil.getMatches(expectedImage, actualImage), greaterThan(70));
        } else {
            assertThat("I see a picture in the conversation", webContext.getPagesCollection().getPage(ConversationPage.class)
                    .isImageFromLinkPreviewNotVisible());
        }
    }

    private int getXLastMessageIndex(String indexValue) throws Exception {
        int indexNummer = 1;
        if (indexValue == null) {
            return indexNummer;
        }
        switch (indexValue) {
            case "third ":
                indexNummer = 3;
                break;
            case "second ":
                indexNummer = 2;
                break;
            default:
                indexNummer = 1;
                break;
        }
        return indexNummer;
    }
}
