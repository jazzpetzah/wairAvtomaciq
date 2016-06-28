package com.wire.picklejar.gherkin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.Arrays;
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

    public Scenario(String feature, String name, int exampleNum, String keyword, List<Step> steps, List<Tag> tags) {
        this.line = 1;
        this.name = name.trim();
        this.id = (feature.toLowerCase() + ";" + this.name.toLowerCase()).replaceAll("[^a-zA-Z0-9]", "-")+";;"+exampleNum;
        this.type = "scenario";
        this.keyword = keyword;
        this.steps = steps;
        this.tags = tags;
        this.description = "";
        this.before = Arrays.asList(new Around[]{new Around()});
        this.after = Arrays.asList(new Around[]{new Around()});
    }

    public List<Step> getSteps() {
        return steps;
    }

    public String getName() {
        return name;
    }

    public List<Tag> getTags() {
        return tags;
    }
    
    @Override
    public String toString() {
        return "Scenario{" + "before=" + before + ", after=" + after + ", line=" + line + ", name=" + name + ", description=" + description + ", id=" + id + ", keyword=" + keyword + ", type=" + type + ", steps=" + steps + ", tags=" + tags + '}';
    }

}
