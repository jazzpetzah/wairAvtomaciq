package com.wearezeta.auto.ios.tools;

import com.wearezeta.auto.common.driver.ZetaDriver;

public class IOSKeyboard {
	private static final String TAP_KEYBOARD_BUTTON = "target.frontMostApp().keyboard().elements()[\"%s\"].tap();";
 

	private static enum KeyboardState {
		UNKNOWN(0), ALPHA(1), ALPHA_CAPS(2), NUMBER_PUNCTUATION(3);

		private int value;

		private KeyboardState(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private ZetaDriver driver = null;

	public IOSKeyboard(ZetaDriver driver) {

		this.driver = driver;

	}

	private KeyboardState getState() {
		final String emptyElement = "[object UIAElementNil]";
		final String getStateTemplate = "target.frontMostApp().keyboard().keys().firstWithName(\"%s\").toString()";
		if (driver.executeScript(String.format(getStateTemplate, "a"))
				.toString() != emptyElement) {
			return KeyboardState.ALPHA;
		} else if (driver.executeScript(String.format(getStateTemplate, "A"))
				.toString() != emptyElement) {
			return KeyboardState.ALPHA_CAPS;
		} else if (driver.executeScript(String.format(getStateTemplate, "1"))
				.toString() != emptyElement) {
			return KeyboardState.NUMBER_PUNCTUATION;
		}

		return KeyboardState.UNKNOWN;

	}

	public void typeString(String message, Boolean shouldSend) {
		typeString(message);
		if (shouldSend) {
			driver.executeScript(String.format(TAP_KEYBOARD_BUTTON,"Return"));
		}
	}
	
	public void typeString(String message) {

		KeyboardState keyboardState = getState();
		boolean isAllCapital = KeyboardState.ALPHA_CAPS == keyboardState;

		for (int i = 0; i < message.length(); i++) {
			char c = message.charAt(i);
			String messageChar = Character.toString(c);

			if (messageChar.matches("[a-z]")
					&& keyboardState == KeyboardState.ALPHA_CAPS
					&& !isAllCapital) {
				driver.executeScript(String.format(TAP_KEYBOARD_BUTTON, "shift"));
				keyboardState = KeyboardState.ALPHA;
			} else if (messageChar.matches("[A-Z]")
					&& keyboardState == KeyboardState.ALPHA) {
				driver.executeScript(String.format(TAP_KEYBOARD_BUTTON, "shift"));
				keyboardState = KeyboardState.ALPHA_CAPS;
			} else if (messageChar.matches("[A-z]")
					&& keyboardState == KeyboardState.NUMBER_PUNCTUATION) {
				driver.executeScript(String.format(TAP_KEYBOARD_BUTTON,
						"more, letters"));
				keyboardState = KeyboardState.ALPHA;
			} else if (messageChar.matches("[0-9]")
					&& keyboardState != KeyboardState.NUMBER_PUNCTUATION) {
				driver.executeScript(String.format(TAP_KEYBOARD_BUTTON,
						"more, numbers"));
				keyboardState = KeyboardState.NUMBER_PUNCTUATION;
			}

			if (messageChar.matches("[a-z]")
					&& keyboardState == KeyboardState.ALPHA_CAPS) {
				messageChar = messageChar.toUpperCase();
			}
			if (messageChar.equals(" ")) {
				driver.executeScript(String.format(TAP_KEYBOARD_BUTTON, "space"));
			} else if(messageChar.equals("\n")){
				driver.executeScript(String.format(TAP_KEYBOARD_BUTTON,"Return"));
			}
			else if (messageChar.matches("[0-9]")) 
			{
				if (messageChar.equals("0")) {
					messageChar = "9";
				} else {
					messageChar = "" + (char) ((int) messageChar.charAt(0) - 1);
				}
				driver.executeScript(String.format(TAP_KEYBOARD_BUTTON,
						messageChar));
			} else {
				driver.executeScript(String.format(TAP_KEYBOARD_BUTTON,
						messageChar));
			}
		}

	}

}
