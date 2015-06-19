package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android.pages.GiphyPreviewPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

public class GiphySteps {
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
		.getInstance();

	private GiphyPreviewPage getGiphyPreviewPage() throws Exception {
		return (GiphyPreviewPage) pagesCollection.getPage(GiphyPreviewPage.class);
	}
	
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
}
