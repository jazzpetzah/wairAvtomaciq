package com.wearezeta.auto.ios;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.ios.IOSConstants;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.user_management.ClientUsersManager;

import cucumber.api.java.en.When;

import org.junit.Assert;

public class ImageFullScreenPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	BufferedImage referenceImage;

	@When("^I see Full Screen Page opened$")
	public void ISeeFullScreenPage() {
		Assert.assertTrue(PagesCollection.imageFullScreenPage
				.isImageFullScreenShown());
	}

	@When("I see expected image in fullscreen (.*)")
	public void ISeeExpectedImageInFullScreen(String filename) throws Throwable {
		referenceImage = PagesCollection.imageFullScreenPage.takeScreenshot();
		BufferedImage templateImage = ImageUtil.readImageFromFile(IOSPage
				.getImagesPath() + filename);
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage);
		System.out.print("SCORE: " + score);
		Assert.assertTrue(
				"Overlap between two images has no enough score. Expected >= "
						+ IOSConstants.MIN_IMG_SCORE + " , current = " + score,
				score >= IOSConstants.MIN_IMG_SCORE);
	}

	@When("I zoom image in fullscreen and see it is zoomed")
	public void IZoomImageAndVerifyIsZoomed() {
		// TODO Piotr implement zoom
		// TODO Piotr compare before and after zoom
	}

	@When("I tap on fullscreen page")
	public void ITapFullScreenPage() {
		PagesCollection.imageFullScreenPage = PagesCollection.imageFullScreenPage
				.tapOnFullScreenPage();
	}

	@When("I see sender first name (.*) on fullscreen page")
	public void ISeeSenderName(String sender) {
		String senderFirstName = usrMgr.findUserByNameAlias(sender).getName()
				.split(" ")[0].toUpperCase();
		Assert.assertEquals(senderFirstName,
				PagesCollection.imageFullScreenPage.getSenderName());
	}

	@When("I see send date on fullscreen page")
	public void ISeeSendDate() {
		long actualDate = DialogPageSteps.sendDate;
		long expectedDate = IOSCommonUtils
				.stringToTime(PagesCollection.imageFullScreenPage
						.getTimeStamp());
		boolean flag = Math.abs(actualDate - expectedDate) < IOSConstants.DELTA_SEND_TIME;
		Assert.assertTrue("Expected date: " + expectedDate
				+ " is different from actual: " + actualDate, flag);
	}

	@When("I see download button shown on fullscreen page")
	public void ISeeDownloadButtonOnFullscreenPage() {
		Assert.assertTrue(PagesCollection.imageFullScreenPage
				.isDownloadButtonVisible());
	}

	@When("I tap download button on fullscreen page")
	public void ITapDownloadButtonOnFullscreenPage() {
		PagesCollection.imageFullScreenPage.clickDownloadButton();
	}

	@When("I verify image is downloaded and is same as original")
	public void IVerifyDownloadedImageSameOriginal() {
		// TODO Piotr compare with downloaded image
	}

	@When("I verify image caption and download button are not shown")
	public void IDontSeeImageCaptionAndDownloadButton() {
		Assert.assertFalse(PagesCollection.imageFullScreenPage
				.isDownloadButtonVisible());
		Assert.assertFalse(PagesCollection.imageFullScreenPage
				.isSenderNameVisible());
		Assert.assertFalse(PagesCollection.imageFullScreenPage
				.isSentTimeVisible());
	}

	@When("I tap close fullscreen page button")
	public void ITapCloseFullscreenButton() throws IOException {
		PagesCollection.imageFullScreenPage.clickCloseButton();
	}

}
