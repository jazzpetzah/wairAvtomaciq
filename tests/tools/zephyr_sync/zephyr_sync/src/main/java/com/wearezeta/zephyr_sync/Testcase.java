package com.wearezeta.zephyr_sync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public abstract class Testcase {
	public final static String MAGIC_TAG_PREFIX = "@";
	public final static String ID_TAG_PREFIX = MAGIC_TAG_PREFIX + "id";

	// id can contain more that one value
	// multiple values are separated by SPACE character
	private String id = "";
	private Set<String> tags = new LinkedHashSet<String>();
	private String name = "";
	private boolean isAutomated = false;

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
		return new LinkedHashSet<String>(this.tags);
	}

	public void setTags(Set<String> tags) {
		this.tags = new LinkedHashSet<String>(tags);
		isChanged = true;
	}

	public String getName() {
		return name;
	}

	public boolean getIsChanged() {
		return this.isChanged;
	}

	public Testcase(String id, String name, Set<String> tags,
			boolean isAutomated) {
		this.id = id;
		this.name = name;
		this.tags = new LinkedHashSet<String>(tags);
		this.isAutomated = isAutomated;
	}

	public static String extractIdsFromTags(Set<String> tags) {
		List<String> resultList = new ArrayList<String>();
		for (String tag : tags) {
			if (tag.startsWith(ID_TAG_PREFIX)) {
				resultList.add(tag.substring(ID_TAG_PREFIX.length()));
			}
		}
		if (resultList.size() > 0) {
			return StringUtils.join(resultList, " ");
		} else {
			return "";
		}
	}

	public static Set<String> extractTagsFromString(String in) {
		in = in.trim();
		if (in.length() > 0) {
			final Set<String> tags = new LinkedHashSet<String>(Arrays.asList(in
					.split("\\s+")));
			Set<String> resultTags = new LinkedHashSet<String>();
			for (String tag : tags) {
				if (tag.startsWith(MAGIC_TAG_PREFIX)) {
					resultTags.add(tag);
				} else {
					resultTags.add(MAGIC_TAG_PREFIX + tag);
				}
			}
			return resultTags;
		} else {
			return new LinkedHashSet<String>();
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
		if (!(other instanceof Testcase)) {
			return false;
		}
		final String thisId = this.getId();
		final String otherId = ((Testcase) other).getId();
		return thisId.length() > 0 && otherId.equals(thisId);
	}
}
