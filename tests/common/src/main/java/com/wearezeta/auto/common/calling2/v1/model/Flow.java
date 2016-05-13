package com.wearezeta.auto.common.calling2.v1.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flow {

    private final Long bytesIn;
    private final Long bytesOut;
    private final Map<String, Object> data;
    private final String remoteUserId;

    @JsonCreator
    public Flow(@JsonProperty("bytesIn") Long bytesIn,
            @JsonProperty("bytesOut") Long bytesOut,
            @JsonProperty("remoteUserId") String remoteUserId,
            @JsonProperty("data") Map<String, Object> data){
        this.bytesIn = bytesIn;
        this.bytesOut = bytesOut;
        this.remoteUserId = remoteUserId;
        this.data = data;
    }

    public Long getBytesIn() {
        return bytesIn;
    }

    public Long getBytesOut() {
        return bytesOut;
    }

    public String getRemoteUserId() {
        return remoteUserId;
    }

    @Override
    public String toString() {
        return "Flow{" + "bytesIn=" + bytesIn + ", bytesOut=" + bytesOut + ", data=" + data + ", remoteUserId=" + remoteUserId + '}';
    }
    
}
