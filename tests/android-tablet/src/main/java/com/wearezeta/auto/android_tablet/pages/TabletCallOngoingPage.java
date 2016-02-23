package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.CallOngoingPage;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;

public class TabletCallOngoingPage extends AndroidTabletPage {

    public TabletCallOngoingPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private CallOngoingPage getPage() throws Exception {
        return this.getAndroidPageInstance(CallOngoingPage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        return getPage().waitUntilVisible();
    }

    public boolean waitUntilNotVisible() throws Exception {
        return getPage().waitUntilNotVisible();
    }

    public boolean waitUntilNameAppearsOnCallingBarCaption(String name) throws Exception {
        return getPage().waitUntilNameAppearsOnCallingBarCaption(name);
    }

    public boolean hangupIsVisible() throws Exception {
        return getPage().hangupIsVisible();
    }

    public boolean toggleMuteIsVisible() throws Exception {
        return getPage().toggleMuteIsVisible();
    }

    public boolean toggleSpeakerIsVisible() throws Exception {
        return getPage().toggleSpeakerIsVisible();
    }

    public boolean toggleVideoIsVisible() throws Exception {
        return getPage().toggleVideoIsVisible();
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

    public int getNumberOfParticipants() throws Exception {
        return getPage().getNumberOfParticipants();
    }

    public FunctionalInterfaces.StateGetter getMuteButtonStateFunction() throws Exception {
        return getPage().getMuteButtonStateFunction();
    }

    public FunctionalInterfaces.StateGetter getSpecialButtonStateFunction() throws Exception {
        return getPage().getSpecialButtonStateFunction();
    }
}
