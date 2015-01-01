package com.wearezeta.auto.android;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.user_management.UsersManager;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.When;

public class CommonAndroidSteps extends CommonSteps {
	static {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	public static final String PATH_ON_DEVICE = "/mnt/sdcard/DCIM/Camera/userpicture.jpg";
	private String path;

	@Before("@performance")
	public void setUpPerformance() throws Exception {
		try {
			AndroidCommonUtils.disableHints();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UsersManager.getInstance().generatePerformanceUser();
		path = CommonUtils
				.getAndroidApplicationPathFromConfig(CommonAndroidSteps.class);
		if (PagesCollection.loginPage == null) {
			PagesCollection.loginPage = new LoginPage(
					CommonUtils
							.getAndroidAppiumUrlFromConfig(CommonAndroidSteps.class),
					path);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
			PagesCollection.loginPage.dismissUpdate();
		}
	}

	@Before({ "~@unicode", "~@performance" })
	public void setUp() throws Exception {

		commonBefore();

		if (PagesCollection.loginPage == null) {
			PagesCollection.loginPage = new LoginPage(
					CommonUtils
							.getAndroidAppiumUrlFromConfig(CommonAndroidSteps.class),
					path);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
			PagesCollection.loginPage.dismissUpdate();
		}
	}

	@Before({ "@unicode", "~@performance" })
	public void setUpUnicode() throws Exception {

		commonBefore();

		if (PagesCollection.loginPage == null) {
			PagesCollection.loginPage = new LoginPage(
					CommonUtils
							.getAndroidAppiumUrlFromConfig(CommonAndroidSteps.class),
					path, true);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
			PagesCollection.loginPage.dismissUpdate();
		}
	}

	@After
	public void tearDown() throws Exception {
		PagesCollection.loginPage.Close();
		AndroidPage.clearPagesCollection();
	}


	@When("^I press back button$")
	public void PressBackButton() throws Exception {
		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.navigateBack();
		}
	}

	private void commonBefore() throws Exception {
		try {
			AndroidCommonUtils.uploadPhotoToAndroid(PATH_ON_DEVICE);
		} catch (Exception ex) {
			System.out.println("Failed to deploy pictures into simulator");
		}

		try {
			AndroidCommonUtils.disableHints();
			String backendJSON = AndroidCommonUtils
					.createBackendJSON(CommonUtils.getBackendType(this
							.getClass()));
			AndroidCommonUtils.deployBackendFile(backendJSON);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		path = CommonUtils
				.getAndroidApplicationPathFromConfig(CommonAndroidSteps.class);

		ZetaFormatter.setBuildNumber(AndroidCommonUtils
				.readClientVersionFromAdb());
	}
	
	@When("^I minimize the application$")
	public void IMimizeApllication() throws InterruptedException {
		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.minimizeApplication();
		}
	}

	@When("^I restore the application$")
	public void IRestoreApllication() {
		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.restoreApplication();
		}
	}
}
