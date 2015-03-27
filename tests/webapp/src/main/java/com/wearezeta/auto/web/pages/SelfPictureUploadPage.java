package com.wearezeta.auto.web.pages;

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
		final String showPathInputJScript = "$('"
				+ WebAppLocators.SelfPictureUploadPage.cssSendPictureInput
				+ "').css({'left': '0', 'opacity': '100', 'z-index': '100'});";
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
}
