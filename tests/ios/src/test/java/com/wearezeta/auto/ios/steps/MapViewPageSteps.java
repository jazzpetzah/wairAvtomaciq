package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.MapViewPage;
import cucumber.api.java.en.When;

public class MapViewPageSteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private MapViewPage getMapViewPage() throws Exception {
        return pagesCollection.getPage(MapViewPage.class);
    }

    /**
     * Tap on Send location button from map view
     *
     * @throws Exception
     * @step. ^I tap Send location button from map view$
     */
    @When("^I tap Send location button from map view$")
    public void ITapSendLocationButtonFromMapView() throws Exception {
        getMapViewPage().clickSendLocationButton();
    }
}
