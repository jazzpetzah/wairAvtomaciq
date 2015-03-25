package com.wearezeta.auto.android.common;

import java.util.ArrayList;
import java.util.List;

public class AndroidResource {
	
	private int resourceIndex;
	private String resourceName;
	private double medianValue;
	private double averageValue;
	private double totalValue;
	private List<Double> values = new ArrayList<>();
	
	public double getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(double i) {
		this.totalValue = i;
	}
	
	public List<Double> getValues() {
		return values;
	}
	public void setValues(List<Double> values) {
		this.values = values;
	}
	
	public void addValue(double value) {
		values.add(value);
	}
	
	public void removeValueByIndex(int valueIndex) {
		values.remove(valueIndex);
	}
	
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public Double getMedianValue() {
		return medianValue;
	}
	public void setMedianValue(Double medianValue) {
		this.medianValue = medianValue;
	}
	public Double getAverageValue() {
		return averageValue;
	}
	public void setAverageValue(Double averageValue) {
		this.averageValue = averageValue;
	}
	public int getResourceIndex() {
		return resourceIndex;
	}
	public void setResourceIndex(int resourceIndex) {
		this.resourceIndex = resourceIndex;
	}

}
