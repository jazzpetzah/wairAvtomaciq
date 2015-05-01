package com.wearezeta.auto.web.pages;

import java.io.File;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ProfilePicturePage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(ProfilePicturePage.class.getSimpleName());

	@FindBy(how = How.XPATH, using = WebAppLocators.ProfilePicturePage.xpathConfirmPictureSelectionButton)
	private WebElement confirmButton;

	public ProfilePicturePage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void waitUntilVisible(int timeoutSeconds) throws Exception {
		assert DriverUtils
				.isElementDisplayed(
						this.getDriver(),
						By.xpath(WebAppLocators.ProfilePicturePage.xpathSelectPictureButton),
						timeoutSeconds) : "Profile picture dialog has not been shown after "
				+ timeoutSeconds + " second(s) timeout";
	}

	public void waitUntilNotVisible(int timeoutSeconds) throws Exception {
		assert DriverUtils
				.waitUntilElementDissapear(
						this.getDriver(),
						By.xpath(WebAppLocators.ProfilePicturePage.xpathSelectPictureButton),
						timeoutSeconds) : "Profile picture dialog has not been hidden after "
				+ timeoutSeconds + " second(s) timeout";
	}

	public void dropPicture(String pictureName) throws Exception {
		final String srcPicturePath = WebCommonUtils
				.getFullPicturePath(pictureName);
		assert new File(srcPicturePath).exists() : srcPicturePath
				+ " file should exist on hub file system";

		/*
		 * The code below allows to upload the picture to the remote mode
		 * without Selenium interaction This could be useful when we have better
		 * solution for drag and drop
		 */
		// final String dstPicturePathForScp = WebAppConstants.TMP_ROOT + "/"
		// + pictureName;
		// WebCommonUtils.putFileOnExecutionNode(this.getDriver().getNodeIp(),
		// srcPicturePath, dstPicturePathForScp);
		//
		// String dstPicturePath = null;
		// if (WebAppExecutionContext.isCurrentPlatfromWindows()) {
		// dstPicturePath = WebAppConstants.WINDOWS_TMP_ROOT + "\\"
		// + pictureName;§
		// } else {
		// dstPicturePath = dstPicturePathForScp;
		// }

		// http://stackoverflow.com/questions/5188240/using-selenium-to-imitate-dragging-a-file-onto-an-upload-element
		final String inputId = "SelfImageUpload";
		this.getDriver().executeScript(
				inputId + " = window.$('<input id=\"" + inputId
						+ "\"/>').attr({type:'file'}).appendTo('body');");
		// The file is expected to be uploaded automatically by Webdriver
		getDriver().findElement(By.id(inputId)).sendKeys(srcPicturePath);
		this.getDriver().executeScript(
				"e = $.Event('drop'); e.originalEvent = {dataTransfer : { files : "
						+ inputId + ".get(0).files } }; $(\""
						+ WebAppLocators.ProfilePicturePage.cssDropZone
						+ "\").trigger(e);");

		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				confirmButton) : "Confirm button is not visible/clickable";
	}

	public void clickConfirmImageButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				confirmButton);
		confirmButton.click();
	}
}
