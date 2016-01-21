package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class StartedCallPage extends CallPage {

    private static final double MIN_ACCEPTABLE_SCORE = 0.9;

    private static final By xpathCallingMessage =
            By.xpath("//UIAStaticText[contains(@name, 'CallStatusLabel') and @visible='true']");

    private static final By nameEndCallButton = By.name("LeaveCallButton");

    private static final By nameSpeakersButton = By.name("CallSpeakerButton");

    private static final By nameMuteCallButton = By.name("CallMuteButton");

    private static final Function<String, String> xpathStrStartedCallMessageUserByName = name ->
            String.format("//UIAStaticText[@name='%s']", name);

    public StartedCallPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isCallingMessageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathCallingMessage);
    }

    public boolean waitCallingMessageDisappear() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathCallingMessage);
    }

    public boolean isStartedCallMessageVisible(String contact) throws Exception {
        final By locator = By.xpath(xpathStrStartedCallMessageUserByName.apply(contact));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isEndCallVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameEndCallButton);
    }

    public boolean isSpeakersVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameSpeakersButton);
    }

    public boolean isMuteCallVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameMuteCallButton);
    }

    public void clickEndCallButton() throws Exception {
        getElement(nameEndCallButton).click();
    }

    public boolean isMuteCallButtonSelected() throws Exception {
        BufferedImage muteCallButtonIcon = getElementScreenshot(getElement(nameMuteCallButton)).orElseThrow(
                IllegalStateException::new);
        BufferedImage referenceImage = ImageUtil.readImageFromFile(IOSPage.getImagesPath()
                + "selectedMuteCallButton.png");

        double score = ImageUtil.getOverlapScore(referenceImage, muteCallButtonIcon,
                ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
        return score > MIN_ACCEPTABLE_SCORE;
    }
}
