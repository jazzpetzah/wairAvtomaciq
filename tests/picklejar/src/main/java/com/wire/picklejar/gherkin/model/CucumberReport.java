package com.wire.picklejar.gherkin.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.ArrayList;

public class CucumberReport extends ArrayList<Feature> implements Serializable {

    static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return super.toString();
        }
    }

}
