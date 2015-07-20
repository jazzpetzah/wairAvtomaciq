package com.wearezeta.auto.common.performance;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource.Builder;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rest.CommonRESTHandlers;

/**
 * This class implements PUSH functionality for Geckoboard API. More info on
 * https://developer.geckoboard.com
 * 
 */
public class GeckoboardAPI {
	private static final String BASE_URL = "https://push.geckoboard.com/v1";

	private String apiKey;

	public String getApiKey() {
		return this.apiKey;
	}

	private static final Logger log = ZetaLogger.getLog(GeckoboardAPI.class
			.getSimpleName());

	private static Client client = Client.create();

	private static final CommonRESTHandlers restHandlers = new CommonRESTHandlers(
			GeckoboardAPI::verifyRequestResult);

	private static void verifyRequestResult(int currentResponseCode,
			int[] acceptableResponseCodes) throws GeckoboardAPIError {
		if (!ArrayUtils.contains(acceptableResponseCodes, currentResponseCode)) {
			throw new GeckoboardAPIError(
					String.format(
							"Geckoboard request failed. Request return code is: %d. Expected codes are: %s",
							currentResponseCode,
							Arrays.toString(acceptableResponseCodes)),
					currentResponseCode);
		}
	}

	public GeckoboardAPI(String apiKey) {
		this.apiKey = apiKey;
	}

	private Builder buildDefaultRequest(String restAction, String widgetId)
			throws Exception {
		final String dstUrl = String.format("%s/%s/%s", BASE_URL, restAction,
				widgetId);
		log.debug(String.format("Making request to %s...", dstUrl));
		return client.resource(dstUrl).accept(MediaType.MEDIA_TYPE_WILDCARD);
	}

	public void updateWidget(String widgetId, final JSONObject data)
			throws Exception {
		final Builder webResource = buildDefaultRequest("send", widgetId).type(
				MediaType.APPLICATION_JSON);
		final JSONObject body = new JSONObject();
		body.put("api_key", this.getApiKey());
		body.put("data", data);
		restHandlers.httpPost(webResource, body.toString(),
				new int[] { HttpStatus.SC_OK });
	}
}
