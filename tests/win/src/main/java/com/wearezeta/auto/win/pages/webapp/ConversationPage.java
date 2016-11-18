package com.wearezeta.auto.win.pages.webapp;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.driver.ZetaWinWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Future;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;

public class ConversationPage extends com.wearezeta.auto.web.pages.ConversationPage {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(ConversationPage.class.getName());
    
    private static final String PASTE_SCRIPT = "security restrictions";

    // TODO hide behind driver impl
    private final Robot robot = new Robot();

    public ConversationPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }
    
    @Override
    public BufferedImage getImageFromLastLinkPreview() throws Exception {
        List<WebElement> images = getPreviewImages();
        WebElement lastImage = images.get(images.size() - 1);
        return ImageIO.read(new ByteArrayInputStream(lastImage.getScreenshotAs(OutputType.BYTES)));
    }
    
    @Override
    public void pressShortCutForPing() throws Exception {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_K);
        robot.keyRelease(KeyEvent.VK_K);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void pressShortCutForUndo() throws Exception {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_Z);
        robot.keyRelease(KeyEvent.VK_Z);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void pressShortCutForRedo() throws Exception {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_Z);
        robot.keyRelease(KeyEvent.VK_Z);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void pressShortCutForSelectAll() throws Exception {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void pressShortCutForCut() throws Exception {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_X);
        robot.keyRelease(KeyEvent.VK_X);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void pressShortCutForPaste() throws Exception {
        String script = new String(Files.readAllBytes(Paths.get(getClass().getResource(PASTE_SCRIPT).toURI())));
        ((ZetaWinWebAppDriver) getDriver()).getWinDriver().executeScript(script);
    }

    public void pressShortCutForCopy() throws Exception {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    @Override
    public void pressShortCutForCall() throws Exception {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_R);
        robot.keyRelease(KeyEvent.VK_R);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }
}
