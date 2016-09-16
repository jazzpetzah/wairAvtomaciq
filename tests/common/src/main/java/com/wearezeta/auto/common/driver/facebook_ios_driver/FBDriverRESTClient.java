package com.wearezeta.auto.common.driver.facebook_ios_driver;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rest.CommonRESTHandlers;
import com.wearezeta.auto.common.rest.RESTError;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Optional;

/**
 * https://github.com/facebook/WebDriverAgent/tree/ab2e0d52e8b4df25423d37f2582bbb9a7903e065/WebDriverAgentLib/Commands
 */
final class FBDriverRESTClient {

    private static final Logger log = ZetaLogger.getLog(FBDriverRESTClient.class.getSimpleName());

    public static final String URL_PROTOCOL = "http://";

    private static final String EMPTY_JSON_BODY = new JSONObject().toString();

    private String hostname;
    private String port;

    public FBDriverRESTClient(String ip, String port) {
        this.hostname = ip;
        this.port = port;
    }

    public String getApiRoot() {
        System.out.print(String.format("%s%s:%s/", URL_PROTOCOL, this.hostname, this.port));
        return String.format("%s%s:%s", URL_PROTOCOL, this.hostname, this.port);
    }

    private static final CommonRESTHandlers restHandlers =
            new CommonRESTHandlers(FBDriverRESTClient::verifyRequestResult);

    private static void verifyRequestResult(int currentResponseCode, int[] acceptableResponseCodes,
                                            String message) throws FBDriverAPIException {
        if (!ArrayUtils.contains(acceptableResponseCodes, currentResponseCode)) {
            throw new FBDriverAPIException(String.format(
                    "Facebook WebDriver service API request failed. " +
                            "Request return code is: %d. Expected codes are: %s. Message from service is: %s",
                    currentResponseCode, Arrays.toString(acceptableResponseCodes), message), currentResponseCode);
        }
    }

    private Builder buildDefaultRequest() {
        final String dstUrl = getApiRoot() + "/";
        log.debug(String.format("Request to %s...", dstUrl));
        final Client client = ClientBuilder.newClient();
        return client.target(dstUrl).request()
                .header("Content-type", MediaType.APPLICATION_JSON);
    }

    private Builder buildDefaultRequest(String restAction, String sessionId) {
        final String dstUrl = String.format("%s/session/%s/%s", getApiRoot(), sessionId, restAction);
        log.debug(String.format("Request to %s...", dstUrl));
        final Client client = ClientBuilder.newClient();
        return client.target(dstUrl).request()
                .header("Content-type", MediaType.APPLICATION_JSON);
    }

    public JSONObject findElement(String sessionId, String using, String value) throws RESTError {
        final Builder webResource = buildDefaultRequest("element", sessionId);
        final JSONObject body = new JSONObject();
        body.put("using", using);
        body.put("value", value);
        return new JSONObject(restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    public JSONArray findElements(String sessionId, String using, String value) throws RESTError {
        final Builder webResource = buildDefaultRequest("elements", sessionId);
        final JSONObject body = new JSONObject();
        body.put("using", using);
        body.put("value", value);
        return new JSONArray(restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    public JSONObject findElement(String sessionId, String parentUUID, String using, String value) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/element", parentUUID), sessionId);
        final JSONObject body = new JSONObject();
        body.put("using", using);
        body.put("value", value);
        return new JSONObject(restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    public JSONArray findElements(String sessionId, String parentUUID, String using, String value) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/elements", parentUUID), sessionId);
        final JSONObject body = new JSONObject();
        body.put("using", using);
        body.put("value", value);
        return new JSONArray(restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    public void click(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/click", uuid), sessionId);
        restHandlers.httpPost(webResource, EMPTY_JSON_BODY, new int[]{HttpStatus.SC_OK});
    }

    public void setValue(String sessionId, String uuid, String newValue) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/value", uuid), sessionId);
        final JSONObject body = new JSONObject();
        body.put("value", newValue);
        restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK});
    }

    public void clear(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/clear", uuid), sessionId);
        restHandlers.httpPost(webResource, EMPTY_JSON_BODY, new int[]{HttpStatus.SC_OK});
    }

    public JSONObject getTagName(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/name", uuid), sessionId);
        return new JSONObject(restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    public JSONObject getAttribute(String sessionId, String uuid, String attrName) throws RESTError {
        final Builder webResource =
                buildDefaultRequest(String.format("element/%s/attribute/%s", uuid, attrName), sessionId);
        return new JSONObject(restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    public JSONObject getSession() throws RESTError {
        final Builder webResource = buildDefaultRequest();
        return new JSONObject(restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    public JSONObject isEnabled(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/enabled", uuid), sessionId);
        return new JSONObject(restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    public JSONObject getText(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/text", uuid), sessionId);
        return new JSONObject(restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    public JSONObject getIsDisplayed(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/displayed", uuid), sessionId);
        return new JSONObject(restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    public JSONObject getRect(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/rect", uuid), sessionId);
        return new JSONObject(restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    public void doubleTap(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("uiaElement/%s/doubleTap", uuid), sessionId);
        restHandlers.httpPost(webResource, EMPTY_JSON_BODY, new int[]{HttpStatus.SC_OK});
    }

    public void touchAndHold(String sessionId, String uuid, double durationSeconds) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("uiaElement/%s/touchAndHold", uuid), sessionId);
        final JSONObject body = new JSONObject();
        body.put("duration", durationSeconds);
        restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK});
    }

    public void scroll(String sessionId, String uuid, Optional<String> toChildNamed,
                       Optional<String> direction, Optional<String> predicateString,
                       Optional<Boolean> toVisible) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("uiaElement/%s/scroll", uuid), sessionId);
        final JSONObject body = new JSONObject();
        if (toChildNamed.isPresent()) {
            body.put("name", toChildNamed.get());
        }
        if (direction.isPresent()) {
            body.put("direction", direction.get());
        }
        if (predicateString.isPresent()) {
            body.put("predicateString", predicateString.get());
        }
        if (toVisible.isPresent()) {
            body.put("toVisible", toVisible.get());
        }
        restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK});
    }

    public void tap(String sessionId, String uuid, double x, double y) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("tap/%s", uuid), sessionId);
        final JSONObject body = new JSONObject();
        body.put("x", x);
        body.put("y", y);
        restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK});
    }

    public void sendKeys(String sessionId, String value) throws RESTError {
        final Builder webResource = buildDefaultRequest("keys", sessionId);
        final JSONObject body = new JSONObject();
        body.put("value", value);
        restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK});
    }

    public JSONObject getIsAccessible(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/accessible", uuid), sessionId);
        return new JSONObject(restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }
}
