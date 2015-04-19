package com.wearezeta.auto.web.common;

import java.util.NoSuchElementException;

public class WebAppConstants {

	public enum Browser {
		Safari("safari"), InternetExplorer("ie"), Chrome("chrome"), Firefox(
				"firefox");

		private final String stringRepresentation;

		private Browser(String stringRepresentation) {
			this.stringRepresentation = stringRepresentation;
		}

		@Override
		public String toString() {
			return this.stringRepresentation;
		}

		public boolean isOneOf(Browser[] set) {
			for (Browser item : set) {
				if (this == item) {
					return true;
				}
			}
			return false;
		}

		public static Browser fromString(String stringRepresentation) {
			for (Browser name : Browser.values()) {
				if (name.toString().equalsIgnoreCase(stringRepresentation)) {
					return name;
				}
			}
			throw new NoSuchElementException(String.format(
					"Unknown browser name '%s'", stringRepresentation));
		}

		public static boolean isSubSetContains(Browser[] subSet, Browser item) {
			for (Browser status : subSet) {
				if (item == status) {
					return true;
				}
			}
			return false;
		}
	}

	public static final class Scripts {
		public static final String SAFARI_SEND_PICTURE_SCRIPT = "safari_choose_image.txt";

		public static final String SAFARI_OPEN_TAB_SCRIPT = "safari_open_tab.txt";

		public static final String RESOURCES_SCRIPTS_ROOT = "scripts";
	}

	public static final class Calling {
		private static final Browser[] BROWSERS_WITH_CALLING_SUPPORT = new Browser[] {
				Browser.Chrome, Browser.Firefox };

		public static boolean isSupportedIn(Browser item) {
			return Browser
					.isSubSetContains(BROWSERS_WITH_CALLING_SUPPORT, item);
		}
	}
}
