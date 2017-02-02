package com.wearezeta.auto.common.driver.facebook_ios_driver;

import com.google.common.base.Throwables;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.Timedelta;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * https://github.com/facebook/WebDriverAgent/tree/master/WebDriverAgentLib/Commands
 */
final class FBDriverRESTClient {

    private static final Logger log = ZetaLogger.getLog(FBDriverRESTClient.class.getSimpleName());

    private static final String URL_PROTOCOL = "http://";

    private static final String EMPTY_JSON_BODY = new JSONObject().toString();

    private String hostname;
    private int port;

    FBDriverRESTClient(String ip, int port) {
        this.hostname = ip;
        this.port = port;
    }

    private String getApiRoot() {
        return String.format("%s%s:%s", URL_PROTOCOL, this.hostname, this.port);
    }

    static boolean isAlive(String hostname, int port) {
        try {
            return CommonRESTHandlers.isAlive(new URL(String.format("%s%s:%s", URL_PROTOCOL, hostname, port)));
        } catch (MalformedURLException e) {
            Throwables.propagate(e);
        }
        return false;
    }

    private static final CommonRESTHandlers restHandlers =
            new CommonRESTHandlers(FBDriverRESTClient::verifyRequestResult);

    private static void verifyRequestResult(int currentResponseCode, int[] acceptableResponseCodes,
                                            String message) throws FBDriverAPIError {
        if (!ArrayUtils.contains(acceptableResponseCodes, currentResponseCode)) {
            throw new FBDriverAPIError(String.format(
                    "Facebook WebDriver service API request failed. " +
                            "Request return code is: %d. Expected codes are: %s. Message from service is: %s",
                    currentResponseCode, Arrays.toString(acceptableResponseCodes), message), currentResponseCode);
        }
    }

    private Builder buildDefaultRequest() {
        final String dstUrl = getApiRoot() + "/";
        log.debug(String.format("Request to %s...", dstUrl));
        final Client client = ClientBuilder.newClient();
        return client.target(dstUrl).request().header("Content-type", MediaType.APPLICATION_JSON);
    }

    private Builder buildDefaultRequest(String restAction, String sessionId) {
        final String dstUrl = String.format("%s/session/%s/%s", getApiRoot(), sessionId, restAction);
        log.debug(String.format("Request to %s...", dstUrl));
        final Client client = ClientBuilder.newClient();
        return client.target(dstUrl).request().header("Content-type", MediaType.APPLICATION_JSON);
    }

    private Builder buildDefaultRequest(String restAction) {
        final String dstUrl = String.format("%s/%s", getApiRoot(), restAction);
        log.debug(String.format("Request to %s...", dstUrl));
        final Client client = ClientBuilder.newClient();
        return client.target(dstUrl).request().header("Content-type", MediaType.APPLICATION_JSON);
    }

