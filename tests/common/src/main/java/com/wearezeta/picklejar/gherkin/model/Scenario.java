package com.wearezeta.picklejar.gherkin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"before", "line", "name", "description", "id", "after", "type", "keyword", "steps"})
public class Scenario implements Serializable {

	@JsonProperty("before")
	private List<Around> before;
	@JsonProperty("after")
	private List<Around> after;
	@JsonProperty("line")
	private long line;
	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("id")
	private String id;
	@JsonProperty("keyword")
	private String keyword;
	@JsonProperty("type")
	private String type;
	@JsonProperty("steps")
	private List<Step> steps;
	@JsonProperty("tags")
	private List<Tag> tags;

	@Override
	public String toString() {
		return "Scenario{" + "before=" + before + ", after=" + after + ", line=" + line + ", name=" + name + ", description=" + description + ", id=" + id + ", keyword=" + keyword + ", type=" + type + ", steps=" + steps + ", tags=" + tags + '}';
	}


}
