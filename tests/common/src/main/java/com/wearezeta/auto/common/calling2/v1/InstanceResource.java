package com.wearezeta.auto.common.calling2.v1;

import com.wearezeta.auto.common.backend.BackendRequestException;
import com.wearezeta.auto.common.calling2.v1.exception.CallingServiceInstanceException;
import com.wearezeta.auto.common.calling2.v1.model.Flow;
import com.wearezeta.auto.common.calling2.v1.model.Instance;
import com.wearezeta.auto.common.calling2.v1.model.InstanceRequest;
import com.wearezeta.auto.common.rest.CommonRESTHandlers;
import com.wearezeta.auto.common.rest.RESTError;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

public class InstanceResource {

    private static final Logger LOG = Logger.getLogger(InstanceResource.class
            .getName());

    private static final int MAX_REQUEST_RETRY_COUNT = 2;

    private final Client client;
    private final String callingServiceAdress;
    private final String callingServiceVersion;
    private final CommonRESTHandlers restHandler = new CommonRESTHandlers(
            InstanceResource::verifyRequestResult, MAX_REQUEST_RETRY_COUNT);

    public InstanceResource(String callingServiceAdress,
            String callingServiceVersion, boolean trace) {
        ClientConfig config = new ClientConfig();
        if (trace) {
            config.register(new LoggingFilter(java.util.logging.Logger
                    .getLogger(InstanceResource.class.getName()), true));
        }
        client = initClient(config);
        this.callingServiceAdress = callingServiceAdress;
        this.callingServiceVersion = callingServiceVersion;
    }

    private Client initClient(Configuration config) {
        TrustManager[] certs = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }
        }};
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, certs, new SecureRandom());
        } catch (KeyManagementException | NoSuchAlgorithmException ex) {
            LOG.error(ex);
        }

        return ClientBuilder
                .newBuilder()
                .withConfig(config)
                .hostnameVerifier((String hostname, SSLSession session) -> true)
                .sslContext(ctx).build();
    }

    private static void verifyRequestResult(int currentResponseCode,
            int[] acceptableResponseCodes, String message)
            throws BackendRequestException {
        if (!ArrayUtils.contains(acceptableResponseCodes, currentResponseCode)) {
            throw new BackendRequestException(
                    String.format(
                            "Calling service instance request failed. Request return code is: %d. Expected codes are: %s. Message from service is: %s",
                            currentResponseCode,
                            Arrays.toString(acceptableResponseCodes), message),
                    currentResponseCode);
        }
    }

    private Invocation.Builder buildDefaultRequest(String url, String accept) {
        LOG.debug(String.format("Making request to %s...", url));
        return client.target(url).request(accept);
    }

    public Instance createInstance(InstanceRequest instanceRequest)
            throws CallingServiceInstanceException {
        final String target = String.format("%s/api/v%s/instance/create",
                callingServiceAdress, callingServiceVersion);
        try {
            return restHandler.httpPost(
                    buildDefaultRequest(target, MediaType.APPLICATION_JSON),
                    instanceRequest, Instance.class,
                    new int[]{HttpStatus.SC_OK});
        } catch (RESTError ex) {
            throw new CallingServiceInstanceException(ex);
        }
    }

    public Instance destroyInstance(Instance instance)
            throws CallingServiceInstanceException {
        final String target = String.format("%s/api/v%s/instance/%s/destroy",
                callingServiceAdress, callingServiceVersion, instance.getId());
        try {
            return restHandler.httpPut(
                    buildDefaultRequest(target, MediaType.APPLICATION_JSON),
                    "", Instance.class, new int[]{HttpStatus.SC_OK});
        } catch (RESTError ex) {
            throw new CallingServiceInstanceException(ex);
        }
    }

    public Instance getInstance(Instance instance)
            throws CallingServiceInstanceException {
        final String target = String.format("%s/api/v%s/instance/%s/status",
                callingServiceAdress, callingServiceVersion, instance.getId());
        try {
            return restHandler.httpGet(
                    buildDefaultRequest(target, MediaType.APPLICATION_JSON),
                    new GenericType<Instance>() {
                    }, new int[]{HttpStatus.SC_OK});
        } catch (RESTError ex) {
            throw new CallingServiceInstanceException(ex);
        }
    }

    public List<Flow> getFlows(Instance instance)
            throws CallingServiceInstanceException {
        final String target = String.format("%s/api/v%s/instance/%s/flows",
                callingServiceAdress, callingServiceVersion, instance.getId());
        try {
            return restHandler.httpGet(
                    buildDefaultRequest(target, MediaType.APPLICATION_JSON),
                    new GenericType<List<Flow>>() {
                    }, new int[]{HttpStatus.SC_OK});
        } catch (RESTError ex) {
            throw new CallingServiceInstanceException(ex);
        }
    }

    public String getLog(Instance instance)
            throws CallingServiceInstanceException {
        final String target = String.format("%s/api/v%s/instance/%s/log",
                callingServiceAdress, callingServiceVersion, instance.getId());
        try {
            return restHandler.httpGet(
                    buildDefaultRequest(target, MediaType.APPLICATION_JSON),
                    new GenericType<String>() {
                    }, new int[]{HttpStatus.SC_OK, HttpStatus.SC_NOT_FOUND});
        } catch (RESTError ex) {
            throw new CallingServiceInstanceException(ex);
        }
    }
}
