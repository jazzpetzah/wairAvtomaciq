package com.wearezeta.auto.zephyr;

import java.util.HashSet;
import java.util.Set;

import com.wearezeta.auto.common.rc.RCTestcase;


public class ExecutedZephyrTestcase extends RCTestcase {
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
	
	private ZephyrTestPhase parentPhase;
	public ZephyrTestPhase getParentPhase() {
		return this.parentPhase;
	}
	
	// ! This method is for internal usage only
	public void setParentPhase(ZephyrTestPhase parent) {
		this.parentPhase = parent;
	}

	public ExecutedZephyrTestcase(String id, String executionId, String name,
			String executionComment, ZephyrExecutionStatus status) {
		super(id, name, new HashSet<String>(), false);
		this.executionComment = executionComment;
		this.executionStatus = status;
		this.executionId = executionId;
	}

}
