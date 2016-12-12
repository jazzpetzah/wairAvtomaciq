package com.wearezeta.auto.android.pages.details_overlay.common;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DeviceListPage extends AndroidPage{
    private static final By idDevice = By.id("ttv__row_otr_device");

    private static final Function<Integer, String> xpathDeviceByIdx = idx -> String
            .format("(//*[@id='ttv__row_otr_device'])[%d]", idx);
    private static final Function<Integer, String> xpathDeviceShieldByIdx = idx -> String
            .format("(//*[@id='iv__row_otr_icon'])[%d]", idx);

    public DeviceListPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public List<String> getParticipantDevices() throws Exception {
        return selectVisibleElements(idDevice).stream().map(WebElement::getText).map(
                (string) -> string.replaceAll("(PHONE)|(DESKTOP)|(\\n)|(\\s)|(ID:)|(ID)", ""
                ).toLowerCase()).collect(Collectors.toList());
    }

    public void tapOnDevice(int deviceIndex) throws Exception {
        getElement(By.xpath(xpathDeviceByIdx.apply(deviceIndex)),
                String.format("The %d th device is not found", deviceIndex)).click();
    }

    public BufferedImage getDeviceShieldScreenshot(int deviceIndex) throws Exception {
        final WebElement deviceShield = getElement(By.xpath(xpathDeviceShieldByIdx.apply(deviceIndex)),
                String.format("The %d th device is not found", deviceIndex));
        return getElementScreenshot(deviceShield).orElseThrow(IllegalStateException::new);
    }
}
