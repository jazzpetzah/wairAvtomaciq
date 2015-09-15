package com.wearezeta.auto.osx.steps;

import com.wearezeta.auto.osx.pages.OSXPagesCollection;
import cucumber.api.java.en.When;

public class MainWirePageSteps {

	/**
	 * Closes the app
	 *
	 * @step. ^I close the app$
	 *
	 * @throws Exception
	 *             if Selenium fails to wait until action completes
	 */
	@When("^I close the app$")
	public void ICloseApp() throws Exception {
		OSXPagesCollection.mainWirePage.closeWindow();
	}

	/**
	 * Minimizes the app
	 *
	 * @step. ^I minimize the app$
	 *
	 * @throws Exception
	 *             if Selenium fails to wait until action completes
	 */
	@When("^I minimize the app$")
	public void IMinimizeApp() throws Exception {
		OSXPagesCollection.mainWirePage.minimizeWindow();
	}

	/**
	 * Maximize the app
	 *
	 * @step. ^I maximize the app$
	 *
	 * @throws Exception
	 *             if Selenium fails to wait until action completes
	 */
	@When("^I maximize the app$")
	public void IMaximizeApp() throws Exception {
		OSXPagesCollection.mainWirePage.maximizeWindow();
	}

}
