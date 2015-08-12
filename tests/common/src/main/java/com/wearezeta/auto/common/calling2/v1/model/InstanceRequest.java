package com.wearezeta.auto.common.calling2.v1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class InstanceRequest {

	private final String email;
	private final String password;
	private final BackendType backend;
	private final InstanceType instanceType;
	private final String name;
	private final long timeout = 1000L * 60 * 60;

	public InstanceRequest(String email, String password, BackendType backend,
			InstanceType instanceType, String name) {
		this.email = email;
		this.password = password;
		this.backend = backend;
		this.instanceType = instanceType;
		this.name = name;
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

	@Override
	public String toString() {
		return "InstanceRequest{" + "email=" + email + ", password=" + password
				+ ", backend=" + backend + ", instanceType=" + instanceType
				+ ", timeout=" + timeout + '}';
	}

}
