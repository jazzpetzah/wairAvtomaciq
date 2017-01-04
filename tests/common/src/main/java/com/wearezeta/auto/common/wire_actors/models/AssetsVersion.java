package com.wearezeta.auto.common.wire_actors.models;

public enum AssetsVersion {
    V2("2"), V3("3");

    private String strRepresentation;

    AssetsVersion(String strRepresentation) {
        this.strRepresentation = strRepresentation;
    }

    public String stringRepresentation() {
        return this.strRepresentation;
    }
}
