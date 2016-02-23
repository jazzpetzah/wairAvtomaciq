package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.CallOutgoingPage;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;

public class TabletCallOutgoingPage extends AndroidTabletPage {


    public TabletCallOutgoingPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }
    
    private CallOutgoingPage getPage() throws Exception {
        return this.getAndroidPageInstance(CallOutgoingPage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        return getPage().waitUntilVisible();
    }

    public boolean waitUntilNotVisible() throws Exception {
        return getPage().waitUntilNotVisible();
    }
    
    public void toggleMute() throws Exception {
        getPage().toggleMute();
    }
    
    public void hangup() throws Exception {
        getPage().hangup();
    }
    
    public void toggleSpeaker() throws Exception {
        getPage().toggleSpeaker();
    }
    
    public void toggleVideo() throws Exception {
        getPage().toggleVideo();
    }
    
    public FunctionalInterfaces.StateGetter getMuteButtonStateFunction() throws Exception {
        return getPage().getMuteButtonStateFunction();
    }

    public FunctionalInterfaces.StateGetter getSpecialButtonStateFunction() throws Exception {
        return getPage().getSpecialButtonStateFunction();
    }
    
}
