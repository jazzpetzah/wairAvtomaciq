package com.wearezeta.auto.ios.reporter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class IOSPerformanceHelpers {

	public static void storeWidgetDataAsJSON(
			final IOSPerfReportModel model, final String path)
			throws IOException {
		FileUtils.writeStringToFile(new File(path), model.asJSON().toString(4));
	}
}