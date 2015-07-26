package com.wearezeta.auto.common.calling2.v1.model;

public enum InstanceStatus {
	NON_EXISTENT, STARTING, STARTED, STOPPING, STOPPED, DESTROYED, ERROR;

	public static boolean isContainedInSubset(InstanceStatus[] subSet,
			InstanceStatus item) {
		for (InstanceStatus status : subSet) {
			if (item == status) {
				return true;
			}
		}
		return false;
	}
}
