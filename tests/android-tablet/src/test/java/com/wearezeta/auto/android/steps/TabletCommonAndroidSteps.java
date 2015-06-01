package com.wearezeta.auto.android.steps;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.TabletLoginPage;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class TabletCommonAndroidSteps {
	static {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private CommonAndroidSteps steps = new CommonAndroidSteps();
	{
		steps.setSkipBeforeAfter(true);
	}

	private final CommonSteps commonSteps = CommonSteps.getInstance();

	private static String getUrl() throws Exception {
		return CommonUtils
				.getAndroidAppiumUrlFromConfig(TabletCommonAndroidSteps.class);
	}

	private static String getPath() throws Exception {
		return CommonUtils
				.getAndroidApplicationPathFromConfig(TabletCommonAndroidSteps.class);
	}

	private void initFirstPage(boolean isUnicode) throws Exception {
		final Future<ZetaAndroidDriver> lazyDriver = steps.resetAndroidDriver(
				getUrl(), getPath(), isUnicode, this.getClass());
		pagesCollection.clearAllPages();
		pagesCollection.setPage(new TabletLoginPage(lazyDriver));
		ZetaFormatter.setLazyDriver(lazyDriver);
	}

	@Before({ "~@unicode", "~@performance" })
	public void setUp() throws Exception {
		steps.commonBefore();
		initFirstPage(false);
	}

	@Before({ "@unicode", "~@performance" })
	public void setUpUnicode() throws Exception {
		steps.commonBefore();
		initFirstPage(true);
	}

	@After
	public void tabletTearDown() throws Exception {
		pagesCollection.clearAllPages();
		commonSteps.getUserManager().resetUsers();
	}
}
