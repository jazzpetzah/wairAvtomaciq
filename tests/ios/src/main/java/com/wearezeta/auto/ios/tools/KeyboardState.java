package com.wearezeta.auto.ios.tools;

import java.util.regex.Pattern;

import com.wearezeta.auto.common.driver.ZetaDriver;

public abstract class KeyboardState {
	private static final String TAP_KEYBOARD_BUTTON = "target.frontMostApp().keyboard().elements()[\"%s\"].tap();";
	public static final String MORE_NUMBERS = "more, numbers";
	public static final String MORE_LETTERS = "more, letters";
	public static final String MORE_SYMBOLS = "more, symbols";
	public static final String SHIFT = "shift";
	private ZetaDriver driver = null;

	protected KeyboardState(ZetaDriver driver) {

		this.driver = driver;
	}
	
	public abstract void switchTo(KeyboardState finalState);
	
	protected void tapKey(String name){
		driver.executeScript(String.format(TAP_KEYBOARD_BUTTON, name));
	}
	
	public abstract String getCharacterSetPattern();
	
	public abstract String getFirstCharacter();
	
	protected static String charArrayToPattern(String[] arr) {
		StringBuilder result = new StringBuilder();
		for (String chr: arr) {
			result.append(Pattern.quote(chr)); 
		}
		return result.toString();	
	}

}
