package com.wearezeta.auto.common.rest;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
		String result = EMPTY_LOG_RECORD;
		if (entity != null) {
			if (entity instanceof String) {
				result = ((String) entity);
				if (result.length() == 0) {
					result = EMPTY_LOG_RECORD;
				} else if (result.length() > MAX_SINGLE_ENTITY_LENGTH_IN_LOG) {
					result = result.substring(0,
							MAX_SINGLE_ENTITY_LENGTH_IN_LOG) + " ...";
				}
			} else {
				result = entity.toString();
				if (result.length() == 0) {
					result = EMPTY_LOG_RECORD;
				} else if (result.length() > MAX_SINGLE_ENTITY_LENGTH_IN_LOG) {
					result = result.substring(0,
							MAX_SINGLE_ENTITY_LENGTH_IN_LOG) + " ...";
				}
			}
		}
		return result;
	}

	public <T> T httpPost(Builder webResource, Object entity,
			Class<T> responseEntityType, int[] acceptableResponseCodes)
			throws RESTError {
		log.debug("POST REQUEST...");
		Response response = null;
		int tryNum = 0;
		do {
			try {
				response = webResource.post(
						Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE),
						Response.class);
				break;
			} catch (ProcessingException e) {
				log.warn(e.getMessage());
				tryNum++;
			}
		} while (tryNum < this.maxRetries);
		T responseEntity;
		try {
			responseEntity = response.readEntity(responseEntityType);
		} catch (ProcessingException | IllegalStateException
				| NullPointerException e) {
			responseEntity = null;
			log.warn(e.getMessage());
		}
		log.debug(String.format(" >>> Input data: %s\n >>> Response: %s",
				formatLogRecord(entity), formatLogRecord(responseEntity)));
		this.responseHandler.verifyRequestResult(response.getStatus(),
				acceptableResponseCodes);
		return responseEntity;
	}

	public String httpPost(Builder webResource, Object entity,
			int[] acceptableResponseCodes) throws RESTError {
		String returnString = httpPost(webResource, entity, String.class,
				acceptableResponseCodes);
		returnString = returnString == null ? "" : returnString;
		return returnString;
	}

	public <T> T httpPut(Builder webResource, Object entity,
			Class<T> responseEntityType, int[] acceptableResponseCodes)
			throws RESTError {
		log.debug("PUT REQUEST...");
		Response response = null;
		int tryNum = 0;
		do {
			try {
				response = webResource.put(
						Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE),
						Response.class);
				break;
			} catch (ProcessingException e) {
				log.warn(e.getMessage());
				tryNum++;
			}
		} while (tryNum < this.maxRetries);
		T responseEntity;
		try {
			responseEntity = response.readEntity(responseEntityType);
		} catch (ProcessingException | IllegalStateException
				| NullPointerException e) {
			responseEntity = null;
			log.warn(e.getMessage());
		}
		log.debug(String.format(" >>> Input data: %s\n >>> Response: %s",
				formatLogRecord(entity), formatLogRecord(responseEntity)));
		this.responseHandler.verifyRequestResult(response.getStatus(),
				acceptableResponseCodes);
		return responseEntity;
	}

	public String httpPut(Builder webResource, Object entity,
			int[] acceptableResponseCodes) throws RESTError {
		String returnString = httpPut(webResource, entity, String.class,
				acceptableResponseCodes);
		returnString = returnString == null ? "" : returnString;
		return returnString;
	}

	public <T> T httpDelete(Builder webResource, Class<T> responseEntityType,
			int[] acceptableResponseCodes) throws RESTError {
		log.debug("DELETE REQUEST...");
		Response response = null;
		int tryNum = 0;
		do {
			try {
				response = webResource.delete(Response.class);
				break;
			} catch (ProcessingException e) {
				log.warn(e.getMessage());
				tryNum++;
			}
		} while (tryNum < this.maxRetries);
		T responseEntity;
		try {
			responseEntity = response.readEntity(responseEntityType);
		} catch (ProcessingException | IllegalStateException
				| NullPointerException e) {
			responseEntity = null;
			log.warn(e.getMessage());
		}
		log.debug(String.format(" >>> Response: %s",
				formatLogRecord(responseEntity)));
		this.responseHandler.verifyRequestResult(response.getStatus(),
				acceptableResponseCodes);
		return responseEntity;
	}

	public String httpDelete(Builder webResource, int[] acceptableResponseCodes)
			throws RESTError {
		String returnString = httpDelete(webResource, String.class,
				acceptableResponseCodes);
		returnString = returnString == null ? "" : returnString;
		return returnString;
	}

	public <T> T httpGet(Builder webResource, Class<T> responseEntityType,
			int[] acceptableResponseCodes) throws RESTError {
		log.debug("GET REQUEST...");
		Response response = null;
		int tryNum = 0;
		do {
			try {
				response = webResource.get(Response.class);
				break;
			} catch (ProcessingException e) {
				log.warn(e.getMessage());
				tryNum++;
			}
		} while (tryNum < this.maxRetries);
		T responseEntity;
		try {
			responseEntity = response.readEntity(responseEntityType);
		} catch (ProcessingException | IllegalStateException
				| NullPointerException e) {
			responseEntity = null;
			log.warn(e.getMessage());
		}
		log.debug(String.format(" >>> Response: %s",
				formatLogRecord(responseEntity)));
		this.responseHandler.verifyRequestResult(response.getStatus(),
				acceptableResponseCodes);
		return responseEntity;
	}

	public String httpGet(Builder webResource, int[] acceptableResponseCodes)
			throws RESTError {
		String returnString = httpGet(webResource, String.class,
				acceptableResponseCodes);
		returnString = returnString == null ? "" : returnString;
		return returnString;
	}
}
