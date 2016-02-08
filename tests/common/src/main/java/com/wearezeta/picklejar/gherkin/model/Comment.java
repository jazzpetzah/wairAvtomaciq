package com.wearezeta.picklejar.gherkin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"line", "value"})
public class Comment implements Serializable {

	@JsonProperty("line")
	private long line;
	@JsonProperty("value")
	private String value;

	@Override
	public String toString() {
		return "Tag{" + "line=" + line + ", value=" + value + '}';
	}


}
