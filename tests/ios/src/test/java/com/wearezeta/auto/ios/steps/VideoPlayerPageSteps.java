package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.When;

public class VideoPlayerPageSteps {

	@When("I see video player page is opened")
	public void ISeeVideoPlayerPage() throws Exception {
		PagesCollection.videoPlayerPage.waitForVideoPlayerPage();
		Assert.assertTrue("Video Player is not opened",
				PagesCollection.videoPlayerPage.isVideoPlayerPageOpened());
	}

	@When("I tap on Done button on Video player page")
	public void ITapOnDoneButtonOnVideoPlayerPage() throws Exception {
		PagesCollection.videoPlayerPage.tapVideoPage();
		PagesCollection.dialogPage = PagesCollection.videoPlayerPage
				.clickVideoDoneButton();
	}

	@When("I tap Pause button on Video player page")
	public void ITapPauseButtonOnVideoPlayerPage() throws Exception {
		PagesCollection.videoPlayerPage.clickPauseButton();
	}

	@When("I tap video player page")
	public void ITapVideoPlayerPage() {
		PagesCollection.videoPlayerPage.tapVideoPage();
	}
}
