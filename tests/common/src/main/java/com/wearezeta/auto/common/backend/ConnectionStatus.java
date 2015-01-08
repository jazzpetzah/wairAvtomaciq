package com.wearezeta.auto.common.backend;

public enum ConnectionStatus {
	Accepted("accepted"), Blocked("blocked"), Pending("pending"), Ignored(
			"ignored"), Sent("sent");

	private final String name;

	private ConnectionStatus(String name) {
		this.name = name;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : name.equals(otherName);
	}

	@Override
	public String toString() {
		return name;
	}
}
