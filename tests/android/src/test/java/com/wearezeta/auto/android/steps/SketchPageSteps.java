package com.wearezeta.auto.android.steps;

import java.util.Random;

import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android.pages.SketchPage;

import cucumber.api.java.en.When;

public class SketchPageSteps {
	
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
		.getInstance();

	private SketchPage getSketchPage() throws Exception {
		return (SketchPage) pagesCollection.getPage(SketchPage.class);
	}
	
	/**
	 * Draws a sketch consisting of at least numColors colors
	 * in random patterns around the canvas
	 * 
	 * @step. ^I draw a sketch with (.*) colors$
	 * 
	 * @throws Exception
	 */
	@When("^I draw a sketch with (.*) colors$")
	public void WhenISwipeLeftOnTextInput(int numColors) throws Exception {
		for (int i = 0; i < SketchPage.colors.length; i++) {
			getSketchPage().setColor(i);
		}
		
		
		
	}

}
