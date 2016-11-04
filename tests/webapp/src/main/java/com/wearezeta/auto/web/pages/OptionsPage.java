package com.wearezeta.auto.web.pages;

import java.util.List;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class OptionsPage extends WebPage {

    private static final Logger log = ZetaLogger.getLog(AccountPage.class.getSimpleName());

    @FindBy(how = How.CSS, using = "[name='preferences-options-audio']")
    private List<WebElement> soundAlertsRadioboxes;

    @FindBy(how = How.CSS, using = "label[for='preferences-options-audio-all']")
    private WebElement soundAlertsAll;

    @FindBy(how = How.CSS, using = "label[for='preferences-options-audio-some']")
    private WebElement soundAlertsSome;

    @FindBy(how = How.CSS, using = "label[for='preferences-options-audio-none']")
    private WebElement soundAlertsNone;

    @FindBy(how = How.CSS, using = WebAppLocators.OptionsPage.cssImportButton)
    private WebElement importButton;

    public OptionsPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickImportButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), importButton);
        importButton.click();
    }

    public void clickSoundAlertsAll() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), soundAlertsAll);
        soundAlertsAll.click();
    }

    public void clickSoundAlertsSome() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), soundAlertsSome);
        soundAlertsSome.click();
    }

    public void clickSoundAlertsNone() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), soundAlertsNone);
        soundAlertsNone.click();
    }

    public Object getSelectedSoundAlertsSetting() {
        for (WebElement radiobox : soundAlertsRadioboxes) {
            if (radiobox.isSelected()) {
                String id = radiobox.getAttribute("id");
                if (id != null) {
                    return id.replace("preferences-options-audio-", "");
                }
            }
        }
        return "";
    }
}
