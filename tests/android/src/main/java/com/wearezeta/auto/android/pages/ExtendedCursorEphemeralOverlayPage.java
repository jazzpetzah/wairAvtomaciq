package com.wearezeta.auto.android.pages;


import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Future;
import java.util.function.Function;

public class ExtendedCursorEphemeralOverlayPage extends ExtendedCursorOverlayPage {

    public ExtendedCursorEphemeralOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static final String strIdNumberPickerInput = "numberpicker_input";

    private static final By idNumberPickerInput = By.id(strIdNumberPickerInput);

    private static final Function<String, String> xpathStrNumberPickerInputByValue =
            value -> String.format("//*[@id='%s' and @value='%s']", strIdNumberPickerInput, value);

    public enum EphemeralTimeout {
        OFF(0), SECONDS5(1), SECONDS15(2), MINUTE1(3);

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
                default:
                    throw new IllegalArgumentException(String.format("Cannot identify Ephemeral timeout '%s'", value));
            }
        }

    }

    public void setTimeout(String timeoutStr) throws Exception {
        WebElement numberPickerInputElement = DriverUtils.getElementIfDisplayed(getDriver(), idNumberPickerInput)
                .orElseThrow(() -> new IllegalStateException("Ephemeral Extended cursor layout is expected to be visible"));

        int height = numberPickerInputElement.getSize().getHeight();
        int y = numberPickerInputElement.getLocation().getY();
        int previousPositionY = y - height / 2;
        int nextPositionY = y + height + height / 2;

        EphemeralTimeout destinationTimeout = EphemeralTimeout.getByIndex(timeoutStr);
        EphemeralTimeout currentTimeout = EphemeralTimeout.getByIndex(numberPickerInputElement.getText());

        int countOfTaps = destinationTimeout.getIndex() - currentTimeout.getIndex();
        while (countOfTaps != 0) {
            if (countOfTaps > 0) {
                this.getDriver().tap(1, 50, nextPositionY, DriverUtils.SINGLE_TAP_DURATION);
                countOfTaps--;
            } else {
                this.getDriver().tap(1, 50, previousPositionY, DriverUtils.SINGLE_TAP_DURATION);
                countOfTaps++;
            }
            Thread.sleep(1000);
        }
    }
}
