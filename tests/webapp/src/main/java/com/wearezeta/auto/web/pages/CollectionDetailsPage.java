package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.concurrent.Future;



public class CollectionDetailsPage extends WebPage {

    @FindBy(id = WebAppLocators.CollectionPage.idCollectionDetails)
    private WebElement collectionDetails;

    @FindBy(css = WebAppLocators.CollectionPage.cssBackToCollectionButton)
    private WebElement backToCollectionButton;

    @FindBy(css = WebAppLocators.CollectionPage.cssPicturesOnCollectionDetails)
    private List<WebElement> picturesOnCollectionDetails;

    @FindBy(css = WebAppLocators.CollectionPage.cssAudioFilesOnCollectionDetails)
    private List<WebElement> audioFilesOnCollectionDetails;

    public CollectionDetailsPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }
    public int getNumberOfPictures() {
        return picturesOnCollectionDetails.size();
    }

    public void clickBackButtonCollectionDetails(){
        backToCollectionButton.click();
    }

    public int getNumberOfAudioFiles(){
        return audioFilesOnCollectionDetails.size();
    }
}
