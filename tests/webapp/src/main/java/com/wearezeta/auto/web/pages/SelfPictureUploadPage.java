package com.wearezeta.auto.web.pages;

import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class SelfPictureUploadPage extends WebPage {
	@FindBy(how = How.CSS, using = WebAppLocators.SelfPictureUploadPage.cssSendPictureInput)
	private WebElement picturePathInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfPictureUploadPage.xpathConfirmPictureSelectionButton)
	private WebElement pictureSelectionConfirmButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfPictureUploadPage.xpathNextCarouselImageBtn)
	private WebElement nextCarouselImageBtn;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfPictureUploadPage.xpathPreviousCarouselImageBtn)
	private WebElement previousCarouselImageBtn;

	public SelfPictureUploadPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void waitUntilVisible(int secondsTimeout) throws Exception {
		assert DriverUtils.isElementDisplayed(driver,
				By.xpath(WebAppLocators.SelfPictureUploadPage.xpathRootDiv),
				secondsTimeout) : "Picture selection dialog has not been show within "
				+ secondsTimeout + "second(s) timeout";
	}

	public void uploadPicture(String pictureName) throws Exception {
		final String picturePath = WebCommonUtils
				.getFullPicturePath(pictureName);
		final String showPathInputJScript = "$(\""
				+ WebAppLocators.SelfPictureUploadPage.cssSendPictureInput
				+ "\").css({'left': '0', 'opacity': '100', 'z-index': '100'});";
		driver.executeScript(showPathInputJScript);
		assert DriverUtils
				.isElementDisplayed(
						driver,
						By.cssSelector(WebAppLocators.SelfPictureUploadPage.cssSendPictureInput),
						5);
		if (WebAppExecutionContext.browserName
				.equals(WebAppConstants.Browser.SAFARI)) {
			WebCommonUtils.sendPictureInSafari(picturePath);
		} else {
			picturePathInput.sendKeys(picturePath);
		}
	}

	public void confirmPictureSelection() throws Exception {
		assert DriverUtils
				.isElementDisplayed(
						driver,
						By.xpath(WebAppLocators.SelfPictureUploadPage.xpathConfirmPictureSelectionButton),
						5);
		pictureSelectionConfirmButton.click();
	}

	public void forceCarouselMode() {
		final String forceCarouselScript = "window.wire.app.view.content.self_profile.show_get_picture();";
		driver.executeScript(forceCarouselScript);
	}

	private static final Random random = new Random();

	private static final int COUNT_OF_CAROUSEL_PICTURES = 5;
	private static final int CHANGE_PICTURE_TRANSITION_TIMEOUT_SECONDS = 1;

	public void selectRandomPictureFromCarousel() throws InterruptedException {
		for (int i = 0; i < COUNT_OF_CAROUSEL_PICTURES; i++) {
			if (random.nextInt(100) < 50) {
				nextCarouselImageBtn.click();
			} else {
				previousCarouselImageBtn.click();
			}
			Thread.sleep(CHANGE_PICTURE_TRANSITION_TIMEOUT_SECONDS * 1000);
		}
	}
}
