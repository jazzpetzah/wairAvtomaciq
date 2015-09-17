package com.wearezeta.auto.osx.steps;

import com.wearezeta.auto.osx.pages.OSXPagesCollection;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class MainWirePageSteps {

	/**
	 * Closes the app
	 *
	 * @step. ^I close the app$
	 *
	 * @throws Exception
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
	 */
	@When("^I maximize the app$")
	public void IMaximizeApp() throws Exception {
		OSXPagesCollection.mainWirePage.maximizeWindow();
	}

	/**
	 * Verifies wether the app is in fullscreen or not
	 *
	 * @step. ^I verify app is in fullscreen$
	 *
	 * @throws Exception
	 */
	@When("^I verify app is in fullscreen$")
	public void IVerifyAppFullscreen() throws Exception {
		Assert.assertTrue(OSXPagesCollection.mainWirePage.isFullscreen());
	}

	/**
	 * Verifies wether the app is in minimum size or not
	 *
	 * @step. ^I verify app is in minimum size$
	 *
	 * @throws Exception
	 */
	@When("^I verify app is in minimum size$")
	public void IVerifyAppMini() throws Exception {
		Assert.assertTrue(OSXPagesCollection.mainWirePage.isMini());
	}

	/**
	 * Resizes the app to the max by hand
	 *
	 * @step. ^I resize the app to the max by hand$
	 *
	 * @throws Exception
	 */
	@When("^I resize the app to the max by hand$")
	public void IResizeToMaxByHand() throws Exception {
		OSXPagesCollection.mainWirePage.resizeToMaxByHand();
	}

	/**
	 * Resizes the app to the min by hand
	 *
	 * @step. ^I resize the app to the min by hand$
	 *
	 * @throws Exception
	 */
	@When("^I resize the app to the min by hand$")
	public void IResizeToMinByHand() throws Exception {
		OSXPagesCollection.mainWirePage.resizeToMinByHand();
	}

	/**
	 * Ensures that the position of the app is in the upper left corner with
	 * respect to the dock (assuming it's on the left side).
	 *
	 * @step. ^I ensure initial positioning$
	 *
	 * @throws Exception
	 */
	@When("^I ensure initial positioning$")
	public void IEnsureInitialPositioning() throws Exception {
		OSXPagesCollection.mainWirePage.ensurePosition();
	}

	/**
	 * Repositions the app to the given coordinates
	 *
	 * @step. ^I change position of the app to X (\\d+) and Y (\\d+)$
	 *
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 *
	 *
	 * @throws Exception
	 */
	@When("^I change position of the app to X (\\d+) and Y (\\d+)$")
	public void IPositioningTo(int x, int y) throws Exception {
		OSXPagesCollection.mainWirePage.positionByHand(x, y);
	}

	/**
	 * Verifies the position of the app
	 *
	 * @step. ^I verify app X coordinate is (\\d+) and Y coordinate is (\\d+)$
	 *
	 * @param x
	 *            the width to verify
	 * @param y
	 *            the height to verify
	 *
	 *
	 * @throws Exception
	 */
	@When("^I verify app X coordinate is (\\d+) and Y coordinate is (\\d+)$")
	public void IVerifyPosition(int x, int y) throws Exception {
		Assert.assertTrue("Expected X coordinate " + x
				+ " does not match the actual value "
				+ OSXPagesCollection.mainWirePage.getX(),
				OSXPagesCollection.mainWirePage.isX(x));
		Assert.assertTrue("Expected Y coordinate " + y
				+ " does not match the actual value "
				+ OSXPagesCollection.mainWirePage.getY(),
				OSXPagesCollection.mainWirePage.isY(y));
	}

	/**
	 * Resizes the app to the given size
	 *
	 * @step. ^I resize the app to width (\\d+)px and height (\\d+)px$
	 *
	 * @param width
	 *            the width to resize to
	 * @param height
	 *            the height to resize to
	 *
	 *
	 * @throws Exception
	 */
	@When("^I resize the app to width (\\d+) px and height (\\d+) px$")
	public void IResizeTo(int width, int height) throws Exception {
		OSXPagesCollection.mainWirePage.resizeByHand(width, height);
	}

	/**
	 * Verifies size of the app
	 *
	 * @step. ^I verify app width is (\\d+)px and height is (\\d+)px$
	 *
	 * @param width
	 *            the width to verify
	 * @param height
	 *            the height to verify
	 *
	 *
	 * @throws Exception
	 */
	@When("^I verify app width is (\\d+) px and height is (\\d+) px$")
	public void IVerifySizeOf(int width, int height) throws Exception {
		Assert.assertTrue(OSXPagesCollection.mainWirePage
				.isApproximatelyHeight(height));
		Assert.assertTrue(OSXPagesCollection.mainWirePage
				.isApproximatelyWidth(width));
	}

}
