package com.wearezeta.auto.common.calling2.v1.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Instance {

	private final String id;
	private final InstanceStatus instanceStatus;

	private final String email;
	private final String password;
	private final Call currentCall;
	private final BackendType backend;
	private final InstanceType instanceType;
	private String screenshot;
	private String name;
	private final long timeout;
	private final long created;

	@JsonCreator
	public Instance(@JsonProperty("id") String id,
			@JsonProperty("instanceStatus") InstanceStatus instanceStatus,
			@JsonProperty("currentCall") Call currentCall,
			@JsonProperty("email") String email,
			@JsonProperty("password") String password,
			@JsonProperty("backend") BackendType backend,
			@JsonProperty("instanceType") InstanceType instanceType,
			@JsonProperty("name") String name,
			@JsonProperty("timeout") long timeout,
			@JsonProperty("created") long created) {
		this.id = id;
		this.instanceStatus = instanceStatus;
		this.currentCall = currentCall;
		this.email = email;
		this.password = password;
		this.backend = backend;
		this.instanceType = instanceType;
		this.name = name;
		this.timeout = timeout;
		this.created = created;
	}

	public String getId() {
		return id;
	}

	public InstanceStatus getStatus() {
		return instanceStatus;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public BackendType getBackend() {
		return backend;
	}

	public InstanceType getInstanceType() {
		return instanceType;
	}

	public long getTimeout() {
		return timeout;
	}

	public long getCreated() {
		return created;
	}

	public Call getCurrentCall() {
		return currentCall;
	}

	public String getScreenshot() {
		return screenshot;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "InstanceDTO{" + "id=" + id + ", instanceStatus="
				+ instanceStatus + ", email=" + email + ", password="
				+ password + ", currentCall=" + currentCall + ", backend="
				+ backend + ", instanceType=" + instanceType
				+ ", screenshot=<skipped>, timeout=" + timeout + ", created="
				+ created + '}';
	}

}
