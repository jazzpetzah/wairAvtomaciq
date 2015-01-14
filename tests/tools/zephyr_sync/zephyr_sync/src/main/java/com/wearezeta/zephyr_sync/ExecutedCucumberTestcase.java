package com.wearezeta.zephyr_sync;

import java.util.Set;

import com.wearezeta.zephyr_sync.storages.CucumberExecutionStatus;

public class ExecutedCucumberTestcase extends CucumberTestcase {
	private CucumberExecutionStatus status = CucumberExecutionStatus.Undefined;

	public CucumberExecutionStatus getStatus() {
		return this.status;
	}

	@Override
	public void setIsAutomated(boolean isAutomated) {
		throw new RuntimeException("The property is read-only");
	}

	@Override
	public void setTags(Set<String> tags) {
		throw new RuntimeException("The property is read-only");
	}

	public ExecutedCucumberTestcase(String id, String name, Set<String> tags,
			String cucumberId, CucumberExecutionStatus status) {
		super(id, name, tags, cucumberId);
		this.status = status;
	}

	@Override
	public String toString() {
		return String
				.format("%s[\n\tid: %s\n\tname: %s\n\ttags: %s\n\tisAutomated: %s\n\t"
						+ "cucumberId: %s\n\tstatus: %s\n\tChanged: %s\n]",
						this.getClass().getName(), this.getId(),
						this.getName(), this.getTags().toString(),
						this.getIsAutomated(), this.getCucumberId(),
						this.getStatus(), this.getIsChanged());
	}
}
