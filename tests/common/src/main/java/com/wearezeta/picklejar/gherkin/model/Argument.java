package com.wearezeta.picklejar.gherkin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"val", "offset"})
public class Argument implements Serializable {

	@JsonProperty("val")
	private String val;

	@JsonProperty("offset")
	private Long offset;

	@Override
	public String toString() {
		return "Argument{" + "val=" + val + ", offset=" + offset + '}';
	}
}
