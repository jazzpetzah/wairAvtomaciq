package com.wearezeta.auto.ios.tools;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.WebElement;

public class KeyboardStateAlphaCaps extends KeyboardState {

	public KeyboardStateAlphaCaps(ZetaIOSDriver driver, WebElement keyboard) {
        super(driver, keyboard);
    }

	@Override
	public void switchTo(KeyboardState finalState) throws InterruptedException {
		if (finalState instanceof KeyboardStateAlphaCaps) {
			return;
		} else if (finalState instanceof KeyboardStateAlpha) {
			tapSpecialKey(SHIFT);
		} else if (finalState instanceof KeyboardStateNumbers) {
			tapSpecialKey(MORE_NUMBERS);
		} else if (finalState instanceof KeyboardStateSpecial) {
			tapSpecialKey(MORE_NUMBERS);
			tapSpecialKey(SHIFT);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public String getCharacterSetPattern() {
		return "[A-Z\\s]";
	}

	@Override
	public String getFirstCharacter() {
		return "A";
	}

}
