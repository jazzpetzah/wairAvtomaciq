package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android.pages.SketchPage;

import cucumber.api.java.en.When;

public class SketchPageSteps {

	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private SketchPage getSketchPage() throws Exception {
		return pagesCollection.getPage(SketchPage.class);
	}

	private DialogPage getDialogPage() throws Exception {
		return pagesCollection.getPage(DialogPage.class);
	}

	/**
	 * Draws a sketch consisting of at least numColors colors in random patterns
	 * around the canvas
	 * 
	 * @step. ^I draw a sketch( on image)? with (.*) colors$
	 * 
	 * @throws Exception
	 */
	@When("^I draw a sketch( on image)? with (.*) colors$")
	public void WhenIDrawASketchWithXColors(String onImage, int numColors)
			throws Exception {
		SketchPage page = getSketchPage();
		for (int i = 0; i < numColors; i++) {
			page.setColor(i);
			page.drawRandomLines(1);
		}
	}

	/**
	 * Presses the send button from the sketch page
	 * 
	 * @step. ^I send my sketch$
	 * 
	 * @throws Exception
	 */
	@When("^I send my sketch$")
	public void WhenISendMySketch() throws Exception {
		getSketchPage().tapSendButton();
	}

}
