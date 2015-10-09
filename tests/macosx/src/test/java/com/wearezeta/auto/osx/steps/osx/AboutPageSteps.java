package com.wearezeta.auto.osx.steps.osx;

import com.wearezeta.auto.osx.pages.osx.AboutPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import cucumber.api.java.en.Then;

import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class AboutPageSteps {

	private final OSXPagesCollection osxPagesCollection = OSXPagesCollection
			.getInstance();

	/**
	 * Closes the about window
	 *
	 * @step. ^I close the about window$
	 *
	 * @throws Exception
	 */
	@When("^I close the about window$")
	public void ICloseApp() throws Exception {
		osxPagesCollection.getPage(AboutPage.class).closeWindow();
	}

	/**
	 * Minimizes the about window
	 *
	 * @step. ^I minimize the about window$
	 *
	 * @throws Exception
	 */
	@When("^I minimize the about window$")
	public void IMinimizeApp() throws Exception {
		osxPagesCollection.getPage(AboutPage.class).minimizeWindow();
	}

	/**
	 * Verifies whether the about window is visible or not
	 *
	 * @step. ^I verify about window is visible$
	 *
	 * @throws Exception
	 */
	@Then("^I verify about window is visible$")
	public void IVerifyAboutWindowIsVisible() throws Exception {
		assertTrue("About window not visible within timeout",
				osxPagesCollection.getPage(AboutPage.class).isVisible());
	}

}
