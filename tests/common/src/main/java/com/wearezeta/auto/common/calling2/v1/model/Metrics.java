package com.wearezeta.auto.common.calling2.v1.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Metrics {
    
    private final boolean success;
    private final String setupTime;

    @JsonCreator
    public Metrics(@JsonProperty("success") boolean success, @JsonProperty("setup_time") String setupTime) {
        this.success = success;
        this.setupTime = setupTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getSetupTime() {
        return setupTime;
    }

    @Override
    public String toString() {
        return "Metrics{" +
                "success=" + success +
                ", setupTime='" + setupTime + '\'' +
                '}';
    }
}
