package com.wearezeta.auto.android_tablet.steps;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletCallingOverlayPage;
import com.wearezeta.auto.common.ImageUtil;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CallingPageSteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletCallingOverlayPage getCallingOverlayPage() throws Exception {
		return (TabletCallingOverlayPage) pagesCollection
				.getPage(TabletCallingOverlayPage.class);
	}

	/**
	 * Check calling Big bar is visible or not
	 * 
	 * @step. ^I (do not )?see calling overlay Big bar$
	 * 
	 * @param shouldNotSee
	 *            equals to null is "do not " part is NOT present
	 * 
	 * @throws Exception
	 */
	@When("^I (do not )?see calling overlay Big bar$")
	public void WhenISeeCallingOverlayBigBar(String shouldNotSee)
			throws Exception {
		if (shouldNotSee == null) {
			Assert.assertTrue(getCallingOverlayPage().callingOverlayIsVisible());
			Assert.assertTrue(getCallingOverlayPage()
					.incomingCallerAvatarIsVisible());
		} else {
			Assert.assertTrue(getCallingOverlayPage()
					.incomingCallerAvatarIsInvisible());
		}
	}

	/**
	 * Check calling Mini bar is visible or not
	 * 
	 * @step. ^I (do not )?see calling overlay Mini bar$
	 * 
	 * @param shouldNotSee
	 *            equals to null is "do not " part is NOT present
	 * 
	 * @throws Exception
	 */
	@When("^I (do not )?see calling overlay Mini bar$")
	public void WhenISeeCallingOverlayMiniBar(String shouldNotSee)
			throws Exception {
		if (shouldNotSee == null) {
			Assert.assertTrue(getCallingOverlayPage()
					.ongoingCallMinibarIsVisible());
		} else {
			Assert.assertTrue(getCallingOverlayPage()
					.ongoingCallMinibarIsInvisible());
		}
	}

	/**
	 * Tap the corresponding button on call overlay
	 * 
	 * @step. ^I tap (.*) button on (?:\\s*|the )calling overlay$
	 * 
	 * @param buttonName
	 *            button name to tap. See OverlayButton enum for possible values
	 * @throws Exception
	 */
	@When("^I tap (.*) button on (?:\\s*|the )calling overlay$")
	public void ITapButton(String buttonName) throws Exception {
		final OverlayButton dstButton = OverlayButton.valueOf(buttonName
				.toUpperCase());
		switch (dstButton) {
		case ACCEPT:
			getCallingOverlayPage().tapAcceptButton();
			break;
		case DISMISS:
			getCallingOverlayPage().tapDismissButton();
			break;
		case MUTE:
			getCallingOverlayPage().tapMuteButton();
			break;
		case SPEAKER:
			getCallingOverlayPage().tapSpeakerButton();
			break;
		default:
			throw new IllegalStateException(String.format(
					"This step is not implemented for '%s' button", buttonName));
		}
	}

	private Map<OverlayButton, BufferedImage> savedButtonStates = new HashMap<>();

	private static enum OverlayButton {
		ACCEPT, DISMISS, MUTE, SPEAKER;
	}

	private Optional<BufferedImage> getButtonStateScreenshot(OverlayButton btn)
			throws Exception {
		switch (btn) {
		case MUTE:
			return getCallingOverlayPage().getMuteButtonScreenshot();
		default:
			throw new IllegalStateException(String.format(
					"This step is not implemented for '%s' button", btn.name()));
		}
	}

	/**
	 * Saves current button state on UI into the internal data structure
	 * 
	 * @step. ^I remember the current state of (.*) button on (?:\\s*|the
	 *        )calling overlay$
	 * 
	 * @param buttonName
	 *            the name of a particular button. See the OverlayButton enum
	 *            for possible values
	 * @throws Exception
	 */
	@When("^I remember the current state of (.*) button on (?:\\s*|the )calling overlay$")
	public void IRememberButtonState(String buttonName) throws Exception {
		final OverlayButton dstButton = OverlayButton.valueOf(buttonName
				.toUpperCase());
		savedButtonStates.put(dstButton, getButtonStateScreenshot(dstButton)
				.orElseThrow(IllegalStateException::new));
	}

	private final static double MAX_SIMILARITY_THRESHOLD = 0.6;

	/**
	 * Verify whether button state has been changed or not since it was
	 * remembered last time
	 * 
	 * @step. ^I see (.*) button state is (not )?changed on (?:\\s*|the )calling
	 *        overlay$
	 * 
	 * @param buttonName
	 *            the name of a particular button. See the OverlayButton enum
	 *            for possible values
	 * @param shouldNotChange
	 *            this equals to null is "not " part is not present
	 * @throws Exception
	 */
	@Then("^I see (.*) button state is (not )?changed on (?:\\s*|the )calling overlay$")
	public void ICompareButtonStates(String buttonName, String shouldNotChange)
			throws Exception {
		final OverlayButton dstButton = OverlayButton.valueOf(buttonName
				.toUpperCase());
		BufferedImage previousStateScreenshot = null;
		if (this.savedButtonStates.containsKey(dstButton)) {
			previousStateScreenshot = this.savedButtonStates.get(dstButton);
		} else {
			throw new IllegalStateException(String.format(
					"Please take a screenshot of '%s' button state first",
					buttonName));
		}
		final BufferedImage currentStateScreenshot = getButtonStateScreenshot(
				dstButton).orElseThrow(IllegalStateException::new);
		final double score = ImageUtil.getOverlapScore(currentStateScreenshot,
				previousStateScreenshot,
				ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
		if (shouldNotChange == null) {
			Assert.assertTrue(
					String.format(
							"The current and previous states of '%s' seem to be very similar (%.2f >= %.2f)",
							buttonName, score, MAX_SIMILARITY_THRESHOLD),
					score < MAX_SIMILARITY_THRESHOLD);
		} else {
			Assert.assertTrue(
					String.format(
							"The current and previous states of '%s' seem to be diffreent (%.2f < %.2f)",
							buttonName, score, MAX_SIMILARITY_THRESHOLD),
					score >= MAX_SIMILARITY_THRESHOLD);
		}
	}

	/**
	 * Verify whether the particular button is present on call overlay
	 * 
	 * @step. ^I (do not )?see (.*) button on (?:\\s*|the )calling overlay$
	 * 
	 * @param shouldNotSee
	 *            this equals to null is "not " part is not present
	 * @param buttonName
	 *            the name of a particular button. See the OverlayButton enum
	 *            for possible values
	 * @throws Exception
	 */
	@Then("^I (do not )?see (.*) button on (?:\\s*|the )calling overlay$")
	public void ISeeButton(String shouldNotSee, String buttonName)
			throws Exception {
		final OverlayButton dstButton = OverlayButton.valueOf(buttonName
				.toUpperCase());
		boolean result = false;
		switch (dstButton) {
		case SPEAKER:
			result = (shouldNotSee == null) ? getCallingOverlayPage()
					.waitUntilSpeakerButtonVisible() : getCallingOverlayPage()
					.waitUntilSpeakerButtonInvisible();
			break;
		default:
			throw new IllegalStateException(String.format(
					"This step is not implemented for '%s' button", buttonName));
		}
		Assert.assertTrue(String.format("The button '%s' is %svisible",
				buttonName, (shouldNotSee == null) ? "" : "in"), result);
	}

	/**
	 * Swipe up on calling overlay to dismiss it
	 * 
	 * @step. ^I swipe up on (?:\\s*|the )calling overlay$
	 * 
	 * @throws Exception
	 */
	@When("^I swipe up on (?:\\s*|the )calling overlay$")
	public void ISwipeUp() throws Exception {
		getCallingOverlayPage().dismissBySwipeUp();
	}
}
