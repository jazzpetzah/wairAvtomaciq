package com.wearezeta.auto.osx.steps;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.en.When;

public class DebugSteps {
	
	private static final Logger log = ZetaLogger.getLog(DebugSteps.class.getSimpleName());
	
	@When("^I output page source$")
	public void IOutputPageSource() {
		log.debug(PagesCollection.mainMenuPage.getPageSource());
	}
}
