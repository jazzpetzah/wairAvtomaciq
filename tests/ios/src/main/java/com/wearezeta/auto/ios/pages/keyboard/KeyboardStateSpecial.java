package com.wearezeta.auto.ios.pages.keyboard;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.WebElement;

class KeyboardStateSpecial extends KeyboardState {

    public KeyboardStateSpecial(ZetaIOSDriver driver, WebElement keyboard) {
        super(driver, keyboard);
    }

    @Override
    public void switchTo(KeyboardState finalState) throws InterruptedException {
        if (finalState instanceof KeyboardStateSpecial) {
            return;
        } else if (finalState instanceof KeyboardStateAlpha) {
            tapSpecialKey(MORE_LETTERS);
        } else if (finalState instanceof KeyboardStateAlphaCaps) {
            tapSpecialKey(MORE_LETTERS);
            tapSpecialKey(SHIFT);
        } else if (finalState instanceof KeyboardStateNumbers) {
            tapSpecialKey(MORE_NUMBERS);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public String getCharacterSetPattern() {
        final String[] SPECIAL_CHARS_ARR = new String[]{"[", "]", "{", "}",
                "#", "%", "^", "*", "+", "=", "_", "\\", "|", "~", "<", ">",
                "¥", "☻", "£", "€"};
        return String.format("[%s\\s]", charArrayToPattern(SPECIAL_CHARS_ARR));
    }

    @Override
    public String getFirstCharacter() {
        return "[";
    }

}
