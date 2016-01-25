package com.wearezeta.auto.ios.pages.keyboard;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.WebElement;

class KeyboardStateNumbers extends KeyboardState {

    public KeyboardStateNumbers(ZetaIOSDriver driver, WebElement keyboard) {
        super(driver, keyboard);
    }

    @Override
    public void switchTo(KeyboardState finalState) throws InterruptedException {

        if (finalState instanceof KeyboardStateNumbers) {
            return;
        } else if (finalState instanceof KeyboardStateAlpha) {
            tapSpecialKey(MORE_LETTERS);
        } else if (finalState instanceof KeyboardStateAlphaCaps) {
            tapSpecialKey(MORE_LETTERS);
            tapSpecialKey(SHIFT);
        } else if (finalState instanceof KeyboardStateSpecial) {
            tapSpecialKey(SHIFT);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public String getCharacterSetPattern() {
        final String[] SPECIAL_CHARS_IN_NUMBERS_ARR = new String[]{"-", "/",
                ":", ";", "(", ")", "$", "&", "@", "\"", ".", ",", "?", "!",
                "'"};
        return String.format("[0-9%s\\s]",
                charArrayToPattern(SPECIAL_CHARS_IN_NUMBERS_ARR));
    }

    @Override
    public String getFirstCharacter() {
        return "1";
    }

}
