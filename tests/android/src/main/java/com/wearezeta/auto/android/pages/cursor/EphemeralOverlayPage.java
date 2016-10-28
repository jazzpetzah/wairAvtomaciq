package com.wearezeta.auto.android.pages.cursor;


import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Future;

public class EphemeralOverlayPage extends CursorOverlayPage {

    public EphemeralOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static final String strIdNumberPickerInput = "numberpicker_input";

    private static final By idNumberPickerInput = By.id(strIdNumberPickerInput);

    public enum EphemeralTimeout {
        OFF(0), SECONDS5(1), SECONDS15(2), MINUTE1(3), MINUTE5(4), DAY1(5);

        private int index;

        EphemeralTimeout(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static EphemeralTimeout getByIndex(String value) {
            switch (value.toLowerCase()) {
                case "off":
                    return OFF;
                case "5 seconds":
                    return SECONDS5;
                case "15 seconds":
                    return SECONDS15;
                case "1 minute":
                    return MINUTE1;
                case "5 minutes":
                    return MINUTE5;
                case "1 day":
                    return DAY1;
                default:
                    throw new IllegalArgumentException(String.format("Cannot identify Ephemeral timeout '%s'", value));
            }
        }
    }

    public void setTimeout(String timeoutStr) throws Exception {
        WebElement numberPickerInputElement = DriverUtils.getElementIfDisplayed(getDriver(), idNumberPickerInput)
                .orElseThrow(() -> new IllegalStateException("Ephemeral Extended cursor layout is expected to be visible"));

        final int pickerInputHeight = numberPickerInputElement.getSize().getHeight();
        final int pickerInputWidth = numberPickerInputElement.getSize().getWidth();
        final int pickerInputY = numberPickerInputElement.getLocation().getY();
        final int pickerInputX = numberPickerInputElement.getLocation().getX();

        final int pickerInputCenterX = pickerInputX + pickerInputWidth / 2;
        final int previousPositionY = pickerInputY - pickerInputHeight / 2;
        final int nextPositionY = pickerInputY + pickerInputHeight + pickerInputHeight / 2;

        EphemeralTimeout destinationTimeout = EphemeralTimeout.getByIndex(timeoutStr);
        EphemeralTimeout currentTimeout = EphemeralTimeout.getByIndex(numberPickerInputElement.getText());

        int countOfTaps = destinationTimeout.getIndex() - currentTimeout.getIndex();
        while (countOfTaps != 0) {
            if (countOfTaps > 0) {
                this.getDriver().tap(1, pickerInputCenterX, nextPositionY, DriverUtils.SINGLE_TAP_DURATION);
                countOfTaps--;
            } else {
                this.getDriver().tap(1, pickerInputCenterX, previousPositionY, DriverUtils.SINGLE_TAP_DURATION);
                countOfTaps++;
            }
            Thread.sleep(1000);
        }
    }
}
