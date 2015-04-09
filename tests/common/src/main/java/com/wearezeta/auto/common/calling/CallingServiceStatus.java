package com.wearezeta.auto.common.calling;

public enum CallingServiceStatus {

	Waiting("waiting");
	
	private final String name;
	
	private CallingServiceStatus(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
