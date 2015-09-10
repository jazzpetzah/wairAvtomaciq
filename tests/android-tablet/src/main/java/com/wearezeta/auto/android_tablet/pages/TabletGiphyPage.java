package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.GiphyPreviewPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletGiphyPage extends AndroidTabletPage {
	public TabletGiphyPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private GiphyPreviewPage getAndroidGiphyPage() throws Exception {
		return (GiphyPreviewPage) this
				.getAndroidPageInstance(GiphyPreviewPage.class);
	}

	public boolean waitUntilIsVisible() throws Exception {
		return getAndroidGiphyPage().isGiphyPreviewShown();
	}

	public void tapSendButton() throws Exception {
		getAndroidGiphyPage().clickeSendButton();
	}

}
