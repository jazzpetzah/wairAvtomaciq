package com.wire.picklejar.gherkin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import static com.wire.picklejar.gherkin.model.CucumberReport.MAPPER;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    public Step(String name) {
        this.line = 1;
        this.name = name;
        this.keyword = "And ";
        this.match = new Match();
    }

    public void setResult(Result result) {
        System.out.println("setting result for step "+name+" to "+result);
        this.result = result;
    }

    public Result getResult() {
        return result;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Step other = (Step) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
    
    
    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return super.toString();
        }
    }

}
