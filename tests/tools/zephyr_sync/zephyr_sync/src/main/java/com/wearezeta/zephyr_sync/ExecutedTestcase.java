package com.wearezeta.zephyr_sync;

import java.util.Set;

public class ExecutedTestcase extends CucumberTestcase {
	private Boolean isPassed;
	private Boolean isSkipped;
	
	public Boolean getIsPassed() {
		return isPassed;
	}
	
	public Boolean getIsSkipped() {
		return isSkipped;
	}

	@Override
	public void setIsAutomated(Boolean isAutomated) {
		throw new RuntimeException("The property is read-only");
	}

	@Override
	public void setTags(Set<String> tags) {
		throw new RuntimeException("The property is read-only");
	}

	public ExecutedTestcase(String id, String name, Set<String> tags,
			String cucumberId, Boolean isPassed, Boolean isSkipped) {
		super(id, name, tags, cucumberId);
		this.isPassed = isPassed;
		this.isSkipped = isSkipped;
	}

	@Override
	public String toString() {
		return String
				.format("%s[\n\tid: %s\n\tname: %s\n\ttags: %s\n\tisAutomated: %s\n\t" 
						+ "cucumberId: %s\n\tisPassed: %s\n\tisSkipped: %s\n\tisChanged: %s\n]",
						this.getClass().getName(),
						this.getId(), this.getName(), this.getTags().toString(),
						this.getIsAutomated(), this.getCucumberId(),
						this.getIsPassed(), this.getIsSkipped(), this.getIsChanged());
	}
}
