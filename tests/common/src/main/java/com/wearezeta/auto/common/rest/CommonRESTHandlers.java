package com.wearezeta.auto.common.rest;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

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

    private static final Logger log = ZetaLogger.getLog(CommonRESTHandlers.class.getSimpleName());
    private static final String EMPTY_LOG_RECORD = "EMPTY";
    private static final int IS_ALIVE_CONNECTION_TIMEOUT_MS = 5000;

    public static boolean isAlive(URL siteURL) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(IS_ALIVE_CONNECTION_TIMEOUT_MS);
            connection.connect();
            final int responseCode = connection.getResponseCode();
            log.debug(String.format("Response code from %s: %s", siteURL.toString(), responseCode));
            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            // Just ignore
            // e.printStackTrace();
        }
        return false;
    }

    private static final int MAX_SINGLE_ENTITY_LENGTH_IN_LOG = 400;

    private static String formatLogRecord(Object entity) {
        String result = EMPTY_LOG_RECORD;
        if (entity != null) {
            if (entity instanceof String) {
                result = ((String) entity);
            } else {
                result = entity.toString();
            }
            if (result.length() == 0) {
                result = EMPTY_LOG_RECORD;
            } else if (result.length() > MAX_SINGLE_ENTITY_LENGTH_IN_LOG) {
                result = result.substring(0, MAX_SINGLE_ENTITY_LENGTH_IN_LOG)
                        + " ...";
            }
        }
        return result;
    }

    public <T> T httpPost(Builder webResource, Object entity,
            Class<T> responseEntityType, int[] acceptableResponseCodes)
            throws RESTError {
        return httpPost(webResource, MediaType.APPLICATION_JSON, entity,
                responseEntityType, acceptableResponseCodes);
    }

    public <T> T httpPost(Builder webResource, String contentType,
            Object entity, Class<T> responseEntityType,
            int[] acceptableResponseCodes) throws RESTError {
        log.debug("POST REQUEST...");
        log.debug(String.format(" >>> Input data: %s", formatLogRecord(entity)));
        Response response = null;
        int tryNum = 0;
        do {
            try {
                response = webResource.post(Entity.entity(entity, contentType),
                        Response.class);
                break;
            } catch (ProcessingException e) {
                log.warn(e.getMessage());
                tryNum++;
            }
        } while (tryNum < this.maxRetries);
        T responseEntity;
        try {
            response.bufferEntity();
            responseEntity = response.readEntity(responseEntityType);
            log.debug(String.format(" >>> Response: %s",
                    formatLogRecord(responseEntity)));
            this.responseHandler.verifyRequestResult(response.getStatus(),
                    acceptableResponseCodes, formatLogRecord(responseEntity));
        } catch (ProcessingException | IllegalStateException | NullPointerException e) {
            responseEntity = null;
            log.warn(e.getMessage());

            if (!"java.lang.String".equals(responseEntityType.getName())) {
                try {
                    String responseString = response.readEntity(String.class);
                    log.debug(String.format(" >>> Response: %s",
                            formatLogRecord(responseString)));
                    this.responseHandler.verifyRequestResult(
                            response.getStatus(), acceptableResponseCodes, formatLogRecord(responseString));
                } catch (ProcessingException | IllegalStateException | NullPointerException ex) {
                    log.warn(ex.getMessage());
                }
            }
        }
        return responseEntity;
    }

    public String httpPost(Builder webResource, Object entity,
            int[] acceptableResponseCodes) throws RESTError {
        String returnString = httpPost(webResource, entity, String.class,
                acceptableResponseCodes);
        returnString = returnString == null ? "" : returnString;
        return returnString;
    }

    public String httpPost(Builder webResource, Object entity,
            String contentType, int[] acceptableResponseCodes) throws RESTError {
        String returnString = httpPost(webResource, contentType, entity,
                String.class, acceptableResponseCodes);
        returnString = returnString == null ? "" : returnString;
        return returnString;
    }

    public <T> T httpPut(Builder webResource, Object entity,
            Class<T> responseEntityType, int[] acceptableResponseCodes)
            throws RESTError {
        log.debug("PUT REQUEST...");
        log.debug(String.format(" >>> Input data: %s", formatLogRecord(entity)));
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
            response.bufferEntity();
            responseEntity = response.readEntity(responseEntityType);
            log.debug(String.format(" >>> Response: %s",
                    formatLogRecord(responseEntity)));
            this.responseHandler.verifyRequestResult(response.getStatus(),
                    acceptableResponseCodes, formatLogRecord(responseEntity));
        } catch (ProcessingException | IllegalStateException | NullPointerException e) {
            responseEntity = null;
            log.warn(e.getMessage());
            if (!"java.lang.String".equals(responseEntityType.getName())) {
                try {
                    String responseString = response.readEntity(String.class);
                    log.debug(String.format(" >>> Response: %s",
                            formatLogRecord(responseString)));
                    this.responseHandler.verifyRequestResult(
                            response.getStatus(), acceptableResponseCodes, formatLogRecord(responseString));
                } catch (ProcessingException | IllegalStateException | NullPointerException ex) {
                    log.warn(ex.getMessage());
                }
            }
        }
        return responseEntity;
    }

    public String httpPut(Builder webResource, Object entity,
            int[] acceptableResponseCodes) throws RESTError {
        String returnString = httpPut(webResource, entity, String.class,
                acceptableResponseCodes);
        returnString = returnString == null ? "" : returnString;
        return returnString;
    }

    public <T> T httpDelete(Builder webResource, Optional<Object> entity, Class<T> responseEntityType,
            int[] acceptableResponseCodes) throws RESTError {
        log.debug("DELETE REQUEST...");
        Response response = null;
        int tryNum = 0;
        do {
            try {
                if (entity.isPresent()) {
                    log.debug(String.format(" >>> Input data: %s", formatLogRecord(entity)));
                    response = webResource.method("DELETE",
                            Entity.entity(entity.get(), MediaType.APPLICATION_JSON_TYPE), Response.class);
                } else {
                    response = webResource.delete(Response.class);
                }
                break;
            } catch (ProcessingException e) {
                log.warn(e.getMessage());
                tryNum++;
            }
        } while (tryNum < this.maxRetries);
        T responseEntity;
        try {
            response.bufferEntity();
            responseEntity = response.readEntity(responseEntityType);
            log.debug(String.format(" >>> Response: %s",
                    formatLogRecord(responseEntity)));
            this.responseHandler.verifyRequestResult(response.getStatus(),
                    acceptableResponseCodes, formatLogRecord(responseEntity));
        } catch (ProcessingException | IllegalStateException | NullPointerException e) {
            responseEntity = null;
            log.warn(e.getMessage());
            if (!"java.lang.String".equals(responseEntityType.getName())) {
                try {
                    String responseString = response.readEntity(String.class);
                    log.debug(String.format(" >>> Response: %s",
                            formatLogRecord(responseString)));
                    this.responseHandler.verifyRequestResult(
                            response.getStatus(), acceptableResponseCodes, formatLogRecord(responseString));
                } catch (ProcessingException | IllegalStateException | NullPointerException ex) {
                    log.warn(ex.getMessage());
                }
            }
        }
        return responseEntity;
    }

    public String httpDelete(Builder webResource, Object entity, int[] acceptableResponseCodes)
            throws RESTError {
        String returnString = httpDelete(webResource, Optional.of(entity), String.class,
                acceptableResponseCodes);
        returnString = returnString == null ? "" : returnString;
        return returnString;
    }

    public String httpDelete(Builder webResource, int[] acceptableResponseCodes)
            throws RESTError {
        String returnString = httpDelete(webResource, Optional.empty(), String.class,
                acceptableResponseCodes);
        returnString = returnString == null ? "" : returnString;
        return returnString;
    }

    public <T> T httpGet(Builder webResource,
            GenericType<T> responseEntityType, int[] acceptableResponseCodes)
            throws RESTError {
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
            response.bufferEntity();
            responseEntity = response.readEntity(responseEntityType);
            log.debug(String.format(" >>> Response: %s",
                    formatLogRecord(responseEntity)));
            this.responseHandler.verifyRequestResult(response.getStatus(),
                    acceptableResponseCodes, formatLogRecord(responseEntity));
        } catch (ProcessingException | IllegalStateException | NullPointerException e) {
            responseEntity = null;
            log.warn(e.getMessage());
            if (!"java.lang.String".equals(responseEntityType.getRawType()
                    .getName())) {
                try {
                    String responseString = response.readEntity(String.class);
                    log.debug(String.format(" >>> Response: %s",
                            formatLogRecord(responseString)));
                    this.responseHandler.verifyRequestResult(
                            response.getStatus(), acceptableResponseCodes, formatLogRecord(responseString));
                } catch (ProcessingException | IllegalStateException | NullPointerException ex) {
                    log.warn(ex.getMessage());
                }
            }
        }
        return responseEntity;
    }

    public String httpGet(Builder webResource, int[] acceptableResponseCodes)
            throws RESTError {
        String returnString = httpGet(webResource, new GenericType<String>() {
        }, acceptableResponseCodes);
        returnString = returnString == null ? "" : returnString;
        return returnString;
    }
}
