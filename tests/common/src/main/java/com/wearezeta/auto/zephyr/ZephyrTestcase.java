package com.wearezeta.auto.zephyr;

import java.util.Set;

import com.wearezeta.auto.common.rc.RCTestcase;

public class ZephyrTestcase extends RCTestcase {
	private String automatedScriptName = "";
	private String automatedScriptPath = "";
	
	public String getAutomatedScriptPath() {
		return automatedScriptPath;
	}

	public void setAutomatedScriptPath(String automatedScriptPath) {
		this.automatedScriptPath = automatedScriptPath;
		this.isChanged = true;
	}

	public String getAutomatedScriptName() {
		return automatedScriptName;
	}

	public void setAutomatedScriptName(String automatedScriptName) {
		this.automatedScriptName = automatedScriptName;
		this.isChanged = true;
	}
	
	public ZephyrTestcase(String id, String name, Set<String> tags, boolean isAutomated,
			String automatedScriptName, String automatedScriptPath) {
		super(id, name, tags, isAutomated);
		this.automatedScriptName = automatedScriptName;
		this.automatedScriptPath = automatedScriptPath;
	}
}
