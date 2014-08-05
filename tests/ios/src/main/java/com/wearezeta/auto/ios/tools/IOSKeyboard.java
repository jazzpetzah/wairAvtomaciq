package com.wearezeta.auto.ios.tools;

import java.util.ArrayList;
import java.util.List;

import com.wearezeta.auto.common.driver.ZetaDriver;

public class IOSKeyboard {
	private static final String TAP_KEYBOARD_BUTTON = "target.frontMostApp().keyboard().elements()[\"%s\"].tap();";
	private static final KeyboardState UNKNOWN_STATE = new KeyboardStateUnknown();
	private static final int TAP_DELAY = 10;
	private List<KeyboardState> CACHED_STATES = new ArrayList<KeyboardState>();
	private static final String DEFAULT_RETURN_NAME = "Return";
	
	private String returnName = DEFAULT_RETURN_NAME;

	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	private List<KeyboardState> getStatesList() {
		if (CACHED_STATES.size() == 0) {
			CACHED_STATES.add(new KeyboardStateAlpha(driver));
			CACHED_STATES.add(new KeyboardStateAlphaCaps(driver));
			CACHED_STATES.add(new KeyboardStateNumbers(driver));
			CACHED_STATES.add(new KeyboardStateSpecial(driver));
		}
		return CACHED_STATES;
	}

	private KeyboardState getFinalState(char c) {
		String messageChar = "" + c;

		for (KeyboardState state : getStatesList()) {
			if (messageChar.matches(state.getCharacterSetPattern())) {
				return state;
			}
		}
		return UNKNOWN_STATE;
	}

	private ZetaDriver driver = null;

	public IOSKeyboard(ZetaDriver driver) {
		this.driver = driver;
	}

	private KeyboardState getInitialState() {
		final String emptyElement = "[object UIAElementNil]";
		final String getStateTemplate = "target.frontMostApp().keyboard().keys().firstWithName(\"%s\").toString()";
		for (KeyboardState state : getStatesList()) {
			final String firstStateChar = state.getFirstCharacter();
			final String firstCharResponse = driver.executeScript(
					String.format(getStateTemplate, firstStateChar)).toString();
			if (!firstCharResponse.equals(emptyElement)) {
				return state;
			}
		}

		return UNKNOWN_STATE;
	}

	public void typeString(String message) throws InterruptedException {
		KeyboardState currentState = new KeyboardStateAlpha(driver);
		for (int i = 0; i < message.length(); i++) {
			char c = message.charAt(i);
			String messageChar = Character.toString(c);

			KeyboardState finalState = getFinalState(c);
			if (currentState != finalState) {
				if ((finalState instanceof KeyboardStateAlpha && currentState instanceof KeyboardStateAlphaCaps)
						|| (finalState instanceof KeyboardStateAlphaCaps && currentState instanceof KeyboardStateAlpha)) {
					currentState = getInitialState();
				}
				currentState.switchTo(finalState);
				Thread.sleep(TAP_DELAY);
				currentState = finalState;
			}

			if (messageChar.equals(" ")) {
				driver.executeScript(String
						.format(TAP_KEYBOARD_BUTTON, "space"));
			}else if (messageChar.equals("\n")) {
				driver.executeScript(String
							.format(TAP_KEYBOARD_BUTTON, getReturnName()));
			} else if (messageChar.matches("[0-9]")) {
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

			Thread.sleep(TAP_DELAY);
		}

	}

}
