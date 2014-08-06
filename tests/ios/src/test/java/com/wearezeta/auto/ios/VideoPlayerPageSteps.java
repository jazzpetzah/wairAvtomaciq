package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.When;

public class VideoPlayerPageSteps {
	
	@When("I see video player page is opened")
	public void ISeeVideoPlayerPage() throws InterruptedException{
		PagesCollection.videoPlayerPage.waitForVideoPlayerPage();
		Assert.assertTrue("Video Player is not opened", PagesCollection.videoPlayerPage.isVideoPlayerPageOpened());
	}
	
	@When("I tap on Done button on Video player page")
	public void ITapOnDoneButtonOnVideoPlayerPage() throws IOException{
		PagesCollection.dialogPage = PagesCollection.videoPlayerPage.clickVideoDoneButton();
	}
	
	@When("I tap Pause button on Video player page")
	public void ITapPauseButtonOnVideoPlayerPage(){
		PagesCollection.videoPlayerPage.clickPauseButton();
	}

}
