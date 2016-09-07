package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CameraRollPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.jcodec.common.Assert;

public class CameraRollPageSteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private CameraRollPage getCameraRollPage() throws Exception {
        return pagesCollection.getPage(CameraRollPage.class);
    }

    private int cameraRollPhotoCountSave;

    /**
     * Tap the first visible picture on Camera Roll page
     *
     * @step. ^I select the first picture from Camera Roll$
     * @throws Exception
     */
    @When("^I select the first picture from Camera Roll$")
    public void ISelectFirstPicture() throws Exception {
        getCameraRollPage().selectFirstPicture();
    }

    @When("I remember count of the photos in Camera Roll")
    public void IRememberCountInCameraRoll() throws Exception {
        cameraRollPhotoCountSave = getCameraRollPage().getCameraRollPhotoCount();
    }

    @Then("I see count of the photos in Camera Roll is increased by (\\d+)")
    public void ISeePhotoCountIsIncreasedByX(int increment) throws Exception {
        Assert.assertEquals(getCameraRollPage().getCameraRollPhotoCount() - increment, cameraRollPhotoCountSave);
    }
}
