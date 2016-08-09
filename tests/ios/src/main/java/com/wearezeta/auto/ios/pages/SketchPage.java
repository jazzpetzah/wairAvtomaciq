package com.wearezeta.auto.ios.pages;

import java.util.Random;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.DriverUtils;

public class SketchPage extends IOSPage {
    private static final By nameSketchSendButton = MobileBy.AccessibilityId("SketchConfirmButton");
    private static final By xpathTapColorLabel = By.xpath("//UIAStaticText[contains(@name, 'TAP COLOR')]");

    public SketchPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    private static final Random rand = new Random();

    public void sketchRandomLines() throws Exception {
        tapColorLabel();
        for (int i = 0; i < 2; i++) {
            int startX = 5 + rand.nextInt(90);
            int endX = 5 + rand.nextInt(90);
            int startY = 30 + rand.nextInt(40);
            int endY = 30 + rand.nextInt(40);
            if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
                IOSSimulatorHelper.swipe(startX / 100.0, startY / 100.0, endX / 100.0, endY / 100.0);
            } else {
                DriverUtils.swipeByCoordinates(getDriver(), 500, startX, startY, endX, endY);
            }
        }
    }

    public void sendSketch() throws Exception {
        getElement(nameSketchSendButton).click();
    }

    private void tapColorLabel() throws Exception {
        getElement(xpathTapColorLabel).click();
    }

}
