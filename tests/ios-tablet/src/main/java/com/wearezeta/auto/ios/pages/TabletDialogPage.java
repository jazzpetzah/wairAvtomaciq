package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.IOSConstants;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class TabletDialogPage extends DialogPage {
	
	@FindBy(how = How.NAME, using = IOSLocators.nameAddPictureButton)
	private WebElement addPictureButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameOpenConversationDetails)
	protected WebElement openConversationDetails;
	
	public TabletDialogPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
	
	public void isImageShown(String filename) throws Throwable{
		
		BufferedImage templateImage = takeImageScreenshot();
		BufferedImage referenceImage = ImageUtil.readImageFromFile(IOSPage
				.getImagesPath() + filename);
		
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage,
				ImageUtil.RESIZE_TEMPLATE_TO_RESOLUTION);
		
		System.out.println("SCORE: " + score);
		
		Assert.assertTrue(
				"Overlap between two images has no enough score. Expected >= "
						+ IOSConstants.MIN_IMG_SCORE + ", current = " + score,
				score >= IOSConstants.MIN_IMG_SCORE);
	}
	
	public CameraRollTabletPopoverPage pressAddPictureiPadButton() throws Exception {
		addPictureButton.click();
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(IOSLocators.nameCameraLibraryButton));
		return new CameraRollTabletPopoverPage(this.getLazyDriver());
	}
	
	public TabletConversationDetailPopoverPage pressConversationDetailiPadButton() throws Exception{
		openConversationDetails.click();
		return new TabletConversationDetailPopoverPage(this.getLazyDriver());
	}
	
	public TabletGroupConversationDetailPopoverPage pressGroupConversationDetailiPadButton() throws Exception{
		openConversationDetails.click();
		return new TabletGroupConversationDetailPopoverPage(this.getLazyDriver());
	}
	
}
