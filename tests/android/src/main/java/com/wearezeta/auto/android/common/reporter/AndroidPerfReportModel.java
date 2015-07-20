package com.wearezeta.auto.android.common.reporter;

import org.json.JSONObject;

import com.wearezeta.auto.common.performance.PerfReportModel;

public class AndroidPerfReportModel extends PerfReportModel {

	public AndroidPerfReportModel() {
		// TODO Auto-generated constructor stub
	}

	public void loadFromLogCat(final String output) {
	}

	@Override
	public void loadFromJSON(final JSONObject jsonObj) throws Exception {
		super.loadFromJSON(jsonObj);
	}

}
