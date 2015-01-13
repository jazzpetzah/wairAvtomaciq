package com.wearezeta.zephyr_sync.storages;

import java.util.ArrayList;
import java.util.List;

public class ZephyrTestCycle {
	private String id;
	public String getId() {
		return id;
	}

	private String name;
	public String getName() {
		return name;
	}

	private List<ZephyrTestPhase> childPhases = new ArrayList<ZephyrTestPhase>();
	public List<ZephyrTestPhase> getPhases() {
		return new ArrayList<ZephyrTestPhase>(this.childPhases);
	}

	public static class PhaseNotFoundException extends Exception {
		private static final long serialVersionUID = -3160848973229746341L;

		public PhaseNotFoundException(String msg) {
			super(msg);
		}
	}

	public ZephyrTestPhase getPhaseByName(String name)
			throws PhaseNotFoundException {
		for (ZephyrTestPhase phase : this.childPhases) {
			if (phase.getName().equals(name)) {
				return phase;
			}
		}
		throw new PhaseNotFoundException(String.format(
				"The phase '%s' could not be found in test cycle '%s'", name,
				this.getName()));
	}

	public ZephyrTestCycle(String id, String name, List<ZephyrTestPhase> childPhases) {
		this.id = id;
		this.name = name;
		this.childPhases = childPhases;
		for (ZephyrTestPhase phase : this.childPhases) {
			phase.setParentCycle(this);
		}
	}
}
