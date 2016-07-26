package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.KeyboardGalleryPage;

import cucumber.api.java.en.When;

public class KeyboardGalleryPageSteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private KeyboardGalleryPage getKeyboardGalleryPage() throws Exception {
        return pagesCollection.getPage(KeyboardGalleryPage.class);
    }

    /**
     * Tap the first visible picture on Keyboard Gallery overlay
     *
     * @throws Exception
     * @step. ^I select the first picture from Keyboard Gallery$
     */
    @When("^I select the first picture from Keyboard Gallery$")
    public void ISelectFirstPicture() throws Exception {
        getKeyboardGalleryPage().selectFirstPicture();
    }

    /**
     * Tap the corresponding button on Keyboard Gallery overlay
     *
     * @param name one of possible button names
     * @throws Exception
     * @step.
     */
    @When("^I tap (Camera Shutter|Camera Roll|Sketch|Toggle Camera) button on Keyboard Gallery overlay$")
    public void ITapButton(String name) throws Exception {
        switch (name.toLowerCase()) {
            case "camera shutter":
                getKeyboardGalleryPage().tapTakePictureButton();
                break;
            case "camera roll":
                getKeyboardGalleryPage().tapCameraRollButton();
                break;
            case "sketch":
                getKeyboardGalleryPage().tapSketchButton();
                break;
            case "toggle camera":
                getKeyboardGalleryPage().tapToggleCameraButton();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }
}
