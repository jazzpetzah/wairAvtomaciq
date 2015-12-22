package com.wearezeta.auto.ios.steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CommonIOSTabletSteps {
    static {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	private CommonIOSSteps steps = new CommonIOSSteps(); {
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
        steps.tearDown();
	}

}
