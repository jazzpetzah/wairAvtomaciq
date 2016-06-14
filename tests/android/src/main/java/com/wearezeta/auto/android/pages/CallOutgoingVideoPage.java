package com.wearezeta.auto.android.pages;


import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class CallOutgoingVideoPage extends AbstractCallOutgoingPage {

    private static final By xpathOutgoingVideoCallContainer =
            By.xpath("//*[@id='ttv__calling__header__subtitle' and contains(@value, 'Ringing') " +
                    "and //*[@id='ccbv__button_right']]");

    public CallOutgoingVideoPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected By getOutgoingCallContainer() {
        return xpathOutgoingVideoCallContainer;
    }
}
