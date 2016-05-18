package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.w3c.dom.Node;

import java.util.Optional;
import java.util.concurrent.Future;

public class SecurityAlertPage extends AndroidPage {

    private static final String xpathDenyButton =
            "//*[@resource-id='com.android.packageinstaller:id/permission_deny_button']";

    private static final String xpathAcceptButton =
            "//*[@resource-id='com.android.packageinstaller:id/permission_allow_button']";

    public SecurityAlertPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void accept() throws Exception {
        getUIAutomatorDriver().clickElement(xpathAcceptButton);
    }

    public void acceptIfVisible() throws Exception {
        final Optional<Node> acceptButton = getUIAutomatorDriver().getElementIfPresent(xpathAcceptButton);
        if (acceptButton.isPresent()) {
            getUIAutomatorDriver().clickElement(acceptButton.get());
        }
    }

    public void dismiss() throws Exception {
        getUIAutomatorDriver().clickElement(xpathDenyButton);
    }

    public void dismissIfVisible() throws Exception {
        final Optional<Node> denyButton = getUIAutomatorDriver().getElementIfPresent(xpathDenyButton);
        if (denyButton.isPresent()) {
            getUIAutomatorDriver().clickElement(denyButton.get());
        }
    }
}
