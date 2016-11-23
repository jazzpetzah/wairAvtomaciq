package com.wearezeta.auto.common.calling2.v1.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Metrics {
    
    private final boolean success;
    private final long estabTime;

    @JsonCreator
    public Metrics(@JsonProperty("success") boolean success, @JsonProperty("estab_time") long estabTime) {
        this.success = success;
        this.estabTime = estabTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public long getEstabTime() {
        return estabTime;
    }

    @Override
    public String toString() {
        return "Metrics{" +
                "success=" + success +
                ", estabTime='" + estabTime + '\'' +
                '}';
    }
}
