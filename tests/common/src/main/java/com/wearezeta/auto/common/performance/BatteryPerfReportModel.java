package com.wearezeta.auto.common.performance;

import org.json.JSONObject;

public abstract class BatteryPerfReportModel extends AbstractPerfReportModel {

	/**
	 * All times are in milliseconds
	 */
	private int previousCapacityValue;
	private int currentCapacityValue;
	private long previousRxBytes;
	private long currentRxBytes;
	private long previousTxBytes;
	private long currentTxBytes;
	private int minutesDuration;

	public int getMinutesDuration() {
		return minutesDuration;
	}

	public void setMinutesDuration(int minutesDuration) {
		this.minutesDuration = minutesDuration;
	}

	public int getPreviousCapacityValue() {
		return previousCapacityValue;
	}

	public void setPreviousCapacityValue(int previousCapacityValue) {
		this.previousCapacityValue = previousCapacityValue;
	}

	public int getCurrentCapacityValue() {
		return currentCapacityValue;
	}

	public void setCurrentCapacityValue(int currentCapacityValue) {
		this.currentCapacityValue = currentCapacityValue;
	}

	public long getPreviousRxBytes() {
		return previousRxBytes;
	}

	public void setPreviousRxBytes(long previousRxBytes) {
		this.previousRxBytes = previousRxBytes;
	}

	public long getCurrentRxBytes() {
		return currentRxBytes;
	}

	public void setCurrentRxBytes(long currentRxBytes) {
		this.currentRxBytes = currentRxBytes;
	}

	public long getPreviousTxBytes() {
		return previousTxBytes;
	}

	public void setPreviousTxBytes(long previousTxBytes) {
		this.previousTxBytes = previousTxBytes;
	}

	public long getCurrentTxBytes() {
		return currentTxBytes;
	}

	public void setCurrentTxBytes(long currentTxBytes) {
		this.currentTxBytes = currentTxBytes;
	}

	public BatteryPerfReportModel() {
		// Keep this empty constructor for serialization purposes
	}

	public JSONObject asJSON() {
		final JSONObject result = super.asJSON();
		result.put("previousCapacityValue", this.getPreviousCapacityValue());
		result.put("currentCapacityValue", this.getCurrentCapacityValue());
		result.put("previousRxBytes", this.getPreviousRxBytes());
		result.put("currentRxBytes", this.getCurrentRxBytes());
		result.put("previousTxBytes", this.getPreviousTxBytes());
		result.put("currentTxBytes", this.getCurrentTxBytes());
		result.put("minutesDuration", this.getMinutesDuration());
		return result;
	}

	protected void loadFromJSON(final JSONObject jsonObj) throws Exception {
		super.loadFromJSON(jsonObj);
		this.setPreviousCapacityValue(jsonObj.getInt("previousCapacityValue"));
		this.setCurrentCapacityValue(jsonObj.getInt("currentCapacityValue"));
		this.setPreviousRxBytes(jsonObj.getLong("previousRxBytes"));
		this.setCurrentRxBytes(jsonObj.getLong("currentRxBytes"));
		this.setPreviousTxBytes(jsonObj.getLong("previousTxBytes"));
		this.setCurrentTxBytes(jsonObj.getLong("currentTxBytes"));
		this.setMinutesDuration(jsonObj.getInt("minutesDuration"));
	}
}
