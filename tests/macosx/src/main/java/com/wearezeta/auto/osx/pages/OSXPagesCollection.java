package com.wearezeta.auto.osx.pages;

import java.lang.reflect.Field;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.osx.pages.common.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.common.NoInternetConnectionPage;
import com.wearezeta.auto.osx.pages.common.ProblemReportPage;

public class OSXPagesCollection extends AbstractPagesCollection {

	// OSX pages
	public static MainWirePage mainWirePage = null;

	public static MenuBarPage menuBarPage = null;

	public static ChoosePicturePage choosePicturePage = null;

	public static NoInternetConnectionPage noInternetPage = null;

	public static ProblemReportPage problemReportPage = null;

	public void closeAllPages() throws Exception {
		for (Field f : this.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			if (OSXPage.class.isAssignableFrom(f.getType())) {
				OSXPage page = (OSXPage) f.get(this);
				if (page != null) {
					page.close();
				}
			}
		}
	}
}
