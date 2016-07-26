package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CameraPage;
import cucumber.api.java.en.When;

public class CameraPageSteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private CameraPage getCameraPage() throws Exception {
        return pagesCollection.getPage(CameraPage.class);
    }

    /**
     * Tap the corresponding button on Camera page
     *
     * @step. I tap (Take Photo|Camera Roll) button on Camera page$
     * @throws Exception
     */
    @When("^I tap (Take Photo|Camera Roll) button on Camera page$")
    public void TTapButton(String name) throws Exception {
        switch (name.toLowerCase()) {
            case "take photo":
                getCameraPage().tapTakePhotoButton();
                break;
            case "camera roll":
                getCameraPage().tapCameraRollButton();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }
}
