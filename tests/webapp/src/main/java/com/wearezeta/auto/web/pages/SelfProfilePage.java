package com.wearezeta.auto.web.pages;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.Browser;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class SelfProfilePage extends WebPage {

    @FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathCameraButton)
    private WebElement cameraButton;

    @FindBy(how = How.CSS, using = WebAppLocators.SelfProfilePage.cssSelectPicture)
    private WebElement selectPictureInput;

    @FindBy(how = How.CSS, using = WebAppLocators.SelfPictureUploadPage.cssChooseYourOwnInput)
    private WebElement chooseYourOwnInput;

    @FindBy(how = How.XPATH, using = WebAppLocators.SelfProfilePage.xpathAccentColorPickerChildren)
    private List<WebElement> colorsInColorPicker;

    public SelfProfilePage(Future<ZetaWebAppDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    private WebElement waitForHalt(int timeoutInMillisForHalt, int timeoutInMillisForElement,
            ExpectedCondition<WebElement> innerConditionForElementDetection) throws Exception {
        final ZetaWebAppDriver driver = getDriver();
        return new WebDriverWait(driver, 1, timeoutInMillisForElement).withTimeout(timeoutInMillisForHalt,
                TimeUnit.MILLISECONDS).until(new ExpectedCondition<WebElement>() {
                    int x;
                    int y;

                    @Override
                    public WebElement apply(WebDriver input) {
                        final WebElement element = new WebDriverWait(driver, 1)
                                .withTimeout(timeoutInMillisForElement, TimeUnit.MILLISECONDS)
                                .ignoring(TimeoutException.class)
                                .ignoring(NoSuchElementException.class)
                                .ignoring(StaleElementReferenceException.class)
                                .ignoring(InvalidElementStateException.class)
                                .until(innerConditionForElementDetection);
                        if (element == null) {
                            return null;
                        }
                        if (x == element.getLocation().getX() && y == element.getLocation().getY()) {
                            return element;
                        }
                        x = element.getLocation().getX();
                        y = element.getLocation().getY();
                        return null;
                    }
                });
    }

    public void selectAccentColor(String colorName) throws Exception {
        final int id = AccentColor.getByName(colorName).getId();
        final String xpathAccentColorDiv = WebAppLocators.SelfProfilePage.xpathAccentColorDivById
                .apply(id);
        assert DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(xpathAccentColorDiv));
        final WebElement accentColorDiv = this.getDriver().findElementByXPath(
                xpathAccentColorDiv);
        assert DriverUtils.waitUntilElementClickable(this.getDriver(),
                accentColorDiv);
        accentColorDiv.click();
    }

    public String getCurrentAccentColor() throws Exception {
        final WebElement accentColorCircleDiv = this
                .getDriver()
                .findElementByXPath(
                        WebAppLocators.SelfProfilePage.xpathCurrentAccentColorCircleDiv);
        return accentColorCircleDiv.getCssValue("border-top-color");
    }

    public int getCurrentAccentColorId() {
        int i = 1;
        for (WebElement childDiv : colorsInColorPicker) {
            if (childDiv.getAttribute("class").toLowerCase()
                    .contains("selected")) {
                return i;
            }
            i++;
        }
        throw new RuntimeException(
                "No accent color is selected in color picker");
    }

    public void clickCameraButton() throws Exception {
        DriverUtils.waitUntilElementClickable(this.getDriver(), cameraButton);
        cameraButton.click();
    }

    public AccentColor getCurrentAvatarAccentColor() throws Exception {
        final WebElement backgroundAvatarAccentColor = this
                .getDriver()
                .findElementByXPath(
                        WebAppLocators.SelfProfilePage.xpathBackgroundAvatarAccentColor);
        return AccentColor.getByRgba(backgroundAvatarAccentColor
                .getCssValue("background-color"));
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
    }

    public void uploadPicture(String pictureName) throws Exception {
        final String picturePath = WebCommonUtils
                .getFullPicturePath(pictureName);
        if (WebAppExecutionContext.getBrowser() == Browser.Safari) {
            WebCommonUtils.sendPictureInSafari(picturePath, this.getDriver().getNodeIp());
        } else {
            selectPictureInput.sendKeys(picturePath);
        }
        if (WebAppExecutionContext.getBrowser() == Browser.Firefox) {
            // manually trigger change event on input
            this.getDriver().executeScript("evt = new Event('change');arguments[0].dispatchEvent(evt);", chooseYourOwnInput);
        }
    }
}
