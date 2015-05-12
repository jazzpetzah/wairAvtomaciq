package com.wearezeta.auto.web.common;

import java.util.NoSuchElementException;

public class WebAppConstants {

	public static final String INVITATION_CODE = "zeta22beta";

	public static final String TMP_ROOT = "/tmp";
	public static final String WINDOWS_TMP_ROOT = "C:\\cygwin\\tmp";

	public static final int MIN_WEBAPP_WINDOW_WIDTH = 1366;
	public static final int MIN_WEBAPP_WINDOW_HEIGHT = 768;

	public enum Browser {
		Safari("safari"), InternetExplorer("ie"), Chrome("chrome"), Firefox(
				"firefox"), Opera("opera");

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

		public static final String PRESS_CTRL_C_WIN = "ctrl_c.vbs";

		public static final String PRESS_CMD_C_MAC = "cmd_c.txt";

		public static final String RESOURCES_SCRIPTS_ROOT = "scripts";
	}
}
