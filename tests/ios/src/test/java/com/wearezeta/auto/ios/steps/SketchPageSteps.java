package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.SketchPage;

import cucumber.api.java.en.When;

public class SketchPageSteps {
	
	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();
	
	private SketchPage getSketchPage() throws Exception {
		return (SketchPage) pagesCollecton.getPage(SketchPage.class);
	}
	
	/**
	 * randomly draws lines in sketch feature
	 * 
	 * @step. ^I draw a random sketch$
	 * @throws Exception
	 */
	@When("^I draw a random sketch$")
	public void IDrawRandomSketches() throws Exception {
		getSketchPage().sketchRandomLines();
	}
	
	/**
	 * Sends the sketch
	 * 
	 * @step. ^I send my sketch$
	 * @throws Exception
	 */
	@When("^I send my sketch$") 
	public void IRememberMySketch() throws Exception {
		getSketchPage().sendSketch();
	}
}
