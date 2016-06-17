package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

public class CallOutgoingAudioPage extends AbstractCallOutgoingPage {

    private static final By xpathOutgoingAudioCallContainer =
            By.xpath("//*[@id='ttv__calling__header__subtitle' and contains(@value, 'Ringing') " +
                    "and //*[@id='ccbv__button_middle']]");

    public CallOutgoingAudioPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected By getOutgoingCallContainer() {
        return xpathOutgoingAudioCallContainer;
    }
    
}
