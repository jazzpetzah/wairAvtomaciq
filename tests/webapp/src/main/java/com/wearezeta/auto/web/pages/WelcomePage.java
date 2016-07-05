package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.Browser;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class WelcomePage extends WebPage {

	private static final int TIMEOUT_UNSPLASH = 15; // seconds

	@FindBy(how = How.CSS, using = WebAppLocators.SelfPictureUploadPage.cssChooseYourOwnInput)
	private WebElement chooseYourOwnInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfPictureUploadPage.xpathConfirmPictureSelectionButton)
	private WebElement pictureSelectionConfirmButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfPictureUploadPage.xpathNextCarouselImageBtn)
	private WebElement nextCarouselImageBtn;

	@FindBy(how = How.XPATH, using = WebAppLocators.SelfPictureUploadPage.xpathPreviousCarouselImageBtn)
	private WebElement previousCarouselImageBtn;

	@FindBy(how = How.CSS, using = WebAppLocators.SelfPictureUploadPage.cssKeepPictureButton)
	private WebElement keepPictureButton;

	public WelcomePage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void waitUntilNotVisible() throws Exception {
		assert DriverUtils
				.waitUntilLocatorDissapears(
						this.getDriver(),
						By.xpath(WebAppLocators.SelfPictureUploadPage.cssKeepPictureButton)) : "Keep picture button is still visible";
	}

	public void waitUntilButtonsAreClickable() throws Exception {
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				keepPictureButton, TIMEOUT_UNSPLASH) : "Keep picture button was not clickable";
	}

	public void uploadPicture(String pictureName) throws Exception {
		final String picturePath = WebCommonUtils
				.getFullPicturePath(pictureName);
		if (WebAppExecutionContext.getBrowser() == Browser.Firefox) {
			this.getDriver().executeScript("arguments[0].style.left='0';arguments[0].style.opacity='100';arguments[0].style.zIndex='100';", chooseYourOwnInput);
		}
		chooseYourOwnInput.sendKeys(picturePath);
		// manually trigger change event on input
		this.getDriver().executeScript("evt = new Event('change');arguments[0].dispatchEvent(evt);", chooseYourOwnInput);
	}

	public void keepPicture() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				keepPictureButton, TIMEOUT_UNSPLASH) : String.format(
				"Keep picture button not clickable after %s seconds",
				TIMEOUT_UNSPLASH);
		keepPictureButton.click();
	}
}
