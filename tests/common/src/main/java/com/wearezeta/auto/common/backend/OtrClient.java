package com.wearezeta.auto.common.backend;

import org.json.JSONObject;

import java.util.Optional;

public class OtrClient {
    private String cookie;
    private String time;
    private Optional<Location> location = Optional.empty();
    private Optional<String> address = Optional.empty();
    private Optional<String> model = Optional.empty();
    private String id;
    private String klass;
    private Optional<String> label = Optional.empty();
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Optional<Location> getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = Optional.of(location);
    }

    public Optional<String> getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = Optional.of(address);
    }

    public Optional<String> getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = Optional.of(model);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKlass() {
        return klass;
    }

    public void setKlass(String klass) {
        this.klass = klass;
    }

    public Optional<String> getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = Optional.of(label);
    }

    public class Location {
        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        private double latitude;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longtitude) {
            this.longitude = longtitude;
        }

        private double longitude;

        public Location() {}

        public Location(JSONObject source) {
            this.latitude = source.getDouble("lat");
            this.longitude = source.getDouble("lon");
        }

        public JSONObject asJson() {
            final JSONObject result = new JSONObject();
            result.put("lat", this.getLatitude());
            result.put("lon", this.getLongitude());
            return result;
        }
    }

    public OtrClient() {}

    public OtrClient(JSONObject source) {
        this.cookie = source.getString("cookie");
        this.time = source.getString("time");
        if (source.has("location")) {
            this.location = Optional.of(new Location(source.getJSONObject("location")));
        }
        if (source.has("address")) {
            this.address = Optional.of(source.getString("address"));
        }
        if (source.has("model")) {
            this.model = Optional.of(source.getString("model"));
        }
        this.id = source.getString("id");
        this.type = source.getString("type");
        this.klass = source.getString("class");
        if (source.has("label")) {
            this.label = Optional.of(source.getString("label"));
        }
    }

    public JSONObject asJson() {
        final JSONObject result = new JSONObject();
        result.put("cookie", this.getCookie());
        result.put("time", this.getTime());
        if (this.getLocation().isPresent()) {
            result.put("location", this.getLocation().get().asJson());
        }
        if (this.getAddress().isPresent()) {
            result.put("address", this.getAddress().get());
        }
        if (this.getModel().isPresent()) {
            result.put("model", this.getModel().get());
        }
        result.put("id", this.getId());
        result.put("type", this.getType());
        result.put("class", this.getKlass());
        if (this.getLabel().isPresent()) {
            result.put("label", this.getLabel().get());
        }
        return result;
    }
}
