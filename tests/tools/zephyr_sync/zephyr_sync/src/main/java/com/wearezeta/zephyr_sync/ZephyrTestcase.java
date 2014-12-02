package com.wearezeta.zephyr_sync;

import java.util.Set;

public class ZephyrTestcase extends Testcase {
	private String automatedScriptName = "";
	
	public String getAutomatedScriptName() {
		return automatedScriptName;
	}

	public void setAutomatedScriptName(String automatedScriptName) {
		this.automatedScriptName = automatedScriptName;
		this.isChanged = true;
	}
	
	public ZephyrTestcase(String id, String name, Set<String> tags, boolean isAutomated,
			String automatedScriptName) {
		super(id, name, tags, isAutomated);
		this.automatedScriptName = automatedScriptName;
	}
}
