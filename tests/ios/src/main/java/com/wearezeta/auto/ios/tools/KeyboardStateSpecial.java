package com.wearezeta.auto.ios.tools;

import com.wearezeta.auto.common.driver.ZetaDriver;

public class KeyboardStateSpecial extends KeyboardState {

	public KeyboardStateSpecial(ZetaDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void switchTo(KeyboardState finalState) {
		if (finalState instanceof KeyboardStateSpecial) {
			return;
		} else if (finalState instanceof KeyboardStateAlpha) {
			tapKey(MORE_LETTERS);
		} else if (finalState instanceof KeyboardStateAlphaCaps) {
			tapKey(MORE_LETTERS);
			tapKey(SHIFT);
		} else if (finalState instanceof KeyboardStateNumbers) {
			tapKey(MORE_NUMBERS);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public String getCharacterSetPattern() {
		final String[] SPECIAL_CHARS_ARR = new String[] { "[", "]", "{", "}",
				"#", "%", "^", "*", "+", "=", "_", "\\", "|", "~", "<", ">",
				"¥", "☻", "£", "€" };
		return String.format("[%s\\s]", charArrayToPattern(SPECIAL_CHARS_ARR));
	}

	@Override
	public String getFirstCharacter() {
		return "[";
	}

}
