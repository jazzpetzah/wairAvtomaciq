package com.wearezeta.auto.osx.pages.webapp;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;
import javax.imageio.ImageIO;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;

public class ConversationPage extends com.wearezeta.auto.web.pages.ConversationPage {

    public ConversationPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }
    
    @Override
    public BufferedImage getImageFromLastLinkPreview() throws Exception {
        List<WebElement> images = getPreviewImages();
        WebElement lastImage = images.get(images.size() - 1);
        return ImageIO.read(new ByteArrayInputStream(lastImage.getScreenshotAs(OutputType.BYTES)));
    }
}
