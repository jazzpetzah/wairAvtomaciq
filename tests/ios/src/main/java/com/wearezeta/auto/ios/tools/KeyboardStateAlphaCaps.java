package com.wearezeta.auto.ios.tools;

import com.wearezeta.auto.common.driver.ZetaDriver;

public class KeyboardStateAlphaCaps extends KeyboardState {

	public KeyboardStateAlphaCaps(ZetaDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void switchTo(KeyboardState finalState) {

		if (finalState instanceof KeyboardStateAlphaCaps) {
			return;
		} else if (finalState instanceof KeyboardStateAlpha) {
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
		return "[A-Z\\s]";
	}

	@Override
	public String getFirstCharacter() {
		return "A";
	}

}
