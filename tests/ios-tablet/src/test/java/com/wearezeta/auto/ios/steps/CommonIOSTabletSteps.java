package com.wearezeta.auto.ios.steps;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.When;

public class CommonIOSTabletSteps {
	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(CommonIOSTabletSteps.class.getSimpleName());

	private final CommonSteps commonSteps = CommonSteps.getInstance();

	static {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	private CommonIOSSteps steps = new CommonIOSSteps();
	{
		steps.setSkipBeforeAfter(true);
	}

	@Before("~@noAcceptAlert")
	public void setUpAcceptAlerts() throws Exception {

		steps.commonBefore(steps.resetIOSDriver(true));
	}

	@Before("@noAcceptAlert")
	public void setUpNoAlerts() throws Exception {

		steps.commonBefore(steps.resetIOSDriver(false));
	}

	@After
	public void tearDown() throws Exception {

		if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
			IOSCommonUtils.collectSimulatorLogs(
					CommonUtils.getDeviceName(getClass()),
					steps.getTestStartedDate());
		}

		commonSteps.getUserManager().resetUsers();
	}

	/**
	 * Tap on the screen to dismiss popover
	 * 
	 * @step I dismiss popover on iPad$
	 * 
	 * @throws Exception
	 */
	@When("^I dismiss popover on iPad$")
	public void IDismissPopover() throws Exception {
		steps.ITapOnTopLeftCornerOfTheScreen();
	}

}
