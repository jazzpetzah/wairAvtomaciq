package com.wearezeta.auto.ios;

import org.junit.Assert;

import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.When;

public class VideoPlayerPageSteps {
	
	@When("I see video player page is opened")
	public void ISeeVideoPlayerPage(){
		Assert.assertTrue("Video Player is not opened", PagesCollection.videoPlayerPage.isVideoPlayerPageOpened());
	}

}
