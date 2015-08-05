package com.wearezeta.auto.common.performance;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class PerformanceHelpers {

	public static void storeWidgetDataAsJSON(
			final PerfReportModel model, final String path)
			throws IOException {
		FileUtils.writeStringToFile(new File(path), model.asJSON().toString(4));
	}
}
