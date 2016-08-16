package com.wearezeta.auto.common.calling2.v1.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import java.util.Map;
import org.apache.log4j.Logger;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flow {

    private static final Logger LOG = Logger.getLogger(Flow.class.getName());

    private final Meta meta;
    private final Telemetry telemetry;
    @JsonRawValue
    private final Map<String, Object> rtcPeerConnection;

    @JsonCreator
    public Flow(
            @JsonProperty("meta") Meta meta,
            @JsonProperty("telemetry") Telemetry telemetry,
            @JsonProperty("rtcPeerConnection") Map<String, Object> rtcPeerConnection) {
        this.meta = meta;
        this.telemetry = telemetry;
        this.rtcPeerConnection = rtcPeerConnection;
    }

    public Meta getMeta() {
        return meta;
    }

    public Telemetry getTelemetry() {
        return telemetry;
    }

    public Map<String, Object> getPeerConnection() {
        return rtcPeerConnection;
    }

    @Override
    public String toString() {
        return "Flow{" + "meta=" + meta + ", telemetry=" + telemetry + ", rtcPeerConnection=" + rtcPeerConnection + '}';
    }
    

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {

        private final String id;
        private final String remoteUserId;

        @JsonCreator
        public Meta(
                @JsonProperty("id") String id,
                @JsonProperty("remote_user_id") String remoteUserId) {
            this.id = id;
            this.remoteUserId = remoteUserId;
        }

        public String getId() {
            return id;
        }

        public String getRemoteUserId() {
            return remoteUserId;
        }

        @Override
        public String toString() {
            return "Meta{" + "id=" + id + ", remoteUserId=" + remoteUserId + '}';
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Telemetry {

        private final Statistics stats;
        @JsonRawValue
        private final Map<String, Object> timings;

        @JsonCreator
        public Telemetry(
                @JsonProperty("statistics") Statistics stats,
                @JsonProperty("timings") Map<String, Object> timings) {
            this.stats = stats;
            this.timings = timings;
        }

        public Statistics getStats() {
            return stats;
        }

        public Map<String, Object> getTimings() {
            return timings;
        }

        @Override
        public String toString() {
            return "Telemetry{" + "stats=" + stats + ", timings=" + timings + '}';
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Statistics {

        private final AudioStats audio;
        private final VideoStats video;

        @JsonCreator
        public Statistics(
                @JsonProperty("audio") AudioStats audio,
                @JsonProperty("video") VideoStats video) {
            this.audio = audio;
            this.video = video;
        }

        public AudioStats getAudio() {
            return audio;
        }

        public VideoStats getVideo() {
            return video;
        }

        @Override
        public String toString() {
            return "Statistics{" + "audio=" + audio + ", video=" + video + '}';
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AudioStats {

        private final long bytesReceived;
        private final long bytesSent;

        @JsonCreator
        public AudioStats(
                @JsonProperty("bytes_received") long bytesReceived,
                @JsonProperty("bytes_sent") long bytesSent) {
            this.bytesReceived = bytesReceived;
            this.bytesSent = bytesSent;
        }

        public long getBytesReceived() {
            return bytesReceived;
        }

        public long getBytesSent() {
            return bytesSent;
        }

        @Override
        public String toString() {
            return "AudioStats{" + "bytesReceived=" + bytesReceived + ", bytesSent=" + bytesSent + '}';
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VideoStats {

        private final long bytesReceived;
        private final long bytesSent;

        @JsonCreator
        public VideoStats(
                @JsonProperty("bytes_received") long bytesReceived,
                @JsonProperty("bytes_sent") long bytesSent) {
            this.bytesReceived = bytesReceived;
            this.bytesSent = bytesSent;
        }

        public long getBytesReceived() {
            return bytesReceived;
        }

        public long getBytesSent() {
            return bytesSent;
        }

        @Override
        public String toString() {
            return "VideoStats{" + "bytesReceived=" + bytesReceived + ", bytesSent=" + bytesSent + '}';
        }
    }
}
