package com.wearezeta.auto.win.pages.common;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.driver.ZetaWinDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.win.common.WinExecutionContext;
import static com.wearezeta.auto.win.common.WinExecutionContext.USER_HOME;
import com.wearezeta.auto.win.locators.WinLocators;
import com.wearezeta.auto.win.pages.win.WinPage;

public class ChoosePicturePage extends WinPage {

	private static final Logger log = ZetaLogger.getLog(ChoosePicturePage.class
			.getSimpleName());

	@FindBy(how = How.ID, using = WinLocators.ChoosePicturePage.idOpenButton)
	private WebElement openButton;

	@FindBy(how = How.ID, using = WinLocators.ChoosePicturePage.idCancelButton)
	private WebElement cancelButton;

	@FindBy(how = How.XPATH, using = WinLocators.ChoosePicturePage.xpathFileListScrollArea)
	private WebElement fileListArea;

	@FindBy(how = How.XPATH, using = WinLocators.ChoosePicturePage.xpathSelectColumnViewButton)
	private WebElement selectColumnViewButton;

	public ChoosePicturePage(Future<ZetaWinDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public Boolean isVisible() {
		boolean isVisible = (cancelButton != null);
		if (isVisible) {
			log.debug("Choose picture page opened");
		}
		return isVisible;
	}

	public void searchForImage(String imageName) throws Exception {
		String xpath = String.format(WinLocators.ChoosePicturePage.xpathFormatFinderImageFile,
				imageName);

		WebElement el = getDriver().findElement(By.xpath(xpath));
		el.click();
	}

	public boolean selectColumnView() throws Exception {
		try {
			selectColumnViewButton.click();
			return true;
		} catch (NoSuchElementException e) {
			log.debug("Can't find column view selector.\n"
					+ this.getDriver().getPageSource());
			return false;
		}
	}

	public void goToSelectedFavoritesFolder(String folderName) throws Exception {
		try {
			String xpath = String
					.format(WinLocators.ChoosePicturePage.xpathFormatFavoritesFolderPopUp,
							folderName);
			WebElement folder = getDriver().findElement(By.xpath(xpath));
			folder.click();
		} catch (NoSuchElementException e) {
			log.debug("No " + folderName + " folder found in favorites.\n"
					+ this.getDriver().getPageSource());
		}
	}

	public boolean isOpenButtonEnabled() {
		try {
			return openButton.isEnabled();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void openImage(String filename) throws Exception {
		if (selectColumnView()) {
			goToSelectedFavoritesFolder(USER_HOME);
			searchForImage(filename);
		} else {
			String openImageScript = "tell application \"System Events\" to tell application process \"Wire\"\n"
					+ "set frontmost to true\n"
					+ "end tell\n"
					+ "delay 3\n"
					+ "tell application \"System Events\" to keystroke \""
					+ filename
					+ "\"\n"
					+ "tell application \"System Events\"\n"
					+ "key code 36\n"
					+ "end tell";
			this.getDriver().executeScript(openImageScript);
			this.getDriver().navigate().to(WinExecutionContext.WIRE_APP_PATH);
		}
	}
}
