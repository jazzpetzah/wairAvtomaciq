package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.CallOngoingPage;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

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

    public boolean toggleMuteIsVisible() throws Exception {
        return getPage().toggleMuteIsVisible();
    }

    public boolean toggleSpeakerIsVisible() throws Exception {
        return getPage().toggleSpeakerIsVisible();
    }

    public void toggleMute() throws Exception {
        getPage().toggleMute();
    }

    public void hangup() throws Exception {
        getPage().hangup();
    }

    public void toggleVideo() throws Exception {
        getPage().toggleVideo();
    }

    public int getNumberOfParticipants() throws Exception {
        return getPage().getNumberOfParticipants();
    }

    public BufferedImage getMuteButtonScreenshot() throws Exception {
        return getPage().getMuteButtonScreenshot();
    }

    public BufferedImage getSpecialButtonScreenshot() throws Exception {
        return getPage().getMuteButtonScreenshot();
    }

    public boolean toggleMuteIsNotVisible() throws Exception {
        return getPage().toggleMuteIsNotVisible();
    }

    public boolean toggleSpeakerIsNotVisible() throws Exception {
        return getPage().toggleSpeakerIsNotVisible();
    }
}
