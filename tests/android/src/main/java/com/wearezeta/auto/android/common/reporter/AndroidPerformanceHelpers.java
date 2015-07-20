package com.wearezeta.auto.android.common.reporter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class AndroidPerformanceHelpers {

	public static void storeWidgetDataAsJSON(final String widgetId,
			final AndroidPerfReportModel model, final String path)
			throws IOException {
		final JSONObject result = new JSONObject();
		result.put("widgetId", widgetId);
		result.put("model", model.asJSON());
		FileUtils.writeStringToFile(new File(path), result.toString(4));
	}
}