package com.wearezeta.auto.ios.tools.ABProvisioner;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rest.CommonRESTHandlers;
import com.wearezeta.auto.common.rest.RESTError;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.json.JSONArray;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;

/**
 * https://github.com/wireapp/wire-automation-addressbook-ios/blob/master/README.md
 */
final class ABProvisionerRESTClient {

    private static final Logger log = ZetaLogger.getLog(ABProvisionerRESTClient.class.getSimpleName());

    public static final String URL_PROTOCOL = "http://";

    private String ip;

    public ABProvisionerRESTClient(String ip) {
        this.ip = ip;
    }

    public String getApiRoot() {
        return String.format("%s%s/", URL_PROTOCOL, this.ip);
    }

    private static final CommonRESTHandlers restHandlers =
            new CommonRESTHandlers(ABProvisionerRESTClient::verifyRequestResult);

    private static void verifyRequestResult(int currentResponseCode, int[] acceptableResponseCodes,
                                            String message) throws ABProvisionerAPIException {
        if (!ArrayUtils.contains(acceptableResponseCodes, currentResponseCode)) {
            throw new ABProvisionerAPIException(String.format(
                    "Address Book Provisioner service API request failed. " +
                            "Request return code is: %d. Expected codes are: %s. Message from service is: %s",
                    currentResponseCode, Arrays.toString(acceptableResponseCodes), message), currentResponseCode);
        }
    }

    private Builder buildDefaultRequest(String restAction) {
        final String dstUrl = String.format("%s/%s", getApiRoot(), restAction);
        log.debug(String.format("Request to %s...", dstUrl));
        final Client client = ClientBuilder.newClient();
        return client.target(dstUrl).request().accept(MediaType.APPLICATION_JSON);
    }

    public JSONArray getContacts() throws RESTError {
        final Builder webResource = buildDefaultRequest("contacts");
        final String output = restHandlers.httpGet(webResource, new int[]{HttpStatus.SC_OK});
        return new JSONArray(output);
    }

    public String addContacts(JSONArray newContacts) throws RESTError {
        final Builder webResource = buildDefaultRequest("contacts");
        return restHandlers.httpPost(webResource, newContacts.toString(), new int[]{HttpStatus.SC_OK});
    }

    public String clearContacts() throws RESTError {
        final Builder webResource = buildDefaultRequest("contacts/all");
        return restHandlers.httpDelete(webResource, new int[]{HttpStatus.SC_OK});
    }
}
