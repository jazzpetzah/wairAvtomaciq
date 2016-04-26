package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.TakePicturePage;

import cucumber.api.java.en.When;
import org.junit.Assert;

public class TakePicturePageSteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private TakePicturePage getTakePicturePage() throws Exception {
        return pagesCollection.getPage(TakePicturePage.class);
    }

    @When("^I press Camera Roll button$")
    public void IPressCameraRollButton() throws Throwable {
        getTakePicturePage().pressSelectFromLibraryButton();
    }

    @When("^I choose a picture from camera roll$")
    public void IChooseAPictureFromCameraRoll() throws Throwable {
        getTakePicturePage().selectImageFromLibrary();
    }

    /**
     * Presses the sketch button on the camera roll page
     *
     * @throws Throwable
     * @step. ^I press sketch button on camera roll page$
     */
    @When("^I press sketch button on camera roll page$")
    public void IPressSketchButtonOnCameraRollPage() throws Throwable {
        getTakePicturePage().clickCameraRollSketchButton();
    }


    /**
     * Tap the shutter button to take a camera picture
     *
     * @throws Exception
     * @step. ^I tap Camera Shutter button$
     */
    @When("^I tap Camera Shutter button$")
    public void ITapCameraShutterButton() throws Exception {
        getTakePicturePage().tapShutterButton();
    }

    /**
     * Taps lens camera button to take picture
     *
     * @throws Exception
     * @step. ^I tap Lens button$
     */
    @When("^I tap Lens button$")
    public void ITapLensButton() throws Exception {
        getTakePicturePage().tapCameraButton();
    }

    /**
     * Tap the corresponding button to switch camera
     *
     * @throws Exception
     * @step. ^I tap Toggle Camera button$
     */
    @When("^I tap Toggle Camera button$")
    public void ITapToggleCameraButton() throws Exception {
        getTakePicturePage().tapToggleCameraButton();
    }

    private Integer imagesInGalleryRememberedCount;

    /**
     * Save the count of images in gallery in local variable imagesInGalleryRememberedCount
     *
     * @throws Exception
     * @step. ^I remember the count of images in gallery$
     */
    @When("^I remember the count of images in gallery$")
    public void RememeberCountImagesInGallery() throws Exception {
        imagesInGalleryRememberedCount = getTakePicturePage().getImageInGalleryCount();
    }

    /**
     * Verify that the count of images in gallery has increased by pointed value.
     * 'I remember the count of images in gallery' step should be called before this step.
     *
     * @param count images increase value
     * @throws Exception
     * @step. ^I see the count of images in gallery has increased by (\d+)$
     */
    @When("^I see the count of images in gallery has increased by (\\d+)$")
    public void ISeeCountOfImagesInGalleryIncreased(int count) throws Exception {
        if (imagesInGalleryRememberedCount == null) {
            throw new IllegalStateException("Call 'I remember the count of images in gallery' step first");
        }
        int actualCount = getTakePicturePage().getImageInGalleryCount();
        int diff = actualCount - imagesInGalleryRememberedCount;
        Assert.assertEquals(String.format("Actual images count in gallery is '%s' but expected count is '%s'",
                actualCount, imagesInGalleryRememberedCount + count),
                diff, count);
    }

    /**
     * Tap close camera button
     *
     * @throws Exception
     * @step. ^I tap close camera button$
     */
    @When("^I tap close camera button$")
    public void ITapCloseCameraButton() throws Exception {
        getTakePicturePage().clickCloseCameraButton();
    }

    /**
     * Tap Camera Roll view
     *
     * @throws Exception
     * @step. ^I select Camera Roll view$
     */
    @When("^I select Camera Roll view$")
    public void ISelectCameraRollView() throws Exception {
        getTakePicturePage().clickCamerRollView();
    }
}
