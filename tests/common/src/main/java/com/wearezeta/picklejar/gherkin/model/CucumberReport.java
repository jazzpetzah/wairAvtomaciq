package com.wearezeta.picklejar.gherkin.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.ArrayList;

public class CucumberReport extends ArrayList<Feature> implements Serializable {

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException ex) {
			return super.toString();
		}
	}

}
