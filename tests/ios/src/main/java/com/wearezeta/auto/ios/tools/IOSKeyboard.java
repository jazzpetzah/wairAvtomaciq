package com.wearezeta.auto.ios.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class IOSKeyboard {
	private static final String TAP_KEYBOARD_BUTTON = "target.frontMostApp().keyboard().elements()[\"%s\"].tap();";
	private static final KeyboardState UNKNOWN_STATE = new KeyboardStateUnknown();
	private static final int TAP_DELAY = 500;
	private List<KeyboardState> CACHED_STATES = new ArrayList<KeyboardState>();
	private static final String DEFAULT_RETURN_NAME = "Send";
	
	private static final Logger log = ZetaLogger.getLog(IOSKeyboard.class
			.getSimpleName());
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

	private ZetaIOSDriver driver = null;

	protected IOSKeyboard() {
		
	}

	private static IOSKeyboard instance = null;
	public static synchronized IOSKeyboard getInstance() {
		if(instance == null) {
			instance = new IOSKeyboard();
		}
		return instance;
	}
	
	public static synchronized void dispose() {
		instance = null;
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

	public void typeString(String message, ZetaIOSDriver driver) throws InterruptedException {
		this.driver = driver;
		String messageChar = "";

		for (int i = 0; i < message.length(); i++) {
			
			char c = message.charAt(i);
			messageChar = Character.toString(c);
			
			KeyboardState currentState = new KeyboardStateAlpha(driver);
			if (!messageChar.equals("\n")) {
				currentState = getInitialState();
			}
			
			KeyboardState finalState = getFinalState(c);
			if (currentState.getClass() != finalState.getClass()) {
				currentState.switchTo(finalState);
				Thread.sleep(TAP_DELAY);
				currentState = finalState;
			}
			
			switch (messageChar) {
				case " " : 
					messageChar = "space";
					break;
				case "\n" : 
					messageChar = getReturnName();
					break;
				default : 
					if (messageChar.matches("[0-9]")) {
						if (messageChar.equals("0")) {
							messageChar = "9";
						} else {
							messageChar = "" + (char) ((int) messageChar.charAt(0) - 1);
						}
					} 
					break;
			}
			
			try {
				driver.executeScript(String.format(TAP_KEYBOARD_BUTTON,
						messageChar));
			} catch (WebDriverException ex) {
				log.debug(ex.getMessage());
			}
		}
		Thread.sleep(TAP_DELAY);
	}

}
