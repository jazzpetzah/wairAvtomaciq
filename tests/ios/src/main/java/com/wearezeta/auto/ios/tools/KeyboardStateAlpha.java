package com.wearezeta.auto.ios.tools;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class KeyboardStateAlpha extends KeyboardState {

	public KeyboardStateAlpha(ZetaIOSDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void switchTo(KeyboardState finalState) {

		if (finalState instanceof KeyboardStateAlpha) {
			return;
		} else if (finalState instanceof KeyboardStateAlphaCaps) {
			tapKey(SHIFT);
		} else if (finalState instanceof KeyboardStateNumbers) {
			tapKey(MORE_NUMBERS);
		} else if (finalState instanceof KeyboardStateSpecial) {
			tapKey(MORE_NUMBERS);
			tapKey(MORE_SYMBOLS);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public String getCharacterSetPattern() {
		return "[a-z\\s]";
	}

	@Override
	public String getFirstCharacter() {
		return "a";
	}

}
