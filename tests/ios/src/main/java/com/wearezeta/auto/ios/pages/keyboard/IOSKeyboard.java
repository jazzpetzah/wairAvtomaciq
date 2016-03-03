package com.wearezeta.auto.ios.pages.keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.DriverUtils;
import io.appium.java_client.MobileBy;
import org.apache.commons.lang3.StringEscapeUtils;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.WebElement;

public class IOSKeyboard extends BasePage {
    private static final KeyboardState UNKNOWN_STATE = new KeyboardStateUnknown();
    private static final String xpathStrKeyboard = "//UIAKeyboard";
    private static By classNameKeyboard = By.className("UIAKeyboard");
    private static final By xpathCommitKey = By.xpath(xpathStrKeyboard +
                    "//*[@name='Go' or @name='Send' or @name='Done' or @name='return' or @name='Return']");

    private static final By nameSpaceButton = MobileBy.AccessibilityId("space");

    private static final By nameHideKeyboardButton = MobileBy.AccessibilityId("Hide keyboard");

    private static final By nameKeyboardDeleteButton = MobileBy.AccessibilityId("delete");

    private static final int DEFAULT_VISIBILITY_TIMEOUT = 5; //seconds

    private KeyboardState getFinalState(List<KeyboardState> statesList, char c) throws Exception {
        String messageChar = "" + c;

        for (KeyboardState state : statesList) {
            if (messageChar.matches(state.getCharacterSetPattern())) {
                return state;
            }
        }
        return UNKNOWN_STATE;
    }

    public IOSKeyboard(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected ZetaIOSDriver getDriver() throws Exception {
        return (ZetaIOSDriver) super.getDriver();
    }

    public boolean isVisible(int timeoutSeconds) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), classNameKeyboard, timeoutSeconds);
    }

    public boolean isVisible() throws Exception {
        return isVisible(DEFAULT_VISIBILITY_TIMEOUT);
    }

    public void pressSpaceButton() throws Exception {
        getElement(nameSpaceButton).click();
    }

    public void pressHideButton() throws Exception {
        getElement(nameHideKeyboardButton).click();
    }

    public void pressDeleteButton() throws Exception {
        getElement(nameKeyboardDeleteButton).click();
    }

    public void pressCommitButton() throws Exception {
        getElement(xpathCommitKey, "Keyboard commit key is not visible", 15).click();
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
        final WebElement keyboard = DriverUtils.verifyPresence(getDriver(), classNameKeyboard);

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
                    keyLocator = xpathCommitKey;
                    break;
                case " ":
                    keyLocator = MobileBy.AccessibilityId("space");
                    break;
                case "&":
                    keyLocator = MobileBy.AccessibilityId("ampersand");
                    break;
                default:
                    keyLocator = MobileBy.AccessibilityId(StringEscapeUtils.escapeXml11(messageChar));
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
