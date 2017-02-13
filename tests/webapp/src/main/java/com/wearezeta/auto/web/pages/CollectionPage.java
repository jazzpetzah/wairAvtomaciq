package com.wearezeta.auto.web.pages;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CollectionPage extends WebPage {

    @FindBy(css = WebAppLocators.CollectionPage.cssCloseButton)
    private WebElement closeButton;

    @FindBy(css = WebAppLocators.CollectionPage.cssNoItemsPlaceholder)
    private WebElement noItemsPlaceholder;

    @FindBy(css = WebAppLocators.CollectionPage.cssPictures)
    private List<WebElement> pictures;

    @FindBy(css = WebAppLocators.CollectionPage.cssPictureCollectionSize)
    private WebElement pictureCollectionSize;

    @FindBy(css = WebAppLocators.CollectionPage.cssVideos)
    private List<WebElement> videos;

    @FindBy(css = WebAppLocators.CollectionPage.cssVideoCollectionSize)
    private WebElement videoCollectionSize;

    @FindBy(css = WebAppLocators.CollectionPage.cssAudioFiles)
    private List<WebElement> audioFiles;

    @FindBy(css = WebAppLocators.CollectionPage.cssAudioCollectionSize)
    private WebElement audioCollectionSize;

    @FindBy(css = WebAppLocators.CollectionPage.cssFiles)
    private List<WebElement> files;

    @FindBy(css = WebAppLocators.CollectionPage.cssFilesPage)
    private List<WebElement> filesPage;

    @FindBy(css = WebAppLocators.CollectionPage.cssFileCollectionSize)
    private WebElement fileCollectionSize;

    @FindBy(css = WebAppLocators.CollectionPage.cssLinkPreviewUrls)
    private List<WebElement> linkPreviewUrls;

    @FindBy(css = WebAppLocators.CollectionPage.cssLinkPreviewTitles)
    private List<WebElement> linkPreviewTitles;

    @FindBy(css = WebAppLocators.CollectionPage.cssLinkPreviewImages)
    private List<WebElement> linkPreviewImages;

    @FindBy(css = WebAppLocators.CollectionPage.cssLinkPreviewFroms)
    private List<WebElement> linkPreviewFroms;

    @FindBy(css = WebAppLocators.CollectionPage.cssLinkCollectionSize)
    private WebElement linkCollectionSize;

    @FindBy(css = WebAppLocators.CollectionPage.cssShowAllPictures)
    private WebElement showAllPictures;

    public CollectionPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickClose() {
        closeButton.click();
    }

    public String getNoItemsPlaceholder() {
        return noItemsPlaceholder.getText();
    }

    public int getNumberOfPictures() {
        return pictures.size();
    }

    public String getLabelOfPictureCollectionSize() {
        return pictureCollectionSize.getText();
    }

    public int getNumberOfVideos() {
        return videos.size();
    }

    public int getNumberOfAudioFiles() {
        return audioFiles.size();
    }

    public String getLabelOfVideoCollectionSize() {
        return videoCollectionSize.getText();
    }

    public String getLabelOfAudioCollectionSize() {
        return audioCollectionSize.getText();
    }

    public int getNumberOfFiles() {
        return files.size();
    }

    public int getNumberOfFilesInFileDetailPage() {
        return filesPage.size();
    }

    public String getLabelOfFileCollectionSize() {
        return fileCollectionSize.getText();
    }

    public int getNumberOfLinks() {
        return linkPreviewUrls.size();
    }

    public List<String> getLinkPreviewUrls() {
        List<String> urls = new ArrayList<>();
        for (WebElement element : linkPreviewUrls) {
            urls.add(element.getText());
        }
        return urls;
    }

    public List<String> getLinkPreviewTitles() {
        return linkPreviewTitles.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public BufferedImage getLinkPreviewImage() throws Exception {
        return this.getElementScreenshot(linkPreviewImages.get(linkPreviewImages.size() - 1))
                .orElseThrow(IllegalStateException::new);
    }

    public List<String> getLinkPreviewFroms() {
        return linkPreviewFroms.stream().map(e -> e.getText().toLowerCase()).collect(Collectors.toList());
    }

    public String getLabelOfLinkCollectionSize() {
        return linkCollectionSize.getText();
    }

    public void clickFirstPictureInCollection(int index) {
        pictures.get(index - 1).click();
    }

    public void clickShowAllPicturesInCollection() {showAllPictures.click();}

    public void clickShowAllFilesLabel() {
        fileCollectionSize.click();
    }

    public void clickShowAllAudioInCollection() {audioCollectionSize.click();}
}
