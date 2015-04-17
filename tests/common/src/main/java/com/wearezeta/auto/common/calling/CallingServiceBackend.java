package com.wearezeta.auto.common.calling;

public enum CallingServiceBackend {
	
	Autocall("autocall"), Webdriver("webdriver"), Blender("blender");
	
	private final String name;
	
	private CallingServiceBackend(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

}
