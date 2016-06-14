package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.CallOngoingAudioPage;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletCallOngoingAudioPage extends AndroidTabletPage {

    public TabletCallOngoingAudioPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private CallOngoingAudioPage getPage() throws Exception {
        return this.getAndroidPageInstance(CallOngoingAudioPage.class);
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
