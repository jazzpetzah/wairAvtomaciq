package com.wearezeta.auto.android.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class BaseUnconnectedUserOverlay extends BaseUserDetailsOverlay implements ISupportsCommonConnections {
    private static final String idStrUserName = "tv__send_connect__toolbar__title";
    private static final By idAvatar = By.id("iaiv__send_connect");
    private static final By idConnectButton = By.id("zb__send_connect_request__connect_button");

    public BaseUnconnectedUserOverlay(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    public boolean waitUntilCommonUserVisible(String userName) throws Exception {
        return super.waitUntilCommonUserVisible(userName);
    }

    public boolean waitUntilCommonUserInvisible(String userName) throws Exception {
        return super.waitUntilCommonUserInvisible(userName);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch(name.toLowerCase()) {
            case "connect":
                return idConnectButton;
        }
        return super.getButtonLocatorByName(name);
    }

    protected By getAvatarLocator() {
        return idAvatar;
    }

    protected String getUserNameId() {
        return idStrUserName;
    }
}
