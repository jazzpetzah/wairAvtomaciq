package com.wearezeta.auto.ios.tools.ABProvisioner;

import com.google.common.base.Throwables;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rest.RESTError;
import org.apache.log4j.Logger;
import org.json.JSONArray;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This API presents https://github.com/wireapp/wire-automation-addressbook-ios/blob/master/README.md
 * and works only if real iDevice is connected to the machine and this device is connected to the same network
 * where host machine is connected
 */
public class ABProvisionerAPI {
    private static final Logger log = ZetaLogger.getLog(ABProvisionerAPI.class.getSimpleName());

    private static final String HOST_NAME = "localhost";
    private static final String PORT_NUMBER = "8001";

    private ABProvisionerRESTClient client;

    private ABProvisionerAPI() throws Exception {
        this.client = new ABProvisionerRESTClient(HOST_NAME, PORT_NUMBER);
    }

    private static ABProvisionerAPI instance = null;

    public synchronized static ABProvisionerAPI getInstance() {
        if (instance == null) {
            try {
                instance = new ABProvisionerAPI();
            } catch (Exception e) {
                e.printStackTrace();
                Throwables.propagate(e);
            }
        }
        return instance;
    }

    public List<ABContact> getContacts() throws RESTError {
        final List<ABContact> result = new ArrayList<>();
        final JSONArray contacts = client.getContacts();
        for (int i = 0; i < contacts.length(); i++) {
            result.add(ABContact.fromJSON(contacts.getJSONObject(i)));
        }
        return result;
    }

    public void addContacts(List<ABContact> newContacts) throws RESTError {
        final JSONArray arg = new JSONArray();
        newContacts.stream().forEach(x -> arg.put(x.toJSON()));
        client.addContacts(arg);
    }

    public void clearContacts() throws RESTError {
        client.clearContacts();
    }

    public boolean isAlive() {
        try {
            final URL siteURL = new URL(String.format("%s%s:%s/", ABProvisionerRESTClient.URL_PROTOCOL, HOST_NAME, PORT_NUMBER));
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
}
