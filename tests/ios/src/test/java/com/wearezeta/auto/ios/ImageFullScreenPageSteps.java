package com.wearezeta.auto.ios;

import java.io.IOException;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.When;
import cucumber.api.java.it.Date;

import org.junit.Assert;

public class ImageFullScreenPageSteps {
	
	@When("^I see Full Screen Page opened$")
	public void ISeeFullScreenPage(){
		Assert.assertTrue(PagesCollection.imageFullScreenPage.isImageFullScreenShown());
	}
	
	@When("I see expected image in fullscreen")
	public void ISeeExpectedImageInFullScreen(){
		//TODO Piotr iamge comparison
	}
	
	@When("I zoom image in fullscreen and see it is zoomed")
	public void IZoomImageAndVerifyIsZoomed(){
		//TODO Piotr implement zoom
		//TODO Piotr compare before and after zoom
	}
	
	@When("I tap on fullscreen page")
	public void ITapFullScreenPage(){
		PagesCollection.imageFullScreenPage = PagesCollection.imageFullScreenPage.tapOnFullScreenPage();
	}
	
	@When("I see sender first name (.*) on fullscreen page")
	public void ISeeSenderName(String sender){
		String senderFirstName = CommonUtils.retrieveRealUserContactPasswordValue(sender).split(" ")[0].toUpperCase();
		Assert.assertEquals(senderFirstName, PagesCollection.imageFullScreenPage.getSenderName());
	}
	
	@When("I see send date on fullscreen page")
	public void ISeeSendDate(){
		Assert.assertEquals(DialogPageSteps.sendDate, PagesCollection.imageFullScreenPage.getTimeStamp());
	}
	
	@When("I see download button shown on fullscreen page")
	public void ISeeDownloadButtonOnFullscreenPage(){
		Assert.assertTrue(PagesCollection.imageFullScreenPage.isDownloadButtonVisible());
	}
	
	@When("I tap download button on fullscreen page")
	public void ITapDownloadButtonOnFullscreenPage(){
		PagesCollection.imageFullScreenPage.clickDownloadButton();
	}
	
	@When("I verify image is downloaded and is same as original")
	public void IVerifyDownloadedImageSameOriginal(){
		//TODO Piotr compare with downloaded image
	}
	
	@When("I verify image caption and download button are not shown")
	public void IDontSeeImageCaptionAndDownloadButton(){
		Assert.assertFalse(PagesCollection.imageFullScreenPage.isDownloadButtonVisible());
		Assert.assertFalse(PagesCollection.imageFullScreenPage.isSenderNameVisible());
		Assert.assertFalse(PagesCollection.imageFullScreenPage.isSentTimeVisible());
	}
	
	@When("I tap close fullscreen page button")
	public void ITapCloseFullscreenButton() throws IOException{
		PagesCollection.imageFullScreenPage.clickCloseButton();
	}

}
