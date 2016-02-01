package com.wearezeta.auto.ios.pages.keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.DriverUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.WebElement;

public class IOSKeyboard {
    private static final KeyboardState UNKNOWN_STATE = new KeyboardStateUnknown();
    private static final String xpathStrKeyboardLocator = "//UIAKeyboard";
    public static By xpathKeyboardLocator = By.xpath(xpathStrKeyboardLocator);
    private static final By xpathReturnKeyLocator =
            By.xpath(xpathStrKeyboardLocator + "//*[@name='Go' or @name='Send']");

    private KeyboardState getFinalState(List<KeyboardState> statesList, char c) throws Exception {
        String messageChar = "" + c;

        for (KeyboardState state : statesList) {
            if (messageChar.matches(state.getCharacterSetPattern())) {
                return state;
            }
        }
        return UNKNOWN_STATE;
    }

    private Future<ZetaIOSDriver> lazyDriver;
    private long driverTimeoutMilliseconds;

    public IOSKeyboard(Future<ZetaIOSDriver> lazyDriver, long driverTimeoutMilliseconds) {
        this.lazyDriver = lazyDriver;
        this.driverTimeoutMilliseconds = driverTimeoutMilliseconds;
    }

    private ZetaIOSDriver getDriver() throws Exception {
        return lazyDriver.get(driverTimeoutMilliseconds, TimeUnit.MILLISECONDS);
    }

    private KeyboardState getInitialState(List<KeyboardState> statesList) throws Exception {
        final String emptyElement = "[object UIAElementNil]";
        final Function<String, String> getStateScript = firstChar ->
                String.format("target.frontMostApp().keyboard().keys().firstWithName(\"%s\").toString()",
                        firstChar);

        for (KeyboardState state : statesList) {
            final String firstStateChar = state.getFirstCharacter();
            final String firstCharResponse = getDriver().executeScript(
                    getStateScript.apply(firstStateChar)).toString();
            if (!firstCharResponse.equals(emptyElement)) {
                return state;
            }
        }

        return UNKNOWN_STATE;
    }

    public void typeString(String message) throws Exception {
        final WebElement keyboard = DriverUtils.verifyPresence(getDriver(), xpathKeyboardLocator);

        final KeyboardStateAlpha keyboardStateAlpha = new KeyboardStateAlpha(getDriver(), keyboard);
        final KeyboardStateAlphaCaps keyboardStateAlphaCaps = new KeyboardStateAlphaCaps(getDriver(), keyboard);
        final KeyboardStateNumbers keyboardStateNumbers = new KeyboardStateNumbers(getDriver(), keyboard);
        final KeyboardStateSpecial keyboardStateSpecial = new KeyboardStateSpecial(getDriver(), keyboard);
        final List<KeyboardState> statesList = new ArrayList<>();
        Collections.addAll(statesList,
                keyboardStateAlpha, keyboardStateAlphaCaps, keyboardStateNumbers, keyboardStateSpecial);

        String messageChar;
        KeyboardState currentState = getInitialState(statesList);
        for (int i = 0; i < message.length(); i++) {
            final char c = message.charAt(i);
            messageChar = Character.toString(c);

            KeyboardState finalState = getFinalState(statesList, c);
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

            if (currentState.equals(keyboardStateAlphaCaps)) {
                // Shift state is reset after uppercase character is typed
                currentState = keyboardStateAlpha;
            }
        }
    }
}
