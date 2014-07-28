package com.wearezeta.auto.image_send;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;

public class ZImageSender {
	private String accessToken;
	private String tokenType;

	public String getTokenType() {
		return tokenType;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public ZImageSender(String accessToken, String tokenType) {
		this.accessToken = accessToken;
		this.tokenType = tokenType;
	}

	public List<Map<String, String>> send(List<AssetRequest> requests)
			throws IOException {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Client client = Client.create();
		client.addFilter(new LoggingFilter(System.out));

		for (AssetRequest request : requests) {
			WebResource assetResource = client.resource(String.format("%s/%s",
					request.getHost(), request.getEndpoint()));
			System.out.println(request.getContentDisposition());
			ClientResponse response = assetResource
					.type(request.getContentType())
					.header(HttpHeaders.AUTHORIZATION,
							String.format("%s %s", this.getTokenType(),
									this.getAccessToken()))
					.header("Content-Disposition",
							request.getContentDisposition())
					.header("Content-Length", request.getContentLength())
					.post(ClientResponse.class, request.getPayload());
			Map<String, String> responseResult = new HashMap<String, String>();
			responseResult.put(String.format("%d (%s)", response.getStatus(),
					response.getStatusInfo().getReasonPhrase()), response
					.getEntity(String.class));
			result.add(responseResult);
		}
		return result;
	}

}
