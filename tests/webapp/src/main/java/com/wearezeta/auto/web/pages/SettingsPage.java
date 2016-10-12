package com.wearezeta.auto.web.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class SettingsPage extends WebPage {

    private static final int SLIDER_CIRCLE_SIZE = 20;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssSettingsCloseButton)
    private WebElement settingsCloseButton;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssSoundAlertsLevel)
    private WebElement soundAlertsLevel;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssDeviceLabels)
    private List<WebElement> deviceLabels;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssConfirmText)
    private WebElement confirmText;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssImportButton)
    private WebElement importButton;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssBackButton)
    private WebElement backButton;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssVerificationToggle)
    private WebElement verificationToggle;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssDeviceIds)
    private WebElement firstDevice;

    public SettingsPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        final String xpath = WebAppLocators.SettingsPage.xpathSettingsDialogRoot;
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(xpath));
    }

    public void clickCloseButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), settingsCloseButton);
        settingsCloseButton.click();
    }

    public enum SoundAlertsLevel {
        None("None", 2), Some("Some", 1), All("All", 0);

        private final String stringRepresentation;
        private final int intRepresentation;

        public int getIntRepresenation() {
            return this.intRepresentation;
        }

        private SoundAlertsLevel(String stringRepresentation,
                int intRepresentation) {
            this.stringRepresentation = stringRepresentation;
            this.intRepresentation = intRepresentation;
        }

        @Override
        public String toString() {
            return this.stringRepresentation;
        }

        public static SoundAlertsLevel fromString(String value) {
            for (SoundAlertsLevel level : SoundAlertsLevel.values()) {
                if (level.toString().equalsIgnoreCase(value)) {
                    return level;
                }
            }
            throw new NoSuchElementException(String.format(
                    "There is no alert level with name '%s'", value));
        }

        public static SoundAlertsLevel fromInt(int value) {
            for (SoundAlertsLevel level : SoundAlertsLevel.values()) {
                if (level.getIntRepresenation() == value) {
                    return level;
                }
            }
            throw new NoSuchElementException(String.format(
                    "There is no alert level with index '%s'", value));
        }
    }

    public void setSoundAlertsLevel(SoundAlertsLevel newLevel) throws Exception {
        assert SoundAlertsLevel.values().length > 1;
        if (WebAppExecutionContext.getBrowser().isSupportingNativeMouseActions()) {
            final Actions builder = new Actions(this.getDriver());
            final int width = soundAlertsLevel.getSize().width;
            final int height = soundAlertsLevel.getSize().height;
            final int dstX = (width - SLIDER_CIRCLE_SIZE)
                    / (SoundAlertsLevel.values().length - 1)
                    * newLevel.getIntRepresenation();
            final int dstY = height / 2;
            builder.clickAndHold(soundAlertsLevel)
                    .moveToElement(soundAlertsLevel, dstX, dstY).release()
                    .build().perform();
        } else if (WebAppExecutionContext.getBrowser().isSupportingAccessToJavascriptContext()) {
            // Workaround for browsers, which don't support native events
            final String[] sliderMoveCode = new String[]{"$(\"" + WebAppLocators.SettingsPage.cssSoundAlertsLevel + "\")"
                + ".val(" + newLevel.getIntRepresenation() + ");", "wire.app.view.content.self_profile.user_repository"
                + ".save_property_sound_alerts('" + newLevel.toString().toLowerCase() + "');"};
            this.getDriver().executeScript(
                    StringUtils.join(sliderMoveCode, "\n"));
        } else {
            throw new Exception("Geckodriver is unable to access script context in Firefox < 48. See https://bugzilla"
                    + ".mozilla.org/show_bug.cgi?id=1123506");
        }
    }

    public SoundAlertsLevel getSoundAlertsLevel() {
        return SoundAlertsLevel.fromInt(Integer.parseInt(soundAlertsLevel
                .getAttribute("value")));
    }

    public List<String> getDeviceLabels() {
        return deviceLabels.stream().map(w -> w.getText())
                .collect(Collectors.toList());
    }

    public void clickDevice(String device) throws Exception {
        final String locator = WebAppLocators.SettingsPage.xpathDeviceLabel
                .apply(device);
        DriverUtils.waitUntilElementClickable(getDriver(), getDriver().findElement(By.xpath(locator)));
        getDriver().findElement(By.xpath(locator)).click();
    }

    public String getDeleteInfo() throws Exception {
        return confirmText.getText();
    }

    public void clickImportButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), importButton);
        importButton.click();
    }

    public void clickBackButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), backButton);
        backButton.click();
    }

    public void verifyDevice() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), verificationToggle);
        verificationToggle.click();
    }

    public boolean waitForDevices() throws Exception {
        // Unfortunately there is no other workaround than waiting for 1 second
        Thread.sleep(1000);
        return DriverUtils.waitUntilElementClickable(this.getDriver(), firstDevice);
    }

    public List<String> getVerifiedDeviceIds() throws Exception {
        final By useElement = By.xpath(".//*[local-name()='use']");
        List<WebElement> deviceList = getDriver().findElements(useElement);
        List<String> idList = new ArrayList<>();

        for (int i = 0; i < deviceList.size(); i++) {
            if ("user-device-verified".equals(deviceList.get(i).getAttribute("data-uie-name"))) {
                WebElement parent = deviceList.get(i).findElement(By.xpath("parent::*"));
                idList.add(parent.getAttribute("data-uie-value").toUpperCase());
            }
        }
        return idList;
    }
}
