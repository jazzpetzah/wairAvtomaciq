package com.wearezeta.auto.ios.pages.details_overlay;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

public interface ICanContainVerificationShield {
    boolean isShieldIconVisible() throws Exception;

    boolean isShieldIconNotVisible() throws Exception;
}
