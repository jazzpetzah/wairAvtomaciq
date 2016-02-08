package com.wearezeta.picklejar.gherkin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"result", "match"})
public class Around implements Serializable {

	@JsonProperty("result")
	private Result result;

	@JsonProperty("match")
	private Match match;


	@Override
	public String toString() {
		return "Around{" + "result=" + result + ", match=" + match + '}';
	}
}
