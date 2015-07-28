package com.wearezeta.auto.android.common.reporter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class AndroidPerformanceHelpers {

	public static void storeWidgetDataAsJSON(
			final AndroidPerfReportModel model, final String path)
			throws IOException {
		FileUtils.writeStringToFile(new File(path), model.asJSON().toString(4));
	}
}