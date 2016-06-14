package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.CallOutgoingAudioPage;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletCallOutgoingAudioPage extends AbstractTabletCallOutgoingPage {
    public TabletCallOutgoingAudioPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private CallOutgoingAudioPage getAudioAndroidPage() throws Exception {
        return this.getAndroidPageInstance(CallOutgoingAudioPage.class);
    }

    public void toggleSpeaker() throws Exception {
        getAudioAndroidPage().toggleSpeaker();
    }

    public BufferedImage getMuteButtonScreenshot() throws Exception {
        return getAudioAndroidPage().getMuteButtonScreenshot();
    }

    public BufferedImage getSpecialButtonScreenshot() throws Exception {
        return getAudioAndroidPage().getSpecialButtonScreenshot();
    }

    protected Class getAndroidPageClass() {
        return CallOutgoingAudioPage.class;
    }
}
