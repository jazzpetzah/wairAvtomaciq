package com.wire.picklejar.gherkin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;

@JsonPropertyOrder({"duration", "error_message", "status"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result implements Serializable {

    @JsonProperty("duration")
    private Long duration;
    @JsonProperty("status")
    private String status;
    @JsonProperty("error_message")
    private String error_message;

    public Result(Long duration, String status, String error_message) {
        this.duration = duration;
        this.status = status;
        this.error_message = error_message;
    }
    
    @Override
    public String toString() {
        return "Result{" + "duration=" + duration + ", status=" + status + ", error_message=" + error_message + '}';
    }
}
