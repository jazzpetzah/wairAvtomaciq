package com.wearezeta.auto.ios.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import org.junit.Assert;

import com.wearezeta.auto.ios.pages.VideoPlayerPage;

import cucumber.api.java.en.When;

public class VideoPlayerPageSteps {

	private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

	private VideoPlayerPage getVideoPlayerPage() throws Exception {
		return pagesCollection.getPage(VideoPlayerPage.class);
	}

	@When("I see video player page is opened")
	public void ISeeVideoPlayerPage() throws Exception {
		Assert.assertTrue("Video Player is not opened", getVideoPlayerPage().isVideoPlayerPageOpened());
	}

	@When("I tap on Done button on Video player page")
	public void ITapOnDoneButtonOnVideoPlayerPage() throws Exception {
		getVideoPlayerPage().tapVideoPage();
		getVideoPlayerPage().clickVideoDoneButton();
	}

	@When("I tap Pause button on Video player page")
	public void ITapPauseButtonOnVideoPlayerPage() throws Exception {
		getVideoPlayerPage().clickPauseButton();
	}

	/**
	 * Verifies that the video message video player is opened
	 *
	 * @throws Exception
	 * @step. ^I see video message player page is opened$
     */
	@Then("^I see video message player page is opened$")
	public void ISeeVideoMessagePlayerPageIsOpened() throws Exception {
		Assert.assertTrue("Video Message Player is not opened", getVideoPlayerPage().
				isVideoMessagePlayerPageDoneButtonVisible());
	}
}
