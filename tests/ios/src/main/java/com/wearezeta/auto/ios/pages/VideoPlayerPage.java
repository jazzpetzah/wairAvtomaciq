package com.wearezeta.auto.ios.pages;

import java.util.Optional;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class VideoPlayerPage extends IOSPage {
    private static final By xpathVideoMainPage = By.xpath("//UIAWebView/UIAButton[@name='Home']");

    private static final By nameVideoDoneButton = By.name("Done");

    private static final By nameVideoPauseButton = By.name("PauseButton");

    public VideoPlayerPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVideoPlayerPageOpened() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathVideoMainPage);
    }

    public void tapVideoPage() throws Exception {
        getElement(xpathVideoMainPage).click();
    }

    public void clickVideoDoneButton() throws Exception {
        final WebElement videoDoneButton = getElement(nameVideoDoneButton);
        DriverUtils.tapByCoordinates(this.getDriver(), videoDoneButton);
        try {
            DriverUtils.tapByCoordinates(this.getDriver(), videoDoneButton);
        } catch (WebDriverException e) {
            // ignore silently
        }
    }

    public void clickPauseButton() throws Exception {
        final Optional<WebElement> videoPauseButton = getElementIfDisplayed(nameVideoPauseButton);
        if (videoPauseButton.isPresent()) {
            videoPauseButton.get().click();
        } else {
            tapVideoPage();
            getElement(nameVideoPauseButton).click();
        }
    }
}
