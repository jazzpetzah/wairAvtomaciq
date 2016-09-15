package com.wearezeta.auto.common.driver.facebook_ios_driver;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rest.RESTError;
import org.apache.log4j.Logger;
import org.json.JSONArray;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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

    private FBDriverRESTClient client;
    private Optional<String> sessionId = Optional.empty();

    public FBDriverAPI() {
        this.client = new FBDriverRESTClient(HOST_NAME, PORT_NUMBER);
    }

    private String getSessionId() throws RESTError {
        if (!this.sessionId.isPresent()) {
            this.sessionId = Optional.of(this.client.getSession().getString("sessionId"));
        }
        return this.sessionId.get();
    }

    public FBElement findElementByFBAccessibilityId(String query) throws RESTError {
        return new FBElement(client.findElement(getSessionId(), BY_ACCESSIBILITY_ID_STRING, query));
    }

    public List<FBElement> findElementsByFBAccessibilityId(String query) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), BY_ACCESSIBILITY_ID_STRING, query));
    }

    public FBElement findElementByFBXPath(String query) throws RESTError {
        return new FBElement(client.findElement(getSessionId(), BY_XPATH_STRING, query));
    }

    public List<FBElement> findElementsByFBXPath(String query) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), BY_XPATH_STRING, query));
    }

    public FBElement findElementByFBPredicate(String query) throws RESTError {
        return new FBElement(client.findElement(getSessionId(), BY_PREDICATE_STRING, query));
    }

    public List<FBElement> findElementsByFBPredicate(String query) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), BY_PREDICATE_STRING, query));
    }

    public FBElement findElementByFBClassName(String query) throws RESTError {
        return new FBElement(client.findElement(getSessionId(), BY_CLASS_NAME_STRING, query));
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

    public FBElement findChildElementByFBAccessibilityId(String uuid, String value) throws RESTError {
        return new FBElement(client.findElement(getSessionId(), uuid, BY_ACCESSIBILITY_ID_STRING, value));
    }

    private static List<FBElement> parseFindElementsOutput(JSONArray output) {
        final List<FBElement> result = new ArrayList<>();
        for (int i = 0; i < output.length(); i++) {
            result.add(new FBElement(output.getJSONObject(i).getString("ELEMENT")));
        }
        return result;
    }

    public List<FBElement> findChildElementsByFBAccessibilityId(String uuid, String value) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), uuid, BY_ACCESSIBILITY_ID_STRING, value));
    }

    public FBElement findChildElementByFBClassName(String uuid, String value) throws RESTError {
        return new FBElement(client.findElement(getSessionId(), uuid, BY_CLASS_NAME_STRING, value));
    }

    public List<FBElement> findChildElementsByFBClassName(String uuid, String value) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), uuid, BY_CLASS_NAME_STRING, value));
    }

    public FBElement findChildElementByFBXPath(String uuid, String value) throws RESTError {
        return new FBElement(client.findElement(getSessionId(), uuid, BY_XPATH_STRING, value));
    }

    public List<FBElement> findChildElementsByFBXPath(String uuid, String value) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), uuid, BY_XPATH_STRING, value));
    }

    public FBElement findChildElementByFBPredicate(String uuid, String value) throws RESTError {
        return new FBElement(client.findElement(getSessionId(), uuid, BY_PREDICATE_STRING, value));
    }

    public List<FBElement> findChildElementsByFBPredicate(String uuid, String value) throws RESTError {
        return parseFindElementsOutput(client.findElements(getSessionId(), uuid, BY_PREDICATE_STRING, value));
    }

    public void click(String uuid) throws RESTError {
        client.click(getSessionId(), uuid);
    }

    public void setValue(String uuid, String newValue) throws RESTError {
        client.setValue(getSessionId(), uuid, newValue);
    }

    public void clear(String uuid) throws RESTError {
        client.clear(getSessionId(), uuid);
    }

    public String getTagName(String uuid) throws RESTError {
        return client.getTagName(getSessionId(), uuid);
    }

    public String getAttribute(String uuid, String attrName) throws RESTError {
        return client.getAttribute(getSessionId(), uuid, attrName);
    }
}
