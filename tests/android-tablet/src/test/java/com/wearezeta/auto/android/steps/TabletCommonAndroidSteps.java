package com.wearezeta.auto.android.steps;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.android.pages.TabletLoginPage;
import com.wearezeta.auto.android.pages.TabletPagesCollection;
import com.wearezeta.auto.android.CommonAndroidSteps;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.When;

public class TabletCommonAndroidSteps {
	static {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

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
		final ZetaAndroidDriver driver = steps.resetAndroidDriver(getUrl(),
				getPath(), isUnicode, this.getClass());
		final WebDriverWait wait = PlatformDrivers
				.createDefaultExplicitWait(driver);
		TabletPagesCollection.loginPage = new TabletLoginPage(driver, wait);
		PagesCollection.loginPage = TabletPagesCollection.loginPage;

		ZetaFormatter.setDriver(TabletPagesCollection.loginPage.getDriver());
		TabletPagesCollection.loginPage.dismissUpdate();
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
		TabletLoginPage.clearPagesCollection();
		commonSteps.getUserManager().resetUsers();
	}
}