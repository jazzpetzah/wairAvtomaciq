package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class StartedCallPage extends CallPage {

    private static final double MIN_ACCEPTABLE_SCORE = 0.9;

    private static final String xpathCallingMessage =
            "//UIAStaticText[contains(@name, 'CallStatusLabel') and @visible='true']";
    @FindBy(xpath = xpathCallingMessage)
    private WebElement callingMessage;

    private static final String nameEndCallButton = "LeaveCallButton";
    @FindBy(name = nameEndCallButton)
    private WebElement endCallButton;

    private static final String nameSpeakersButton = "CallSpeakerButton";
    @FindBy(name = nameSpeakersButton)
    private WebElement speakersButton;

    private static final String nameMuteCallButton = "CallMuteButton";
    @FindBy(name = nameMuteCallButton)
    private WebElement muteCallButton;

    private static final String nameCallingMessageUser = "CallStatusLabel";
    @FindBy(name = nameCallingMessageUser)
    private WebElement callingMessageUser;

    private static final Function<String, String> xpathStartedCallMessageUserByName = name ->
            String.format("//UIAStaticText[@name='%s']", name);

    public boolean isCallingMessageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathCallingMessage));
    }

    public boolean waitCallingMessageDisappear() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(xpathCallingMessage));
    }

    public boolean isStartedCallMessageVisible(String contact) throws Exception {
        final By locator = By.xpath(xpathStartedCallMessageUserByName.apply(contact));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public StartedCallPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isEndCallVisible() throws Exception {
        DriverUtils.waitUntilLocatorAppears(getDriver(), By.name(nameEndCallButton));
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                endCallButton);
    }

    public boolean isSpeakersVisible() throws Exception {
        DriverUtils.waitUntilLocatorAppears(getDriver(), By.name(nameSpeakersButton));
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                speakersButton);
    }

    public boolean isMuteCallVisible() throws Exception {
        DriverUtils.waitUntilLocatorAppears(getDriver(), By.name(nameMuteCallButton));
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                muteCallButton);
    }

    public void clickEndCallButton() {
        endCallButton.click();
    }

    public boolean isMuteCallButtonSelected() throws Exception {
        BufferedImage muteCallButtonIcon = getElementScreenshot(muteCallButton).orElseThrow(
                IllegalStateException::new);
        BufferedImage referenceImage = ImageUtil.readImageFromFile(IOSPage.getImagesPath()
                + "selectedMuteCallButton.png");

        double score = ImageUtil.getOverlapScore(referenceImage, muteCallButtonIcon,
                ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
        return score > MIN_ACCEPTABLE_SCORE;
    }
}
