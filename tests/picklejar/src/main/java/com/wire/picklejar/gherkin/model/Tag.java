package com.wire.picklejar.gherkin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"line", "name"})
public class Tag implements Serializable {

    @JsonProperty("line")
    private long line;
    @JsonProperty("name")
    private String name;

    public Tag(String name) {
        this.line = 1;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" + "line=" + line + ", name=" + name + '}';
    }

}
