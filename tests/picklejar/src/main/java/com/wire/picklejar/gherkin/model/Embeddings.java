package com.wire.picklejar.gherkin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"data", "mime_type"})
public class Embeddings implements Serializable {

	@JsonProperty("data")
	private String data;

	@JsonProperty("mime_type")
	private String mime_type;

	@Override
	public String toString() {
		return "Embeddings{" + "data=" + data + ", mime_type=" + mime_type + '}';
	}
}
