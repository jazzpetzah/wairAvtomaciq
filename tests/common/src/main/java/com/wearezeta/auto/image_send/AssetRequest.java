package com.wearezeta.auto.image_send;

import java.io.IOException;

import javax.ws.rs.core.UriBuilderException;

import com.wearezeta.auto.common.BackEndREST;

public class AssetRequest {
	private String endpoint = "assets";

	private String host = "";
	private String method = "POST";
	private String contentType;
	private String contentDisposition;
	private long contentLength;
	private byte[] payload;
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getHost() throws IllegalArgumentException, UriBuilderException, IOException {
		if (host.equals(""))
		{
			setHost();
		}
		return host;
	}

	public void setHost() throws IllegalArgumentException, UriBuilderException, IOException {
		host = BackEndREST.getBaseURI().toString();
	}
	
	public AssetRequest() {
		// TODO Auto-generated constructor stub
	}
	
}
