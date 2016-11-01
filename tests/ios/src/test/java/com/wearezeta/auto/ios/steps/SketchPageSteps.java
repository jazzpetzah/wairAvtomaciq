package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.SketchPage;

import cucumber.api.java.en.When;

public class SketchPageSteps {
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private SketchPage getSketchPage() throws Exception {
        return pagesCollection.getPage(SketchPage.class);
    }

    /**
     * randomly draws lines in sketch feature
     *
     * @throws Exception
     * @step. ^I draw a random sketch$
     */
    @When("^I draw a random sketch$")
    public void IDrawRandomSketches() throws Exception {
        getSketchPage().sketchRandomLines();
    }

    /**
     * Tap the corresponding button on sketch page
     *
     * @param btnName one of available button names
     * @throws Exception
     * @step. ^I tap (Send|Draw|Open Gallery|Emoji) button on Sketch page$
     */
    @When("^I tap (Send|Draw|Open Gallery|Emoji) button on Sketch page$")
    public void IRememberMySketch(String btnName) throws Exception {
        getSketchPage().tapButton(btnName);
    }
}
