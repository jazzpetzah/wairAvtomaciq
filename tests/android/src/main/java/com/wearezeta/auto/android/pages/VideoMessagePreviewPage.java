package com.wearezeta.auto.android.pages;


import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class VideoMessagePreviewPage extends AndroidPage{

    private final static String strIdToolbar = "t_video_message_preview_toolbar";

    private final static By idVideoPreviewContainer = By.id("fl__video_container");
    private final static By idSendVideoBtn = By.id("action_send");
    private final static By idToolbar = By.id(strIdToolbar);
    private final static By xpathBackButton = By.xpath(String.format("//*[@id='%s']//*[1]", strIdToolbar));


    public VideoMessagePreviewPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVideoPreviewVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idVideoPreviewContainer);
    }

    public boolean isToolbarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idToolbar);
    }

    public void tapOnSendVideoButton() throws Exception {
        getElement(idSendVideoBtn).click();
    }

    public void tapOnCancelVideoButton()  throws Exception {
        getElement(xpathBackButton).click();
    }
}
