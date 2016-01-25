package com.wearezeta.auto.ios.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.DriverUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.WebElement;

public class IOSKeyboard {
    private static final KeyboardState UNKNOWN_STATE = new KeyboardStateUnknown();
    private List<KeyboardState> CACHED_STATES = new ArrayList<>();
    public static By xpathKeyboardLocator = By.xpath("//UIAKeyboard");
    private static final By xpathReturnKeyLocator = By.xpath("//*[@name='Go' or @name='Send']");

    private List<KeyboardState> getStatesList(ZetaIOSDriver driver, WebElement keyboard) {
        if (CACHED_STATES.size() == 0) {
            CACHED_STATES.add(new KeyboardStateAlpha(driver, keyboard));
            CACHED_STATES.add(new KeyboardStateAlphaCaps(driver, keyboard));
            CACHED_STATES.add(new KeyboardStateNumbers(driver, keyboard));
            CACHED_STATES.add(new KeyboardStateSpecial(driver, keyboard));
        }
        return CACHED_STATES;
    }

    private KeyboardState getFinalState(ZetaIOSDriver driver, WebElement keyboard, char c) {
        String messageChar = "" + c;

        for (KeyboardState state : getStatesList(driver, keyboard)) {
            if (messageChar.matches(state.getCharacterSetPattern())) {
                return state;
            }
        }
        return UNKNOWN_STATE;
    }

    protected IOSKeyboard() {

    }

    private static IOSKeyboard instance = null;

    public static synchronized IOSKeyboard getInstance() {
        if (instance == null) {
            instance = new IOSKeyboard();
        }
        return instance;
    }

    public static synchronized void dispose() {
        instance = null;
    }

    private KeyboardState getInitialState(ZetaIOSDriver driver, WebElement keyboard) {
        final String emptyElement = "[object UIAElementNil]";
        final Function<String, String> getState = firstChar ->
                String.format("target.frontMostApp().keyboard().keys().firstWithName(\"%s\").toString()", firstChar);

        for (KeyboardState state : getStatesList(driver, keyboard)) {
            final String firstStateChar = state.getFirstCharacter();
            final String firstCharResponse = driver.executeScript(getState.apply(firstStateChar)).toString();
            if (!firstCharResponse.equals(emptyElement)) {
                return state;
            }
        }

        return UNKNOWN_STATE;
    }

    public void typeString(String message, ZetaIOSDriver driver) throws Exception {
        final WebElement keyboard = DriverUtils.verifyPresence(driver, xpathKeyboardLocator);
        String messageChar;

        KeyboardState currentState = getInitialState(driver, keyboard);
        for (int i = 0; i < message.length(); i++) {
            final char c = message.charAt(i);
            messageChar = Character.toString(c);

            KeyboardState finalState = getFinalState(driver, keyboard, c);
            if (!currentState.equals(finalState)) {
                currentState.switchTo(finalState);
                currentState = finalState;
            }

            By keyLocator;
            switch (messageChar) {
                case "\n":
                    keyLocator = xpathReturnKeyLocator;
                    break;
                case " ":
                    keyLocator = By.name("space");
                    break;
                case "&":
                    keyLocator = By.name("ampersand");
                    break;
                default:
                    keyLocator = By.name(StringEscapeUtils.escapeXml11(messageChar));
                    break;
            }
            keyboard.findElement(keyLocator).click();
            Thread.sleep(DriverUtils.SINGLE_TAP_DURATION);
        }
    }
}
