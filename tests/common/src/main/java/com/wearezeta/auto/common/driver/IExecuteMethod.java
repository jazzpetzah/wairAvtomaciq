package com.wearezeta.auto.common.driver;

import java.util.Map;

import org.openqa.selenium.remote.Response;

@FunctionalInterface
public interface IExecuteMethod {
	public Response execute(String driverCommand, Map<String, ?> parameters);
}