    JSONObject setRotation(String sessionId, FBDeviceRotation o) throws RESTError {
        final Builder webResource = buildDefaultRequest("rotation", sessionId);
        final JSONObject body = new JSONObject();
        body.put("x", 0);
        body.put("y", 0);
        body.put("z", o.getAngle());
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject getRotation(String sessionId) throws RESTError {
        final Builder webResource = buildDefaultRequest("rotation", sessionId);
        return waitForResponse(() -> restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject setOrientation(String sessionId, FBDeviceOrientation o) throws RESTError {
        final Builder webResource = buildDefaultRequest("orientation", sessionId);
        final JSONObject body = new JSONObject();
        body.put("orientation", o.name());
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject getOrientation(String sessionId) throws RESTError {
        final Builder webResource = buildDefaultRequest("orientation", sessionId);
        return waitForResponse(() -> restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject dragFromToForDuration(String sessionId, FBDragArguments fbDragArguments) throws RESTError  {
        final Builder webResource = buildDefaultRequest("wda/dragfromtoforduration", sessionId);
        final JSONObject body = new JSONObject();
        body.put("fromX", fbDragArguments.getFromX());
        body.put("fromY", fbDragArguments.getFromY());
        body.put("toX", fbDragArguments.getToX());
        body.put("toY", fbDragArguments.getToY());
        body.put("duration", fbDragArguments.getDuration());
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    @FunctionalInterface
    public interface RequestSender {
        String send() throws RESTError;
    }

    private final ExecutorService pool = Executors.newFixedThreadPool(1);

    private static final Timedelta WD_REQUEST_TIMEOUT = ZetaIOSDriver.MAX_COMMAND_DURATION;

    private JSONObject waitForResponse(RequestSender r) throws RESTError {
        final Future<String> future = pool.submit(r::send);
        try {
            return new JSONObject(future.get(WD_REQUEST_TIMEOUT.asMilliSeconds(), TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            if (e.getCause() instanceof FBDriverAPIError) {
                throw (FBDriverAPIError) e.getCause();
            }
            throw new FBDriverAPIError(e);
        }
    }

    JSONObject findElement(String sessionId, String using, String value) throws RESTError {
        final Builder webResource = buildDefaultRequest("element", sessionId);
        final JSONObject body = new JSONObject();
        body.put("using", using);
        body.put("value", value);
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject findElements(String sessionId, String using, String value) throws RESTError {
        final Builder webResource = buildDefaultRequest("elements", sessionId);
        final JSONObject body = new JSONObject();
        body.put("using", using);
        body.put("value", value);
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject findElement(String sessionId, String parentUUID, String using, String value) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/element", parentUUID), sessionId);
        final JSONObject body = new JSONObject();
        body.put("using", using);
        body.put("value", value);
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject findElements(String sessionId, String parentUUID, String using, String value) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/elements", parentUUID), sessionId);
        final JSONObject body = new JSONObject();
        body.put("using", using);
        body.put("value", value);
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject click(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/click", uuid), sessionId);
        return waitForResponse(() -> restHandlers.httpPost(webResource, EMPTY_JSON_BODY, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject swipe(String sessionId, String uuid, FBSwipeDirection direction) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("wda/element/%s/swipe", uuid), sessionId);
        final JSONObject body = new JSONObject();
        body.put("direction", direction.name().toLowerCase());
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject pinch(String sessionId, String uuid, FBPinchArguments args) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("wda/element/%s/pinch", uuid), sessionId);
        final JSONObject body = new JSONObject();
        body.put("scale", args.getScale());
        body.put("velocity", args.getVelocity());
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject twoFingerTap(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("wda/element/%s/twoFingerTap", uuid), sessionId);
        return waitForResponse(() -> restHandlers.httpPost(webResource, EMPTY_JSON_BODY, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject setValue(String sessionId, String uuid, CharSequence... charSequences) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/value", uuid), sessionId);
        final JSONObject body = new JSONObject();
        final JSONArray value = new JSONArray();
        for (CharSequence item : charSequences) {
            value.put(item.toString());
        }
        body.put("value", value);
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject clear(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/clear", uuid), sessionId);
        return waitForResponse(() -> restHandlers.httpPost(webResource, EMPTY_JSON_BODY, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject getTagName(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/name", uuid), sessionId);
        return waitForResponse(() -> restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject getAttribute(String sessionId, String uuid, String attrName) throws RESTError {
        final Builder webResource =
                buildDefaultRequest(String.format("element/%s/attribute/%s", uuid, attrName), sessionId);
        return waitForResponse(() -> restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject getSession() throws RESTError {
        final Builder webResource = buildDefaultRequest();
        return waitForResponse(() -> restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject isEnabled(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/enabled", uuid), sessionId);
        return waitForResponse(() -> restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject getText(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/text", uuid), sessionId);
        return waitForResponse(() -> restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject getIsDisplayed(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/displayed", uuid), sessionId);
        return waitForResponse(() -> restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject getRect(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("element/%s/rect", uuid), sessionId);
        return waitForResponse(() -> restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject doubleTap(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("wda/element/%s/doubleTap", uuid), sessionId);
        return waitForResponse(() -> restHandlers.httpPost(webResource, EMPTY_JSON_BODY, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject doubleTap(String sessionId, double x, double y) throws RESTError {
        final Builder webResource = buildDefaultRequest("wda/doubleTap", sessionId);
        final JSONObject body = new JSONObject();
        body.put("x", x);
        body.put("y", y);
        return waitForResponse(() -> restHandlers.httpPost(webResource, body, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject touchAndHold(String sessionId, String uuid, Timedelta duration) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("wda/element/%s/touchAndHold", uuid), sessionId);
        final JSONObject body = new JSONObject();
        body.put("duration", duration.asFloatSeconds());
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject touchAndHold(String sessionId, double x, double y, Timedelta duration) throws RESTError {
        // TODO: available since Appium 1.6.1+
        final Builder webResource = buildDefaultRequest("wda/touchAndHold", sessionId);
        final JSONObject body = new JSONObject();
        body.put("x", x);
        body.put("y", y);
        body.put("duration", duration.asFloatSeconds());
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject scroll(String sessionId, String uuid, Optional<String> toChildNamed,
                             Optional<String> direction, Optional<String> predicateString,
                             Optional<Boolean> toVisible) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("wda/element/%s/scroll", uuid), sessionId);
        final JSONObject body = new JSONObject();
        toChildNamed.ifPresent( x-> body.put("name", x));
        direction.ifPresent(x -> body.put("direction", x));
        predicateString.ifPresent(x -> body.put("predicateString", x));
        toVisible.ifPresent(x -> body.put("toVisible", x));
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject tap(String sessionId, String uuid, double x, double y) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("wda/tap/%s", uuid), sessionId);
        final JSONObject body = new JSONObject();
        body.put("x", x);
        body.put("y", y);
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject tap(String sessionId, double x, double y) throws RESTError {
        final Builder webResource = buildDefaultRequest("wda/tap/0", sessionId);
        final JSONObject body = new JSONObject();
        body.put("x", x);
        body.put("y", y);
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject sendKeys(String sessionId, CharSequence... charSequences) throws RESTError {
        final Builder webResource = buildDefaultRequest("wda/keys", sessionId);
        final JSONObject body = new JSONObject();
        final JSONArray value = new JSONArray();
        for (CharSequence item : charSequences) {
            value.put(item.toString());
        }
        body.put("value", value);
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject getIsAccessible(String sessionId, String uuid) throws RESTError {
        final Builder webResource = buildDefaultRequest(String.format("wda/element/%s/accessible", uuid), sessionId);
        return waitForResponse(() -> restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject getWindowSize(String sessionId) throws RESTError {
        final Builder webResource = buildDefaultRequest("window/size", sessionId);
        return waitForResponse(() -> restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject deactivateApp(String sessionId, Timedelta duration) throws RESTError {
        final Builder webResource = buildDefaultRequest("wda/deactivateApp", sessionId);
        final JSONObject body = new JSONObject();
        body.put("duration", duration.asFloatSeconds());
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject switchToHomescreen() throws RESTError {
        final Builder webResource = buildDefaultRequest("wda/homescreen");
        return waitForResponse(() -> restHandlers.httpPost(webResource, EMPTY_JSON_BODY, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject getAlertText(String sessionId) throws RESTError {
        final Builder webResource = buildDefaultRequest("alert/text", sessionId);
        return waitForResponse(() -> restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }

    JSONObject acceptAlert(String sessionId) throws RESTError {
        final Builder webResource = buildDefaultRequest("alert/accept", sessionId);
        return waitForResponse(
                () -> restHandlers.httpPost(webResource, EMPTY_JSON_BODY, new int[]{HttpStatus.SC_OK})
        );
    }

    JSONObject dismissAlert(String sessionId) throws RESTError {
        final Builder webResource = buildDefaultRequest("alert/dismiss", sessionId);
        return waitForResponse(
                () -> restHandlers.httpPost(webResource, EMPTY_JSON_BODY, new int[]{HttpStatus.SC_OK})
        );
    }

    JSONObject dragFromToForDuration(String sessionId, String uuid, FBDragArguments fbDragArguments)
            throws RESTError {
        final Builder webResource = buildDefaultRequest(
                String.format("wda/element/%s/dragfromtoforduration", uuid), sessionId);
        final JSONObject body = new JSONObject();
        body.put("fromX", fbDragArguments.getFromX());
        body.put("fromY", fbDragArguments.getFromY());
        body.put("toX", fbDragArguments.getToX());
        body.put("toY", fbDragArguments.getToY());
        body.put("duration", fbDragArguments.getDuration());
        return waitForResponse(() -> restHandlers.httpPost(webResource, body.toString(), new int[]{HttpStatus.SC_OK}));
    }

    JSONObject getScreenshot(String sessionId) throws RESTError {
        final Builder webResource = buildDefaultRequest("screenshot", sessionId);
        return waitForResponse(() -> restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK}));
    }
}
