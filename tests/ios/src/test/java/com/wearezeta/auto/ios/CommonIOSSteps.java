package com.wearezeta.auto.ios;


import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.ios.tools.IOSKeyboard;
import com.wearezeta.auto.ios.tools.IOSSimulatorPhotoLibHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.When;

public class CommonIOSSteps extends CommonSteps{
	private static final Logger log = ZetaLogger.getLog(CommonIOSSteps.class
			.getSimpleName());

	static {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	@Before("~@noAcceptAlert")
	public void setUpAcceptAlerts() throws Exception {
		commonBefore();

		String path = CommonUtils
				.getIosApplicationPathFromConfig(TestRun.class);

		if (PagesCollection.loginPage == null) {
			PagesCollection.loginPage = new LoginPage(
					CommonUtils.getIosAppiumUrlFromConfig(TestRun.class), path);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
		}

	}

	@Before("@noAcceptAlert")
	public void setUpNoAlerts() throws Exception {
		commonBefore();

		String path = CommonUtils
				.getIosApplicationPathFromConfig(TestRun.class);

		if (PagesCollection.loginPage == null) {
			PagesCollection.loginPage = new LoginPage(
					CommonUtils.getIosAppiumUrlFromConfig(TestRun.class), path,
					false);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
		}

	}

	private void commonBefore() throws Exception {

		if (CommonUtils.getIsSimulatorFromConfig(CommonIOSSteps.class)) {
			try {
				String[] picturepath = new String[] { CommonUtils
						.getUserPicturePathFromConfig(CommonIOSSteps.class) };
				IOSSimulatorPhotoLibHelper.CreateSimulatorPhotoLib("8.1",
						picturepath, true, true);
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error("Failed to deploy pictures into simulator.\n"
						+ ex.getMessage());
			}
		}

		if (PagesCollection.loginPage != null
				&& PagesCollection.loginPage.getDriver().isSessionLost()) {
			log.info("Session was lost, reseting pages collection");
			IOSPage.clearPagesCollection();
		}

		ZetaFormatter.setBuildNumber(IOSCommonUtils
				.readClientVersionFromPlist().getClientBuildNumber());
	}

	@After
	public void tearDown() throws Exception {
		PagesCollection.loginPage.Close();
		IOSPage.clearPagesCollection();
		IOSKeyboard.dispose();
	}

	@When("I see keyboard")
	public void ISeeKeyboard() {
		Assert.assertTrue(PagesCollection.dialogPage.isKeyboardVisible());
	}

	@When("I dont see keyboard")
	public void IDontSeeKeyboard() {
		Assert.assertFalse(PagesCollection.dialogPage.isKeyboardVisible());
	}

	@When("I press keyboard Delete button")
	public void IPressKeyboardDeleteBtn() {
		PagesCollection.iOSPage.clickKeyboardDeleteButton();
	}

	@When("I scroll up page a bit")
	public void IScrollUpPageABit() {
		PagesCollection.loginPage.smallScrollUp();
	}

	@When("I accept alert")
	public void IAcceptAlert() {
		PagesCollection.loginPage.acceptAlert();
	}

	@When("I dismiss alert")
	public void IDismissAlert() {
		PagesCollection.loginPage.dismissAlert();
	}
}
