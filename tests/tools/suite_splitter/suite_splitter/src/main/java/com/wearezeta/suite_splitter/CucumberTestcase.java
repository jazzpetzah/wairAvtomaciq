package com.wearezeta.suite_splitter;

import java.util.Set;

public class CucumberTestcase extends Testcase {
	private String cucumberId;

	public String getCucumberId() {
		return cucumberId;
	}
	
	public CucumberTestcase(String id, String name, Set<String> tags,
			String cucumberId) {
		super(id, name, tags, true);
		this.cucumberId = cucumberId;
	}

	@Override
	public String toString() {
		return String
				.format("%s[\n\tid: %s\n\tname: %s\n\ttags: %s\n\t"
						+ "isAutomated: %s\n\tcucumberId: %s\n]",
						this.getClass().getName(),
						this.getId(), this.getName(), this.getTags().toString(),
						this.getIsAutomated(), this.getCucumberId());
	}
}
