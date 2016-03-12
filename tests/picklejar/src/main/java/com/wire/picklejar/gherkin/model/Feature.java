package com.wire.picklejar.gherkin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"line", "elements", "name", "description", "id", "keyword", "uri"})
public class Feature implements Serializable {

	@JsonProperty("line")
	private long line;
	@JsonProperty("elements")
	private List<Scenario> scenarios;
	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("id")
	private String id;
	@JsonProperty("keyword")
	private String keyword;
	@JsonProperty("uri")
	private String uri;

	@Override
	public String toString() {
		return "Feature{" + "line=" + line + ", scenarios=" + scenarios + ", name=" + name + ", description=" + description + ", id=" + id + ", keyword=" + keyword + ", uri=" + uri + '}';
	}

	public List<Scenario> getScenarios() {
		return scenarios;
	}

	public void setScenarios(List<Scenario> scenarios) {
		this.scenarios = scenarios;
	}


	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 47 * hash + Objects.hashCode(this.name);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Feature other = (Feature) obj;
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		return true;
	}



}
