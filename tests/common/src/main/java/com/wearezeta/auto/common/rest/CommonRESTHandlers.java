package com.wearezeta.auto.common.rest;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource.Builder;
import com.wearezeta.auto.common.log.ZetaLogger;

public final class CommonRESTHandlers {
	private RESTResponseHandler responseHandler;
	private int maxRetries = 1;

	public CommonRESTHandlers(RESTResponseHandler responseHandler) {
		this.responseHandler = responseHandler;
	}

	public CommonRESTHandlers(RESTResponseHandler responseHandler,
			int maxRetries) {
		this.responseHandler = responseHandler;
		this.maxRetries = maxRetries;
	}

	private static final Logger log = ZetaLogger
			.getLog(CommonRESTHandlers.class.getSimpleName());
	private static final String EMPTY_LOG_RECORD = "<EMPTY>";

	private static final int MAX_SINGLE_ENTITY_LENGTH_IN_LOG = 400;

	private static String formatLogRecord(Object entity) {
		String result = "<" + entity.getClass().getSimpleName() + ">";
		if (entity instanceof String) {
			result = ((String) entity);
			if (result.length() == 0) {
				result = EMPTY_LOG_RECORD;
			} else if (result.length() > MAX_SINGLE_ENTITY_LENGTH_IN_LOG) {
				result = result.substring(0, MAX_SINGLE_ENTITY_LENGTH_IN_LOG)
						+ "...";
			}
		}
		return result;
	}

	public String httpPost(Builder webResource, Object entity,
			int[] acceptableResponseCodes) throws RESTError {
		log.debug("PUT REQUEST...");
		ClientResponse response = null;
		int tryNum = 0;
		do {
			try {
				response = webResource.post(ClientResponse.class, entity);
				break;
			} catch (ClientHandlerException e) {
				e.printStackTrace();
				tryNum++;
			}
		} while (tryNum < this.maxRetries);
		String responseStr;
		try {
			responseStr = response.getEntity(String.class);
		} catch (UniformInterfaceException e) {
			responseStr = "";
		}
		log.debug(String.format(" >>> Input data: %s\n >>> Response: %s",
				formatLogRecord(entity), formatLogRecord(responseStr)));
		this.responseHandler.verifyRequestResult(response.getStatus(),
				acceptableResponseCodes);
		return responseStr;
	}

	public String httpPut(Builder webResource, Object entity,
			int[] acceptableResponseCodes) throws RESTError {
		log.debug("PUT REQUEST...");
		ClientResponse response = null;
		int tryNum = 0;
		do {
			try {
				response = webResource.put(ClientResponse.class, entity);
				break;
			} catch (ClientHandlerException e) {
				e.printStackTrace();
				tryNum++;
			}
		} while (tryNum < this.maxRetries);
		String responseStr;
		try {
			responseStr = response.getEntity(String.class);
		} catch (UniformInterfaceException e) {
			responseStr = "";
		}
		log.debug(String.format(" >>> Input data: %s\n >>> Response: %s",
				formatLogRecord(entity), formatLogRecord(responseStr)));
		this.responseHandler.verifyRequestResult(response.getStatus(),
				acceptableResponseCodes);
		return responseStr;
	}

	public String httpDelete(Builder webResource, int[] acceptableResponseCodes)
			throws RESTError {
		log.debug("DELETE REQUEST...");
		ClientResponse response = null;
		int tryNum = 0;
		do {
			try {
				response = webResource.delete(ClientResponse.class);
				break;
			} catch (ClientHandlerException e) {
				e.printStackTrace();
				tryNum++;
			}
		} while (tryNum < this.maxRetries);
		String responseStr;
		try {
			responseStr = response.getEntity(String.class);
		} catch (UniformInterfaceException e) {
			responseStr = "";
		}
		log.debug(String.format(" >>> Response: %s",
				formatLogRecord(responseStr)));
		this.responseHandler.verifyRequestResult(response.getStatus(),
				acceptableResponseCodes);
		return responseStr;
	}

	public Object httpGet(Builder webResource, Class<?> entityClass,
			int[] acceptableResponseCodes) throws RESTError {
		log.debug("GET REQUEST...");
		ClientResponse response = null;
		int tryNum = 0;
		do {
			try {
				response = webResource.get(ClientResponse.class);
				break;
			} catch (ClientHandlerException e) {
				e.printStackTrace();
				tryNum++;
			}
		} while (tryNum < this.maxRetries);
		Object responseObj = null;
		try {
			responseObj = response.getEntity(entityClass);
		} catch (UniformInterfaceException e) {
			// Do nothing
		}
		log.debug(String.format(" >>> Response: %s",
				formatLogRecord(responseObj)));
		this.responseHandler.verifyRequestResult(response.getStatus(),
				acceptableResponseCodes);
		return responseObj;
	}

	public String httpGet(Builder webResource, int[] acceptableResponseCodes)
			throws RESTError {
		log.debug("GET REQUEST...");
		ClientResponse response = null;
		int tryNum = 0;
		do {
			try {
				response = webResource.get(ClientResponse.class);
				break;
			} catch (ClientHandlerException e) {
				e.printStackTrace();
				tryNum++;
			}
		} while (tryNum < this.maxRetries);
		String responseStr;
		try {
			responseStr = response.getEntity(String.class);
		} catch (UniformInterfaceException e) {
			responseStr = "";
		}
		log.debug(String.format(" >>> Response: %s",
				formatLogRecord(responseStr)));
		this.responseHandler.verifyRequestResult(response.getStatus(),
				acceptableResponseCodes);
		return responseStr;
	}
}
