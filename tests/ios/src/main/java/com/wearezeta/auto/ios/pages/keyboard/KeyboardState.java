package com.wearezeta.auto.ios.pages.keyboard;

import java.util.regex.Pattern;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

abstract class KeyboardState {
    public static final String MORE_NUMBERS = "more, numbers";
    public static final String MORE_LETTERS = "more, letters";
    public static final String SHIFT = "shift";
    private WebElement keyboard;
    private ZetaIOSDriver driver;

    protected KeyboardState(ZetaIOSDriver driver, WebElement keyboard) {
        this.driver = driver;
        this.keyboard = keyboard;
    }

    public abstract void switchTo(KeyboardState finalState) throws InterruptedException;

    protected void tapSpecialKey(String keyName) throws InterruptedException {
        if (keyName.equals(SHIFT) || keyName.equals(MORE_LETTERS) || keyName.equals(MORE_NUMBERS)) {
            final WebElement el = keyboard.findElement(By.name(keyName));
            DriverUtils.tapByCoordinates(driver, el);
            Thread.sleep(1000);
        } else {
            throw new IllegalArgumentException(String.format("The key '%s' is not recognized as special", keyName));
        }
    }

    public abstract String getCharacterSetPattern();

    public abstract String getFirstCharacter();

    protected static String charArrayToPattern(String[] arr) {
        StringBuilder result = new StringBuilder();
        for (String chr : arr) {
            result.append(Pattern.quote(chr));
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other != null && this.getClass().equals(other.getClass());
    }

}
