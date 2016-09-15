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

/**
 * https://github.com/facebook/WebDriverAgent/tree/ab2e0d52e8b4df25423d37f2582bbb9a7903e065/WebDriverAgentLib/Commands
 */
final class FBDriverRESTClient {

    private static final Logger log = ZetaLogger.getLog(FBDriverRESTClient.class.getSimpleName());

    public static final String URL_PROTOCOL = "http://";

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

    public String findElement(String sessionId, String using, String value) throws RESTError {
        final Builder webResource = buildDefaultRequest("element", sessionId);
        final JSONObject body = new JSONObject();
        body.put("using", using);
        body.put("value", value);
        return restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK});
    }

    public JSONArray findElements(String sessionId, String using, String value) throws RESTError {
        final Builder webResource = buildDefaultRequest("elements", sessionId);
        final JSONObject body = new JSONObject();
        body.put("using", using);
        body.put("value", value);
        return new JSONArray(restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    public String findElement(String sessionId, String parentUUID, String using, String value) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/element", parentUUID), sessionId);
        final JSONObject body = new JSONObject();
        body.put("using", using);
        body.put("value", value);
        return restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK});
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
        final JSONObject body = new JSONObject();
        restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK});
    }

    public void setValue(String sessionId, String uuid, String newValue) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/value", uuid), sessionId);
        final JSONObject body = new JSONObject();
        body.put("value", newValue);
        restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK});
    }

    public void clear(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/clear", uuid), sessionId);
        final JSONObject body = new JSONObject();
        restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK});
    }

    public String getTagName(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/name", uuid), sessionId);
        return restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK});
    }

    public String getAttribute(String sessionId, String uuid, String attrName) throws RESTError {
        final Builder webResource =
                buildDefaultRequest(String.format("element/%s/attribute/%s", uuid, attrName), sessionId);
        return restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK});
    }

    public JSONObject getSession() throws RESTError {
        final Builder webResource = buildDefaultRequest();
        return new JSONObject(restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }
}
