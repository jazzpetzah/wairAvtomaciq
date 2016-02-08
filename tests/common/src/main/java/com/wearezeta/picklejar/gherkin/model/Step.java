package com.wearezeta.picklejar.gherkin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;

@JsonPropertyOrder({"result", "comments", "embeddings", "line", "name", "match", "matchedColumns", "keyword"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Step implements Serializable {

	@JsonProperty("line")
	private long line;
	@JsonProperty("name")
	private String name;
	@JsonProperty("keyword")
	private String keyword;
	@JsonProperty("result")
	private Result result;
	@JsonProperty("match")
	private Match match;
	@JsonProperty("embeddings")
	private List<Embeddings> embeddings;
	@JsonProperty("matchedColumns")
	private int[] matchedColumns;
	@JsonProperty("comments")
	private List<Comment> comments;

	@Override
	public String toString() {
		return "Step{" + "line=" + line + ", name=" + name + ", keyword=" + keyword + ", result=" + result + ", match=" + match + ", embeddings=" + embeddings + ", matchedColumns=" + matchedColumns + ", comments=" + comments + '}';
	}


}
