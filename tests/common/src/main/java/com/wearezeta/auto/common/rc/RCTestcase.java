package com.wearezeta.auto.common.rc;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class RCTestcase {
	public final static String MAGIC_TAG_PREFIX = "@";
	public final static String ZEPHYR_ID_TAG_PREFIX = MAGIC_TAG_PREFIX + "id";
	public final static String TESTRAIL_ID_TAG_PREFIX = MAGIC_TAG_PREFIX + "C";
	public final static String RC_TAG = MAGIC_TAG_PREFIX + "rc";

	// id can contain more that one value
	// multiple values are separated by SPACE character
	private String id = "";
	private Set<String> tags = new LinkedHashSet<>();
	private String name = "";
	protected boolean isAutomated = false;

	protected boolean isChanged = false;

	public boolean getIsAutomated() {
		return isAutomated;
	}

	public void setIsAutomated(boolean isAutomated) {
		this.isAutomated = isAutomated;
		isChanged = true;
	}

	public String getId() {
		return id;
	}

	public Set<String> getTags() {
		return new LinkedHashSet<>(this.tags);
	}

	public void setTags(Set<String> tags) {
		this.tags = new LinkedHashSet<>(tags);
		isChanged = true;
	}

	public String getName() {
		return name;
	}

	public boolean getIsChanged() {
		return this.isChanged;
	}

	public RCTestcase(String id, String name, Set<String> tags,
			boolean isAutomated) {
		this.id = id;
		this.name = name;
		this.tags = new LinkedHashSet<>(tags);
		this.isAutomated = isAutomated;
	}

	public static Set<String> extractTagsFromString(String in) {
		in = in.trim();
		if (in.length() > 0) {
			final Set<String> tags = new LinkedHashSet<>(Arrays.asList(in
					.split("\\s+")));
			Set<String> resultTags = new LinkedHashSet<>();
			for (String tag : tags) {
				if (tag.startsWith(MAGIC_TAG_PREFIX)) {
					resultTags.add(tag);
				} else {
					resultTags.add(MAGIC_TAG_PREFIX + tag);
				}
			}
			return resultTags;
		} else {
			return new LinkedHashSet<>();
		}
	}

	@Override
	public String toString() {
		return String
				.format("%s[\n\tid: %s\n\tname: %s\n\ttags: %s\n\tisAutomated: %s\n\tisChanged: %s\n]",
						this.getClass().getName(), this.getId(),
						this.getName(), this.getTags().toString(),
						this.getIsAutomated(), this.getIsChanged());
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof RCTestcase)) {
			return false;
		}
		final String thisId = this.getId();
		final String otherId = ((RCTestcase) other).getId();
		return thisId.length() > 0 && otherId.equals(thisId);
	}
}
