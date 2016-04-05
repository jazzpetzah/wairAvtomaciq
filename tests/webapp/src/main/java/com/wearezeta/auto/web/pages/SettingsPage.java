package com.wearezeta.auto.web.pages;

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
import com.wearezeta.auto.web.common.Browser;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class SettingsPage extends WebPage {
    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssSettingsCloseButton)
    private WebElement settingsCloseButton;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssSoundAlertsLevel)
    private WebElement soundAlertsLevel;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssCurrentDevice)
    private WebElement currentDevice;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssDeviceLabels)
    private List<WebElement> deviceLabels;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssDeleteAccountButton)
    private WebElement deleteAccountButton;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssCancelDeleteAccountButton)
    private WebElement cancelDeleteAccountButton;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssConfirmDeleteAccountButton)
    private WebElement confirmDeleteAccountButton;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssConfirmText)
    private WebElement confirmText;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssImportButton)
    private WebElement importButton;

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

    private static final int SLIDER_CIRCLE_SIZE = 20;

    public void setSoundAlertsLevel(SoundAlertsLevel newLevel) throws Exception {
        assert SoundAlertsLevel.values().length > 1;
        if (WebAppExecutionContext.getBrowser() == Browser.Firefox) {
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
        } else {
            // Workaround for browsers, which don't support native events
            // TODO: workaround should be clicking instead of sliding and not JS
            final String[] sliderMoveCode = new String[]{
                    "$(\"" + WebAppLocators.SettingsPage.cssSoundAlertsLevel
                            + "\").val(" + newLevel.getIntRepresenation()
                            + ");",
                    "wire.app.view.content.self_profile.user_repository.save_property_sound_alerts('"
                            + newLevel.toString().toLowerCase() + "');"};
            this.getDriver().executeScript(
                    StringUtils.join(sliderMoveCode, "\n"));
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
        getDriver().findElement(By.xpath(locator)).click();
    }

    public String getCurrentDeviceId() {
        return currentDevice.getAttribute("data-uie-uid");
    }

    public void clickDeleteAccountButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), deleteAccountButton);
        deleteAccountButton.click();
    }

    public void clickCancelDeleteAccountButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), cancelDeleteAccountButton);
        cancelDeleteAccountButton.click();
    }

    public void clickConfirmDeleteAccountButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), confirmDeleteAccountButton);
        confirmDeleteAccountButton.click();
    }

    public String getDeleteInfo() throws Exception {
        return confirmText.getText();
    }

    public void clickImportButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), importButton);
        importButton.click();
    }
}
