package com.wearezeta.auto.ios.tools;

import com.wearezeta.auto.common.driver.ZetaDriver;

public class KeyboardStateNumbers extends KeyboardState {

	public KeyboardStateNumbers(ZetaDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void switchTo(KeyboardState finalState) {

		if (finalState instanceof KeyboardStateNumbers) {
			return;
		} else if (finalState instanceof KeyboardStateAlpha) {
			tapKey(MORE_LETTERS);
		} else if (finalState instanceof KeyboardStateAlphaCaps) {
			tapKey(MORE_LETTERS);
			tapKey(SHIFT);
		} else if (finalState instanceof KeyboardStateSpecial) {
			tapKey(MORE_SYMBOLS);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public String getCharacterSetPattern() {
		final String[] SPECIAL_CHARS_IN_NUMBERS_ARR = new String[] { "-", "/",
				":", ";", "(", ")", "$", "&", "@", "\"", ".", ",", "?", "!",
				"'" };
		return String.format("[0-9%s\\s]",
				charArrayToPattern(SPECIAL_CHARS_IN_NUMBERS_ARR));
	}

	@Override
	public String getFirstCharacter() {
		return "1";
	}

}
