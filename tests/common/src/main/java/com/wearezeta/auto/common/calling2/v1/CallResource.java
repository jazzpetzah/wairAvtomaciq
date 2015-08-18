package com.wearezeta.auto.common.calling2.v1;

import com.wearezeta.auto.common.backend.BackendRequestException;
import com.wearezeta.auto.common.calling2.v1.exception.CallingServiceCallException;
import com.wearezeta.auto.common.calling2.v1.model.Call;
import com.wearezeta.auto.common.calling2.v1.model.Instance;
import com.wearezeta.auto.common.calling2.v1.model.CallRequest;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rest.CommonRESTHandlers;
import com.wearezeta.auto.common.rest.RESTError;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

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
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

public class CallResource {

	private static final org.apache.log4j.Logger LOG = ZetaLogger
			.getLog(CallingServiceClient.class.getName());

	private static final int MAX_REQUEST_RETRY_COUNT = 2;

	private final Client client;
	private final String callingServiceAdress;
	private final String callingServiceVersion;
	private final CommonRESTHandlers restHandler = new CommonRESTHandlers(
			CallResource::verifyRequestResult, MAX_REQUEST_RETRY_COUNT);

	public CallResource(String callingServiceAdress,
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
		TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
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
		} };
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
			int[] acceptableResponseCodes) throws BackendRequestException {
		if (!ArrayUtils.contains(acceptableResponseCodes, currentResponseCode)) {
			throw new BackendRequestException(
					String.format(
							"Calling service call request failed. Request return code is: %d. Expected codes are: %s",
							currentResponseCode,
							Arrays.toString(acceptableResponseCodes)),
					currentResponseCode);
		}
	}

	private Invocation.Builder buildDefaultRequest(String url, String accept) {
		LOG.debug(String.format("Making request to %s...", url));
		return client.target(url).request(accept);
	}

	public Call start(Instance instance, CallRequest callRequest)
			throws CallingServiceCallException {
		final String target = String.format(
				"%s/api/v%s/instance/%s/call/start", callingServiceAdress,
				callingServiceVersion, instance.getId());
		try {
			return restHandler.httpPost(
					buildDefaultRequest(target, MediaType.APPLICATION_JSON),
					callRequest, Call.class, new int[] { HttpStatus.SC_OK });
		} catch (RESTError ex) {
			throw new CallingServiceCallException(ex);
		}
	}

	public Call acceptNext(Instance instance, CallRequest callRequest)
			throws CallingServiceCallException {

		final String target = String.format(
				"%s/api/v%s/instance/%s/call/acceptNext", callingServiceAdress,
				callingServiceVersion, instance.getId());
		try {
			return restHandler.httpPost(
					buildDefaultRequest(target, MediaType.APPLICATION_JSON),
					callRequest, Call.class, new int[] { HttpStatus.SC_OK });
		} catch (RESTError ex) {
			throw new CallingServiceCallException(ex);
		}
	}

	public Call stop(Instance instance, Call call)
			throws CallingServiceCallException {
		final String target = String.format(
				"%s/api/v%s/instance/%s/call/%s/stop", callingServiceAdress,
				callingServiceVersion, instance.getId(), call.getId());
		try {
			return restHandler.httpPut(
					buildDefaultRequest(target, MediaType.APPLICATION_JSON),
					"", Call.class, new int[] { HttpStatus.SC_OK });
		} catch (RESTError ex) {
			throw new CallingServiceCallException(ex);
		}
	}

	public Call getCall(Instance instance, Call call)
			throws CallingServiceCallException {
		final String target = String.format(
				"%s/api/v%s/instance/%s/call/%s/status", callingServiceAdress,
				callingServiceVersion, instance.getId(), call.getId());
		try {
			return restHandler.httpGet(
					buildDefaultRequest(target, MediaType.APPLICATION_JSON),
					new GenericType<Call>() {
					}, new int[] { HttpStatus.SC_OK });
		} catch (RESTError ex) {
			throw new CallingServiceCallException(ex);
		}
	}

}
