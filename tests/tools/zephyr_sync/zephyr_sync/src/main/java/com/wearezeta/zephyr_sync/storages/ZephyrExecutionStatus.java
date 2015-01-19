package com.wearezeta.zephyr_sync.storages;

import java.util.NoSuchElementException;

public enum ZephyrExecutionStatus {
	Undefined(null, "Undefined"), Pass(1, "Pass"), Fail(2, "Fail"), WIP(3,
			"WIP"), Blocked(4, "Blocked");

	private final Integer id;
	private final String name;

	public Integer getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	private ZephyrExecutionStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return this.getName();
	}

	public static ZephyrExecutionStatus getById(Integer id) {
		if (id == null) {
			return ZephyrExecutionStatus.Undefined;
		}
		for (ZephyrExecutionStatus status : ZephyrExecutionStatus.values()) {
			if (status.getId() != null
					&& status.getId().intValue() == id.intValue()) {
				return status;
			}
		}
		throw new NoSuchElementException(String.format(
				"Execution result id '%d' is unknown", id));
	}

	public static ZephyrExecutionStatus getByName(String resultName) {
		for (ZephyrExecutionStatus result : ZephyrExecutionStatus.values()) {
			if (result.getName().equalsIgnoreCase(resultName)) {
				return result;
			}
		}
		throw new NoSuchElementException(String.format(
				"Execution Result '%s' is unknown", resultName));
	}

}
