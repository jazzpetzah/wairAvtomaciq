package com.wearezeta.zephyr_sync.storages;

import java.util.List;

import com.wearezeta.zephyr_sync.Testcase;

public abstract class TestcasesStorage {

	public TestcasesStorage() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract List<? extends Testcase> getTestcases() throws Throwable;
	
	public abstract void syncTestcases(List<? extends Testcase> testcases) throws Throwable;
}
