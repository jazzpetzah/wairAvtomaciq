package com.wearezeta.auto.ios.steps;

import java.awt.image.BufferedImage;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.IOSConstants;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.ImageFullScreenPage;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

public class ImageFullScreenPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private ImageFullScreenPage getImageFullScreenPage() throws Exception {
		return (ImageFullScreenPage) pagesCollecton
				.getPage(ImageFullScreenPage.class);
	}

	public static final double FULLSCREEN_SCORE = 0.35;

	BufferedImage referenceImage;

	@When("^I see Full Screen Page opened$")
	public void ISeeFullScreenPage() throws Exception {
		Assert.assertTrue(getImageFullScreenPage().isImageFullScreenShown());
	}

	@When("I see expected image in fullscreen (.*)")
	public void ISeeExpectedImageInFullScreen(String filename) throws Throwable {
		referenceImage = getImageFullScreenPage().takeScreenshot().orElseThrow(
				AssertionError::new);
		BufferedImage templateImage = ImageUtil.readImageFromFile(IOSPage
				.getImagesPath() + filename);
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage,
				ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
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
	public void ITapFullScreenPage() throws Exception {
		getImageFullScreenPage().tapOnFullScreenPage();
	}

	@When("I see sender first name (.*) on fullscreen page")
	public void ISeeSenderName(String sender) throws Exception {
		String senderFirstName = usrMgr.findUserByNameOrNameAlias(sender)
				.getName().split(" ")[0].toUpperCase();
		Assert.assertEquals(senderFirstName, getImageFullScreenPage()
				.getSenderName());
	}

	@When("I see send date on fullscreen page")
	public void ISeeSendDate() throws Exception {
		long actualDate = DialogPageSteps.sendDate;
		long expectedDate = IOSCommonUtils
				.stringToTime(getImageFullScreenPage().getTimeStamp());
		boolean flag = Math.abs(actualDate - expectedDate) < IOSConstants.DELTA_SEND_TIME;
		Assert.assertTrue("Expected date: " + expectedDate
				+ " is different from actual: " + actualDate, flag);
	}

	@When("I see download button shown on fullscreen page")
	public void ISeeDownloadButtonOnFullscreenPage() throws Exception {
		Assert.assertTrue(getImageFullScreenPage().isDownloadButtonVisible());
	}

	@When("I tap download button on fullscreen page")
	public void ITapDownloadButtonOnFullscreenPage() throws Exception {
		getImageFullScreenPage().clickDownloadButton();
	}

	@When("I verify image is downloaded and is same as original")
	public void IVerifyDownloadedImageSameOriginal() {
		// TODO Piotr compare with downloaded image
	}

	@When("I verify image caption and download button are not shown")
	public void IDontSeeImageCaptionAndDownloadButton() throws Exception {
		Assert.assertFalse(getImageFullScreenPage().isDownloadButtonVisible());
		Assert.assertFalse(getImageFullScreenPage().isSenderNameVisible());
		Assert.assertFalse(getImageFullScreenPage().isSentTimeVisible());
	}

	@When("I tap close fullscreen page button")
	public void ITapCloseFullscreenButton() throws Exception {
		getImageFullScreenPage().clickCloseButton();
	}

	/**
	 * When the image is shown in fullscreen mode, the device gets rotated
	 * 
	 * @step. ^I see image rotated in fullscreen mode$
	 * @throws Exception
	 */
	@Then("^I see image rotated in fullscreen mode$")
	public void ISeeImageRotatedInFullscreenMode() throws Exception {
		Thread.sleep(2000);
		referenceImage = getImageFullScreenPage().takeScreenshot().orElseThrow(
				AssertionError::new);
		BufferedImage templateImage = ImageUtil.readImageFromFile(IOSPage
				.getImagesPath() + "rotatedFullscreenImage.png");
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage,
				ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
		System.out.print("SCORE: " + score);
		Assert.assertTrue(
				"Overlap between two images has no enough score. Expected >= "
						+ FULLSCREEN_SCORE + " , current = " + score,
				score >= FULLSCREEN_SCORE);
	}

}
