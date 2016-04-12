package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.CallIncomingPage;
import static com.wearezeta.auto.android.pages.CallIncomingPage.idMainContent;
import com.wearezeta.auto.common.driver.DriverUtils;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletCallIncomingPage extends AndroidTabletPage {
    
    public TabletCallIncomingPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private CallIncomingPage getPage() throws Exception {
        return this.getAndroidPageInstance(CallIncomingPage.class);
    }

    public boolean waitUntilVisible(String subtitle) throws Exception {
        return getPage().waitUntilVisible(subtitle);
    }

    public boolean waitUntilNotVisible(String subtitle) throws Exception {
        return getPage().waitUntilNotVisible(subtitle);
    }

    public boolean waitUntilNameAppearsOnCallingBarCaption(String name) throws Exception {
        return getPage().waitUntilNameAppearsOnCallingBarCaption(name);
    }

    public void ignoreCallLandscape() throws Exception {
        // !!! Only portrait mode is supported for calls
        // DriverUtils.swipeElementPointToPoint(getDriver(), getElement(idMainContent), 1500, 50, 85, 40, 85);
        ignoreCallPortrait();
    }

    public void acceptCallLandscape() throws Exception {
        // !!! Only portrait mode is supported for calls
        // DriverUtils.swipeElementPointToPoint(getDriver(), getElement(idMainContent), 1500, 50, 85, 60, 85);
        acceptCallPortrait();
    }
    
    public void ignoreCallPortrait() throws Exception {
        DriverUtils.swipeElementPointToPoint(getDriver(), getElement(idMainContent), 1500, 50, 92, 30, 92);
    }

    public void acceptCallPortrait() throws Exception {
        DriverUtils.swipeElementPointToPoint(getDriver(), getElement(idMainContent), 1500, 50, 92, 70, 92);
    }

}
