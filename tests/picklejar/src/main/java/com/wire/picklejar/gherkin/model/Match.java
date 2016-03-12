package com.wire.picklejar.gherkin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"arguments", "location"})
public class Match implements Serializable {

	@JsonProperty("arguments")
	private List<Argument> arguments;

	@JsonProperty("location")
	private String location;

	@Override
	public String toString() {
		return "Match{" + "arguments=" + arguments + ", location=" + location + '}';
	}

	

}
