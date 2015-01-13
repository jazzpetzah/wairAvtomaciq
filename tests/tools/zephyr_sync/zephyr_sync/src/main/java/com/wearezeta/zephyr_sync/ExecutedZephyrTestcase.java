package com.wearezeta.zephyr_sync;

import java.util.HashSet;
import java.util.Set;

import com.wearezeta.zephyr_sync.storages.ZephyrExecutionStatus;

public class ExecutedZephyrTestcase extends Testcase {
	private ZephyrExecutionStatus executionStatus = ZephyrExecutionStatus.Undefined;

	public ZephyrExecutionStatus getExecutionStatus() {
		return this.executionStatus;
	}

	public void setExecutionStatus(ZephyrExecutionStatus newStatus) {
		this.executionStatus = newStatus;
		this.isChanged = true;
	}

	private String executionComment = null;

	public String getExecutionComment() {
		return this.executionComment;
	}

	public void setExecutionComment(String newComment) {
		this.executionComment = newComment;
		this.isChanged = true;
	}

	private String executionId;

	public String getExecutionId() {
		return this.executionId;
	}

	@Override
	public void setIsAutomated(boolean isAutomated) {
		throw new RuntimeException("The property is read-only");
	}

	@Override
	public void setTags(Set<String> tags) {
		throw new RuntimeException("The property is read-only");
	}

	public ExecutedZephyrTestcase(String id, String executionId, String name,
			String executionComment, ZephyrExecutionStatus status) {
		super(id, name, new HashSet<String>(), false);
		this.executionComment = executionComment;
		this.executionStatus = status;
		this.executionId = executionId;
	}

}
