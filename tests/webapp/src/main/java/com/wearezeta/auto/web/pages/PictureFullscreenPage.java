package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;
import java.awt.image.BufferedImage;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class PictureFullscreenPage extends WebPage {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(PictureFullscreenPage.class.getSimpleName());
    private static final int TIMEOUT_IMAGE_MESSAGE_UPLOAD = 40; // seconds
    
    @FindBy(css = WebAppLocators.PictureFullscreenPage.cssFullscreenImage)
    private WebElement fullscreenImage;

    @FindBy(xpath = WebAppLocators.PictureFullscreenPage.xpathXButton)
    private WebElement xButton;


    public PictureFullscreenPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }
    
    public boolean isPictureInModalDialog() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.cssSelector(WebAppLocators.PictureFullscreenPage.cssModalDialog));
    }

    public boolean isPictureInFullscreen() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                        By.cssSelector(WebAppLocators.PictureFullscreenPage.cssFullscreenImage));
    }

    public boolean isPictureNotInModalDialog() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
                By.cssSelector(WebAppLocators.PictureFullscreenPage.cssModalDialog));
    }

    public void clickXButton() throws Exception {
        xButton.click();
    }

    public void clickOnBlackBorder() throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingNativeMouseActions()) {
            Actions builder = new Actions(getDriver());
            builder.moveToElement(fullscreenImage, -10, -10).click().build().perform();
        } else {
            WebElement blackBorder = getDriver().findElement(By.cssSelector("#detail-view.modal"));
            getDriver().executeScript("var evt = new MouseEvent('click', {view: window});arguments[0].dispatchEvent(evt);",
                    blackBorder);
        }
    }
    
    public double getOverlapScoreOfFullscreenImage(String pictureName) throws Exception {
        final String picturePath = WebCommonUtils
                .getFullPicturePath(pictureName);
        if (!isImageMessageFound()) {
            return 0.0;
        }
        // comparison of the fullscreen image and sent picture
        BufferedImage actualImage = this.getElementScreenshot(fullscreenImage).orElseThrow(IllegalStateException::new);
        BufferedImage expectedImage = ImageUtil.readImageFromFile(picturePath);
        return ImageUtil.getOverlapScore(actualImage, expectedImage,
                ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
    }
    
    public boolean isImageMessageFound() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.cssSelector(WebAppLocators.ConversationPage.cssFirstImage),
                TIMEOUT_IMAGE_MESSAGE_UPLOAD);
    }

}
