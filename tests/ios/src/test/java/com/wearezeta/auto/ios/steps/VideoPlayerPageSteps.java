package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.ios.pages.VideoPlayerPage;

import cucumber.api.java.en.When;

public class VideoPlayerPageSteps {

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection.getInstance();

	private VideoPlayerPage getVideoPlayerPage() throws Exception {
		return pagesCollecton.getPage(VideoPlayerPage.class);
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
}
