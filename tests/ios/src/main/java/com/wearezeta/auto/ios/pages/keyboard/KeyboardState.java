package com.wearezeta.auto.ios.pages.keyboard;

import java.util.regex.Pattern;

import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import org.openqa.selenium.WebElement;

abstract class KeyboardState {
    public static final String MORE_NUMBERS = "more, numbers";
    public static final String MORE_LETTERS = "more, letters";
    public static final String SHIFT = "shift";
    private WebElement keyboard;

    protected KeyboardState(WebElement keyboard) {
        this.keyboard = keyboard;
    }

    public abstract void switchTo(KeyboardState finalState) throws InterruptedException;

    protected void tapSpecialKey(String keyName) throws InterruptedException {
        if (keyName.equals(SHIFT) || keyName.equals(MORE_LETTERS) || keyName.equals(MORE_NUMBERS)) {
            final FBElement el = (FBElement) keyboard.findElement(FBBy.FBAccessibilityId(keyName));
            el.click();
            Thread.sleep(1500);
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
