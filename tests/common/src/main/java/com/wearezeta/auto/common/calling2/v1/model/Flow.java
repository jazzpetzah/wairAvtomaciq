package com.wearezeta.auto.common.calling2.v1.model;

public class Flow {
	private final Long bytesIn;
	private final Long bytesOut;
	private final String remoteUserId;

	public Flow(Long bytesIn, Long bytesOut, String remoteUserId) {
		this.bytesIn = bytesIn;
		this.bytesOut = bytesOut;
		this.remoteUserId = remoteUserId;
	}

	public Long getBytesIn() {
		return bytesIn;
	}

	public Long getBytesOut() {
		return bytesOut;
	}

	public String getRemoteUserId() {
		return remoteUserId;
	}
}
