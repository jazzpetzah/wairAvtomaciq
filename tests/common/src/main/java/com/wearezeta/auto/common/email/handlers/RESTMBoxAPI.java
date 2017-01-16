package com.wearezeta.auto.common.email.handlers;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import com.wearezeta.auto.common.rest.RESTError;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rest.CommonRESTHandlers;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;

import org.glassfish.jersey.client.ClientProperties;

final class RESTMBoxAPI {

    private static final Logger log = ZetaLogger.getLog(RESTMBoxAPI.class.getSimpleName());

    public static String getApiRoot() {
        try {
            return CommonUtils.getDefaultEmailListenerUrlFromConfig(RESTMBoxAPI.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final CommonRESTHandlers restHandlers = new CommonRESTHandlers(RESTMBoxAPI::verifyRequestResult);

    private static void verifyRequestResult(int currentResponseCode,
                                            int[] acceptableResponseCodes, String message) throws RESTMBoxException {
        if (!ArrayUtils.contains(acceptableResponseCodes, currentResponseCode)) {
            throw new RESTMBoxException(String.format(
                    "Mailbox service API request failed. Request return code is: %d. Expected codes are: %s. " +
                            "Message from service is: %s",
                    currentResponseCode, Arrays.toString(acceptableResponseCodes), message), currentResponseCode);
        }
    }

    private static Builder buildDefaultRequest(String restAction, int timeoutMilliseconds) {
        final String dstUrl = String.format("%s/%s", getApiRoot(), restAction);
        log.debug(String.format("Request to %s...", dstUrl));
        final Client client = ClientBuilder.newClient();
        client.property(ClientProperties.READ_TIMEOUT, timeoutMilliseconds);
        return client.target(dstUrl).request().accept(MediaType.APPLICATION_JSON);
    }

    public static JSONArray getRecentEmailsForUser(String email, int minCount,
                                                   int maxCount, int timeoutMilliseconds) throws RESTError {
        Builder webResource = buildDefaultRequest(String.format("recent_emails/%s/%s/%s", email, maxCount, minCount),
                timeoutMilliseconds);
        final String output = restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK});
        try {
            return new JSONArray(output);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}
