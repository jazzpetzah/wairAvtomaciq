package com.wearezeta.auto.common.rest;

@FunctionalInterface
public interface RESTResponseHandler {

    public void verifyRequestResult(int currentResponseCode,
            int[] acceptableResponseCodes, String message) throws RESTError;
}
