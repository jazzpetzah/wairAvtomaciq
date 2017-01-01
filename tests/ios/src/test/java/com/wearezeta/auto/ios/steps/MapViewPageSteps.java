package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.MapViewPage;
import cucumber.api.java.en.When;

public class MapViewPageSteps {
    private MapViewPage getMapViewPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(MapViewPage.class);
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
