package com.wearezeta.auto.common.driver.facebook_ios_driver;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rest.RESTError;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class FBDriverAPI {
    private static final Logger log = ZetaLogger.getLog(FBDriverAPI.class.getSimpleName());

    private static final String HOST_NAME = "localhost";
    private static final String PORT_NUMBER = "8100";

    private static final String BY_PREDICATE_STRING = "predicate string";
    private static final String BY_CLASS_NAME_STRING = "class name";
    private static final String BY_XPATH_STRING = "xpath";
    private static final String BY_ACCESSIBILITY_ID_STRING = "accessibility id";

    private static final String OBJC_YES = "true";

    private static final FBDriverRESTClient client = new FBDriverRESTClient(HOST_NAME, PORT_NUMBER);
    private Optional<String> sessionId = Optional.empty();

    public FBDriverAPI() {
    }

    private String getSessionId() throws RESTError {
        if (!this.sessionId.isPresent()) {
            if (!this.isAlive()) {
                throw new IllegalStateException(String.format("Facebook Driver service listener at %s:%s is dead",
                        HOST_NAME, PORT_NUMBER));
            }
            this.sessionId = Optional.of(client.getSession().getString("sessionId"));
        }
        return this.sessionId.get();
    }

    public Optional<FBElement> findElementByFBAccessibilityId(String query) throws RESTError {
        return parseFindElementOutput(client.findElement(getSessionId(), BY_ACCESSIBILITY_ID_STRING, query));
    }

    public List<FBElement> findElementsByFBAccessibilityId(String query) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), BY_ACCESSIBILITY_ID_STRING, query));
    }

    public Optional<FBElement> findElementByFBXPath(String query) throws RESTError {
        return parseFindElementOutput(client.findElement(getSessionId(), BY_XPATH_STRING, query));
    }

    public List<FBElement> findElementsByFBXPath(String query) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), BY_XPATH_STRING, query));
    }

    public Optional<FBElement> findElementByFBPredicate(String query) throws RESTError {
        return parseFindElementOutput(client.findElement(getSessionId(), BY_PREDICATE_STRING, query));
    }

    public List<FBElement> findElementsByFBPredicate(String query) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), BY_PREDICATE_STRING, query));
    }

    public Optional<FBElement> findElementByFBClassName(String query) throws RESTError {
        return parseFindElementOutput(client.findElement(getSessionId(), BY_CLASS_NAME_STRING, query));
    }

    public List<FBElement> findElementsByFBClassName(String query) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), BY_CLASS_NAME_STRING, query));
    }

    public boolean isAlive() {
        try {
            final URL siteURL = new URL(String.format("%s%s:%s/", FBDriverRESTClient.URL_PROTOCOL,
                    HOST_NAME, PORT_NUMBER));
            final HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            final int responseCode = connection.getResponseCode();
            log.debug(String.format("Response code from %s: %s", siteURL.toString(), responseCode));
            return (responseCode == 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<FBElement> findChildElementByFBAccessibilityId(String uuid, String value) throws RESTError {
        return parseFindElementOutput(client.findElement(getSessionId(), uuid, BY_ACCESSIBILITY_ID_STRING, value));
    }

    private Optional<FBElement> parseFindElementOutput(JSONObject output) {
        String value;
        try {
            value = parseResponseWithStatus(output);
        } catch (StatusNotZeroError e) {
            return Optional.empty();
        }
        return Optional.of(new FBElement(new JSONObject(value).getString("ELEMENT"), this));
    }

    private List<FBElement> parseFindElementsOutput(JSONObject output) {
        String value;
        try {
            value = parseResponseWithStatus(output);
        } catch (StatusNotZeroError e) {
            return Collections.emptyList();
        }
        final JSONArray elementsList = new JSONArray(value);
        final List<FBElement> result = new ArrayList<>();
        for (int i = 0; i < elementsList.length(); i++) {
            result.add(new FBElement(elementsList.getJSONObject(i).getString("ELEMENT"), this));
        }
        return result;
    }

    public List<FBElement> findChildElementsByFBAccessibilityId(String uuid, String value) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), uuid, BY_ACCESSIBILITY_ID_STRING, value));
    }

    public Optional<FBElement> findChildElementByFBClassName(String uuid, String value) throws RESTError {
        return parseFindElementOutput(client.findElement(getSessionId(), uuid, BY_CLASS_NAME_STRING, value));
    }

    public List<FBElement> findChildElementsByFBClassName(String uuid, String value) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), uuid, BY_CLASS_NAME_STRING, value));
    }

    public Optional<FBElement> findChildElementByFBXPath(String uuid, String value) throws RESTError {
        return parseFindElementOutput(client.findElement(getSessionId(), uuid, BY_XPATH_STRING, value));
    }

    public List<FBElement> findChildElementsByFBXPath(String uuid, String value) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), uuid, BY_XPATH_STRING, value));
    }

    public Optional<FBElement> findChildElementByFBPredicate(String uuid, String value) throws RESTError {
        return parseFindElementOutput(client.findElement(getSessionId(), uuid, BY_PREDICATE_STRING, value));
    }

    public List<FBElement> findChildElementsByFBPredicate(String uuid, String value) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), uuid, BY_PREDICATE_STRING, value));
    }

    public void click(String uuid) throws RESTError, StatusNotZeroError {
        parseResponseWithStatus(client.click(getSessionId(), uuid));
    }

    public void setValue(String uuid, CharSequence... charSequences) throws RESTError, StatusNotZeroError {
        parseResponseWithStatus(client.setValue(getSessionId(), uuid, charSequences));
    }

    public void clear(String uuid) throws RESTError, StatusNotZeroError {
        parseResponseWithStatus(client.clear(getSessionId(), uuid));
    }

    public void deactivateApp(double durationSeconds) throws RESTError, StatusNotZeroError {
        parseResponseWithStatus(client.deactivateApp(getSessionId(), durationSeconds));
    }

    public static class StatusNotZeroError extends Exception {
        public StatusNotZeroError(String message) {
            super(message);
        }
    }

    private static String parseResponseWithStatus(JSONObject response) throws StatusNotZeroError {
        if (response.getInt("status") == 0) {
            return response.get("value").toString();
        } else {
            throw new StatusNotZeroError(response.get("value").toString());
        }
    }

    public String getTagName(String uuid) throws RESTError, StatusNotZeroError {
        return parseResponseWithStatus(client.getTagName(getSessionId(), uuid));
    }

    public String getAttribute(String uuid, String attrName) throws RESTError, StatusNotZeroError {
        return parseResponseWithStatus(client.getAttribute(getSessionId(), uuid, attrName));
    }

    public boolean isEnabled(String uuid) throws RESTError, StatusNotZeroError {
        return parseResponseWithStatus(client.isEnabled(getSessionId(), uuid)).equals(OBJC_YES);
    }

    public String getText(String uuid) throws RESTError, StatusNotZeroError {
        return parseResponseWithStatus(client.getText(getSessionId(), uuid));
    }

    public boolean getIsDisplayed(String uuid) throws RESTError, StatusNotZeroError {
        return parseResponseWithStatus(client.getIsDisplayed(getSessionId(), uuid)).equals(OBJC_YES);
    }

    public String getRect(String uuid) throws RESTError, StatusNotZeroError {
        return parseResponseWithStatus(client.getRect(getSessionId(), uuid));
    }

    public void doubleTap(String uuid) throws RESTError, StatusNotZeroError {
        parseResponseWithStatus(client.doubleTap(getSessionId(), uuid));
    }

    public void touchAndHold(String uuid, double durationSeconds) throws RESTError, StatusNotZeroError {
        parseResponseWithStatus(client.touchAndHold(getSessionId(), uuid, durationSeconds));
    }

    public void scroll(String uuid, Optional<String> toChildNamed, Optional<ScrollingDirection> direction,
                       Optional<String> predicateString, Optional<Boolean> toVisible)
            throws RESTError, StatusNotZeroError {
        Optional<String> strDirection = Optional.empty();
        if (direction.isPresent()) {
            strDirection = Optional.of(direction.get().toString().toLowerCase());
        }
        parseResponseWithStatus(
                client.scroll(getSessionId(), uuid, toChildNamed, strDirection, predicateString, toVisible));
    }

    public void tap(String uuid, double x, double y) throws RESTError, StatusNotZeroError {
        parseResponseWithStatus(client.tap(getSessionId(), uuid, x, y));
    }

    public void sendKeys(CharSequence... charSequences) throws RESTError, StatusNotZeroError {
        parseResponseWithStatus(client.sendKeys(getSessionId(), charSequences));
    }

    public boolean isAccessible(String uuid) throws RESTError, StatusNotZeroError {
        return parseResponseWithStatus(client.getIsAccessible(getSessionId(), uuid)).equals(OBJC_YES);
    }

    public String getWindowSize(String uuid) throws RESTError, StatusNotZeroError {
        return parseResponseWithStatus(client.getWindowSize(getSessionId(), uuid));
    }

    public enum ScrollingDirection {
        UP, DOWN, LEFT, RIGHT
    }

    public void switchToHomescreen() throws RESTError, StatusNotZeroError {
        parseResponseWithStatus(client.switchToHomescreen(getSessionId()));
    }

    public String getAlertText() throws RESTError, StatusNotZeroError {
        return parseResponseWithStatus(client.getAlertText(getSessionId()));
    }

    public void acceptAlert() throws RESTError, StatusNotZeroError {
        parseResponseWithStatus(client.acceptAlert(getSessionId()));
    }

    public void dismissAlert() throws RESTError, StatusNotZeroError {
        parseResponseWithStatus(client.dismissAlert(getSessionId()));
    }

    public void dragFromToForDuration(String uuid, FBDragArguments FBDragArguments) throws RESTError, StatusNotZeroError {
        parseResponseWithStatus(client.dragFromToForDuration(getSessionId(), uuid, FBDragArguments));
    }

    public String getScreenshot() throws RESTError, StatusNotZeroError {
        return parseResponseWithStatus(client.getScreenshot(getSessionId()));
    }

    public void setRotation(FBDeviceRotation o) throws RESTError, StatusNotZeroError {
        parseResponseWithStatus(client.setRotation(getSessionId(), o));
    }

    public FBDeviceRotation getRotation() throws RESTError, StatusNotZeroError {
        final JSONObject r = new JSONObject(parseResponseWithStatus(client.getRotation(getSessionId())));
        return FBDeviceRotation.fromAngle(r.getInt("z"));
    }

    public void setOrientation(FBDeviceOrientation o) throws RESTError, StatusNotZeroError {
        parseResponseWithStatus(client.setOrientation(getSessionId(), o));
    }

    public FBDeviceOrientation getOrientation() throws RESTError, StatusNotZeroError {
        final String r = parseResponseWithStatus(client.getOrientation(getSessionId()));
        return FBDeviceOrientation.valueOf(r);
    }
}
