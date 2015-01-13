package com.wearezeta.zephyr_sync.storages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wearezeta.zephyr_sync.ExecutedZephyrTestcase;

public class ZephyrTestPhase {
	private String id;

	public String getId() {
		return id;
	}

	private String name;

	public String getName() {
		return name;
	}

	private Date scheduledTo;

	public Date getScheduledTo() {
		return scheduledTo;
	}

	private ZephyrTestCycle parentCycle;

	public ZephyrTestCycle getParentCycle() {
		return parentCycle;
	}
	
	protected void setParentCycle(ZephyrTestCycle parent) {
		this.parentCycle = parent;
	}
	
	private List<ExecutedZephyrTestcase> testcases = new ArrayList<ExecutedZephyrTestcase>();

	public List<ExecutedZephyrTestcase> getTestcases() {
		return new ArrayList<ExecutedZephyrTestcase>(this.testcases);
	}

	public ZephyrTestPhase(String id, String name, Date scheduledTo,
			List<ExecutedZephyrTestcase> testcases) {
		this.id = id;
		this.name = name;
		this.scheduledTo = scheduledTo;
		this.testcases = testcases;
	}
}
