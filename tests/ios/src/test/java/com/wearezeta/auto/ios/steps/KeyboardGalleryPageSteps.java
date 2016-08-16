package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.KeyboardGalleryPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

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
    @When("^I tap (Camera Shutter|Camera Roll|Toggle Camera|Fullscreen Camera|Back) button on Keyboard Gallery overlay$")
    public void ITapButton(String name) throws Exception {
        getKeyboardGalleryPage().tapButton(name);
    }

    /**
     * Verify whether the corresponding button is present on the overlay
     *
     * @param shouldNotSee equals to null if the button should be visible
     * @param name         one of possible button names
     * @throws Exception
     * @step. ^I (do not )?see (Camera Shutter|Camera Roll|Toggle Camera|Fullscreen Camera|Back)
     * button on Keyboard Gallery overlay$
     */
    @Then("^I (do not )?see (Camera Shutter|Camera Roll|Toggle Camera|Fullscreen Camera|Back) " +
            "button on Keyboard Gallery overlay$")
    public void ISeeButton(String shouldNotSee, String name) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The '%s' button is not visible on the Keyboard Gallery overlay", name),
                    getKeyboardGalleryPage().isButtonVisible(name));
        } else {
            Assert.assertTrue(
                    String.format("The '%s' button should not be visible on the Keyboard Gallery overlay", name),
                    getKeyboardGalleryPage().isButtonInvisible(name));
        }
    }
}
