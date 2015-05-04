package com.wearezeta.auto.ios.tools;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public abstract class KeyboardState {
	private static final String TAP_KEYBOARD_BUTTON = "target.frontMostApp().keyboard().elements()[\"%s\"].tap();";
	public static final String MORE_NUMBERS = "more, numbers";
	public static final String MORE_LETTERS = "more, letters";
	public static final String MORE_SYMBOLS = "more, symbols";
	public static final String SHIFT = "shift";
	private ZetaIOSDriver driver = null;
	
	private static final Logger log = ZetaLogger.getLog(KeyboardState.class
			.getSimpleName());

	protected KeyboardState(ZetaIOSDriver driver) {
		this.driver = driver;
	}
	
	public abstract void switchTo(KeyboardState finalState);
	
	protected void tapKey(String name){
		if (name.equals(MORE_SYMBOLS) || name.equals(SHIFT) || name.equals(MORE_LETTERS) || name.equals(MORE_NUMBERS)) {
			WebElement key = this.driver.findElementByName(name);
			DriverUtils.mobileTapByCoordinates(driver, key);
		} else {
			try {
				driver.executeScript(String.format(TAP_KEYBOARD_BUTTON, name));
			}catch (WebDriverException ex) {
				log.debug(ex.getMessage());
			}
		}
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
