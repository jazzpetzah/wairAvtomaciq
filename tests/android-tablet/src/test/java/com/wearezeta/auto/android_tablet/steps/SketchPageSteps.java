package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.pages.TabletSketchPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class SketchPageSteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletSketchPage getSketchPage() throws Exception {
		return pagesCollection.getPage(TabletSketchPage.class);
	}

	/**
	 * Draw a random sketch image
	 * 
	 * @step. ^I draw a sketch with (\\d+) colors on [Ss]ketch page$
	 * 
	 * @param colorsCount
	 *            the count of colors used while drawing (not more than 9)
	 * @throws Exception
	 */
	@When("^I draw a sketch with (\\d+) colors on [Ss]ketch page$")
	public void IDrawSketch(int colorsCount) throws Exception {
		for (int i = 0; i < colorsCount; i++) {
			getSketchPage().setColor(i);
			getSketchPage().drawRandomLines(1);
		}
	}

	/**
	 * Tap Send button to send the sketch
	 * 
	 * @step. ^I tap Send button on Sketch page$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Send button on Sketch page$")
	public void ITapSendButton() throws Exception {
		getSketchPage().tapSendButton();
	}

}
