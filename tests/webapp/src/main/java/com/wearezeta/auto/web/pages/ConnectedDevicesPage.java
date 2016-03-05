package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ConnectedDevicesPage extends WebPage {

    final String cssDialogLocator = "[data-uie-name='modal-conntected-device']";

    @FindBy(how = How.CSS, using = cssDialogLocator)
    private WebElement dialog;

    @FindBy(how = How.CSS, using = "[data-uie-name='modal-conntected-device'] [data-uie-name='do-ok']")
    private WebElement okButton;

    @FindBy(how = How.CSS, using = "[data-uie-name='modal-conntected-device'] [data-uie-name='go-manage-devices']")
    private WebElement manageButton;

    public ConnectedDevicesPage(Future<ZetaWebAppDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public boolean isDialogShown() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.cssSelector(cssDialogLocator));
    }

    public boolean isDialogNotShown() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), By.cssSelector(cssDialogLocator));
    }

    public void clickOKButton() {
        okButton.click();
    }

    public String getDialogText() {
        return dialog.getText();
    }
}
