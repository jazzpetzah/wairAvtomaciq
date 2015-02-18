package com.wearezeta.auto.android;

import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.android.pages.TabletAndroidPage;
import com.wearezeta.auto.android.pages.TabletLoginPage;
import com.wearezeta.auto.android.pages.TabletPagesCollection;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;

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
	
	private final CommonSteps commonSteps = CommonSteps.getInstance();
	private String path;

	@Before({ "~@unicode", "~@performance" })
	public void setUp() throws Exception {
		CommonAndroidSteps steps = new CommonAndroidSteps();
		steps.commonBefore();
		if (TabletPagesCollection.loginPage == null) {
			TabletPagesCollection.loginPage = new TabletLoginPage(CommonUtils.getAndroidAppiumUrlFromConfig(TabletCommonAndroidSteps.class), path);
			PagesCollection.loginPage = TabletPagesCollection.loginPage;
			ZetaFormatter.setDriver(TabletPagesCollection.loginPage.getDriver());
			PagesCollection.loginPage.dismissUpdate();
		}
	}

	@Before({ "@unicode", "~@performance" })
	public void setUpUnicode() throws Exception {
		CommonAndroidSteps steps = new CommonAndroidSteps();
		steps.commonBefore();
		if (TabletPagesCollection.loginPage == null) {
			TabletPagesCollection.loginPage = new TabletLoginPage(CommonUtils.getAndroidAppiumUrlFromConfig(TabletCommonAndroidSteps.class), path, true);
			PagesCollection.loginPage = TabletPagesCollection.loginPage;
			ZetaFormatter.setDriver(TabletPagesCollection.loginPage.getDriver());
			TabletPagesCollection.loginPage.dismissUpdate();
		}
	}

	@After
	public void tabletTearDown() throws Exception {
		TabletPagesCollection.loginPage.Close();
		TabletAndroidPage.clearTabletPagesCollection();
		commonSteps.getUserManager().resetUsers();
	}
}